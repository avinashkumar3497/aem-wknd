package com.adobe.aem.guides.wknd.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ResourceItemTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        // ARRANGE: register model + load test content
        context.addModelsForClasses(ResourceItem.class);
        context.load().json("/com/adobe/aem/guides/wknd/core/models/ResourceItemTest.json", "/content/test");
    }

    @Test
    void testGetters() {
        // ACT: adapt the item0 resource to ResourceItem
        ResourceItem item = context.resourceResolver()
                .getResource("/content/test/item0")
                .adaptTo(ResourceItem.class);

        // ASSERT
        assertNotNull(item);
        assertEquals("Dosage Guide", item.getResourceTitle());
        assertEquals("How to dose the medication", item.getDescription());
        assertEquals("/content/dam/wknd/dosage-guide.pdf", item.getFileReference());
    }
}