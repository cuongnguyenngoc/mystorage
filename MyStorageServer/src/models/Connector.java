
package models;

import java.sql.*;

/**
 *
 * @author cuong nguyen ngoc
 */
public class Connector {
    
    private static Connection Connection = null;
    private static String database = "mystorage";
    private static String host = "localhost";
    private static String username = "root";
    private static String password = "";
    
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("I can't find where driver is");
        }
    }
    public static Connection getConnector(){
        
        try {
            
            String jdbc = "jdbc:mysql://" + host + "/" + database + "?user=" + username + "&password=" + password;
            String jdbcutf8 = "&useUnicode=true&characterEncoding=UTF-8";
            
            Connection = DriverManager.getConnection(jdbc + jdbcutf8);
        } catch (SQLException ex) {
            System.out.println("I can't connect to database. Please check again");
        }
        
        return Connection;
    }
}
