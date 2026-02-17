package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;

@Model(
    adaptables = Resource.class
)
public class AuthorBooks1 {
    
    @Inject
    @Default(values = "Default Value")
    @Required
    private String authorName;

    public String getAuthorName()
    {
        return authorName;
    }

    public List<String> getBooksName(){
        List<String> l = new ArrayList<>();
        Collections.addAll(l,"aaaa","bbbb","cccc","dddd");
        return l;
    }
}
