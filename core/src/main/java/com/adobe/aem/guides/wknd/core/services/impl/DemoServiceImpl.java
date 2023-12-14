package com.adobe.aem.guides.wknd.core.services.impl;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.config.DemoConfig;
import com.adobe.aem.guides.wknd.core.services.DemoService;

@Component(service = DemoService.class, immediate = true)
@Designate(ocd = com.adobe.aem.guides.wknd.core.config.DemoConfig.class)
@ServiceRanking(2)
public class DemoServiceImpl implements DemoService {


    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceImpl.class);
    private String someSetting;
    
    @Activate
    public void activate(ComponentContext componentContext, DemoConfig config){
        LOG.info("==============INSIDE ACTIVATE");
        LOG.info("\n {} = {} ",componentContext.getBundleContext().getBundle().getBundleId(),componentContext.getBundleContext().getBundle().getSymbolicName());
        someSetting=config.someSetting();
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

}
