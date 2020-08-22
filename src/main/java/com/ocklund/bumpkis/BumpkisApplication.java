package com.ocklund.bumpkis;

import com.ocklund.bumpkis.client.Bitbucket;
import com.ocklund.bumpkis.db.DependencyStorage;
import com.ocklund.bumpkis.db.DependencyStoragePostgres;
import com.ocklund.bumpkis.resources.WebhookResource;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.http.client.HttpClient;
import org.jdbi.v3.core.Jdbi;

public class BumpkisApplication extends Application<BumpkisConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BumpkisApplication().run(args);
    }

    @Override
    public String getName() {
        return "Bumpkis";
    }

    @Override
    public void initialize(final Bootstrap<BumpkisConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<BumpkisConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BumpkisConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );
    }

    @Override
    public void run(final BumpkisConfiguration configuration, final Environment environment) {
        final HttpClient httpClient = new HttpClientBuilder(environment)
            .using(configuration.getHttpClientConfiguration())
            .build(getName());
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final Bitbucket bitbucket = new Bitbucket(configuration.getBitbucketConfiguration(), httpClient);
        final DependencyStorage dependencyStorage = new DependencyStoragePostgres(jdbi);
        final WebhookResource resource = new WebhookResource(dependencyStorage, bitbucket);
        environment.jersey().register(resource);
    }

}
