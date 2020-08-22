package com.ocklund.bumpkis.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocklund.bumpkis.api.Commit;
import com.ocklund.bumpkis.api.Event;
import com.ocklund.bumpkis.api.PullRequest;
import com.ocklund.bumpkis.client.*;
import com.ocklund.bumpkis.db.Dependency;
import com.ocklund.bumpkis.db.DependencyStorage;
import com.ocklund.bumpkis.db.DependencyStoragePostgres;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.ocklund.bumpkis.resources.WebhookResource.*;
import static java.nio.file.Files.readAllLines;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class WebhookResourceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DependencyStorage dependencyStorage = mock(DependencyStoragePostgres.class);
    private final Bitbucket bitbucket = mock(Bitbucket.class);
    private final WebhookResource resource = new WebhookResource(dependencyStorage, bitbucket);

    @Test
    public void shouldReceiveReleaseEvent() throws IOException {
        Event event = toEvent(readFile("./src/test/resources/fixtures/release-tag-event.json"));
        String buildGradleContents = readFile("./src/test/resources/fixtures/build.gradle.foo");
        when(bitbucket.readFile(anyString(), anyString(), eq(BUILD_GRADLE))).thenReturn(buildGradleContents);
        when(bitbucket.readFile(anyString(), anyString(), eq(SETTINGS_GRADLE))).thenReturn("rootProject.name = 'bar'");
        Dependency dependency = new Dependency(1L, "PROJECT_1", "foo", ".", "com.example.bar:bar");
        when(dependencyStorage.findBy(anyString())).thenReturn(Collections.singletonList(dependency));
        BranchCreated branchCreated = new BranchCreated("", "", "", "", "", false);
        when(bitbucket.createBranch(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(branchCreated);
        LatestCommits latestCommits = new LatestCommits(
            Collections.singletonList(new Commit("somedisplayid", "someid")));
        when(bitbucket.getLatestCommits(anyString(), anyString(), anyString())).thenReturn(latestCommits);
        FileUpdated fileUpdated = new FileUpdated("someid");
        when(bitbucket.updateFile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(fileUpdated);
        PullRequest pullRequest = new PullRequest("", "", "", true, false, null, null, false, Collections.emptyList());
        when(bitbucket.createPullRequest(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(pullRequest);

        resource.receiveEvent(EVENT_REPO_REFS_CHANGED, event);

        verify(bitbucket, times(1)).createBranch(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(bitbucket, times(1)).createPullRequest(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void shouldReceivePullRequestMergeEvent() throws IOException {
        Event event = toEvent(readFile("./src/test/resources/fixtures/pr-merge-event.json"));
        String buildGradleContents = readFile("./src/test/resources/fixtures/build.gradle.foo");
        FileTree fileTree = objectMapper.readValue(readFile("./src/test/resources/fixtures/file-tree.json"), FileTree.class);
        when(bitbucket.readFile(anyString(), anyString(), contains(BUILD_GRADLE))).thenReturn(buildGradleContents);
        when(bitbucket.readFileTree(anyString(), anyString())).thenReturn(fileTree);
        when(dependencyStorage.findBy(anyString())).thenReturn(Collections.emptyList());

        resource.receiveEvent(EVENT_PR_MERGED, event);

        verify(dependencyStorage, times(3)).create(anyString(), anyString(), anyString(), any());
    }

    private String readFile(String file) throws IOException {
        return readAllLines(Paths.get(file), StandardCharsets.UTF_8).stream()
            .collect(Collectors.joining(System.lineSeparator()));
    }

    private Event toEvent(String eventJson) throws IOException {
        return objectMapper.readValue(eventJson, Event.class);
    }
}