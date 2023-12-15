package com.adobe.aem.guides.wknd.core.services.impl;

import java.util.*;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.config.DemoConfig;
import com.adobe.aem.guides.wknd.core.services.DemoService;

@Component(service = DemoService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = com.adobe.aem.guides.wknd.core.config.DemoConfig.class, factory = true)
@ServiceRanking(2)
public class DemoServiceImpl implements DemoService {


    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceImpl.class);
    private String someSetting;
    private List<DemoService> demoServiceList;
    
    @Override
    public String getSomeSetting() {
        return someSetting;
    }        

    @Activate
    public void activate(ComponentContext componentContext, DemoConfig config){
        LOG.info("==============INSIDE ACTIVATE");
        LOG.info("\n {} = {} ",componentContext.getBundleContext().getBundle().getBundleId(),componentContext.getBundleContext().getBundle().getSymbolicName());
        someSetting=config.someSetting();
    }

    @Reference(service = DemoService.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindDemoService(final DemoService demoService)
    {
        if(demoServiceList == null)
        {
            demoServiceList = new ArrayList<>();
        }
            demoServiceList.add(demoService);
    }

    public void unbindDemoService(final DemoService demoService)
    {
        demoServiceList.remove(demoService);
    }

    @Deactivate
    public void deactivate(){
        LOG.info("==============INSIDE DeACTIVATE");
    }

    @Override
    public String doSomething() {
        LOG.info("======DOING SOMETHING============");
        return "Returning from the implementation DemoServiceImpl. Value of the OSGi Config some setting : "+someSetting;
    }

    @Override
    public List<DemoService> getAllService(){
        return demoServiceList;
    }
    @Override
    public List<DemoService> getAllConfigs(){
        return demoServiceList;
    }
}
