package utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;


public class MySqlConnector {
	
	private static String url="jdbc:mysql://localhost:3306";
	private static String database="agent";
	private static String login="otmangx";
	private static String password="1234560";
	private static String driver="com.mysql.jdbc.Driver";
        private Statement st ;
	private static  Connection con;
	
	private MySqlConnector(){}
	
	public static  Connection  getConnection() throws ClassNotFoundException, SQLException{
		if(con == null){
                    synchronized(MySqlConnector.class) {
                        Class.forName(driver);
                        try {
                            con = DriverManager.getConnection(url+"/"+database,login,password);
                        } catch (SQLException  ex) {
                                    System.out.println("SQLException: " + ex.getMessage());
                                    System.out.println("SQLState: " + ex.getSQLState());
                                    System.out.println("VendorError: " + ex.getErrorCode());
                        }	
                    }
		}
		return con;
	}
}
