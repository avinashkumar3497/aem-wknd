package com.adobe.aem.guides.wknd.core.workflows;

import org.osgi.service.component.annotations.Component;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(
    service = WorkflowProcess.class,
    property = {"process.label=AutoPublishPageProcess"}
)
public class AutoPublishPageProcess implements WorkflowProcess{



    @Override
    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        //WorkItem = current step (gateway to payload + instance)
        //WorkflowSession adapts to a JCR session for the workflow user, but privileged actions like replication should use a service resource resolver
        //MetaDataMap exposes step config incl. PROCESS_ARGS
        

    }

}
