/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Craig Lohrman
 */
public abstract class JDBC {
    
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//127.0.0.1:3306/";
    private static final String databaseName = "farmtracker";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver"; 
    private static final String userName = "root"; //enter the database connections username here.
    private static final String password = ""; //enter the database connections password here.
    
    /**
     * This is used with getConnection, this is the connection return.
     */
    public static  Connection connection;
    
    /**
     * Creates a connection to the database.
     */
    public static void openConnection() {
        try {
            Class.forName(driver); 
            connection = DriverManager.getConnection(jdbcUrl, userName, password); 
            System.out.println("Connection Open");
        } catch(Exception e){
            System.out.println("Error:" + e.getMessage());
        }
    }
    
    /**
     * Closes the connection to the database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed");
        }catch(Exception e){
            System.out.println("Error:" + e.getMessage());
        }
    }
    
    
   /**
    * Gets the connection that was created in openConnection().
    * @return The connection that was created.
    * @throws SQLException  Throws SQL Exception.
    */
    public static Connection getConnection() throws SQLException {
        return connection;
    }
}
