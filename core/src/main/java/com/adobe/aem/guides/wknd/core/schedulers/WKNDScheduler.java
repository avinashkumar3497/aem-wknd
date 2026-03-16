package com.adobe.aem.guides.wknd.core.schedulers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.commons.scheduler.Job;
import org.apache.sling.commons.scheduler.JobContext;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

@Component(
    service=Job.class,
    immediate = true
)
public class WKNDScheduler implements Job {

    @Reference
    Scheduler scheduler;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WKNDScheduler.class);

    @Activate
    protected void activate(){

        ScheduleOptions in=scheduler.EXPR("0 0/10 * * * ?");
        in.name("com.adobe.aem.guides.wknd.core.schedulers.WKNDScheduler.in");
        Map<String,Serializable> inMap = new HashMap<>();
        inMap.put("country","INDIA");
        in.config(inMap);
        scheduler.schedule(this, in);

        ScheduleOptions us=scheduler.EXPR("0 0/5 * * * ?");
        us.name("com.adobe.aem.guides.wknd.core.schedulers.WKNDScheduler.us");
        Map<String,Serializable> usMap = new HashMap<>();
        usMap.put("country","USA");
        us.config(usMap);
        scheduler.schedule(this, us);
        
    }

    @Override
    public void execute(JobContext jobContext) {
        LOG.info("WKNDScheduler is triggered | Country: "+jobContext.getConfiguration().get("country"));
    }

}
