package com.adobe.aem.guides.wknd.core.models.impl;



import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.models.ModelDemo1;
import com.day.cq.wcm.api.PageManager;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = ModelDemo1.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ModelDemoImpl1 implements ModelDemo1{

    private static final Logger LOG = LoggerFactory.getLogger(ModelDemoImpl1.class);

    @SlingObject
    private SlingHttpServletRequest req;

    @SlingObject
    private ResourceResolver resolver;
    
    @ValueMapValue
    private String name;

    @ValueMapValue
    private int score;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getTitle() {
        return resolver.adaptTo(PageManager.class).getContainingPage(resolver.getResource("/content/wknd/us/en/about-us")).getTitle();
    }

    @RequestAttribute(name = "rAttr")
    private String rAttr;

    @Override
    public String getReqAttribute() {
        return rAttr;
    }

    @PostConstruct
    protected void constructed(){
        LOG.info("ModelDemo1 model is constructed");
    }

}
