package com.adobe.aem.guides.wknd.core.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationEvent;

@Component(
    immediate = true,
    property = {
        EventConstants.EVENT_TOPIC+"="+ReplicationEvent.EVENT_TOPIC
    }
)
public class WKNDReplicationEventHandlerForEsCrawl implements EventHandler{

    private static Logger LOG = LoggerFactory.getLogger(WKNDReplicationEventHandlerForEsCrawl.class);

    @Reference
    JobManager jobManager;

    @Override
    public void handleEvent(Event event){
        ReplicationAction replicationAction = ReplicationAction.fromEvent(event);
        if (replicationAction == null) {
            return;
        }
        String replicatedPath = replicationAction.getPath();
        if (StringUtils.isBlank(replicatedPath)) {
            return;
        }
        Map<String,Object> jobProperties = new HashMap<>();
        jobProperties.put("path", replicatedPath);
        jobProperties.put("replication-action-type",replicationAction.getType().toString());
        LOG.info("Replicated Path: {} | Replication Action Type: {}",replicatedPath,replicationAction.getType().toString());
        Job job = jobManager.addJob("com/wknd/elasticAppSearch/crawl", jobProperties);
    }
}
