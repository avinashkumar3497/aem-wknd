package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(
    adaptables = Resource.class
)
public class AuthorBooks1 {
    
    @Self
    Resource resource;

    @Inject
    @Default(values = "Default Value")
    @Required
    private String authorName;

    public String getAuthorName()
    {
        return authorName;
    }

    // @Inject
    // List<String> booksName;

    public List<String> getBooksName(){
        List<String> l = new ArrayList<>();
        Resource multifieldNodeResource = resource.getChild("booksName");
        Iterator<Resource> itr  = multifieldNodeResource.getChildren().iterator();
        while(itr.hasNext()){
            l.add(itr.next().getValueMap().get("booksName",String.class));
        }
        return l;
    }
}
