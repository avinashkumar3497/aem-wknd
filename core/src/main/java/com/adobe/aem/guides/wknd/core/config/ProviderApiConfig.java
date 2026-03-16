package com.adobe.aem.guides.wknd.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name="Provider API Config",
    description = "Stores configuration for the external medical provider API"    
)
public @interface ProviderApiConfig {
    @AttributeDefinition(
        name = "API URL",
        description = "Full URL of the external API"
    )
    String apiUrl() default "https://api.example.com/providers";

    @AttributeDefinition(
        name = "API Key",
        description = "API key for authentication"
    )
    String apiKey() default "";

    @AttributeDefinition(
        name = "Cache TTL (seconds)",
        description = "Time-to-live for cache"
    )
    int cacheTTL() default 300;
}
