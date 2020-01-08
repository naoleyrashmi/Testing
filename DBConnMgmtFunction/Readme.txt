DB Creation Screen :

1. On this screen, below fields will be captured.
    DatabaseType;
    IsSourceDB;
    ConnectionName;
    DBHostIPAddress;
    DBSchemaName;
    DBPort;
    UserName;
    Password;
    TestSQLForDBConnection;

    This entire set will be replicated for Source DB and Destination DB.
    Sample Testing JSON will be as below:

Input to be passed is for CREATE is as below: ( TESTED on Local and API Gateway)

{
    "operation" : "CREATE",
	"dbConnections" : [{
			  "DatabaseType" : "Oracle",
			  "connectionName" : "srcConnection",
			  "DBHostIPAddress" : "Localhost",
			  "DBSchemaName" : "myDevSchema",
			  "DBPort" : 5555,
			  "UserName" : "admin",
			  "Password" : "Password",
			  "TestSQLForDBConnection" : "Select 1 from Dual",
			  "IsSourceDB" : "Y"
    },{
			  "DatabaseType" : "Redshift",
			  "ConnectionName" : "destConnection",
			  "DBHostIPAddress" : "RedshiftAtCloud",
			  "DBSchemaName" : "myDevDestSchema",
			  "DBPort" : 9999,
			  "UserName" : "admin",
			  "Password" : "Password",
			  "TestSQLForDBConnection" : "Select 1 from redshift",
			  "IsSourceDB" : "N"
    }]
}

This will generate TWO ConnectionIds and store them in DynamoDB.

2. To use the UPDATE API, below params will be used :

{
  "operation" : "UPDATE",
  "dbConnections" : [{
    "ConnectionId" : "16fc2da5-823c-4cea-9d6e-eea4a966f4f6",
    "DatabaseType" : "Oracle",
    "ConnectionName" : "ConnName",
    "DBHostIPAddress" : "SAGAR-SG",
    "DBSchemaName" : "myDevSchema",
    "DBPort" : 5555,
    "UserName" : "admin",
    "Password" : "Password",
    "TestSQLForDBConnection" : "Select 1 from Dual",
    "IsSourceDB" : "Y"
  }]
}

3. To use the DELETE API, below params will be used :

{
  "operation" : "DELETE",
  "dbConnections" : [{
    "ConnectionId" : "ced2f432-373e-4093-98e5-a2c0f6c36bde"
  },
  {
    "ConnectionId" : "b15a2adc-7a68-4b1c-a26f-f28d3187a0f4"
  }]
}

You can delete single / multiple connections at a given time.

4. Input to be passed is for LIST (All) is as below: ( TESTED on Local and API Gateway)
   {
     "operation" : "LIST"
   }

   Input to be passed is for LIST (Specific) is as below: ( TESTED on Local and API Gateway)
   {
     "operation" : "LIST",
     "dbConnections" : [{
       "ConnectionId" : "ee2040b7-7a49-4080-ac3e-3835706d0b0e"
     }]
}