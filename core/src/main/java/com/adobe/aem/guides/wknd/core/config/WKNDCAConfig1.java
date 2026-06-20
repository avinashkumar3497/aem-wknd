package com.adobe.aem.guides.wknd.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Demo CAC", description = "CAC config")
public @interface WKNDCAConfig1 {

    @Property(label = "country", description = "country config")
    public String country() default "USA";

}
