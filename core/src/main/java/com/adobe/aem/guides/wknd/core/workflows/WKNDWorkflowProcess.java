package com.adobe.aem.guides.wknd.core.workflows;

import javax.jcr.Node;
import javax.jcr.Session;

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
    service = WorkflowProcess.class,
    immediate = true,
    property = {
        "process.label = WKND Workflow Process"
    }
)
public class WKNDWorkflowProcess implements WorkflowProcess{

    private static Logger LOG = LoggerFactory.getLogger(WKNDWorkflowProcess.class);
    @Override
    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        try {
            WorkflowData workflowData = item.getWorkflowData();
            if(workflowData.getPayloadType().equals("JCR_PATH")){
                Session jcrSession=session.adaptTo(Session.class);
                Node node = jcrSession.getNode(workflowData.getPayload().toString()+"/jcr:content");
                String[] processArgs = args.get("PROCESS_ARGS", "string").split(",");
                for(String wfArgs : processArgs){
                    String[] nameValue = wfArgs.split(":");
                    if(node!=null){
                        node.setProperty(nameValue[0], nameValue[1]);
                    }
                }
            }
        } catch (Exception e) {
            LOG.warn("Exception occured: {}", e.getMessage());
        }
    }
}
