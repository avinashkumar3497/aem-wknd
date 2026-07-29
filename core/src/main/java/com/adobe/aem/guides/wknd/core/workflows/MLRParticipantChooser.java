package com.adobe.aem.guides.wknd.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = ParticipantStepChooser.class,
    property = {
        "chooser.label=MLR: Route Reviewer by Priority"   // shows in the step dropdown
    }
)
public class MLRParticipantChooser implements ParticipantStepChooser {

    private static final Logger LOG = LoggerFactory.getLogger(MLRParticipantChooser.class);

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession wfSession, MetaDataMap args)
            throws WorkflowException {

        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        ResourceResolver resolver = wfSession.adaptTo(ResourceResolver.class);

        // read the priority we stamped in Step 3
        String priority = "medium";
        Resource content = resolver.getResource(payloadPath + "/jcr:content");
        if (content != null) {
            ModifiableValueMap props = content.adaptTo(ModifiableValueMap.class);
            if (props != null) {
                priority = props.get("reviewPriority", "medium");
            }
        }

        // decide the assignee group based on priority
        String assignee = "high".equalsIgnoreCase(priority)
                ? "mlr-senior-reviewers"     // route High -> senior group
                : "mlr-reviewers";           // else -> general group

        LOG.info("[MLR Chooser] priority={} -> assignee={}", priority, assignee);
        return assignee;   // must be a valid user/group id (authorizable)
    }
}