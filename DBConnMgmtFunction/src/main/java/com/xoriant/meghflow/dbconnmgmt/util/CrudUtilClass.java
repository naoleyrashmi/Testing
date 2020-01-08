package com.xoriant.meghflow.dbconnmgmt.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.xoriant.meghflow.dbconnmgmt.pojo.DBConnectionObject;
import com.xoriant.meghflow.dbconnmgmt.pojo.LambdaResponseClass;

import java.util.*;

public class CrudUtilClass {

    public LambdaResponseClass addNewDBEntry (DBConnectionObject srcDBConnectionObject,
                                              DBConnectionObject destDBConnectionObject,
                                              DynamoDB dynamoDB, String dynamoDBName) {

        Table dynamoDBTable = dynamoDB.getTable(dynamoDBName);

        LambdaResponseClass dbResponse = new LambdaResponseClass();
        try {
            Item dynamoItem = new Item();

            // Create Source DB Type
            String srcConnectionId = UUID.randomUUID().toString();
            dynamoItem.withPrimaryKey("ConnectionId", srcConnectionId);
            createDynamoDBItem(srcDBConnectionObject, dynamoDBTable, dynamoItem);

            // Create Destination DB Type
            String destConnectionId = UUID.randomUUID().toString();
            dynamoItem.withPrimaryKey("ConnectionId", destConnectionId);
            createDynamoDBItem(destDBConnectionObject, dynamoDBTable, dynamoItem);

            dbResponse.setResponseCode("200");
            dbResponse.setResponseText("Source Connection DB added with Id :\n" + srcConnectionId + " \n  " +
                    "and Destination DB Added with ID : \n " + destConnectionId);
        }
        catch (Exception e) {
            e.printStackTrace();
            dbResponse.setResponseCode("500");
            dbResponse.setResponseText("New DB Connection operation Failed and Exception is : " + e.getStackTrace());
        }

        return dbResponse;
    }

    private void createDynamoDBItem(DBConnectionObject srcDBConnectionObject, Table dynamoDBTable, Item dynamoItem) {
        dynamoItem.withString("DatabaseType", srcDBConnectionObject.getDatabaseType());
        dynamoItem.withString("ConnectionName", srcDBConnectionObject.getConnectionName());
        dynamoItem.withString("DBHostIPAddress", srcDBConnectionObject.getDBHostIPAddress());
        dynamoItem.withString("DBSchemaName", srcDBConnectionObject.getDBSchemaName());
        dynamoItem.withInt("DBPort", srcDBConnectionObject.getDBPort());
        dynamoItem.withString("UserName", srcDBConnectionObject.getUserName());
        dynamoItem.withString("Password", srcDBConnectionObject.getPassword());
        dynamoItem.withString("TestSQLForDBConnection", srcDBConnectionObject.getTestSQLForDBConnection());
        dynamoItem.withString("IsSourceDB", srcDBConnectionObject.getIsSourceDB());

        PutItemOutcome outcome = dynamoDBTable.putItem(dynamoItem);
    }

