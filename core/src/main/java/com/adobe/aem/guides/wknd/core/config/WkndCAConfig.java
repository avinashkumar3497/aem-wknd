package com.adobe.aem.guides.wknd.core.config;

import org.apache.sling.caconfig.annotation.*;

@Configuration(label="Wknd CA Config", description = "Wknd CA Config")
public @interface WkndCAConfig {

    @Property(label ="country name")
    String countryName() default "No country Assigned" ;
}
