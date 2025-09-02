package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.models.AuthorBooks;

@Model(
    adaptables = SlingHttpServletRequest.class, 
    adapters = AuthorBooks.class, 
    defaultInjectionStrategy = org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL)
public class AuthorBooksImpl implements AuthorBooks {

    @ValueMapValue
    @Default(values = "Unknown Author")
    private String authorname;

    @ValueMapValue
    private List<String> books;
    
    @Override
    public String getAuthorName() {
        return authorname;
    }

    @Override
    public java.util.List<String> getAuthorBooks() {
        if(books != null) {
            return new ArrayList<String>(books);
        }   
        else return Collections.emptyList();
    }

}
