package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.helper.MultifieldHelper2;

@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class AuthorBooks2 {
    
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AuthorBooks2.class);

    @Self
    SlingHttpServletRequest req;

    @ValueMapValue
    private String authorname;

    public String getAuthorName(){
        return authorname;
    }

    public List<String> getBooks(){
        List<String> booknames = new ArrayList<>();
        try {
            Resource booknamesNode = req.getResource().getChild("booknames");
            if(booknamesNode!=null){
                for(Resource bookNameNode : booknamesNode.getChildren()){
                    booknames.add(bookNameNode.getValueMap().get("booknames",String.class));
                }
            }
        } catch (Exception e) {
            LOG.info("error while getting book details {}",e.getMessage());
        }
        return booknames;
    }

    public List<MultifieldHelper2> getBookDetails(){
        List<MultifieldHelper2> bookDetails = new ArrayList<>();
        MultifieldHelper2 multifieldHelper2;
        try {
            Resource bookDetailsNode = req.getResource().getChild("bookdetails");
            if(bookDetailsNode!=null){
                for(Resource r : bookDetailsNode.getChildren()){
                    multifieldHelper2 = new MultifieldHelper2(
                        r.getValueMap().get("bookname",String.class),
                        r.getValueMap().get("bookpublisher",String.class),
                        r.getValueMap().get("copies",Integer.class)
                    );
                    bookDetails.add(multifieldHelper2);
                }
            }
        } catch (Exception e) {
            LOG.info("cant get book details: {}", e.getMessage());
        }
        return bookDetails;
    }

    @RequestAttribute(name = "retr")
    private String retr;
    public String getRtr(){
        return retr;
    }

}
