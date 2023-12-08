package com.adobe.aem.guides.wknd.core.services.impl;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.services.DemoService;

@Component(service = DemoService.class, immediate = true)
public class DemoServiceImpl implements DemoService {


    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceImpl.class);
    
    @Activate
    public void activate(ComponentContext componentContext){
        LOG.info("==============INSIDE ACTIVATE");
        LOG.info("\n {} = {} ",componentContext.getBundleContext().getBundle().getBundleId(),componentContext.getBundleContext().getBundle().getSymbolicName());
    }

    @Deactivate
    public void deactivate(){
        LOG.info("==============INSIDE DeACTIVATE");
    }

    @Override
    public void doSomething() {
        LOG.info("======DOING SOMETHING============");
    }

}
