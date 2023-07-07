package com.snva.employeelist.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableOperation implements CreatTable, DropTable, ResetTable
{
    @Override
    public int creatTable(Connection connection, String tableName, String valueList)
    {
        try
        {
            Statement statement = connection.createStatement();
            boolean result = statement.execute("CREATE TABLE " + tableName + " " + valueList);
            statement.close();
            if(result == false)
            {
                return 1;
            }
            return -1;
        }
        catch (SQLException e)
        {
            System.out.println(e);
            if(e.toString().indexOf("already exists") > 0)
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
    }

    @Override
    public Boolean dropTable(Connection connection, String tableName)
    {
        try
        {
            Statement statement = connection.createStatement();
            boolean result = statement.execute("DROP TABLE " + tableName);
            statement.close();
            if(result == false)
            {
                return true;
            }
            return false;
        }
        catch (SQLException e)
        {
            System.out.println("[Failed]\n" + e);
            return false;
        }
    }

    @Override
    public Boolean resetTable(Connection connection, String tableName)
    {
        try
        {
            Statement statement = connection.createStatement();
            boolean result = statement.execute("TRUNCATE " + tableName);
            statement.close();
            if(result == false)
            {
                return true;
            }
            return false;
        }
        catch (SQLException e)
        {
            System.out.println("[Failed]\n" + e);
            return false;
        }
    }
}
