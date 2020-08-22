package com.ocklund.bumpkis.client;

import static java.lang.invoke.MethodHandles.lookup;

import com.ocklund.bumpkis.BitbucketConfiguration;
import com.ocklund.bumpkis.api.Project;
import com.ocklund.bumpkis.api.PullRequest;
import com.ocklund.bumpkis.api.Ref;
import com.ocklund.bumpkis.api.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bitbucket {
    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String API_URL_REPOS = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/raw/%s";
    private static final String API_URL_BRANCHES = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/branches";
    private static final String API_URL_BROWSE = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/browse/%s";
    private static final String API_URL_LATEST_COMMIT = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/commits?path=%s&limit=1&start=0";
    private static final String API_URL_PULL_REQUESTS = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/pull-requests";
    private static final String API_URL_FILES = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/files";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final BitbucketConfiguration config;
    private final HttpClient httpClient;
    private final HttpClientContext context;

    public Bitbucket(BitbucketConfiguration config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.context = createContext(config);
    }

    private HttpClientContext createContext(BitbucketConfiguration config) {
        HttpHost targetHost = new HttpHost(config.getHost(), config.getPort(), config.getScheme());
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(config.getUser(), config.getPassword()));
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCredentialsProvider(credentialsProvider);
        httpClientContext.setAuthCache(authCache);
        return httpClientContext;
    }

    public String readFile(String projectKey, String repoName, String fileName) throws IOException {
        String url = String.format(API_URL_REPOS, config.getScheme(), config.getHost(), config.getPort(), projectKey,
            repoName, fileName);
        LOGGER.debug("Read file URL {}", url);
        return httpClient.execute(new HttpGet(url), responseHandler, context);
    }

    public FileTree readFileTree(String projectKey, String repoName) throws IOException {
        String url = String.format(API_URL_FILES, config.getScheme(), config.getHost(), config.getPort(), projectKey,
            repoName);
        LOGGER.debug("Read file tree URL {}", url);
        String responseString = httpClient.execute(new HttpGet(url), responseHandler, context);
        return OBJECT_MAPPER.readValue(responseString, FileTree.class);
    }

    public BranchCreated createBranch(String projectKey, String repoName, String branchName, String startPoint, String message)
        throws IOException {
        String url = String.format(API_URL_BRANCHES, config.getScheme(), config.getHost(), config.getPort(), projectKey,
            repoName);
        LOGGER.debug("Create branch URL {}", url);
        HttpPost httpPost = new HttpPost(url);
        String json = "{\"name\": \"" + branchName + "\",\"startPoint\": \"" + startPoint + "\",\"message\": \"" + message + "\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        String responseString = httpClient.execute(httpPost, responseHandler, context);
        return OBJECT_MAPPER.readValue(responseString, BranchCreated.class);
    }

    public FileUpdated updateFile(String projectKey, String repoName, String filePathAndName, String fileContents,
        String branch, String sourceCommitId) throws IOException {
        String url = String.format(API_URL_BROWSE, config.getScheme(), config.getHost(), config.getPort(), projectKey, repoName, filePathAndName);
        LOGGER.debug("Update file URL {}", url);
        HttpPut httpPut = new HttpPut(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("content", fileContents.getBytes(), ContentType.DEFAULT_BINARY, filePathAndName);
        builder.addTextBody("message", "[AUTO] Bump version for dependency", ContentType.TEXT_PLAIN);
        builder.addTextBody("branch", branch, ContentType.TEXT_PLAIN);
        builder.addTextBody("sourceCommitId", sourceCommitId, ContentType.TEXT_PLAIN);
        HttpEntity entity = builder.build();
        httpPut.setEntity(entity);
        String responseString = httpClient.execute(httpPut, responseHandler, context);
        LOGGER.debug("Update file response:\n{}", responseString);
        return OBJECT_MAPPER.readValue(responseString, FileUpdated.class);
    }

    public LatestCommits getLatestCommits(String projectKey, String repoName, String path) throws IOException {
        String url = String.format(API_URL_LATEST_COMMIT, config.getScheme(), config.getHost(), config.getPort(), projectKey, repoName, path);
        LOGGER.debug("Get latest commit URL {}", url);
        return OBJECT_MAPPER.readValue(httpClient.execute(new HttpGet(url), responseHandler, context), LatestCommits.class);
    }

    public PullRequest createPullRequest(String projectKey, String repoName, String branchName, String title, String description)
        throws IOException {
        String url = String.format(API_URL_PULL_REQUESTS, config.getScheme(), config.getHost(), config.getPort(), projectKey, repoName);
        LOGGER.debug("Create pull request URL {}", url);
        HttpPost httpPost = new HttpPost(url);
        Project project = new Project(projectKey);
        Repository repository = new Repository(repoName, repoName, project);
        Ref fromRef = new Ref("refs/heads/" + branchName, repository);
        Ref toRef = new Ref("refs/heads/master", repository);
        String state = "OPEN";
        boolean open = true;
        boolean closed = false;
        boolean locked = false;
        PullRequest openPullRequest = new PullRequest(title, description, state, open, closed, fromRef, toRef, locked, Collections.emptyList());
        StringEntity entity = new StringEntity(openPullRequest.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        String responseString = httpClient.execute(httpPost, responseHandler, context);
        LOGGER.debug("Create pull request response:\n{}", responseString);
        return OBJECT_MAPPER.readValue(responseString, PullRequest.class);
    }

    private final ResponseHandler<String> responseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            String responseString = EntityUtils.toString(response.getEntity());
            LOGGER.error("Unexpected response status: {}, message:\n{}", status, responseString);
            throw new ClientProtocolException("Unexpected response status: " + status + ", message: " + responseString);
        }
    };
}
