package com.adobe.aem.guides.wknd.core.helper;

import java.util.Arrays;

import org.apache.http.client.methods.HttpPost;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.services.ElasticSearchConfigService;

public class ElasticSearchHelper {

    private static final String ENGINE_NAME = "wknd_engine";

    public HttpPost processIncrementalCrawlRequest(String pagePath,ResourceResolver resolver,ElasticSearchConfigService cfg)
    {
        final Resource page = resolver.getResource(pagePath);
        String apiEndPoint = cfg.getApiEndPoint();
        
        //page should exist in jcr
        if(page==null) return null;

        //page should be having allowed templates only
        if(!checkValidTemplate(page,cfg)) return null;

        //engineName should not be null or blank
        // stays relevant once engineName is resolved dynamically.
        if(ENGINE_NAME==null||ENGINE_NAME.isBlank()) return null;

        //apiEndPoint should not be null or blank
        if(apiEndPoint==null||apiEndPoint.isBlank()) return null;

        final HttpPost httpPost = new HttpPost(apiEndPoint.replace("{engine}", ENGINE_NAME));

        return httpPost;
    }

    private boolean checkValidTemplate(Resource page, ElasticSearchConfigService cfg){
        Resource jcrContentNode = page.getChild("jcr:content");
        if(jcrContentNode==null) return false;
        ValueMap valueMap = jcrContentNode.getValueMap();
        String pageTemplate = valueMap.get("cq:template", String.class);
        return cfg.getAllowedTemplates()!=null && Arrays.asList(cfg.getAllowedTemplates()).contains(pageTemplate);
    }

}
