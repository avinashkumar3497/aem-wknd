package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.aem.guides.wknd.core.config.WkndCAConfig;

@Model(
    adaptables = SlingHttpServletRequest.class
)
public class CACDemoModel {

    @Self
    SlingHttpServletRequest request;

    Resource currentResource;

    public String getCountry(){
        currentResource=request.getResource();
        ConfigurationBuilder configurationBuilder = currentResource.adaptTo(ConfigurationBuilder.class);
        WkndCAConfig wkndCAConfig = configurationBuilder.as(WkndCAConfig.class);
        return wkndCAConfig.countryName();
    }
}
