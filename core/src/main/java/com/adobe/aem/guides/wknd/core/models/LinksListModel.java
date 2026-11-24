package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Component;

@Model (
    adaptables = {SlingHttpServletRequest.class, Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class LinksListModel {
    
    @ValueMapValue 
    private String title;

    @ChildResource 
    private List<Resource> links;

    List<LinkItem> linkItems = new ArrayList<>();

    @PostConstruct 
    private void init(){
        for(Resource resource: links){
            LinkItem lItem = new LinkItem();
            lItem.setLabel(resource.getValueMap().get("label",String.class));
            lItem.setPath(resource.getValueMap().get("path",String.class));
            lItem.setNewTab(resource.getValueMap().get("newTab",Boolean.class));
            if(StringUtils.isBlank(lItem.getLabel())||StringUtils.isBlank(lItem.getPath())) continue;
            linkItems.add(lItem);
        }
    }

    public List<LinkItem> getLinks(){
        return linkItems;
    }
}
