package com.ocklund.bumpkis.db;

import static java.lang.invoke.MethodHandles.lookup;

import java.sql.ResultSet;
import java.util.List;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyStoragePostgres implements DependencyStorage {
    private static final String SQL_CREATE = "insert into module_dependency (project_key, repo_name, dir_name, dependency) values (:projectKey, :repoName, :dirName, :dependency)";
    private static final String SQL_DELETE = "delete from module_dependency where id = :id";
    private static final String SQL_FIND_BY_NAME = "select * from module_dependency where dependency = :dependency";
    private static final String SQL_FIND_BY_REPO = "select * from module_dependency where project_key = :projectKey and repo_name = :repoName";
    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());
    private Jdbi jdbi;

    public DependencyStoragePostgres(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void create(String projectKey, String repoName, String dirName, String dependency) {
        try (Handle handle = jdbi.open()) {
            int count = handle.createUpdate(SQL_CREATE)
                .bind("projectKey", projectKey)
                .bind("repoName", repoName)
                .bind("dirName", dirName)
                .bind("dependency", dependency)
                .execute();
            LOGGER.debug("Created {} row for {}/{}/{} -> {} dependency", count, projectKey, repoName, dirName, dependency);
        }
    }

    @Override
    public void delete(Long id) {
        try (Handle handle = jdbi.open()) {
            int count = handle.createUpdate(SQL_DELETE)
                .bind("id", id)
                .execute();
            LOGGER.debug("Deleted {} row dependency id {}", count, id);
        }
    }

    @Override
    public List<Dependency> findBy(String dependency) {
        List<Dependency> dependencies;
        try (Handle handle = jdbi.open()) {
            dependencies = handle.createQuery(SQL_FIND_BY_NAME)
                .bind("dependency", dependency)
                .map((ResultSet rs, StatementContext ctx) ->
                    new Dependency(rs.getLong("id"),
                        rs.getString("project_key"),
                        rs.getString("repo_name"),
                        rs.getString("dir_name"),
                        rs.getString("dependency")))
                .list();
            LOGGER.debug("Found {} for {}", dependencies.size(), dependency);
        }
        return dependencies;
    }

    @Override
    public List<Dependency> findBy(String projectKey, String repoName) {
        List<Dependency> dependencies;
        try (Handle handle = jdbi.open()) {
            dependencies = handle.createQuery(SQL_FIND_BY_REPO)
                .bind("projectKey", projectKey)
                .bind("repoName", repoName)
                .map((rs, ctx) ->
                    new Dependency(rs.getLong("id"), rs.getString("project_key"),
                        rs.getString("repo_name"), rs.getString("dir_name"),
                        rs.getString("dependency"))).list();
            LOGGER.debug("Found {} for {}/{}", dependencies.size(), projectKey, repoName);
        }
        return dependencies;
    }
}
