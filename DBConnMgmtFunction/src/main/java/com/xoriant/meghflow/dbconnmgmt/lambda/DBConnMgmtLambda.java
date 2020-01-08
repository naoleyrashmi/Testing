package com.xoriant.meghflow.dbconnmgmt.lambda;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.xoriant.meghflow.dbconnmgmt.pojo.DBConnectionObject;
import com.xoriant.meghflow.dbconnmgmt.pojo.LambdaReqInput;
import com.xoriant.meghflow.dbconnmgmt.pojo.LambdaResponseClass;
import com.xoriant.meghflow.dbconnmgmt.repository.DynamoDBRepository;
import com.xoriant.meghflow.dbconnmgmt.util.ObjectToPojoConvert;

import java.util.List;

public class DBConnMgmtLambda implements RequestHandler <LambdaReqInput, LambdaResponseClass> {

    // Read below variables from Lambda ENV.
    // REGION NAME                                                  ==> Param Name  ==> REGION          ==> us-east-2
    static String regionName = System.getenv("REGION");

    // DYNAMODB NAME                                                ==> Param Name  ==> DYNAMODB_NAME   ==>DBConnectionInfo
    static String dynamoDBName = System.getenv("DYNAMODB_NAME");

    private AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withRegion(regionName).
            withCredentials(new DefaultAWSCredentialsProviderChain()).build();

    private DynamoDB dynamoDB;
    private DynamoDBMapper dynamoDBMapper;

    public DBConnMgmtLambda() {
        this.dynamoDB = new DynamoDB(dynamoDBClient);
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
    }

    public LambdaResponseClass handleRequest(LambdaReqInput lambdaReqInput, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("This is Database Connection Management Lambda. \n");

        String operation = lambdaReqInput.getOperation();
        List<DBConnectionObject> listDBConnections = lambdaReqInput.getDbConnections();
        logger.log("Operation to be performed is : \n" + operation + "\n\n");

        DynamoDBRepository dbRepository = new DynamoDBRepository();
        LambdaResponseClass methodResponse = new LambdaResponseClass();

        if ("CREATE".equalsIgnoreCase(operation)) {
            logger.log("List of DBConnections is : \n" + listDBConnections.size() + "\n\n");
            methodResponse = dbRepository.addNewDBEntry(
                    new ObjectToPojoConvert().fetchSourceDBFromConnList(listDBConnections),
                    new ObjectToPojoConvert().fetchDestinationDBFromConnList(listDBConnections),
                    dynamoDBMapper);

        } else if ("LIST".equalsIgnoreCase(operation))  {
            if (listDBConnections != null && listDBConnections.size() > 0) {
                logger.log("List of DBConnections is : \n" + listDBConnections.size() + "\n\n");
                logger.log("Getting Connection Information for : \n" + listDBConnections.get(0).getConnectionId() + "\n\n");
                List<DBConnectionObject> retList = dbRepository.getDBConnections( listDBConnections.get(0).getConnectionId(), dynamoDBMapper);

                if ( retList != null && retList.size() > 0 ) {
                    methodResponse.setResponseCode("200");
                    methodResponse.setResponseText("List Items succeeded:\n" + retList);
                } else {
                    methodResponse.setResponseCode("500");
                    methodResponse.setResponseText("Exception while fetching the List \n" + retList);
                }
            } else {
                logger.log("Getting Connection Info For ALL Connection IDs \n\n");
                List<DBConnectionObject> retList = dbRepository.getDBConnections(null, dynamoDBMapper);

                if (retList != null && retList.size() > 0) {
                    methodResponse.setResponseCode("200");
                    methodResponse.setResponseText("List Items succeeded:\n" + retList);
                } else {
                    methodResponse.setResponseCode("500");
                    methodResponse.setResponseText("Exception while fetching the List \n" + retList);
                }
            }
        } else if ("UPDATE".equalsIgnoreCase(operation)) {
                logger.log("List of DBConnections is : \n" + listDBConnections.size() + "\n\n");
                methodResponse = dbRepository.updateDBEntry(listDBConnections.get(0), dynamoDBMapper);

        } else if ("DELETE".equalsIgnoreCase(operation)) {
            logger.log("List of DBConnections is : \n" + listDBConnections.size() + "\n\n");
            methodResponse = dbRepository.deleteDBEntry(listDBConnections,dynamoDBMapper);

            logger.log("Operation DELETE successfully completed. \n\n");

        } else {
            methodResponse.setResponseCode("500");
            methodResponse.setResponseText("Invalid operation specified. Please enter VALID operation\n");
        }

        return methodResponse;
    }
}