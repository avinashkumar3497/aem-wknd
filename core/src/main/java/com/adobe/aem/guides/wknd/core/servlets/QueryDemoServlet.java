package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(
    service=Servlet.class,
    property={
        "sling.servlet.paths=/bin/querydemo"
    }
)
public class QueryDemoServlet extends SlingSafeMethodsServlet {
    
    @Reference
    QueryBuilder queryBuilder;
    
    @Reference
    ResourceResolverFactory resolverFactory;

    ResourceResolver resourceResolver;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(QueryDemoServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res) throws IOException{
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "wkndService");
        try {
            resourceResolver = resolverFactory.getServiceResourceResolver(paramMap);
        } catch (LoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
          
        Session session = resourceResolver.adaptTo(Session.class);
        

        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("path","/content/wknd/us/en");
        queryMap.put("type", "cq:PageContent");

        Query query = null;
        if(session!=null)  query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
        SearchResult searchResult;
        if(query!=null) {
            searchResult=query.getResult();
            List<Hit> hits= new ArrayList<>();
            if(searchResult!=null) hits = searchResult.getHits();
            hits.forEach(a->{
                try {
                    res.getWriter().write("\n"+a.getResource().getValueMap().get("jcr:title", String.class).toString());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    LOG.warn(e.getMessage());
                } catch (RepositoryException e) {
                    // TODO Auto-generated catch block
                    LOG.warn(e.getMessage());
                }
            });
        }
        res.getWriter().write("\nResponse from QueryDemoServlet");
    }
}
