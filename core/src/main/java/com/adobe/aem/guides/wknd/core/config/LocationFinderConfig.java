package com.adobe.aem.guides.wknd.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition (
    name = "Location Finder 1 config",
    description = "Location Finder 1 config description"
)
public @interface LocationFinderConfig {
    
    @AttributeDefinition (name = "apiUrl", description = "apiUrl description")
    public String apiUrl() default "https://geocoding-api.open-meteo.com/v1/search";

    @AttributeDefinition (name = "maxResults")
    public int maxResults() default 5;

    @AttributeDefinition (name = "timeout in milliseconds")
    public int timeoutMs() default 5000;

    @AttributeDefinition (name = "API Key",
        type = AttributeType.PASSWORD
    )
    public String apiKey() default "";
}
