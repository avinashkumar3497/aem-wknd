package com.adobe.aem.guides.wknd.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Roche ES Config Service")
public @interface ElasticSearchConfigServiceImplConfig {

    int DEFAULT_TIMEOUT = 15000;

    String DEFAULT_END_POINT = "https://webhook.site/7abc8160-f958-4aac-b8db-4a4796661290/api/engines/{engine}/crawler/crawl_requests";

    String DEFAULT_API_KEY = "Bearer private-test-123";

    @AttributeDefinition(name = "apiEndPoint", description = "ES Partial Crawl endpoint")
    String apiEndPoint() default DEFAULT_END_POINT;

    @AttributeDefinition(name = "privateKey", description = "Private Key for Authorization")
    String privateKey() default DEFAULT_API_KEY;

    @AttributeDefinition(name = "Connect timeout", description = "Connect Timeout value")
    int connectTimeout() default DEFAULT_TIMEOUT;

    @AttributeDefinition(name = "Connection request timeout", description = "Connection Request Timeout value")
    int connectionRequestTimeout() default DEFAULT_TIMEOUT;

    @AttributeDefinition(name = "Socket timeout", description = "Socket Timeout value")
    int socketTimeout() default DEFAULT_TIMEOUT;

    @AttributeDefinition(name = "allowedTemplates", description = "List of allowed Templates")
    String[] allowedTemplates() default {"/conf/wknd/settings/wcm/templates/page-content"};

}
