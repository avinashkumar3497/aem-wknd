package com.adobe.aem.guides.wknd.core.workflows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@ExtendWith(MockitoExtension.class)
public class WorkflowInitiatorParticipantChooserTest {
    
    private WorkflowInitiatorParticipantChooser participantChooser;

    @Mock
    private WorkItem workItem;

    @Mock
    private WorkflowSession workflowSession;

    @Mock
    private MetaDataMap metaDataMap;

    @Mock
    private Workflow workflow;
   
    @BeforeEach
    void setup(){
        participantChooser = new WorkflowInitiatorParticipantChooser();
    }

    @Test
    void shouldReturnWorkflowInitiator() throws WorkflowException{
        
        //ARRANGE
        when(workItem.getWorkflow()).thenReturn(workflow);
        when(workflow.getInitiator()).thenReturn("author1");

        //ACT
        String actualInitiator = participantChooser.getParticipant(workItem, workflowSession, metaDataMap);

        //ASSERT
        assertEquals("author1", actualInitiator);
    }

}
