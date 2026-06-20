package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

@Component(
    service=Servlet.class,
    property = {
        "sling.servlet.paths="+"/bin/avinash"
    }

)
@ServiceDescription("demo servlet")
public class WKNDPathsServlet1 extends SlingSafeMethodsServlet{
    @Override
    protected void doGet(SlingHttpServletRequest req,SlingHttpServletResponse response) throws ServletException, IOException{
        // response.setContentType("text/plain");
        // response.getWriter().write("hello from WKNDPathsServlet1");
        response.setContentType("application/json");
    }
}
