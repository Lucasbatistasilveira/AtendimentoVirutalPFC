package d.infrastructure;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
	
	private final static String SQL_INSERT_CONTEXT = "INSERT INTO chatlog (guid, context, message, state, registration, name, cpf, chatmsg)"
												   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final static String SQL_CHECK_REGISTER_UNITY = "SELECT * " + 
														   "FROM info_general " + 
														   "WHERE cpf = (SELECT cpf " + 
														   "             FROM info_general " + 
														   "             WHERE registro = ?)";	
	
	private final static String SQL_INSERT_INCONSISTENCY_LOGIN = "INSERT INTO unify_login(login_guid, login_name, register, user_name, cpf) "
																+ "VALUES (?, ?, ?, ?, ?)";
	private final static String SQL_CHECK_VALID_TOKEN = "SELECT * "
														+ "FROM unify_login "
														+ "WHERE unify_login.timestamp > DATE_SUB(NOW(), INTERVAL 2 DAY) "
														+ "AND unify_login.login_guid = ? "
														+ "AND unify_login.viewed = '0' "
														+ "AND unify_login.opened_chamado = '0' ";
	
	private final static String SQL_SE_TOKEN_AS_VIEWD = "UPDATE unify_login SET viewed = '1',opened_chamado = '1' WHERE login_guid = ?";
	
	private final static String SQL_CHECK_COURSES_ASSOCIATED = "SELECT Data FROM usercourses WHERE Registro = ?";
	
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
				User.setName(rs.getString("name"));
				User.setCpf(rs.getString("cpf"));
				//TODO:: Inserir o campo name
			}
			
		
 
		} 
		catch (
		SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void insertUserContext(String chatmsg) 
	{	 
		makeJDBCConnection();
		try { 
			crunchifyPrepareStat = conn.prepareStatement(SQL_INSERT_CONTEXT);
			crunchifyPrepareStat.setString(1, User.getGuid());
			crunchifyPrepareStat.setString(2, User.getContext());
			crunchifyPrepareStat.setString(3, User.getMessage());
			crunchifyPrepareStat.setString(4, User.getState());
			crunchifyPrepareStat.setString(5, User.getRegistration());
			crunchifyPrepareStat.setString(6, User.getName());
			crunchifyPrepareStat.setString(7, User.getCpf());
			crunchifyPrepareStat.setString(8, chatmsg);
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
				User.setName(rs.getString("nome"));
				User.setCpf(rs.getString("cpf"));
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

	public boolean[] CheckCoursesAssociated(String reg) {
		
		boolean[] result = new boolean[2];
		Arrays.fill(result, Boolean.FALSE);
		result[1] = true;
		
		makeJDBCConnection();
		try {
			crunchifyPrepareStat = conn.prepareStatement(SQL_CHECK_COURSES_ASSOCIATED);
			crunchifyPrepareStat.setString(1, reg);
			ResultSet rs = crunchifyPrepareStat.executeQuery();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date current = new Date();
			
			while(rs.next()) {
				result[0] = true;
				Date date = format.parse(rs.getString("Data"));
				long diffInMillies = Math.abs(current.getTime() - date.getTime());
			    long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			    
			    if(diff < 48) {
			    	result[1] = false;
			    }
			    
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		return result;
		
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
