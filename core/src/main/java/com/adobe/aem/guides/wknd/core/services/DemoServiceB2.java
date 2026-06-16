package com.adobe.aem.guides.wknd.core.services;

import java.util.*;

import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.wcm.api.Page;

@Component(service = DemoServiceB2.class, immediate = true)
public class DemoServiceB2 {

    @Reference 
    DemoService2 demoService2;

    public java.util.List<String> getPagesTitles() throws LoginException{
        List titles = new ArrayList<>();
        Iterator<Page> itr = demoService2.getPages();
        while(itr.hasNext()){
            titles.add(itr.next().getTitle());
        }
        return titles;
    }

}
