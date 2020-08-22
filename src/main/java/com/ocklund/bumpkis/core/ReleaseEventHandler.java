package com.ocklund.bumpkis.core;

import static java.lang.invoke.MethodHandles.lookup;

import com.ocklund.bumpkis.api.Event;
import com.ocklund.bumpkis.api.PullRequest;
import com.ocklund.bumpkis.client.Bitbucket;
import com.ocklund.bumpkis.client.BranchCreated;
import com.ocklund.bumpkis.client.FileUpdated;
import com.ocklund.bumpkis.client.LatestCommits;
import com.ocklund.bumpkis.db.Dependency;
import com.ocklund.bumpkis.db.DependencyStorage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ocklund.bumpkis.resources.WebhookResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleaseEventHandler {

    private static final String IMPLEMENTATION = "implementation";
    private static final String GROUP_KEY = "group";
    private static final String PROJECT_NAME_KEY = "rootProject.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());
    private final Bitbucket bitbucket;
    private final DependencyStorage dependencyStorage;

    public ReleaseEventHandler(Bitbucket bitbucket, DependencyStorage dependencyStorage) {
        this.bitbucket = bitbucket;
        this.dependencyStorage = dependencyStorage;
    }

    public void handle(Event event) throws IOException {
        String projectKey = event.getRepository().getProject().getKey();
        String repoName = event.getRepository().getName();
        String groupName = Utils.cleanUpValues(Utils.getValueForKeyInString(GROUP_KEY, bitbucket.readFile(projectKey, repoName, WebhookResource.BUILD_GRADLE))).get(0);
        String appName = Utils.cleanUpValues(Utils.getValueForKeyInString(PROJECT_NAME_KEY, bitbucket.readFile(projectKey, repoName, WebhookResource.SETTINGS_GRADLE))).get(0);
        String version = event.getChanges().get(0).getRef().getDisplayId();
        String groupNameVersion = groupName + ":" + appName + ":" + version;
        LOGGER.info("New release {}", groupNameVersion);

        List<Dependency> dependencies = dependencyStorage.findBy(groupName + ":" + appName);

        if (dependencies.isEmpty()) {
            LOGGER.info("No repo depending on {}", appName);
        } else {
            for (Dependency dep : dependencies) {
                LOGGER.info("Found dependency {}/{}/{} -> {}. Creating pull request for new version {}", dep.getProjectKey(), dep.getRepoName(), dep.getDirName(), dep.getDependency(), groupNameVersion);
                String branchName = "auto/bump-" + appName + "-version-to-" + version.replace(".", "-");
                String startPoint = "refs/heads/master";
                String message = "Automatic bump of dependency version to " + groupNameVersion;
                BranchCreated branchCreated = bitbucket.createBranch(dep.getProjectKey(), dep.getRepoName(), branchName, startPoint, message);
                LOGGER.debug("BranchCreated:\n{}", branchCreated);

                String filePath = ".".equals(dep.getDirName()) ? WebhookResource.BUILD_GRADLE : dep.getDirName() + "/" + WebhookResource.BUILD_GRADLE;
                String oldBuildFileContents = bitbucket.readFile(dep.getProjectKey(), dep.getRepoName(), filePath);
                LOGGER.debug("Old build file contents:\n{}", oldBuildFileContents);

                Optional<String> oldDep = Utils.cleanUpValues(Utils.getValueForKeyInString(IMPLEMENTATION, oldBuildFileContents)).stream()
                    .filter(v -> v.startsWith(dep.getDependency()))
                    .findFirst();
                if (oldDep.isPresent()) {
                    String oldVersion = Utils.getVersion(oldDep.get());

                    LOGGER.debug("Replacing dependency {}:{}", dep.getDependency(), oldVersion);

                    String newBuildFileContents = oldBuildFileContents.replace(dep.getDependency() + ":" + oldVersion, groupNameVersion);
                    LOGGER.debug("New build file contents:\n{}", newBuildFileContents);

                    LatestCommits latestCommits = bitbucket.getLatestCommits(dep.getProjectKey(), dep.getRepoName(), filePath);
                    LOGGER.debug("LatestCommits:\n{}", latestCommits);

                    String sourceCommitId = latestCommits.getValues().get(0).getId();
                    FileUpdated fileUpdated = bitbucket.updateFile(dep.getProjectKey(), dep.getRepoName(), filePath, newBuildFileContents, branchName, sourceCommitId);
                    LOGGER.debug("FileUpdated:\n{}", fileUpdated);

                    PullRequest pullRequest = bitbucket.createPullRequest(dep.getProjectKey(), dep.getRepoName(), branchName,"[AUTO] Bump version to " + groupNameVersion, message);
                    LOGGER.info("PullRequest:\n{}", pullRequest);
                } else {
                    LOGGER.warn("Dependency {} not found in file contents {}", dep, oldBuildFileContents);
                }
            }
        }
    }
}
