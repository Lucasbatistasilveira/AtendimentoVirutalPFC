package Shared;

public class Util {
	private Util() {}
	
	public static class AppConstatns
    {
		public static class JDBCConnection
		{
			public static final String BD = "teeeste";
			public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
			public static final String JDBC_CONNECT_SUCCESS = "Congrats - Seems your MySQL JDBC Driver Registered!";
			public static final String JDBC_CONNECT_FAILLED = "Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly";
			public static final String URL_DATABASE = "jdbc:mysql://localhost:3306/atendimentovirtualufmg";
			public static final String JDBC_FAILLED = "Failed to make connection!";			
		}
    }
}
