package com.ocklund.bumpkis.db;

import java.util.Objects;

public class Dependency {

    private Long id;
    private String projectKey;
    private String repoName;
    private String dirName;
    private String dependency;

    public Dependency(Long id, String projectKey, String repoName, String dirName, String dependency) {
        this.id = id;
        this.projectKey = projectKey;
        this.repoName = repoName;
        this.dirName = dirName;
        this.dependency = dependency;
    }

    public Dependency() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    @Override
    public String toString() {
        return "Dependency{" + "id=" + id + ", projectKey='" + projectKey + '\'' + ", repoName='" + repoName + '\''
            + ", dirName='" + dirName + '\'' + ", dependency='" + dependency + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Dependency)) {
            return false;
        }
        Dependency that = (Dependency) o;
        return Objects.equals(projectKey, that.projectKey) &&
            Objects.equals(repoName, that.repoName) &&
            Objects.equals(dirName, that.dirName) &&
            Objects.equals(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectKey, repoName, dirName, dependency);
    }
}
