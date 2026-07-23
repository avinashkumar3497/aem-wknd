package com.adobe.aem.guides.wknd.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component(
        service = WorkflowProcess.class,
        property = {
            "process.label=MLR: Validate Compliance Metadata"
        }
)
public class ComplianceValidationProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ComplianceValidationProcess.class);

    // jcr:content is where page properties live
    private static final String JCR_CONTENT = "/jcr:content";

    @Override
    public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap args)
            throws WorkflowException {

        // (a) Get the payload path from the WorkItem
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        LOG.info("[MLR Validation] Started for payload: {}", payloadPath);

        // (b) Read PROCESS_ARGS -> which properties are mandatory
        //     In the model you'll type: legalDisclaimer,expiryDate,brandCode
        String argString = args.get("PROCESS_ARGS", String.class);
        List<String> mandatoryProps = (argString == null || argString.isEmpty())
                ? Arrays.asList("legalDisclaimer", "expiryDate", "brandCode")
                : Arrays.stream(argString.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());

        // (c) Get a ResourceResolver from the workflow session
        ResourceResolver resolver = wfSession.adaptTo(ResourceResolver.class);
        if (resolver == null) {
            throw new WorkflowException("Could not obtain ResourceResolver from WorkflowSession");
        }

        // (d) Resolve the page's jcr:content node
        Resource contentRes = resolver.getResource(payloadPath + JCR_CONTENT);
        if (contentRes == null) {
            throw new WorkflowException("No jcr:content found at: " + payloadPath);
        }

        // (e) Validate each mandatory property
        ModifiableValueMap props = contentRes.adaptTo(ModifiableValueMap.class);
        List<String> missing = mandatoryProps.stream()
                .filter(p -> {
                    String val = props.get(p, String.class);
                    return val == null || val.trim().isEmpty();
                })
                .collect(Collectors.toList());

        try {
            if (missing.isEmpty()) {
                // (f-pass) All good -> stamp a flag other steps can read
                props.put("mlrComplianceValid", true);
                props.put("mlrValidationMessage", "All mandatory compliance fields present");
                resolver.commit();
                LOG.info("[MLR Validation] PASSED for {}", payloadPath);
            } else {
                // (f-fail) Missing fields -> stamp reason + fail the workflow
                String msg = "Missing mandatory compliance fields: " + String.join(", ", missing);
                props.put("mlrComplianceValid", false);
                props.put("mlrValidationMessage", msg);
                resolver.commit();
                LOG.warn("[MLR Validation] FAILED for {} -> {}", payloadPath, msg);
                // Throwing here terminates the workflow with an error.
                // (Later we'll swap this for a route/goto so it loops back to author.)
                throw new WorkflowException(msg);
            }
        } catch (org.apache.sling.api.resource.PersistenceException e) {
            throw new WorkflowException("Failed to save validation result", e);
        }
    }
}