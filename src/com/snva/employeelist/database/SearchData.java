package com.snva.employeelist.database;

import java.sql.Connection;
import java.sql.ResultSet;

public interface SearchData
{
    public ResultSet searchData(Connection connection, String columList, String tableName, String whereCondition);
    public ResultSet getAllData(Connection connection, String columList, String tableName);
}
