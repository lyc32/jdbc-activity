package com.snva.employeelist.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB
{
    private String driver;
    private String url;
    private String userName;
    private String password;
    private Connection connection;

    public ConnectDB(String driver, String url, String userName, String password)
    {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
        connection = null;
    }

    public boolean connectToDataBase()
    {
        try
        {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,userName,password);
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("---------------------------------------");
            System.out.println("[Connect Failed]\n" + e);
            System.out.println("---------------------------------------");
            connection = null;
            return false;
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("---------------------------------------");
            System.out.println("[Load Driver Error]\n" + e);
            System.out.println("---------------------------------------");
            connection = null;
            return false;
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    public void closeConnection()
    {
        try
        {
            if( connection != null)
            {
                this.connection.close();
            }
            else
            {
                System.out.println("---------------------------------------");
                System.out.println("[Error] There is no DataBase Connection");
                System.out.println("---------------------------------------");
            }
        }
        catch (SQLException e)
        {
            System.out.println("---------------------------------------");
            System.out.println("[Close Connection Failed]\n" + e);
            System.out.println("---------------------------------------");
        }
    }
}
