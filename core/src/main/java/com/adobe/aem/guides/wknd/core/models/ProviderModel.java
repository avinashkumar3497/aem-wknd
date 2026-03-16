package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import com.adobe.aem.guides.wknd.core.pojos.MedicalProvider;
import com.adobe.aem.guides.wknd.core.services.ProviderService;

@Model(
    adaptables = SlingHttpServletRequest.class,
    resourceType = "apps/component/api-integration-component",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProviderModel {

    @OSGiService
    ProviderService providerService;

    public java.util.List<MedicalProvider> getProvidedData(){
        return providerService.getProviders();
    }
}
