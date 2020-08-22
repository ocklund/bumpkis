package com.ocklund.bumpkis.resources;

import static java.lang.invoke.MethodHandles.lookup;

import com.codahale.metrics.annotation.Timed;
import com.ocklund.bumpkis.api.Event;
import com.ocklund.bumpkis.client.Bitbucket;
import com.ocklund.bumpkis.core.PullRequestMergeEventHandler;
import com.ocklund.bumpkis.core.ReleaseEventHandler;
import com.ocklund.bumpkis.db.DependencyStorage;
import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/bitbucket")
public class WebhookResource {

    public static final String BUILD_GRADLE = "build.gradle";
    public static final String SETTINGS_GRADLE = "settings.gradle";
    public static final String EVENT_REPO_REFS_CHANGED = "repo:refs_changed";
    public static final String EVENT_PR_MERGED = "pr:merged";
    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String CHANGE_REF_TYPE_TAG = "TAG";
    private static final String CHANGE_TYPE_ADD = "ADD";
    private static final Logger LOGGER = LoggerFactory.getLogger(lookup().lookupClass());
    private final PullRequestMergeEventHandler pullRequestMergeEventHandler;
    private final ReleaseEventHandler releaseEventHandler;

    public WebhookResource(DependencyStorage dependencyStorage, Bitbucket bitbucket) {
        this.pullRequestMergeEventHandler = new PullRequestMergeEventHandler(bitbucket, dependencyStorage);
        this.releaseEventHandler = new ReleaseEventHandler(bitbucket, dependencyStorage);
    }

    @POST
    @Timed
    public void receiveEvent(@HeaderParam("X-Event-Key") String eventKey, Event event) throws IOException {
        processEvent(eventKey, event);
    }

    private void processEvent(String eventKey, Event event) throws IOException {
        LOGGER.debug("Received {} event:\n{}", eventKey, event);
        if (EVENT_REPO_REFS_CHANGED.equalsIgnoreCase(eventKey) && isRelease(event)) {
            releaseEventHandler.handle(event);
        } else if (EVENT_PR_MERGED.equalsIgnoreCase(eventKey)) {
            pullRequestMergeEventHandler.handle(event);
        } else {
            LOGGER.debug("Ignored {} event:\n{}", eventKey, event);
        }
    }

    private boolean isRelease(Event event) {
        return event.getChanges().stream().anyMatch(
            change -> CHANGE_REF_TYPE_TAG.equals(change.getRef().getType()) && CHANGE_TYPE_ADD.equals(change.getType())
                && !change.getRef().getDisplayId().contains(SNAPSHOT));
    }
}
