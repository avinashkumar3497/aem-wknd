package com.adobe.aem.guides.wknd.core.consumer;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = JobConsumer.class,
    property={
        JobConsumer.PROPERTY_TOPICS+"=Avi"
    }
)
public class WKNDJobConsumer implements JobConsumer{

    private static Logger LOG = LoggerFactory.getLogger(WKNDJobConsumer.class);

    @Override
    public JobResult process(Job job){
        LOG.info("Job consumed with path {}, and a property with value {}", job.getProperty("path"), job.getProperty("heropage"));
        return JobResult.FAILED;
    }
}
