package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component(
    service = Servlet.class,
    property={
        "sling.servlet.resourceTypes=wknd/components/tools/manualcrawl",
        "sling.servlet.methods=POST",
        "sling.servlet.selectors=submit",
        "sling.servlet.extensions=json"
    }
)
public class ManualCrawlServlet extends SlingAllMethodsServlet{
    
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ManualCrawlServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        final String pagePath;    //Never keep request data in instance fields. Servlet = shared singleton OSGi Service
        pagePath = request.getParameter("pagePath");  //page path received as parameter
        final JsonObject jsonResponseObject = new JsonObject(); //jsonresponse object

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(pagePath==null){
            jsonResponseObject.addProperty("status","error");
            jsonResponseObject.addProperty("message","Page Path is null");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
        else if(pagePath.isBlank()){
            jsonResponseObject.addProperty("status","error");
            jsonResponseObject.addProperty("message","Page Path is empty");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
        else if(!pagePath.startsWith("/content")){
            jsonResponseObject.addProperty("status","error");
            jsonResponseObject.addProperty("message","Invalid Repository path");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
        else{
            jsonResponseObject.addProperty("status","success");
            jsonResponseObject.addProperty("message","Request receieved");
            jsonResponseObject.addProperty("pagePath: ",pagePath);

        }
        String jsonResponseString = new Gson().toJson(jsonResponseObject);
        response.getWriter().write(jsonResponseString);
    }
}
