package com.ocklund.bumpkis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BitbucketConfiguration {
    private String scheme;
    private String host;
    private Integer port;
    private String user;
    private String password;

    public BitbucketConfiguration() {}

    public BitbucketConfiguration(String scheme, String host, Integer port, String user, String password) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    @JsonProperty
    public String getScheme() {
        return scheme;
    }

    @JsonProperty
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public Integer getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(Integer port) {
        this.port = port;
    }

    @JsonProperty
    public String getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
