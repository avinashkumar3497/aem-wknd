package com.adobe.aem.guides.wknd.core.config;

import org.osgi.service.metatype.annotations.*;

@ObjectClassDefinition(name = "Demo Service Configs", description = "praticing the configs")
public @interface DemoConfig {
    @AttributeDefinition(name = "some setting", description = "descripyion")
    String someSetting() default "default value";
}

