package com.adobe.aem.guides.wknd.core.workflows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adobe.granite.workflow.WorkflowException;

public class AutoPublishPageProcessTest {

    //ARRANGE
    //get the mock objects of:
    //WorkItem
    //WorkflowSession
    //MetaDataMap
    //WorkflowData - to be returned when calling getWorkflowData() on the object of WorkItem
    //Object payload - to be returned when calling getPayload() on the object of WorkFlowData
    

    @BeforeEach
    void setup(){
        //get the object of AutoPublishPageProcess
    }

    @Test
    void shouldReplicateWithValidActionAndPath() throws WorkflowException{
        
        //ARRANGE
        //mock the behaviour - when getWorkflowData is called on the object of WorkflowItem
        //mock the behaviour - when getPayload is called on the object of WorkflowData
        //mock the behaviour - when getPayloadType is called on the object of WorkflowData
        //mock the behaviour - when get() is called on the object of MetaDataMap with params - "PROCESS_ARGS",String.class
        //get the object of ResourceResolverFactory from AemContext
        //  
        
        //ACT
        //Call execute() method from the object of AutoPublishPageProcess
        
        //ASSERT
        //verify if replicate() method was called
    }
}
