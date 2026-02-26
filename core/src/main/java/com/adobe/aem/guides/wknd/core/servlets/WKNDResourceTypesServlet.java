package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=wknd/components/page"
    }
)
public class WKNDResourceTypesServlet extends SlingSafeMethodsServlet {
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
}
