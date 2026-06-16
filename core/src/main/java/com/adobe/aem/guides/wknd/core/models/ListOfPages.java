package com.adobe.aem.guides.wknd.core.models;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import com.adobe.aem.guides.wknd.core.services.DemoService2;
import com.adobe.aem.guides.wknd.core.services.DemoServiceB2;
import com.day.cq.wcm.api.Page;

@Model(
    adaptables = SlingHttpServletRequest.class
)
public class ListOfPages {
    
    @OSGiService
    private DemoService2 demoService2;

    @OSGiService
    private DemoServiceB2 demoServiceB2;

    public String getValue(){
        return demoService2.getVal();
    }

    public List<String> getPagesList() throws LoginException{
        return demoServiceB2.getPagesTitles();
    }
}
