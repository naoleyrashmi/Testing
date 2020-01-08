package com.xoriant.meghflow.dbconnmgmt.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.xoriant.meghflow.dbconnmgmt.pojo.DBConnectionObject;
import com.xoriant.meghflow.dbconnmgmt.pojo.LambdaResponseClass;

import java.util.*;

public class DynamoDBRepository {

    public LambdaResponseClass deleteDBEntry(List<DBConnectionObject> listDBConnections , DynamoDBMapper dynamoDBMapper) {

        LambdaResponseClass dbResponse = new LambdaResponseClass();
        StringBuffer responseText = new StringBuffer();
        try {
            for (DBConnectionObject dbConnObj : listDBConnections ) {

                DBConnectionObject item = dynamoDBMapper.load(DBConnectionObject.class , dbConnObj.getConnectionId());
                dynamoDBMapper.delete(item);
                responseText.append("\n" + "Item deleted with ConnectionId : " + dbConnObj.getConnectionId() + "\n");
            }

            dbResponse.setResponseCode("200");
            dbResponse.setResponseText(responseText.toString());

        } catch (Exception exp) {
            exp.printStackTrace();
            dbResponse.setResponseCode("500");
            dbResponse.setResponseText("DB Connection Delete operation Failed and Exception is : " + exp.getLocalizedMessage());
        }
        return dbResponse;
    }

    public List<DBConnectionObject> getDBConnections(String connectionId , DynamoDBMapper dynamoDBMapper) {

        List<DBConnectionObject> retList = new ArrayList<>();

        try {
            if (null == connectionId) {

                PaginatedScanList<DBConnectionObject> itemRetrieved = dynamoDBMapper.scan(DBConnectionObject.class,
                        new DynamoDBScanExpression());

                DBConnectionObject output = null;

                for (DBConnectionObject fromDBdbMapping : itemRetrieved )  {
                    output = new DBConnectionObject();

                    output.setConnectionId(fromDBdbMapping.getConnectionId());
                    output.setConnectionName(fromDBdbMapping.getConnectionName());
                    output.setIsSourceDB(fromDBdbMapping.getIsSourceDB());
                    output.setDatabaseType(fromDBdbMapping.getDatabaseType());
                    output.setDBHostIPAddress(fromDBdbMapping.getDBHostIPAddress());
                    output.setDBSchemaName(fromDBdbMapping.getDBSchemaName());
                    output.setDBPort(fromDBdbMapping.getDBPort());
                    output.setUserName(fromDBdbMapping.getUserName());
                    output.setPassword(fromDBdbMapping.getPassword());
                    output.setTestSQLForDBConnection(fromDBdbMapping.getTestSQLForDBConnection());

                    retList.add(output);
                    }

                } else {

                Map<String, String> attributeNames = new HashMap<String, String>();
                attributeNames.put("#ConnectionId", "ConnectionId");

                Map<String, AttributeValue> attributeValues = new HashMap<String, AttributeValue>();
                attributeValues.put(":connectionIdVal", new AttributeValue().withS(connectionId));

                DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
                        .withFilterExpression("#ConnectionId = :connectionIdVal")
                        .withExpressionAttributeNames(attributeNames)
                        .withExpressionAttributeValues(attributeValues);

                retList = dynamoDBMapper.scan(DBConnectionObject.class, dynamoDBScanExpression);

                }

        } catch (Exception exp) {
            retList = null;
        }
        return retList;
    }

    public LambdaResponseClass addNewDBEntry(DBConnectionObject srcDBConn, DBConnectionObject destDBConn,
                                             DynamoDBMapper dynamoDBMapper) {

        LambdaResponseClass addDBbResponse = new LambdaResponseClass();
        try {
            // Create Source DB Type
            String srcConnectionId = UUID.randomUUID().toString();
            srcDBConn.setConnectionId(srcConnectionId);
            dynamoDBMapper.save(srcDBConn);

            // Create DEST DB Type
            String destConnectionId = UUID.randomUUID().toString();
            destDBConn.setConnectionId(destConnectionId);
            dynamoDBMapper.save(destDBConn);

            addDBbResponse.setResponseCode("200");
            addDBbResponse.setResponseText("Source DB Connection Added Successfully with Id :\n" + srcDBConn.getConnectionId() + " \n  " +
                    " Destination DB Connection Added Successfully with Id :\n" + destDBConn.getConnectionId() + "\n ");

        } catch (Exception exp) {
            exp.printStackTrace();
            addDBbResponse.setResponseCode("500");
            addDBbResponse.setResponseText("Exception while adding Source and Dest DB Connections and Exception is : " + exp.getLocalizedMessage());

        }
        return addDBbResponse;
    }

    public LambdaResponseClass updateDBEntry(DBConnectionObject updateDBConnObject , DynamoDBMapper dynamoDBMapper) {

        LambdaResponseClass dbResponse = new LambdaResponseClass();
        try {
            DBConnectionObject item = dynamoDBMapper.load(DBConnectionObject.class , updateDBConnObject.getConnectionId());
            System.out.println("item retrieved is : " + item.getConnectionName());

            item.setDatabaseType(updateDBConnObject.getDatabaseType());
            item.setIsSourceDB(updateDBConnObject.getIsSourceDB());
            item.setConnectionName(updateDBConnObject.getConnectionName());
            item.setDBHostIPAddress(updateDBConnObject.getDBHostIPAddress());
            item.setDBSchemaName(updateDBConnObject.getDBSchemaName());
            item.setDBPort(updateDBConnObject.getDBPort());
            item.setUserName(updateDBConnObject.getUserName());
            item.setPassword(updateDBConnObject.getPassword());
            item.setTestSQLForDBConnection(updateDBConnObject.getTestSQLForDBConnection());

            dynamoDBMapper.save(item);

            // Retrieve the updated item.
            DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                    .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                    .build();
            DBConnectionObject updatedItem = dynamoDBMapper.load(DBConnectionObject.class,
                    updateDBConnObject.getConnectionId(), config);

            dbResponse.setResponseCode("200");
            dbResponse.setResponseText("Data Flow Updated Successfully with Id :\n" + updatedItem.getConnectionId() + " \n  ");

        } catch (Exception exp) {
            exp.printStackTrace();
            dbResponse.setResponseCode("500");
            dbResponse.setResponseText("Data Flow Update operation Failed and Exception is : " + exp.getLocalizedMessage());
        }
        return dbResponse;
    }
}
