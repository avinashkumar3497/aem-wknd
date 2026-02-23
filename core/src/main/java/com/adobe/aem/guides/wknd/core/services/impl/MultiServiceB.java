package com.adobe.aem.guides.wknd.core.services.impl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;

import com.adobe.aem.guides.wknd.core.services.MultiService;

@Component(
    service = MultiService.class,
    immediate = true
)
@ServiceRanking(1010)
public class MultiServiceB implements MultiService{

    @Override
    public String show() {
        return "Returned from MultiServiceB";
    }

}
