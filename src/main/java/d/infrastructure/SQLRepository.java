package d.infrastructure;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Shared.*;
import Shared.Util.AppConstatns.JDBCConnection;
public class SQLRepository implements ISqlRepository {

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
	
	private final static String SQL_CHECK_REGISTER_UNITY = "SELECT * " + 
														   "FROM info_general " + 
														   "WHERE cpf = (SELECT cpf " + 
														   "             FROM info_general " + 
														   "             WHERE registro = ?)";	
	
	private final static String SQL_INSERT_INCONSISTENCY_LOGIN = "INSERT INTO unify_login(login_guid, login_name, register, user_name, cpf) "
																+ "VALUES (?, ?, ?, ?, ?)";
	private final static String SQL_CHECK_VALID_TOKEN = "TODO::QUERY verificar se já foi utilizado ou se expirou";
	private final static String SQL_SE_TOKEN_AS_VIEWD = "UPDATE unify_login SET viewed = 1,opened_chamado = 1 WHERE login_guid = ?";
	
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
				//TODO:: Inserir o campo name
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
			//TODO:: Inserir o campo name
	
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
	
	public List<Register> getLogin(String reg) {
		makeJDBCConnection();
		try {
			crunchifyPrepareStat = conn.prepareStatement(SQL_CHECK_REGISTER_UNITY);
			crunchifyPrepareStat.setString(1, reg);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
			
			List<Register> list = new ArrayList<Register>();
			while(rs.next()) {
				Register register = new Register();
				register.setLogin(rs.getString("login"));
				register.setRegister(rs.getString("registro"));
				list.add(register);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<LoginDB> GetLoginFromToken(String token) {
		makeJDBCConnection();
		try {
			crunchifyPrepareStat = conn.prepareStatement(SQL_CHECK_VALID_TOKEN);
			crunchifyPrepareStat.setString(1, token);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
			
			List<LoginDB> loginList = new ArrayList<LoginDB>();
			while(rs.next()) {
				LoginDB login = new LoginDB();
				login.setLoginName(rs.getString("login_name"));
				login.setRegister(rs.getString("register"));
				loginList.add(login);
			}
			return loginList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void SetTokenAsViewed(String token) {
		makeJDBCConnection();
		try {
			crunchifyPrepareStat = conn.prepareStatement(SQL_SE_TOKEN_AS_VIEWD);
			crunchifyPrepareStat.setString(1, token);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void InsertInconsistencyLogin(UUID loginGuid, String login, String register, String name, String cpf) {
		makeJDBCConnection();
		try { 
			crunchifyPrepareStat = conn.prepareStatement(SQL_INSERT_INCONSISTENCY_LOGIN);
			crunchifyPrepareStat.setString(1, loginGuid.toString());
			crunchifyPrepareStat.setString(2, login);
			crunchifyPrepareStat.setString(3, register);
			crunchifyPrepareStat.setString(4, name);
			crunchifyPrepareStat.setString(5, cpf);
			//TODO::Inserir o nome!!
			//TODO::Inserir o CPF
	
			crunchifyPrepareStat.executeUpdate();
		} catch (
 
		SQLException e) {
			e.printStackTrace();
		}
		
	}






}
