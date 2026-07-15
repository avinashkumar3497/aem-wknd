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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/locationfinder",
        "sling.servlet.methods=GET"
    }
)
@Designate(ocd = LocationFinderServlet.Configuration.class)
public class LocationFinderServlet extends SlingSafeMethodsServlet {

    private static Logger LOG = LoggerFactory.getLogger(LocationFinderServlet.class);
    private String api_Url="";
    private String api_Key="";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException
    {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RequestConfig config = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(5000).build();

        String apiUrl = api_Url
                        + URLEncoder.encode(request.getParameter("query"), StandardCharsets.UTF_8.name())
                        + "&count=5&language=en&format=json";

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            
            HttpGet getRequest = new HttpGet(apiUrl);
            LOG.info("value of apiUrl: {}",apiUrl);
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

    @Activate
    @Modified
    protected void activate(Configuration configuration){
        LOG.info("API URL from config: {} | API Key from config: {}", configuration.apiUrlValue(), configuration.apiKeyValue());
        api_Url=configuration.apiUrlValue();
    }

    @ObjectClassDefinition(
        name = "config for Location Finder Servlet",
        description = "configuration used for Location Finder Servlet"
    )
    public @interface Configuration{
        
        @AttributeDefinition(
            name="API URL"
        )
        public String apiUrlValue() default "default_URL";

        @AttributeDefinition(
            name = "API Key"
        )
        public String apiKeyValue() default "default_key";

    }
}
