package com.adobe.aem.guides.wknd.core.services;

import java.util.Iterator;

import org.apache.sling.api.resource.LoginException;

import com.day.cq.wcm.api.Page;

public interface DemoService2 {
    public String getVal();
    public Iterator<Page> getPages() throws LoginException;
}
