package com.adobe.aem.guides.wknd.core.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.LoginException;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    service = ResourceChangeListener.class,
    property = {
        ResourceChangeListener.CHANGES+"=ADDED",
        ResourceChangeListener.CHANGES+"=REMOVED",
        ResourceChangeListener.PATHS+"=/content/wknd/us/en"
    }
)
public class WKNDSlingHandler implements ResourceChangeListener{

    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WKNDSlingHandler.class);
    
    @Reference
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver resolver;

    @org.osgi.service.component.annotations.Activate
    protected void Activate() throws org.apache.sling.api.resource.LoginException{
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put(resolverFactory.SUBSERVICE, "WKNDServiceUser");
        try {
            resolver=resolverFactory.getServiceResourceResolver(paramMap);
        } catch (org.apache.sling.api.resource.LoginException e) {
            LOG.warn("Exception while getting the resolver");
        }
    }

    @org.osgi.service.component.annotations.Deactivate
    protected void Deactivate(){
        resolver.close();
    }

    @Override
    public void onChange(List<ResourceChange> listOfChanges){
        LOG.info("WKNDSlingHandler Changes detected");
        for(ResourceChange rc: listOfChanges){
            LOG.info("WKNDSlingHandler Change detected: Type is {}, Path is {}", rc.getType(), rc.getPath());
            try {
                ModifiableValueMap modifiableValueMap = resolver.getResource(rc.getPath()).adaptTo(ModifiableValueMap.class);
                modifiableValueMap.put("eventhandlertask", "event captured: Type:"+rc.getType());
            } catch (Exception e) {
                LOG.warn("Exception occured while setting property for EventHandler: {}, resourcePath: {}",e.getMessage(),rc.getPath());
            }
        }

    }
}
