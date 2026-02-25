package com.adobe.aem.guides.wknd.core.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

@Component(
    service = DemoServiceB.class,
    immediate = true
)
@ServiceRanking(1005)
public class DemoServiceB {

    
    

    public List<String> getPages() throws LoginException{
        Iterator<Page> itr = demoService1.get(0).getPages();
        List<String> l = new ArrayList<>();
        while (itr.hasNext()) {
            l.add(itr.next().getTitle());
        }
        return l;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceB.class);

    private List<DemoService1> demoService1 = new ArrayList<>();

    @Reference(service = DemoService1.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindDemoService1(DemoService1 demoService1){
        this.demoService1.add(demoService1);
        LOG.info("bindDemoService1: an instance of DemoService1 is now available to be used in DemoServiceB with another set of configs");
        LOG.info("config values: "+demoService1.getShowConfig());
    }

    // @Reference(service = DemoService1.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void unbindDemoService1(DemoService1 demoService1){
        this.demoService1.remove(demoService1);
        LOG.info("bindDemoService1: an instance of DemoService1 is now removed from DemoServiceB");
        // LOG.info("config values: ")
    }
}
