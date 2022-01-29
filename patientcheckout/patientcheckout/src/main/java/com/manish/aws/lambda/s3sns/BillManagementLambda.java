package com.manish.aws.lambda.s3sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manish.aws.lambda.s3sns.dto.PatientCheckoutEvent;

/**
 * @author Manish
 * This lambda function gets triggered when receives notification from SNS
 */
public class BillManagementLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(SNSEvent event) {
        event.getRecords().forEach(snsRecord -> {
            try {
                PatientCheckoutEvent patientCheckoutEvent = objectMapper.readValue(snsRecord.getSNS().getMessage(), PatientCheckoutEvent.class);
                System.out.println(patientCheckoutEvent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
