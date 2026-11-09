package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model (
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Testimonial {
    
    @ValueMapValue 
    private String quote;
    @ValueMapValue 
    private String authorName;
    @ValueMapValue 
    private String authorImage;
    @ValueMapValue 
    private String layout;
    @ValueMapValue 
    private boolean showRating;
    
    public String getQuote() {
        return quote;
    }
    public String getAuthorName() {
        return authorName;
    }
    public String getAuthorImage() {
        return authorImage;
    }
    public String getLayout() {
        return layout;
    }
    public boolean isShowRating() {
        return showRating;
    }

    public boolean hasContent(){
        return true; //needs to bereplaced with the actual code to determine the emptyness
    }
}
