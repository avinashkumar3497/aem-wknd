package com.adobe.aem.guides.wknd.core.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceRanking;

import com.day.cq.wcm.api.Page;

@Component(
    service = DemoServiceB.class,
    immediate = true
)
@ServiceRanking(1005)
public class DemoServiceB {

    @Reference
    DemoService1 demoService1;

    public List<String> getPages() throws LoginException{
        Iterator<Page> itr = demoService1.getPages();
        List<String> l = new ArrayList<>();
        while (itr.hasNext()) {
            l.add(itr.next().getTitle());
        }
        return l;
    }
}
