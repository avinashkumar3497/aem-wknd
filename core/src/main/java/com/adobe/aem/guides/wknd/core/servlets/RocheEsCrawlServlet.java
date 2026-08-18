package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.helper.ElasticSearchHelper;
import com.adobe.aem.guides.wknd.core.services.ElasticSearchConfigService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component(
    service = Servlet.class,
    immediate = true,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=wknd/components/elasticCrawlRequest"
    }
    
)
public class RocheEsCrawlServlet extends SlingSafeMethodsServlet{
    
    private static final Logger LOG = LoggerFactory.getLogger(RocheEsCrawlServlet.class);

    @Reference
    private ElasticSearchConfigService configService;
    
    @Reference
    private ResourceResolverFactory resolverFactory;

    // commenting it until the helper is ready
    // @Reference
    // private ElasticSearchHelper esHelper;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException,IOException{
        final String pagePath = request.getParameter("pagePath");

        JsonObject jsonObject = new JsonObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        //fetching pagePath from request parameter
        if(pagePath==null||pagePath.isBlank()){
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "pagePath required");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
        else{
            try(ResourceResolver serviceResolver = resolverFactory.getServiceResourceResolver(Map.of(ResourceResolverFactory.SUBSERVICE,"es-crawl-user"))){
                // jsonObject.addProperty("status", "success");
                // jsonObject.addProperty("message", "pagePath recieved: "+pagePath);
                final ElasticSearchHelper helper = new ElasticSearchHelper();
                final HttpPost httpPost = helper.processIncrementalCrawlRequest(pagePath, serviceResolver, configService);
                if(httpPost!=null){
                    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(configService.getConnectTimeout()).setSocketTimeout(configService.getSocketTimeout()).setConnectionRequestTimeout(configService.getConnectionRequestTimeout()).build();
                    try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build()) {
                        helper.createPostBodyRequest(httpPost, configService.getPrivateKey(), pagePath);
                        try (CloseableHttpResponse esResponse = httpClient.execute(httpPost)) {
                            int status = esResponse.getStatusLine().getStatusCode();
                            response.setStatus(status);                              // set whatever ES returned
                            response.getWriter().write(EntityUtils.toString(esResponse.getEntity(), StandardCharsets.UTF_8));
                            return;
                        } catch (Exception e) {
                            LOG.error("Exception caused while getting response from ES", e);
                            jsonObject.addProperty("status", "failed");
                            jsonObject.addProperty("reason", "Exception occured");
                            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } catch (Exception e) {
                        LOG.error("Exception caused while sending Post request for ES Incremental Crawling", e);
                        jsonObject.addProperty("status", "failed");
                        jsonObject.addProperty("reason", "Exception occured");
                        response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }
                else{
                    jsonObject.addProperty("status", "skipped");
                    jsonObject.addProperty("reason", "template not allowed / no engine");
                }
            }
            catch(LoginException loginException){
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Login Exception occured with message: "+loginException.getMessage());
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            catch(Exception e){
                LOG.error("Unexpected error in ES crawl servlet", e);
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Internal Server Error: Exception occured");
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        String jsonString = new Gson().toJson(jsonObject);
        response.getWriter().write(jsonString);
    }
}
