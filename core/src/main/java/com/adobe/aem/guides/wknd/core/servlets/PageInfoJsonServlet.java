package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component (
    service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=wknd/components/page",
        "sling.servlet.selectors=summary",
        "sling.servlet.extensions=json"
    }
)
@ServiceDescription("returns basic page information as JSON")
public class PageInfoJsonServlet extends SlingSafeMethodsServlet{
    private static final Logger LOG = LoggerFactory.getLogger(PageInfoJsonServlet.class);
    @Override 
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException{
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        // res.getWriter().write("{\"name\": \"Avinash\"}");
        
        //getting the resolver inside doGet
        try {
            ResourceResolver resolver = req.getResourceResolver();
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            Page page = pageManager.getContainingPage(req.getResource());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("path",page.getPath());
            jsonObject.addProperty("title", page.getTitle());
            jsonObject.addProperty("templatePath", page.getTemplate().getPath());
            jsonObject.addProperty("lastModifiedDate", page.getLastModified().getTime().toString());
            Gson gson = new Gson();
            res.getWriter().write(gson.toJson(jsonObject));
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        } 
//         Obtains the service resolver
// Reads /content/products/product-a
// Closes the resolver correctly
// Handles LoginException

// try(ResourceResolver resolver = resolverFactory.getServiceResourceResolver(Map.of(ResourceResolverFactory.SUBSERVICE,"myapp-product-reader"))){
//     resolver.getResource("/content/products/product-a");
// }
// catch(Exception e){
//     LOG.error("Login Eexcption occured while reading product: {}",e);
// }

    }
}
