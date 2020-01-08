package com.xoriant.meghflow.dbconnmgmt.pojo;

import java.util.List;

public class LambdaReqInput {

    public String operation;
    public List<DBConnectionObject> dbConnections;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<DBConnectionObject> getDbConnections() {
        return dbConnections;
    }

    public void setDbConnections(List<DBConnectionObject> dbConnections) {
        this.dbConnections = dbConnections;
    }


    public LambdaReqInput(String operation, List<DBConnectionObject> dbConnections) {
        this.operation = operation;
        this.dbConnections = dbConnections;
    }

    public LambdaReqInput() {
    }
}