package Shared;

public class Util {
	private Util() {}
	
	public static class AppConstatns
    {
		public static class Messages
		{
			public static final String WELCOME = "Seja bem vindo ao Atendimento Virtual UFMG. Aqui você pode tirar dúvidas relacionadas ao cadastro no minhaUFMG, ao correio eletrônico e à conexão com a Internet. Com o que posso ajudar?";
		}
		public static class JDBCConnection
		{
			public static final String BD = "teeeste";
			public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
			public static final String JDBC_CONNECT_SUCCESS = "Congrats - Seems your MySQL JDBC Driver Registered!";
			public static final String JDBC_CONNECT_FAILLED = "Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly";
			public static final String URL_DATABASE = "jdbc:mysql://localhost:3306/atendimentovirtualufmg";
			public static final String JDBC_FAILLED = "Failed to make connection!";			
		}
		
		public static class EmailMessages
		{
			public static final String EMAIL_LOGIN_UNIFY_BUDY = "Olá, %s! Seja bem vindo ao atendimento virtual <br><br> "
												 + "Clique no link http://localhost:8082/antendimentovirtual/unify-login.html?token=%s  para escolher o login que deseja manter<br><br>"
												 + "Atenciosamente, <br>Atenimento Virtual UFMG";
		}
    }
	
}
