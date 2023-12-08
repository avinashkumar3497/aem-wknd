package com.adobe.aem.guides.wknd.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.adobe.aem.guides.wknd.core.models.TryItOut;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = TryItOut.class,
    resourceType = "/wknd/components/tryitout",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TryItOutImpl implements TryItOut {

    @Override
    public String returnSomething() {
        return "Something";
    }
    
}
