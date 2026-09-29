package com.adobe.aem.guides.wknd.core.schedulers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(immediate = true)
@Designate(ocd = ExpiredPageScannerConfig.class)
public class ExpiredPageScanner implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ExpiredPageScanner.class);
    private static final String SUBSERVICE = "wknd-page-scanner";
    private static final String JOB_NAME = ExpiredPageScanner.class.getSimpleName();

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    private String contentRootPath;
    private boolean enabled;
    private int maxResults;

    @Activate
    @Modified
    protected void activate(ExpiredPageScannerConfig config) {
        // Always remove any previously registered job before re-registering
        removeScheduler();

        this.contentRootPath = config.contentRootPath();
        this.enabled = config.enabled();
        this.maxResults = config.maxResults();

        if (!enabled) {
            LOG.info("{} is disabled. Job not scheduled.", JOB_NAME);
            return;
        }
        addScheduler(config.cronExpression());
    }

    @Deactivate
    protected void deactivate() {
        removeScheduler();
    }

    private void addScheduler(String cronExpression) {
        ScheduleOptions options = scheduler.EXPR(cronExpression);
        options.name(JOB_NAME);
        options.canRunConcurrently(false);
        scheduler.schedule(this, options);
        LOG.info("{} scheduled with cron '{}', root={}, maxResults={}",
                JOB_NAME, cronExpression, contentRootPath, maxResults);
    }

    private void removeScheduler() {
        scheduler.unschedule(JOB_NAME);
        LOG.debug("{} unscheduled.", JOB_NAME);
    }

    @Override
    public void run() {
        if (!enabled) {
            LOG.debug("{} is disabled. Skipping execution.", JOB_NAME);
            return;
        }

        Map<String, Object> authInfo =
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfo)) {

            Session session = resolver.adaptTo(Session.class);
            if (session == null) {
                LOG.error("Could not adapt ResourceResolver to Session. Aborting run.");
                return;
            }

            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(buildPredicates()), session);
            query.setStart(0);
            query.setHitsPerPage(maxResults);

            SearchResult result = query.getResult();
            int count = 0;

            for (Hit hit : result.getHits()) {
                LOG.warn("Expired but still active on publish: {}", hit.getPath());
                count++;
            }

            LOG.info("{} finished. root={}, expiredPagesFound={}, executionTimeMs={}",
                    JOB_NAME, contentRootPath, count, result.getExecutionTimeMillis());

        } catch (LoginException e) {
            LOG.error("Unable to obtain service resource resolver for subservice '{}'", SUBSERVICE, e);
        } catch (Exception e) {
            LOG.error("Unexpected error while scanning for expired pages under {}", contentRootPath, e);
        }
    }

    private Map<String, String> buildPredicates() {
        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", contentRootPath);
        predicates.put("type", "cq:PageContent");

        predicates.put("1_property", "cq:lastReplicationAction");
        predicates.put("1_property.value", "Activate");

        predicates.put("2_daterange.property", "offTime");
        predicates.put("2_daterange.upperBound", "now");
        predicates.put("2_daterange.upperOperation", "<=");

        predicates.put("p.limit", String.valueOf(maxResults));
        predicates.put("p.guessTotal", "true");

        return predicates;
    }
}