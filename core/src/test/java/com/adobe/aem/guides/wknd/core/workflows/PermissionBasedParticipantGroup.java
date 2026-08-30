package com.adobe.aem.guides.wknd.core.workflows;

import java.security.Principal;
import java.util.regex.Pattern;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicy;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.api.security.JackrabbitAccessControlEntry;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(
        immediate = true,
        property = {
                ParticipantStepChooser.SERVICE_PROPERTY_LABEL + "=WKND - Permission Based Participant"
        }
)
public class PermissionBasedParticipantGroup implements ParticipantStepChooser {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionBasedParticipantGroup.class);

    /** Approver groups must match this naming convention. */
    private static final Pattern APPROVER_GROUP_PATTERN = Pattern.compile("^wknd-.*approvers$");

    /** Approver groups must hold this privilege on the payload path. */
    private static final String APPROVER_PRIVILEGE = "crx:replicate";

    /** Used when no matching approver group is found. */
    private static final String DEFAULT_PARTICIPANT = "administrators";

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
            throws WorkflowException {

        String payloadPath = getPayloadPath(workItem);
        if (payloadPath == null) {
            LOG.info("Payload is not a JCR path. Falling back to default participant {}", DEFAULT_PARTICIPANT);
            return DEFAULT_PARTICIPANT;
        }

        ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);
        if (resolver == null) {
            throw new WorkflowException("Unable to adapt WorkflowSession to ResourceResolver.");
        }

        AccessControlManager acm = getAccessControlManager(resolver);

        // Walk up the tree from the payload path towards the root.
        String path = payloadPath;
        while (path != null) {
            String participantName = getApproverParticipantName(acm, path);
            if (participantName != null) {
                LOG.info("Approver participant {} selected for {} based on permissions on {}",
                        participantName, payloadPath, path);
                return participantName;
            }
            path = getParentPathOrNull(resolver, path);
        }

        LOG.info("No approver group found for {}. Falling back to default participant {}",
                payloadPath, DEFAULT_PARTICIPANT);
        return DEFAULT_PARTICIPANT;
    }

    /**
     * Scans the ACLs of a single path and returns the first approver group that
     * matches the name pattern and holds the replication privilege.
     */
    private String getApproverParticipantName(AccessControlManager acm, String path) {
        try {
            for (AccessControlPolicy policy : acm.getPolicies(path)) {
                if (policy instanceof JackrabbitAccessControlList) {
                    JackrabbitAccessControlList acl = (JackrabbitAccessControlList) policy;
                    for (AccessControlEntry entry : acl.getAccessControlEntries()) {
                        if (matchesApprover(entry)) {
                            return entry.getPrincipal().getName();
                        }
                    }
                }
            }
        } catch (RepositoryException e) {
            LOG.warn("Unable to read permissions for {}", path, e);
        }
        return null;
    }

    /**
     * True only when the ACE is an ALLOW entry whose principal name matches the
     * approver pattern AND which grants the replication privilege.
     */
    private boolean matchesApprover(AccessControlEntry entry) {
        Principal principal = entry.getPrincipal();
        JackrabbitAccessControlEntry jackrabbitEntry = (JackrabbitAccessControlEntry) entry;

        if (jackrabbitEntry.isAllow() && APPROVER_GROUP_PATTERN.matcher(principal.getName()).matches()) {
            for (Privilege privilege : entry.getPrivileges()) {
                if (APPROVER_PRIVILEGE.equals(privilege.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtains the AccessControlManager from the resolver's underlying JCR session.
     */
    private AccessControlManager getAccessControlManager(ResourceResolver resolver) throws WorkflowException {
        Session session = resolver.adaptTo(Session.class);
        if (session == null) {
            throw new WorkflowException("Session is null while trying to obtain AccessControlManager.");
        }
        try {
            return session.getAccessControlManager();
        } catch (RepositoryException e) {
            throw new WorkflowException("Exception while trying to obtain AccessControlManager.", e);
        }
    }

    /**
     * Returns the parent path, or null if there is no parent.
     */
    private String getParentPathOrNull(ResourceResolver resolver, String path) {
        Resource resource = resolver.getResource(path);
        Resource parent = resource != null ? resource.getParent() : null;
        return parent != null ? parent.getPath() : null;
    }

    /**
     * Extracts the payload path if the payload is a JCR path.
     */
    private String getPayloadPath(final WorkItem workItem) {
        String payloadPath = null;
        WorkflowData workflowData = workItem.getWorkflowData();
        if ("JCR_PATH".equals(workflowData.getPayloadType())) {
            payloadPath = workflowData.getPayload().toString();
        }
        return payloadPath;
    }
}