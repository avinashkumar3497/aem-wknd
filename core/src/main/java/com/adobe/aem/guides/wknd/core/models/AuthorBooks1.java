package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public List<Map<String,String>> getBooksDetails(){
        List<Map<String,String>> l = new ArrayList<>();
        Resource multifieldNodeResource = resource.getChild("booksDetails");
        // Iterator<Resource> itr  = multifieldNodeResource.getChildren().iterator();
        // while(itr.hasNext()){
        //     l.add(itr.next().getValueMap().get("booksName",String.class));
        // }
        for(Resource entryNode: multifieldNodeResource.getChildren()){
            Map<String,String> map;
            map = new HashMap<>();
            map.put("bookName", entryNode.getValueMap().get("bookName", String.class));
            map.put("bookSubject", entryNode.getValueMap().get("bookSubject", String.class));
            l.add(map);
        }
        return l;
    }
}
