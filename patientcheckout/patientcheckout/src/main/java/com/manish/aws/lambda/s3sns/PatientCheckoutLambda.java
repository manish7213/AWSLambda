package com.manish.aws.lambda.s3sns;


import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
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

    public void handler(S3Event event) {
        event.getRecords().forEach(record -> {
                    S3ObjectInputStream s3InputStream = s3.getObject(record.getS3().getBucket().getName(),
                            record.getS3().getObject().getKey()).getObjectContent();
                    try {
                        List<PatientCheckoutEvent> patientCheckoutEvents = Arrays.asList(objectMapper.readValue(s3InputStream, PatientCheckoutEvent[].class));
                        System.out.println(patientCheckoutEvents);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );
    }
}
