package com.example.messagingqueue;

import com.example.messagingqueue.resources.MessagingQueueResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * Created by Chetan
 */
public class MessagingQueueApplication extends Application<MessagingQueueConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MessagingQueueApplication().run(args);
    }

    @Override
    public String getName() {
        return "MessagingQueue";
    }

    @Override
    public void initialize(final Bootstrap<MessagingQueueConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final MessagingQueueConfiguration configuration,
                    final Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi;
        try {
            jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
            environment.jersey().register(new MessagingQueueResource(jdbi));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
