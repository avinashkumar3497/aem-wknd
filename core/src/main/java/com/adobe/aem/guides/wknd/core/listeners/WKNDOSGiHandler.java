package com.adobe.aem.guides.wknd.core.listeners;

import java.util.*;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(
    service = EventHandler.class,
    property = {
        EventConstants.EVENT_TOPIC+"=org/apache/sling/api/resource/Resource/CHANGED",
        EventConstants.EVENT_FILTER+"=(path=/content/wknd/us/en/*)"
    }
)
public class WKNDOSGiHandler implements EventHandler{

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WKNDOSGiHandler.class);

    @Reference
    JobManager jobManager;

    @Override
    public void handleEvent(final Event event){
        LOG.info("WKNDOSGiHandler Event Triggered: Event-Topic = {} | Resource-path: {}", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));
        Map<String,Object> jobPropertiesMap = new HashMap<>();
        jobPropertiesMap.put("path", event.getProperty(SlingConstants.PROPERTY_PATH));
        jobPropertiesMap.put("heropage", "HeroPage");
        jobManager.addJob("Avi", jobPropertiesMap);
    }

}
