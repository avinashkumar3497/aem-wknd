package com.adobe.aem.guides.wknd.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import com.adobe.aem.guides.wknd.core.config.ElasticSearchConfigServiceImplConfig;
import com.adobe.aem.guides.wknd.core.services.ElasticSearchConfigService;

@Component(service = ElasticSearchConfigService.class,
    immediate = true
)
@Designate(ocd = ElasticSearchConfigServiceImplConfig.class)
public class ElasticSearchConfigServiceImpl implements ElasticSearchConfigService{
    
    /**
     * Connect timeout
     */
    private int connectTimeout;

    /**
     * Connection Request Timeout
     */
    private int connectionRequestTimeout;

    /**
     * Socket Timeout
     */
    private int socketTimeout;

    /**
     * ES Crawl Request API end point
     */
    private String apiEndPoint;

    /**
     * Private key for authorization
     */
    private String privateKey;

    /**
     * allowed templates
     */
    private String[] allowedTemplates;

    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    @Override
    public int getSocketTimeout() {
        return socketTimeout;
    }

    @Override
    public String getApiEndPoint() {
        return apiEndPoint;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String[] getAllowedTemplates() {
        return allowedTemplates!=null?allowedTemplates.clone():null;
    }

    @Activate
    @Modified
    protected void activate(final ElasticSearchConfigServiceImplConfig config){
        connectTimeout = config.connectTimeout();
        connectionRequestTimeout = config.connectionRequestTimeout();
        socketTimeout = config.socketTimeout();
        apiEndPoint = config.apiEndPoint();
        privateKey = config.privateKey();
        allowedTemplates = config.allowedTemplates();
    }

}
