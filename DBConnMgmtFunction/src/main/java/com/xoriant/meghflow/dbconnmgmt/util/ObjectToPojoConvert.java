package com.xoriant.meghflow.dbconnmgmt.util;

import com.xoriant.meghflow.dbconnmgmt.pojo.DBConnectionObject;

import java.util.List;

public class ObjectToPojoConvert {

    public DBConnectionObject fetchSourceDBFromConnList (List<DBConnectionObject> listDBConnections) {
        DBConnectionObject retSrcDBConnection = null;
        for (DBConnectionObject srcDBConnection  : listDBConnections) {
            if ("Y".equalsIgnoreCase(srcDBConnection.getIsSourceDB())) {
                retSrcDBConnection = srcDBConnection;
            }
        }
        return retSrcDBConnection;
    }

    public DBConnectionObject fetchDestinationDBFromConnList (List<DBConnectionObject> listDBConnections) {
            DBConnectionObject retDestDBConnection = null;
            for (DBConnectionObject srcDBConnection  : listDBConnections) {
                if ("N".equalsIgnoreCase(srcDBConnection.getIsSourceDB())) {
                    retDestDBConnection = srcDBConnection;
                }
            }
            return retDestDBConnection;
    }
}