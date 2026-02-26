package com.adobe.aem.guides.wknd.core.servlets;



import java.io.IOException;

import javax.json.JsonArray;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/pages",
        "sling.servlet.paths=/wknd/pages"
    }
)
public class WKNDPathsServlet extends SlingSafeMethodsServlet{
    private static final Logger LOG = LoggerFactory.getLogger(WKNDPathsServlet.class);
    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException, ServletException{
        LOG.info("doGet method triggered with Paths based servlet");
        //res.getWriter().write("WKNDPathsServlet");
        JSONArray myArray = new JSONArray();
        JSONObject myJsonObject = new JSONObject();
        myJsonObject.put("demoKey", "demoValue");
        myArray.put(myJsonObject);
        JSONObject myJsonObject2 = new JSONObject();
        myJsonObject2.put("demoKey1", "demoValue1");
        myArray.put(myJsonObject2);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(myArray.toString());
    }
}
