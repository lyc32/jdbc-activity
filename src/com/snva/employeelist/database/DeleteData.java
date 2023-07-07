package com.snva.employeelist.database;

import java.sql.Connection;
public interface DeleteData
{
    public int deleteData(Connection connection, String tableName, String whereCondition);
}
