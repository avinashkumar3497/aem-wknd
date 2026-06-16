package com.adobe.aem.guides.wknd.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;

import com.adobe.aem.guides.wknd.core.services.MultiService1;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class MultiServiceDemoTest {

    private final AemContext context = new AemContext();
    private MultiServiceDemo multiServiceDemo;
    private MultiService1 mockService;

    @BeforeEach
    void setup(){
        //create a mock service
        mockService = mock(MultiService1.class);
        when(mockService.getValue()).thenReturn("returned from mockService for service: MultiService1");

        //register the mock service in OSGi context
        context.registerService(MultiService1.class, mockService);

        // Create a request and adapt it to the model
        SlingHttpServletRequest request = context.request();
        multiServiceDemo = request.adaptTo(MultiServiceDemo.class);
    }

    @Test
    void testGetVal(){
        assertEquals("returned from mockService for service: MultiService1", multiServiceDemo.getVal());
    }

}
