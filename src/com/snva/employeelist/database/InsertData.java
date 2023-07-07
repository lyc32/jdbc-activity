package com.snva.employeelist.database;

import java.sql.Connection;
public interface InsertData
{
    public int insertData(Connection connection, String tableName, String columList, String valueList);
}
