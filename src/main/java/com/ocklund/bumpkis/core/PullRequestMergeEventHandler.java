package com.ocklund.bumpkis.core;

import static java.lang.invoke.MethodHandles.lookup;

import com.ocklund.bumpkis.api.Event;
import com.ocklund.bumpkis.client.Bitbucket;
import com.ocklund.bumpkis.client.FileTree;
import com.ocklund.bumpkis.db.Dependency;
import com.ocklund.bumpkis.db.DependencyStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ocklund.bumpkis.resources.WebhookResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullRequestMergeEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String DEPENDENCY_TYPE = "implementation";
    private static final String ALLOWED_PACKAGE = "com.example";
    private Bitbucket bitbucket;
    private DependencyStorage dependencyStorage;

    public PullRequestMergeEventHandler(Bitbucket bitbucket, DependencyStorage dependencyStorage) {
        this.bitbucket = bitbucket;
        this.dependencyStorage = dependencyStorage;
    }

    public void handle(Event event) throws IOException {
        String projectKey = event.getPullRequest().getFromRef().getRepository().getProject().getKey();
        String repoName = event.getPullRequest().getFromRef().getRepository().getName();

        List<Dependency> repoDependencies = getRepoDependencies(projectKey, repoName);
        List<Dependency> storedDependencies = dependencyStorage.findBy(projectKey, repoName);

        if (repoDependencies.isEmpty()) {
            LOGGER.debug("No {} dependencies found in {}/{}", DEPENDENCY_TYPE, projectKey, repoName);
        } else {
            for (Dependency dependency : repoDependencies) {
                LOGGER.debug("Found dependency {}", dependency);
                if (storedDependencies.contains(dependency)) {
                    LOGGER.info("Dependency {} already stored", dependency);
                } else {
                    LOGGER.info("Storing dependency {}", dependency);
                    dependencyStorage
                        .create(dependency.getProjectKey(), dependency.getRepoName(), dependency.getDirName(),
                            dependency.getDependency());
                }
            }
        }
        updateStorage(repoDependencies, storedDependencies);
    }

    private void updateStorage(List<Dependency> repoDependencies, List<Dependency> storedDependencies) {
        List<Dependency> removedDependencies = storedDependencies.stream().filter(d -> !repoDependencies.contains(d))
            .collect(Collectors.toList());
        for (Dependency dependency : removedDependencies) {
            LOGGER.debug("Removing outdated dependency {} from storage", dependency);
            dependencyStorage.delete(dependency.getId());
        }
    }

    private List<Dependency> getRepoDependencies(String projectKey, String repoName) throws IOException {
        List<Dependency> dependencies = new ArrayList<>();
        FileTree fileTree = bitbucket.readFileTree(projectKey, repoName);
        for (String line : fileTree.getValues()) {
            if (line.contains(WebhookResource.BUILD_GRADLE)) {
                String treeLine = Utils.cleanUpFileTreeLine(line);
                String dirName = treeLine.contains("/") ? treeLine.split("/")[0] : ".";
                List<String> dependencyStrings = Utils.cleanUpValues(
                    Utils.getValueForKeyInString(DEPENDENCY_TYPE, bitbucket.readFile(projectKey, repoName, treeLine)));
                for (String dependencyString : dependencyStrings) {
                    if (dependencyString.contains(ALLOWED_PACKAGE)) {
                        dependencies.add(
                            new Dependency(null, projectKey, repoName, dirName, Utils.removeVersion(dependencyString)));
                    } else {
                        LOGGER.debug("Dependency {} is not internal, skipping", dependencyString);
                    }
                }
            }
        }
        return dependencies;
    }
}