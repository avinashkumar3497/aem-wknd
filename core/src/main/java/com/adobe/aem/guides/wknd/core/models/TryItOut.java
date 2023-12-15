package com.adobe.aem.guides.wknd.core.models;

import java.util.List;

import com.adobe.aem.guides.wknd.core.services.DemoService;

public interface TryItOut {
    
    public String returnSomething();
    public String configValues();
    public List<DemoService> getAllConfigss();
}