    public LambdaResponseClass updateDBEntry (DBConnectionObject dbConnectionObject,
                                              DynamoDB dynamoDB, String dynamoDBName) {
        LambdaResponseClass dbResponse = new LambdaResponseClass();

        Table table = dynamoDB.getTable(dynamoDBName);

        try {
            Map<String, String> expressionAttributeNames = new HashMap<String, String>();
            expressionAttributeNames.put("#P1", "ConnectionName");
            expressionAttributeNames.put("#P2", "DatabaseType");
            expressionAttributeNames.put("#P3", "DBHostIPAddress");
            expressionAttributeNames.put("#P4", "DBPort");
            expressionAttributeNames.put("#P5", "DBSchemaName");
            expressionAttributeNames.put("#P6", "IsSourceDB");
            expressionAttributeNames.put("#P7", "Password");
            expressionAttributeNames.put("#P8", "TestSQLForDBConnection");
            expressionAttributeNames.put("#P9", "UserName");

            Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
            expressionAttributeValues.put(":val1", dbConnectionObject.getConnectionName());
            expressionAttributeValues.put(":val2", dbConnectionObject.getDatabaseType());
            expressionAttributeValues.put(":val3", dbConnectionObject.getDBHostIPAddress());
            expressionAttributeValues.put(":val4", dbConnectionObject.getDBPort());
            expressionAttributeValues.put(":val5", dbConnectionObject.getDBSchemaName());
            expressionAttributeValues.put(":val6", dbConnectionObject.getIsSourceDB());
            expressionAttributeValues.put(":val7", dbConnectionObject.getPassword());
            expressionAttributeValues.put(":val8", dbConnectionObject.getTestSQLForDBConnection());
            expressionAttributeValues.put(":val9", dbConnectionObject.getUserName());

            UpdateItemOutcome outcome =  table.updateItem(
                    "ConnectionId",                                 // key attribute name
                    dbConnectionObject.getConnectionId(),                        // key attribute value
                    "set #P1= :val1, #P2 =:val2, #P3 =:val3, #P4 =:val4, #P5 =:val5, " +
                            "#P6 =:val6, #P7 =:val7, #P8 =:val8, #P9 =:val9",    // UpdateExpression
                    expressionAttributeNames,
                    expressionAttributeValues);

            dbResponse.setResponseCode("200");
            dbResponse.setResponseText("Update Item succeeded:\n" + outcome);

        }
        catch (Exception e) {
            dbResponse.setResponseCode("500");
            dbResponse.setResponseText("Failed to update multiple attributes for Connection ID  : "
                    + dbConnectionObject.getConnectionId() + " and Failed reason is : " + e.getStackTrace());
        }

        return dbResponse;
    }


    public LambdaResponseClass deleteDBEntry (List<DBConnectionObject> listDBConnections,
                                              DynamoDB dynamoDB, String dynamoDBName) {
        LambdaResponseClass dbResponse = new LambdaResponseClass();

        Table table = dynamoDB.getTable(dynamoDBName);
        StringBuffer responseText = new StringBuffer();

        try {

            for (DBConnectionObject connectionObject : listDBConnections) {

                DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                        .withPrimaryKey("ConnectionId", connectionObject.getConnectionId())
                        .withReturnValues(ReturnValue.ALL_OLD);

                DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
                responseText.append("Connection ID Deleted is : " + connectionObject.getConnectionId() + "\n");
            }

            dbResponse.setResponseCode("200");
            dbResponse.setResponseText(responseText.toString());

        }
        catch (Exception e) {
            dbResponse.setResponseCode("500");
            dbResponse.setResponseText("Failed to delete Item in DynamoDB and Exception is : " + e.getStackTrace());
        }

        return dbResponse;
    }

    public List<DBConnectionObject> getDBConnections (String connectionId, String dynamoDBName, AmazonDynamoDB dynamoDBClient) {
        LambdaResponseClass dbResponse = new LambdaResponseClass();
        List<DBConnectionObject> itemsList = new ArrayList<DBConnectionObject>();
        DBConnectionObject dbConnectionObject = null;

        if (connectionId != null) {
            Map<String, String> attributeNames = new HashMap<String, String >();
            attributeNames.put("#connectionId", "ConnectionId");

            Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
            attributeValues.put(":connectionIdVal", new AttributeValue().withS(connectionId));

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(dynamoDBName)
                    .withFilterExpression("#connectionId = :connectionIdVal")
                    .withExpressionAttributeNames(attributeNames)
                    .withExpressionAttributeValues(attributeValues);

            convertDBResponse(dynamoDBClient, itemsList, scanRequest);

        } else {
            ScanRequest scanRequest = new ScanRequest().withTableName(dynamoDBName);
            convertDBResponse(dynamoDBClient, itemsList, scanRequest);
        }
        return itemsList;
    }

    private void convertDBResponse(AmazonDynamoDB dynamoDBClient, List<DBConnectionObject> itemsList, ScanRequest scanRequest) {
        DBConnectionObject dbConnectionObject;
        ScanResult result = dynamoDBClient.scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()) {

            dbConnectionObject = new DBConnectionObject(
                    item.get("ConnectionId").toString(),
                    item.get("DatabaseType").toString(),
                    item.get("IsSourceDB").toString(),
                    item.get("ConnectionName").toString(),
                    item.get("DBHostIPAddress").toString(),
                    item.get("DBSchemaName").toString(),
                    Integer.parseInt(item.get("DBPort").getN()),
                    item.get("UserName").toString(),
                    item.get("Password").toString(),
                    item.get("TestSQLForDBConnection").toString());

            itemsList.add(dbConnectionObject);
        }
    }
}