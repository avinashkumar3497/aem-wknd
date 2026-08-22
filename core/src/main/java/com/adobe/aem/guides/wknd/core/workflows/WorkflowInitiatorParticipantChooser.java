package com.adobe.aem.guides.wknd.core.workflows;

import org.osgi.service.component.annotations.Component;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(
    service = ParticipantStepChooser.class,
    property = {
        "chooser.label=Workflow Initiator"
    }
)
public class WorkflowInitiatorParticipantChooser implements ParticipantStepChooser{

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) {
        return workItem.getWorkflow().getInitiator();
    }

}
