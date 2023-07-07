package com.snva.employeelist.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcCRUD implements SearchData,InsertData,UpdateData,DeleteData
{
    @Override
    public int insertData(Connection connection, String tableName, String columList, String valueList)
    {
        try
        {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO "+ tableName + " " + columList + " VALUES " + valueList;
            System.out.println(sql);
            int result = statement.executeUpdate(sql);
            statement.close();
            return result;
        }
        catch (SQLException e)
        {
            System.out.println("[Error]\n" + e);
            return 0;
        }
    }

    @Override
    public ResultSet searchData(Connection connection, String columList, String tableName, String whereCondition)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + columList + " FROM " + tableName + " WHERE " + whereCondition);
            return resultSet;
        }
        catch (SQLException e)
        {
            System.out.println("[Error]\n" + e);
            return null;
        }
    }

    @Override
    public ResultSet getAllData(Connection connection, String columList, String tableName)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + columList + " FROM " + tableName);
            return resultSet;
        }
        catch (SQLException e)
        {
            System.out.println("[Error]\n" + e);
            return null;
        }
    }

    @Override
    public int deleteData(Connection connection, String tableName, String whereCondition)
    {
        try
        {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate("DELETE FROM " + tableName + " WHERE " + whereCondition);
            statement.close();
            return result;
        }
        catch (SQLException e)
        {
            System.out.println("[Error]\n" + e);
            return 0;
        }
    }

    @Override
    public int updateData(Connection connection, String tableName, String valueList, String whereCondition)
    {
        try
        {
            Statement statement = connection.createStatement();
            System.out.println("UPDATE " + tableName + " SET " + valueList + " WHERE " + whereCondition);
            int result = statement.executeUpdate("UPDATE " + tableName + " SET " + valueList + " WHERE " + whereCondition);
            statement.close();
            return result;
        }
        catch (SQLException e)
        {
            System.out.println("[Error]\n" + e);
            return 0;
        }
    }
}
