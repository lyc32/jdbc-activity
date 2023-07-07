package com.snva.employeelist.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface CreatTable
{
    public int creatTable(Connection connection, String tableName, String valueList);
}
