package com.manish.aws.lambda.s3sns;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manish.aws.lambda.s3sns.dto.PatientCheckoutEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Manish
 * This lambda function gets triggered when object is created in S3.
 */
public class PatientCheckoutLambda {

    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();

    private static final String PATIENT_CHECKOUT_TOPIC = System.getenv("PATIENT_CHECKOUT_TOPIC");

    public void handler(S3Event event, Context context) {
        LambdaLogger logger = context.getLogger(); // Not implemented log4j2 because of vulnerabilities
        event.getRecords().forEach(record -> {
                    S3ObjectInputStream s3InputStream = null;
                    try {
                        s3InputStream = s3.getObject(record.getS3().getBucket().getName(),
                                record.getS3().getObject().getKey()).getObjectContent();
                        logger.log("Reading data from S3");
                        List<PatientCheckoutEvent> patientCheckoutEvents = Arrays.asList(objectMapper.readValue(s3InputStream, PatientCheckoutEvent[].class));
                        logger.log(patientCheckoutEvents.toString());
                        publishMessageToSNS(patientCheckoutEvents);
                        logger.log("Messages published to SNS");
                    } catch (IOException e) {
                        logger.log("Error while processing s3 event" + e);
                        throw new RuntimeException("Error while processing s3 event", e);
                    } finally {
                        try {
                            s3InputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
        );
    }

    private void publishMessageToSNS(List<PatientCheckoutEvent> patientCheckoutEvents) {
        patientCheckoutEvents.forEach(checkoutEvent -> {
            try {
                sns.publish(PATIENT_CHECKOUT_TOPIC, objectMapper.writeValueAsString(checkoutEvent));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
