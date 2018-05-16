package com.example.eksamen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {

    private static DBConn instance;
    private final String CONN_STRING = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSSL=false";
    private final String CONN_STRING1 = "jdbc:mysql://%s:%s/?user=%s&password=%s&useSSL=false";
    private String connString;
    private String connString2;

    private DBConn()
    { connString = String.format(CONN_STRING, "localhost", "3306", "eksamens_project", "root", "1234");
    connString2=String.format(CONN_STRING1, "localhost", "3306","root","1234");
    }

    public static DBConn getInstance(){

        if (instance == null)
            instance = new DBConn();
        return instance;
    }

    public Connection getConn() throws SQLException
    {
        return DriverManager.getConnection(connString);
    }
    public Connection getConn1()throws SQLException{
        return DriverManager.getConnection(connString2);
    }


}
