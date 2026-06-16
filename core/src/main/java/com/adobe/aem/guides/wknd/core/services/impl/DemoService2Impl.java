package com.adobe.aem.guides.wknd.core.services.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.formula.functions.Log;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aem.guides.wknd.core.services.DemoService2;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.ComponentContext;

@Component(service = DemoService2.class,
    immediate = true
)
public class DemoService2Impl implements DemoService2{
    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(DemoService2Impl.class);
    
    @Reference
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver resolver;

    @Activate
    public void activate(BundleContext bundleContext){
        LOG.info("DemoService2 Activated, BundleID: {}", bundleContext.getBundle().getBundleId());
    }

    public String getVal(){
        return "returned from DemoService2";
    }

    public Iterator<Page> getPages() throws LoginException{
        resolver = getResolver();
        return resolver.adaptTo(PageManager.class).getPage("/content/wknd/us/en").listChildren();
    }

    private ResourceResolver getResolver() throws LoginException {
        final Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put(resolverFactory.SUBSERVICE,"WKNDServiceUser");
        return resolverFactory.getServiceResourceResolver(paramsMap);
    }
}
