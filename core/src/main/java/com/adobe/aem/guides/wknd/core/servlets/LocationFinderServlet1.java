package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.poifs.filesystem.EntryUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.config.LocationFinderConfig;
import com.google.gson.*;

@Component (
    service = Servlet.class,
    property = {
        "sling.servlet.paths="+"/bin/locationfinder1",
        "sling.servlet.methods="+ HttpConstants.METHOD_GET
    }
)
@Designate (ocd = com.adobe.aem.guides.wknd.core.config.LocationFinderConfig.class)
@ServiceDescription ("POC servlet Loc finder")
public class LocationFinderServlet1 extends SlingSafeMethodsServlet{

private static final Logger LOG = LoggerFactory.getLogger(LocationFinderServlet1.class);
private String API_URL = "https://geocoding-api.open-meteo.com/v1/search";
private static final String EXTRA_PARAMS = "&count={}&language=en&format=json";
private int TIMEOUT_MS = 5000;
private int maxResults = 5;
private String apiKey = "";

@Activate 
@Modified 
protected void activate(LocationFinderConfig config){
    API_URL = config.apiUrl();
    TIMEOUT_MS = config.timeoutMs();
    maxResults = config.maxResults();
    apiKey = config.apiKey();
}

@Override
protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException {
    res.setContentType("application/json");          // 1. set these FIRST
    res.setCharacterEncoding("UTF-8");

    String query = req.getParameter("query");
    if (StringUtils.isBlank(query)) {
        query = "Delhi";
    }
    query = query.trim();

    if (query.length() > 50) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JsonObject error = new JsonObject();
        error.addProperty("status", "error");
        error.addProperty("message", "Query too long");
        res.getWriter().write(error.toString());
        return;                                      // 2. stop here
    }

    String apiUrl = API_URL + "?name="
            + URLEncoder.encode(query, StandardCharsets.UTF_8.name()) + EXTRA_PARAMS;

    RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(TIMEOUT_MS).setSocketTimeout(TIMEOUT_MS).build();

    try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
         CloseableHttpResponse apiResponse = httpClient.execute(new HttpGet(apiUrl))) {

        int status = apiResponse.getStatusLine().getStatusCode();
        if (status == HttpServletResponse.SC_OK) {
            res.getWriter().write(EntityUtils.toString(apiResponse.getEntity(), StandardCharsets.UTF_8));
        } else {
            res.setStatus(status);
            res.getWriter().write("{\"status\":\"error\",\"message\":\"External API returned " + status + "\"}");
        }
    } catch (IOException e) {                         // 3. covers both resources
        LOG.error("Location Finder API call failed for query '{}'", query, e);
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        res.getWriter().write("{\"status\":\"error\",\"message\":\"Failed to fetch locations\"}");
    }
}

}
