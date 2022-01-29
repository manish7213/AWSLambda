package com.manish.aws.lambda.s3sns;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class ClaimManagementLambda {

    public void handler(SQSEvent event) {
        event.getRecords().forEach(message -> {
            System.out.println(message.getBody());
        });
    }
}
