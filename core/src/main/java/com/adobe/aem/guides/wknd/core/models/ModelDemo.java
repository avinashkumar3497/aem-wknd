package com.adobe.aem.guides.wknd.core.models;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Activate;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ModelDemo {
   
    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resolver;

    @Inject
    private PageManager pageManager;

    @RequestAttribute(name = "country")
    private String country;

    @Required
    @ValueMapValue
    @Default(values = "Default Name")
    private String name;

    public String getName()
    {
        return name;
    }

    @Activate
    protected void activate(){
        
    }

    public String getTitle(){
        return getCurrentPage().getTitle();
    }

    public String getTitleSpecific(){
        return pageManager.getContainingPage(resolver.getResource("/content/wknd/us/en/about-us")).getTitle();
    }

    private Page getCurrentPage(){
        return pageManager.getContainingPage(request.getResource());
    }

    public String getCountry()
    {
        return country;
    }
}
