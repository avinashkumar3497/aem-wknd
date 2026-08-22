package com.adobe.aem.guides.wknd.core.workflows;

import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicatedAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;

@Component(
    service = WorkflowProcess.class,
    property = {"process.label=AutoPublishPageProcess"}
)
public class AutoPublishPageProcess implements WorkflowProcess{

    private static final Logger LOG = LoggerFactory.getLogger(AutoPublishPageProcess.class);
    
    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    @Override
    public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
        //WorkItem = current step (gateway to payload + instance)
        //WorkflowSession adapts to a JCR session for the workflow user, but privileged actions like replication should use a service resource resolver
        //MetaDataMap exposes step config incl. PROCESS_ARGS
        String payloadPath = item.getWorkflowData().getPayload().toString();
        String payloadType = item.getWorkflowData().getPayloadType();
        String action = "ACTIVATE";
        String processArgs = args.get("PROCESS_ARGS",String.class);
        if(processArgs!=null && processArgs.startsWith("action=")){
            action = processArgs.substring("action=".length()).trim();
        }

        ReplicationActionType actionType;

        if ("ACTIVATE".equalsIgnoreCase(action)) {
            actionType = ReplicationActionType.ACTIVATE;
        } else if ("DEACTIVATE".equalsIgnoreCase(action)) {
            actionType = ReplicationActionType.DEACTIVATE;
        } else {
            throw new WorkflowException("Unsupported replication action: " + action);
        }
        
        LOG.info("payloadPath: {}",payloadPath);
        if("JCR_PATH".equals(payloadType)){
            try(ResourceResolver resolver = resolverFactory.getServiceResourceResolver(Map.of(resolverFactory.SUBSERVICE,"workflow-poc-service"))) {
                Session session = resolver.adaptTo(Session.class);
                replicator.replicate(session, ReplicationActionType.ACTIVATE, payloadPath);
            } catch (Exception e) {
                throw new WorkflowException("exception occured while replicating payload",e);
            }
        }
        else{
            throw new WorkflowException("payloadType is invalid:"+payloadType);
        }
    }

}
