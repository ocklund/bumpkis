package com.ocklund.bumpkis.db;

import java.util.List;

public interface DependencyStorage {

    void create(String projectKey, String repoName, String dirName, String dependency);

    void delete(Long id);

    List<Dependency> findBy(String dependencyName);

    List<Dependency> findBy(String projectKey, String repoName);
}
