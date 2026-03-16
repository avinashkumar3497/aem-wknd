package com.adobe.aem.guides.wknd.core.services.impl;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jackrabbit.oak.api.Type;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.LoggerFactory;
import com.google.common.reflect.TypeToken;

import com.adobe.aem.guides.wknd.core.config.ProviderApiConfig;
import com.adobe.aem.guides.wknd.core.pojos.MedicalProvider;
import com.adobe.aem.guides.wknd.core.services.ProviderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TypeKey;
import com.google.gson.Gson;

@Component(
    service = ProviderService.class,
    immediate = true
)
@Designate(ocd = ProviderApiConfig.class)
public class ProviderServiceImpl implements ProviderService {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ProviderServiceImpl.class);

    private String apiUrl;
    private String apiKey;
    private int cacheTTL;
    List<MedicalProvider> cachedProviders; //what is the reason we can't put it in getProviders() method? - because method scope is only when it's called. But this one will stay till the service is active
    private long cacheExpiryTime;

    @Activate
    @Modified
    protected void activate(ProviderApiConfig providerApiConfig){
        LOG.info("ProviderService activated/modified");
        LOG.info("Api URL:{}",providerApiConfig.apiUrl());
        apiUrl = providerApiConfig.apiUrl();
        apiKey = providerApiConfig.apiKey();
        cacheTTL = providerApiConfig.cacheTTL();
        getProviders();
    }

    @Override
    public List<MedicalProvider> getProviders() {
        // List<MedicalProvider> demoList = new ArrayList<>();
        // demoList.add(new MedicalProvider(0, "name1", "email1"));
        // demoList.add(new MedicalProvider(2, "name2", "email2"));
        
        Long now = System.currentTimeMillis();

        if(cachedProviders!=null && now<cacheExpiryTime){
            return cachedProviders;
        }
        
        List<MedicalProvider> providers = callExternalApi();
        cachedProviders = providers;
        cacheExpiryTime = now + (cacheTTL*1000);

        return providers;
    }

    private List<MedicalProvider> callExternalApi(){
                try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet httpGetRequest = new HttpGet(apiUrl);
            httpGetRequest.addHeader("x-api-key",apiKey);
            try(CloseableHttpResponse response = client.execute(httpGetRequest)){
                if(response.getStatusLine().getStatusCode()==200){
                    //demoList.add(EntityUtils.toString(response.getEntity()).substring(0, 50));
                    String jsonString = EntityUtils.toString(response.getEntity());
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<MedicalProvider> providers = objectMapper.readValue(jsonString, new TypeReference<List<MedicalProvider>>() {});
                    LOG.info(providers.get(0).toString());
                    return providers;
                }else{
                    LOG.info("API returned status: "+response.getStatusLine().getStatusCode());
                    return Collections.emptyList();
                }
            }
            catch(Exception e){
                LOG.warn("Error getting response from API:"+e.getMessage());
                return Collections.emptyList();
            }
        }catch(IOException e){
            LOG.info("Error calling API:"+e.getMessage());
            return Collections.emptyList();
        }
    }

}
