package com.adobe.aem.guides.wknd.core.helper;

import java.util.Arrays;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.services.ElasticSearchConfigService;
import com.google.gson.*;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ElasticSearchHelper {

    private static Logger LOG = LoggerFactory.getLogger(ElasticSearchHelper.class);

    private static final String ENGINE_NAME = "wknd_engine";
    
    /** The Constant MAX_CRAWL_DEPTH */
    private static final String MAX_CRAWL_DEPTH = "max_crawl_depth";

    /** The Constant SEED_URLS */
    private static final String SEED_URLS = "seed_urls";

    /** The Constant SITEMAP_URLS */
    private static final String SITEMAP_URLS = "sitemap_urls";

    /** The Constant OVERRIDES */
    private static final String OVERRIDES = "overrides";

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

    public void createPostBodyRequest(HttpPost request, String privateKey, String path) {

        // Step 2 — headers
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        request.setHeader(HttpHeaders.AUTHORIZATION, privateKey);

        try {
            // Step 3 — build JSON body
            JsonObject overridesJson = new JsonObject();
            overridesJson.addProperty(MAX_CRAWL_DEPTH, 1);
            overridesJson.add(SEED_URLS, new JsonArray());
            overridesJson.add(SITEMAP_URLS, new JsonArray());

            JsonObject bodyJson = new JsonObject();
            bodyJson.add(OVERRIDES, overridesJson);

            // Step 4 — attach as StringEntity
            StringEntity entity = new StringEntity(bodyJson.toString(), "UTF-8");
            request.setEntity(entity);

            LOG.info("ES crawl request body: {}", bodyJson);

        } catch (Exception e) {
            LOG.error("Failed to build ES crawl request body", e);
        }
    }   
    
}
