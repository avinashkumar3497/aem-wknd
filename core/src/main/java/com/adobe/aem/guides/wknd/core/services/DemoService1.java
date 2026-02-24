package com.adobe.aem.guides.wknd.core.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.config.DemoConfig1;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;



@Component(
    service = DemoService1.class,
    immediate = true
)
@Designate(ocd = com.adobe.aem.guides.wknd.core.config.DemoConfig1.class)
public class DemoService1 {
    
    private static final Logger LOG = LoggerFactory.getLogger(DemoService1.class);

    public String getShowConfig() {
        return showConfig;
    }

    private String showConfig="";

    @Activate
    public void activate(DemoConfig1 demoConfig1){
        LOG.info("Demo Service 1 is activated");
        LOG.info("Number: "+demoConfig1.number());
        showConfig=demoConfig1.show();
        //LOG.info(Long.toString(componentContext.getBundleContext().getBundle().getBundleId()));
        //LOG.info(bundleContext.getBundle().getBundleId());
    }    

    @Modified
    public void modified(DemoConfig1 demoConfig1){
        LOG.info("Demo Service 1 is modified");
        LOG.info("Number: "+demoConfig1.number());
        showConfig=demoConfig1.show();
    }

    public String show(){
        return "Returned by Demo Service 1";
    }

    @Reference
    private ResourceResolverFactory resolverFactory;

    public Iterator<Page> getPages() throws LoginException{
        //this method return all the Children of /content/wknd
        ResourceResolver resolver;

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "wkndService");
        resolver = resolverFactory.getServiceResourceResolver(paramMap);
        //Resource parentResource = resolver.getResource("/content/wknd");
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page parentPage = pageManager.getPage("/content/wknd/in");
        return parentPage.listChildren();
    }

}
