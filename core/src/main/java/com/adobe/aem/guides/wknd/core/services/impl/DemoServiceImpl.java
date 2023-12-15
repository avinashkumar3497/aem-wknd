package com.adobe.aem.guides.wknd.core.services.impl;

import java.util.ArrayList;
import java.util.List;

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
public class DemoServiceImpl implements DemoService {


    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceImpl.class);
    private String someSetting="default value of someSetting variable in the DemoServiceImpl class";
    private List<DemoService> configList;
    
    @Activate
    public void activate(ComponentContext componentContext, DemoConfig config){
        LOG.info("==============INSIDE ACTIVATE");
        LOG.info("\n {} = {} ",componentContext.getBundleContext().getBundle().getBundleId(),componentContext.getBundleContext().getBundle().getSymbolicName());
        //someSetting=config.someSetting();
    }

    @Reference(service = DemoService.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindDemoService(final DemoService config)
    {
        if(configList == null){
            configList = new ArrayList<>();
        }
        configList.add(config);
    }

    public void unbindDemoService(final DemoService config)
    {
        configList.remove(config);
    }

    @Deactivate
    public void deactivate(){
        LOG.info("==============INSIDE DeACTIVATE");
    }

    @Override
    public String doSomething() {
        LOG.info("======DOING SOMETHING============");
        someSetting = configList.get(0).toString();
        return "Returning from the implementation DemoServiceImpl. Value of the OSGi Config some setting : "+someSetting;
    }

}