package com.xoriant.meghflow.dbconnmgmt.pojo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName="DBConnectionInfo")
public class DBConnectionObject implements Serializable {

    public String ConnectionId;
    public String DatabaseType;
    public String IsSourceDB;
    public String ConnectionName;
    public String DBHostIPAddress;
    public String DBSchemaName;
    public Integer DBPort;
    public String UserName;
    public String Password;
    public String TestSQLForDBConnection;

    @DynamoDBHashKey(attributeName="ConnectionId")
    public String getConnectionId() {
        return ConnectionId;
    }
    public void setConnectionId(String connectionId) {
        ConnectionId = connectionId;
    }

    @DynamoDBAttribute(attributeName="DatabaseType")
    public String getDatabaseType() {
        return DatabaseType;
    }
    public void setDatabaseType(String databaseType) {
        DatabaseType = databaseType;
    }

    @DynamoDBAttribute(attributeName="IsSourceDB")
    public String getIsSourceDB() {
        return IsSourceDB;
    }
    public void setIsSourceDB(String isSourceDB) {
        IsSourceDB = isSourceDB;
    }

    @DynamoDBAttribute(attributeName="ConnectionName")
    public String getConnectionName() {
        return ConnectionName;
    }
    public void setConnectionName(String connectionName) {
        ConnectionName = connectionName;
    }

    @DynamoDBAttribute(attributeName="DBSchemaName")
    public String getDBHostIPAddress() {
        return DBHostIPAddress;
    }
    public void setDBHostIPAddress(String DBHostIPAddress) {
        this.DBHostIPAddress = DBHostIPAddress;
    }

    @DynamoDBAttribute(attributeName="DBHostIPAddress")
    public String getDBSchemaName() {
        return DBSchemaName;
    }
    public void setDBSchemaName(String DBSchemaName) {
        this.DBSchemaName = DBSchemaName;
    }

    @DynamoDBAttribute(attributeName="DBPort")
    public Integer getDBPort() {
        return DBPort;
    }
    public void setDBPort(Integer DBPort) {
        this.DBPort = DBPort;
    }

    @DynamoDBAttribute(attributeName="UserName")
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }

    @DynamoDBAttribute(attributeName="Password")
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }

    @DynamoDBAttribute(attributeName="TestSQLForDBConnection")
    public String getTestSQLForDBConnection() {
        return TestSQLForDBConnection;
    }
    public void setTestSQLForDBConnection(String testSQLForDBConnection) {
        TestSQLForDBConnection = testSQLForDBConnection;
    }

    public DBConnectionObject(String connectionId, String databaseType, String isSourceDB, String connectionName, String DBHostIPAddress, String DBSchemaName, Integer DBPort, String userName, String password, String testSQLForDBConnection) {
        this.ConnectionId = connectionId;
        this.DatabaseType = databaseType;
        this.IsSourceDB = isSourceDB;
        this.ConnectionName = connectionName;
        this.DBHostIPAddress = DBHostIPAddress;
        this.DBSchemaName = DBSchemaName;
        this.DBPort = DBPort;
        this.UserName = userName;
        this.Password = password;
        this.TestSQLForDBConnection = testSQLForDBConnection;
    }

    @Override
    public String toString() {
        return "DBConnectionObject{" +
                "ConnectionId='" + ConnectionId + '\'' +
                ", DatabaseType='" + DatabaseType + '\'' +
                ", IsSourceDB='" + IsSourceDB + '\'' +
                ", ConnectionName='" + ConnectionName + '\'' +
                ", DBHostIPAddress='" + DBHostIPAddress + '\'' +
                ", DBSchemaName='" + DBSchemaName + '\'' +
                ", DBPort=" + DBPort +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", TestSQLForDBConnection='" + TestSQLForDBConnection + '\'' +
                '}';
    }

    public DBConnectionObject() {
    }
}