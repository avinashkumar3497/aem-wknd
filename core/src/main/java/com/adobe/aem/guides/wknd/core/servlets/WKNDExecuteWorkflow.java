package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.scripting.sightly.engine.ResourceResolution;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/executeworkflow",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET
    }
)
public class WKNDExecuteWorkflow extends SlingSafeMethodsServlet{
    private String status = "WorkflowExecuting";
    private static Logger LOG = LoggerFactory.getLogger(WKNDExecuteWorkflow.class);
    @Override
    public void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response) throws IOException{
        ResourceResolver resolver = null;
        String payload=request.getRequestParameter("path").getString();
        try {
            resolver = request.getResourceResolver();
        } catch (Exception e) {
            LOG.warn("Exception while getting resourceResolver: {}",e.getMessage());
        }
        try {
            WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class); 
            WorkflowData workflowData=workflowSession.newWorkflowData("JCR_PATH",payload);
            WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/demo-create-version");
            status = workflowSession.startWorkflow(workflowModel,workflowData).getState();
        } catch (Exception e) {
            LOG.warn("exception while triggering workflow: {}"+e.getMessage());
        }

        response.setContentType("text/plain");
        response.getWriter().write("Status of workflow triggered for "+payload+": "+status);
    }
}
