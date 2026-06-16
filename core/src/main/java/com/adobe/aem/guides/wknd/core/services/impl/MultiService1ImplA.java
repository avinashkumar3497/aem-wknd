package com.adobe.aem.guides.wknd.core.services.impl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;

import com.adobe.aem.guides.wknd.core.services.MultiService1;

@Component(service = MultiService1.class, immediate = true)
public class MultiService1ImplA implements MultiService1{

    @Override
    public String getValue() {
        return "returned from MultiService1ImplA";
    }

}
