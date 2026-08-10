package com.adobe.aem.guides.wknd.core.models;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FeaturedResourcesModel {

    @ChildResource
    private List<ResourceItem> resources;

    public List<ResourceItem> getResources() {
        return resources;
    }

}
