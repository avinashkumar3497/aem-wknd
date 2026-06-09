package com.adobe.aem.guides.wknd.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.ResourceResolver;

public class AemContextTest {

    private final AemContext context = new AemContext();

    @Test
    void testContextCreated() {

        assertNotNull(context.resourceResolver());
    }

    @Test
    void testCreateResource() {

        context.create().resource(
                "/content/article",
                "title", "AEM Rocks"
        );

        ResourceResolver resolver = context.resourceResolver();

        // assertNotNull(resolver.getResource("/content/article"));
        String result = resolver.getResource("/content/article").getValueMap().get("title", String.class);

        assertEquals("AEM Rocks", result);
    }
}
