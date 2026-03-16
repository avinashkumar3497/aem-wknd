package com.adobe.aem.guides.wknd.core.services;

import java.util.List;

import com.adobe.aem.guides.wknd.core.pojos.MedicalProvider;

public interface ProviderService {
    List<MedicalProvider> getProviders();
}
