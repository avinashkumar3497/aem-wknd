package com.adobe.aem.guides.wknd.core.workflows;

import java.util.Calendar;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(
    service = WorkflowProcess.class,
    property = {
        "process.label=MLR: Set Review Priority & Deadline"
    }
)
public class ReviewPriorityProcess implements WorkflowProcess{

    private static Logger LOG = LoggerFactory.getLogger(ReviewPriorityProcess.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap args) throws WorkflowException{
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        LOG.info("[ReviewPriorityProcess] Payload Path: {}", payloadPath);

        String reviewPriority = args.get("reviewPriority", String.class);
        if(reviewPriority==null || reviewPriority.isEmpty()){
            // throwing WorkflowException is not the solution, handle it otherwise
            throw new WorkflowException("reviewPriority is Empty");
        }else{
            LOG.info("[ReviewPriorityProcess] Review Priority: {}", reviewPriority);
        }

        long dueInDays = args.get("dueInDays",3);   //this is how we declare default value 3. similiar could have been done for reviewPriority

        ResourceResolver resolver = wfSession.adaptTo(ResourceResolver.class);
        if (resolver == null) {
            throw new WorkflowException("Could not obtain ResourceResolver from WorkflowSession");
        }

        Resource jcrResource = resolver.getResource(payloadPath+"/jcr:content");
        if (jcrResource == null) {
            throw new WorkflowException("No jcr:content found at: " + payloadPath);
        }

        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, (int) dueInDays);

        ModifiableValueMap props = jcrResource.adaptTo(ModifiableValueMap.class);
        
        if(props!=null){
            try {
                props.put("reviewPriority", reviewPriority);
                props.put("reviewDueDate",dueDate);
                resolver.commit();
            } catch (PersistenceException e) {
                // TODO: handle exception
                throw new WorkflowException("not able to add properties to the page");
            }
        }else{
            //handle props not being able to fetch
            LOG.info("ModifiableValueMap is empty");
        }
    }

}
