package com.adobe.aem.guides.wknd.core.services.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.services.DemoService;

@Component(service = DemoService.class, immediate = true)
@ServiceRanking(1)
public class DemoServiceImplCopy implements DemoService {


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
    public String doSomething() {
        LOG.info("======DOING SOMETHING============");
        return "Returning from the implementation DemoServiceImplCopy";
    }

    @Override
    public List<DemoService> getAllService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllService'");
    }

    @Override
    public String getSomeSetting() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSomeSetting'");
    }

    @Override
    public List<DemoService> getAllConfigs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllConfigs'");
    }
    
}
