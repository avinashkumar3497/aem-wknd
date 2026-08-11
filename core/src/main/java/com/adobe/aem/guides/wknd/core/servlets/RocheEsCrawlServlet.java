package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aem.guides.wknd.core.services.ElasticSearchConfigService;

@Component(
    service = Servlet.class,
    immediate = true,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=wknd/components/elasticCrawlRequest"
    }
    
)
public class RocheEsCrawlServlet extends SlingSafeMethodsServlet{
    
    @Reference
    private ElasticSearchConfigService configService;
    
    @Reference
    private ResourceResolverFactory factory;

    // commenting it until the helper is ready
    // @Reference
    // private ElasticSearchHelper esHelper;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException,IOException{
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("request recieved by RocheEsCrawlServlet");
    }
}
