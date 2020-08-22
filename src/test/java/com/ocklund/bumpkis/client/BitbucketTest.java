package com.ocklund.bumpkis.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ocklund.bumpkis.BitbucketConfiguration;
import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;

public class BitbucketTest {

    private BitbucketConfiguration bitbucketConfiguration;
    private HttpClient httpClient;
    private String createBranchResponse = """
        {
            "id":"refs/heads/master",
            "displayId": "master",
            "type": "BRANCH",
            "latestCommit": "8d51122def5632836d1cb1026e879069e10a1e13",
            "latestChangeset": "8d51122def5632836d1cb1026e879069e10a1e13",
            "isDefault": true
        }
        """;

    private String readFileTreeResponse = """
        {
          "values": [
            ".gitignore",
            "README.md",
            "build.gradle",
            "foo-app/build.gradle",
            "foo-app/gradle.properties",
            "foo-app/src/main/java/com/example/foo/App.java",
            "foo-app/src/test/java/com/example/foo/AppTest.java",
            "foo-core/build.gradle",
            "foo-core/gradle.properties",
            "foo-core/src/main/java/com/example/foo/Core.java",
            "foo-core/src/test/java/com/example/foo/CoreTest.java",
            "gradle.properties",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties",
            "gradlew",
            "gradlew.bat",
            "settings.gradle"
          ],
          "size": 17,
          "isLastPage": true,
          "start": 0,
          "limit": 25,
          "nextPageStart": null
        }
        """;

    @Before
    public void setUp() throws IOException {
        bitbucketConfiguration = mock(BitbucketConfiguration.class);
        when(bitbucketConfiguration.getScheme()).thenReturn("http");
        when(bitbucketConfiguration.getHost()).thenReturn("localhost");
        when(bitbucketConfiguration.getPort()).thenReturn(8080);
        when(bitbucketConfiguration.getUser()).thenReturn("someUser");
        when(bitbucketConfiguration.getPassword()).thenReturn("somePassword");
        httpClient = mock(CloseableHttpClient.class);

        when(httpClient.execute(any(HttpPost.class), any(ResponseHandler.class), any(HttpClientContext.class)))
            .thenReturn(createBranchResponse);
        when(httpClient.execute(any(HttpGet.class), any(ResponseHandler.class), any(HttpClientContext.class)))
            .thenReturn(readFileTreeResponse);
    }

    @Test
    public void shouldCreatePullRequest() throws IOException {
        Bitbucket target = new Bitbucket(bitbucketConfiguration, httpClient);

        BranchCreated result = target.createBranch("PROJECT_1",
            "foo", "auto/bump-bar-version-to-1-0-3", "refs/heads/master",
                "Automatic bump of dependency version to com.example.bar:bar:1.0.3");

        assertThat(result.getType(), is("BRANCH"));
    }

    @Test
    public void shouldReturnFileStructure() throws IOException {
        Bitbucket target = new Bitbucket(bitbucketConfiguration, httpClient);

        FileTree result = target.readFileTree("PROJECT_1", "foo");

        assertThat(result.getSize(), is(17));
    }
}