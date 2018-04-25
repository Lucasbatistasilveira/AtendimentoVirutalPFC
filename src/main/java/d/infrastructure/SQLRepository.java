package d.infrastructure;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Shared.User;
import Shared.UserDB;
import Shared.Util.AppConstatns.JDBCConnection;
public class SQLRepository {

	static Connection conn = null;
	static PreparedStatement crunchifyPrepareStat = null;
	
	private final static String SQL_GET_CONTEXT = "SELECT *" + 
												  "FROM chatlog " + 
												  "WHERE guid = ? " +
												  "ORDER BY count DESC " + 
												  "LIMIT 1";
	private final static String SQL_CHECK_USER_EXISTENT = "SELECT * " + 
													 "FROM chatlog " + 
													 "WHERE guid = ?";
	private final static String SQL_CHECK_REGISTER_EXISTENT = "SELECT * " + 
			 											  	  "FROM info_general " + 
			                                                  "WHERE registro = ?";
	
	private final static String SQL_INSERT_CONTEXT = "INSERT INTO chatlog (guid, context, message, state, registration)"
												   + " VALUES (?, ?, ?, ?,? )";
	
	public void getUserContext(String userGuid) {
		makeJDBCConnection();
		try 
		{
			
			crunchifyPrepareStat = conn.prepareStatement(SQL_GET_CONTEXT);
			crunchifyPrepareStat.setString(1, userGuid);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
			// Let's iterate through the java ResultSet
			while (rs.next()) {
				User.setGuid(rs.getString("guid"));
				User.setContext(rs.getString("context"));
				User.setMessage(rs.getString("message"));
				User.setState(rs.getString("state"));
				User.setRegistration(rs.getString("registration"));
			}
			
		
 
		} 
		catch (
		SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void insertUserContext() 
	{	 
		makeJDBCConnection();
		try { 
			crunchifyPrepareStat = conn.prepareStatement(SQL_INSERT_CONTEXT);
			crunchifyPrepareStat.setString(1, User.getGuid());
			crunchifyPrepareStat.setString(2, User.getContext());
			crunchifyPrepareStat.setString(3, User.getMessage());
			crunchifyPrepareStat.setString(4, User.getState());
			crunchifyPrepareStat.setString(5, User.getRegistration());
	
			crunchifyPrepareStat.executeUpdate();
		} catch (
 
		SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void makeJDBCConnection() {
		
		try {
			Class.forName(JDBCConnection.JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			conn = DriverManager.getConnection(JDBCConnection.URL_DATABASE,"root","");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	
	public boolean ifExistUser(String guid) {
		makeJDBCConnection();
		try {
			crunchifyPrepareStat = conn.prepareStatement(SQL_CHECK_USER_EXISTENT);
			crunchifyPrepareStat.setString(1, guid);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean ifExistRegister(String reg) {
		makeJDBCConnection();
		try {
			crunchifyPrepareStat = conn.prepareStatement(SQL_CHECK_REGISTER_EXISTENT);
			crunchifyPrepareStat.setString(1, reg);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
