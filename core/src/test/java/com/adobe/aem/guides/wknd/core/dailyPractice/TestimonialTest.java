package com.adobe.aem.guides.wknd.core.dailyPractice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.aem.guides.wknd.core.models.Testimonial;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class TestimonialTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(Testimonial.class);
        context.load().json("/com/adobe/aem/guides/wknd/core/models/testimonial.json", "/content/testimonial");
    }

    @Test
    void modelAdapts() {
        context.currentResource("/content/testimonial/full");
        Testimonial model = context.request().adaptTo(Testimonial.class);
        assertNotNull(model);
    }

    @Test 
    void fullConent_returnsAllValues(){
        //act
        //we have the sling model ready with us, mocked using AEMMocks
        //also, we have the JCR node mocked with us
        //now, we need to adapt the request adaptable model from the request.
        //but, how does the request is associated with the resource? the answer is, we need to set the current resource:
        context.currentResource("/content/testimonial/full");
        //now, adapting model from the request
        Testimonial adaptedModel = context.request().adaptTo(Testimonial.class);
        
        //assert
        //now, we need to match the expected values with the returned values
        final String EXPECTEDQUOTE = "<p>Great service</p>";
        final String EXPECTEDAUTHORNAME = "Avinash";
        
        assertEquals(EXPECTEDQUOTE, adaptedModel.getQuote());
        assertEquals(EXPECTEDAUTHORNAME, adaptedModel.getAuthorName());
    }

    @Test 
    void noLayout_defaultsToLeft(){
        context.currentResource("/content/testimonial/noLayout");
        Testimonial adaptedModel = context.request().adaptTo(Testimonial.class);
        assertEquals("left", adaptedModel.getLayout());
    }

    @Test 
    void missingQuote_isEmpty(){
        context.currentResource("/content/testimonial/emptyQuote");
        Testimonial adaptedModel = context.request().adaptTo(Testimonial.class);
        assertTrue(adaptedModel.hasContent());
    }

    @Test 
    void maxQuoteLength(){
        context.currentResource("/content/testimonial/full");
        Testimonial adaptedModel = context.request().adaptTo(Testimonial.class);
        assertEquals(200, adaptedModel.getMaxQuoteLength());
    }
}
