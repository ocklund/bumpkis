package com.ocklund.bumpkis.client;

import static com.ocklund.bumpkis.TestUtils.readFile;
import static java.nio.file.Files.readAllLines;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;

@Ignore("Needs server on localhost")
public class HttpClientTest {

    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 7990;
    private static final String PROJECT_KEY = "PROJECT_1";
    //private static final String REPO_NAME = "rep_1";
    private static final String REPO_NAME = "foo";
    //private static final String PATH = "add_file/add_file.txt";
    private static final String PATH = "build.gradle";
    //private static final String API_URL = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/commits?path=%s&limit=1&start=0";
    private static final String API_URL = "%s://%s:%d/bitbucket/rest/api/1.0/projects/%s/repos/%s/browse/%s";
    private static final String BRANCH = "auto/bump-bar-version-to-1-0-24";
    private static final String SOURCECOMMIT_ID = "c05e180ce29315644eb3939a7555fac815609136";

    public static void main(String[] args) {
        //sendGetRequest();
        sendPutRequest();
    }

    private static void sendPutRequest() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(String.format(API_URL, SCHEME, HOST, PORT, PROJECT_KEY, REPO_NAME, PATH));
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("content", readFile("./src/test/resources/fixtures/build.gradle.foo").getBytes(), ContentType.DEFAULT_BINARY, PATH);
            builder.addTextBody("message", "Bump version for dependency", ContentType.TEXT_PLAIN);
            builder.addTextBody("branch", BRANCH, ContentType.TEXT_PLAIN);
            builder.addTextBody("sourceCommitId", SOURCECOMMIT_ID, ContentType.TEXT_PLAIN);
            HttpEntity entity = builder.build();
            httpPut.setEntity(entity);
            System.out.println("Executing request " + httpPut.getRequestLine());
            String responseBody = httpclient.execute(httpPut, responseHandler, createContext());
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendGetRequest() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(String.format(API_URL, SCHEME, HOST, PORT, PROJECT_KEY, REPO_NAME, PATH));
            System.out.println("Executing request " + httpGet.getRequestLine());
            String responseBody = httpclient.execute(httpGet, responseHandler, createContext());
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpClientContext createContext() {
        HttpHost targetHost = new HttpHost(HOST, PORT, SCHEME);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("admin", "admin"));
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCredentialsProvider(credentialsProvider);
        httpClientContext.setAuthCache(authCache);
        return httpClientContext;
    }

    private static ResponseHandler<String> responseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            String responseString = EntityUtils.toString(response.getEntity());
            System.err.println("Unexpected response status: " + status + "\n" + responseString);
            return responseString;
        }
    };
}
