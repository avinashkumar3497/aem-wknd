package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=core/wcm/components/page/v3/page",
        "sling.servlet.selectors=geeks",
        "sling.servlet.selectors=test",
        "sling.servlet.extensions=json",
        "sling.servlet.extensions=myjson",
        "sling.servlet.methods="+HttpConstants.METHOD_POST,
        "sling.servlet.methods="+HttpConstants.METHOD_GET
    }
)
public class WKNDResourceTypesServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WKNDResourceTypesServlet.class);
    @Override
    protected void doGet(SlingHttpServletRequest req,SlingHttpServletResponse resp) throws ServletException, IOException{
        LOG.info("WKNDResourceTypesServlet triggered");
        LOG.info("Page Title: "+req.getResource().getValueMap().get("jcr:title").toString());
        // resp.setContentType("text/plain");
        // resp.getWriter().write("Title: "+req.getResource().getValueMap().get("jcr:title").toString());
        resp.setContentType("application/json");
        resp.getWriter().write("{\"Title\":\""+req.getResource().getValueMap().get("jcr:title").toString()+"\"}");
    }

    @Override
    protected void doPost(SlingHttpServletRequest req,SlingHttpServletResponse resp) throws ServletException, IOException{
        LOG.info("Post method triggered");
        resp.getWriter().write("doPost method triggered");
    }
}
