package com.adobe.aem.guides.wknd.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import com.adobe.aem.guides.wknd.core.models.TryItOut;
import com.adobe.aem.guides.wknd.core.services.DemoService;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = TryItOut.class,
    resourceType = "/wknd/components/tryitout",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TryItOutImpl implements TryItOut {

    @OSGiService
    private DemoService demoService;
    
    @Override
    public String returnSomething() {
        return demoService.doSomething();
    }
    
}
