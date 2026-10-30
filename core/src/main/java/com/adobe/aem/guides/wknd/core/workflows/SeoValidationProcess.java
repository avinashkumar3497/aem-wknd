package com.adobe.aem.guides.wknd.core.workflows;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(
    service=WorkflowProcess.class,
    immediate=true,
    property={
        "process.label=SeoValidationProcess"
    }
)
public class SeoValidationProcess implements WorkflowProcess{
    
    private final static Logger LOG = LoggerFactory.getLogger(SeoValidationProcess.class);

    @Override 
    public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException{
        
        WorkflowData workflowData = item.getWorkflowData();
        if(!"JCR_PATH".equals(workflowData.getPayloadType())) return;
        String payloadPath = workflowData.getPayload().toString();
        
        // Read one process argument from the step dialog, maxTitleLength=60. If it's missing, default to 60.
        String process_args = args.get("PROCESS_ARGS",String.class);
        
        //TODO: fetch the maxTitleLength from process_agrs. for now keeping the value manually:
        int maxTitleLength=60;

        try (ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class)) {
            ValueMap valueMap = resolver.getResource(payloadPath).getValueMap();
            String title = valueMap.get("jcr:title",String.class);
            String description = valueMap.get("jcr:description", String.class);
            if(title==null||title.length()>maxTitleLength) {
                throw new WorkflowException("jcr:title is null or longer than maxLength");
            }
            if(description==null) throw new WorkflowException("jcr:description is null");
            //TODO: check for tags and do the same
            
        } catch (WorkflowException e) {
            throw e;
        }
        catch (Exception e){
            throw new WorkflowException("Workflow Process Failed",e);
        }
        
        // Store the results in the workflow-level metadata so a later step can read them:
            // seoStatus = PASSED or FAILED
            // seoIssues = the failure messages joined together

    }
}
