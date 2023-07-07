package com.snva.employeelist.database;

import java.sql.Connection;
public interface ResetTable
{
    public Boolean resetTable(Connection connection, String tableName);
}
