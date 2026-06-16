package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import com.adobe.aem.guides.wknd.core.services.MultiService;
import com.adobe.aem.guides.wknd.core.services.MultiService1;

@Model(
    adaptables = SlingHttpServletRequest.class
)
public class MultiServiceDemo {
    @OSGiService
    private MultiService1 multiService;

    public String getVal()
    {
        return multiService.getValue();
    }
}
