package com.snva.employeelist.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface UpdateData
{
    public int updateData(Connection connection, String tableName, String valueList, String whereCondition);
}
