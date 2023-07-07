package com.snva.employeelist.database;

import java.sql.Connection;
public interface DropTable
{
    public Boolean dropTable(Connection connection, String tableName);
}
