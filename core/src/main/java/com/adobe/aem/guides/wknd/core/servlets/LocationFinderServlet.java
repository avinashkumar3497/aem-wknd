package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/locationfinder",
        "sling.servlet.methods=GET"
    }
)
public class LocationFinderServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException
    {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RequestConfig config = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(5000).build();

        String apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name="  
                        + URLEncoder.encode(request.getParameter("query"), StandardCharsets.UTF_8.name())
                        + "&count=5&language=en&format=json";

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            
            HttpGet getRequest = new HttpGet(apiUrl);

            try (CloseableHttpResponse httpResponse = httpClient.execute(getRequest)) {
                int status = httpResponse.getStatusLine().getStatusCode();
                if(status == 200){
                    String json = EntityUtils.toString(httpResponse.getEntity(),StandardCharsets.UTF_8);
                    response.getWriter().write(json);
                }else{
                    response.setStatus(status);
                    response.getWriter().write("{\"error\":\"External API returned status " + status + "\"}");

                }
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
