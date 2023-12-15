package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import com.adobe.aem.guides.wknd.core.models.TryItOut;
import com.adobe.aem.guides.wknd.core.services.DemoService;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = TryItOut.class,
    resourceType = "/wknd/components/tryitout",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TryItOutImpl implements TryItOut {

    @OSGiService
    private DemoService demoService;
    private String demoServiceConfigValues="|||";
    
    @Override
    public String returnSomething() {
        return demoService.doSomething();
    }

    @Override
    public String configValues() {
        for(int i=0; i<demoService.getAllService().size() ; i++){
            try{
                String smString= demoService.getAllService().get(i).getSomeSetting();
                    demoServiceConfigValues = demoServiceConfigValues.concat(smString+"|||");
            }
            catch(Exception e)
            {
                demoServiceConfigValues = demoServiceConfigValues.concat(e.getMessage()+"|||");
            }
        }
       return demoServiceConfigValues;
    }  

    public List<DemoService> getAllConfigss(){
        return demoService.getAllConfigs();
    }
}
