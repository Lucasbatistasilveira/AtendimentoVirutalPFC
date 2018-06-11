package Shared;

public class Util {
	private Util() {}
	
	public static class AppConstatns
    {
		
		public static double CONFIDENCE_THRESHOLD = 0.9;
		
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
												 + "Clique no link http://localhost:8082/antendimentovirtual/unify-login.html?token=%s  para escolher o login que deseja manter.<br><br>"
												 + "Este link será válido por apenas 48 horas. Caso ele se expire, você deverá repetir o processo."
												 + "Atenciosamente, <br>Atenimento Virtual UFMG";
		}
		
		public static class StateMessages 
		{
			public static class StateSetup
			{
				public static final String UNKNOWN = "Desculpe, não entendi o que você falou. Você pode repetir???";
			}
			
			public static class StateInit 
			{
				public static final String GREATING = "Olá, %s! Em que posso ajudar?";
				public static final String UNKNOWN = "Desculpe, não entendi o que você falou. Aqui você pode tirar dúvidas relacionadas ao cadastro no minhaUFMG, ao correio eletrônico e à conexão com a Internet. Com o que posso ajudar?";
			}
			
			public static class StateInternet
			{
				public static final String UNKNOWN = "Desculpe, sistema operacional não conhecido. Poderia repetir, por favor? Alguns exemplos: Linux, IOS, MAC OS, Windows 10, 8, 7 , XP, Vista.";
				public static final String ASK_ABOUT_SO = "%s Ok. Qual é o seu Sistema Operacional?";
				public static class SO 
				{
					public static final String WINDOWS_10 = "%s Ok. <a href=\"http://www.redesemfio.ufmg.br/configuracao/windows-10/\" target=\"_blank\">Clique aqui</a> para visualizar as instruções para conseguir conectar à Internet no Windows 10.";
				}
			}
			
			public static class StateMoodleInconsistency 
			{
				public static final String ASK_ABOUT_REGISTER =  "Identifiquei que você possui problemas relacionados a inconsistências no moodle. Você poderia me informar o seu número de matrícula?";
				public static final String WRONG_REGISTER_FORMAT = "Matrícula no formato não conhecido. Por favor digite-a novamente.";
				public static final String DIFFERENT_LOGIN_FOUND = "Foram encontrados logins diferentes para a mesma pessoa. Caso queira que eu resolva este problema, por favor, digite sim.";
				public static final String LOGIN_NOT_FOUND = "Não foi identificado nenhum cadastro no minhaUFMG associado ao número de matrícula %s. Para realizar o cadastro basta acessar o <a href=\"https://sistemas.ufmg.br/nip\" target=\"_blank\">link</a> e informar o seu CPF e senha provisória cadastrada para ter acesso à sua folha de NIPs'. Posso ajudar com algo mais?";
				public static final String COURSES_NOT_FOUND = "Não foram encontradas nenhuma turma associada à sua matrícula. Favor entrar em contato com o colegiado do seu curso.";
				public static final String COURSES_UNDER_TWO_DAYS = "Alguma(s) turma(s) associada à sua matrícula foi adicionada ao banco de dados com menos de 48 horas. Por favor aguarde esse período para que o sistema se estabilize.";
				public static final String SUCCESS = "Não consegui identificar nenhum problema com a sua matrícula. Deseja verificar o status do seu correio eletrônico?";
				public static final String REGISTER_DENIAL = "Ok. Infelizmente sem o número de matrícula não posso continuar este atendimento. Gostaria de tirar mais alguma dúvida?";
				public static final String STATE_CHANGE = "Vi que não deseja mais verificar os problemas relacionados à sua matrícula.";
			}
			
			public static class StateUnifyLogin
			{
				public static final String USER_AFFIRMATION = "É muito bom me sentir prestativo! E que bom que poderei te ajudar! Estou te enviando um email para que você possa escolher um dos logins ativos. Abraço!";
				public static final String USER_DENIAL = "Pelo que parece você não quer que sejam feitas alterações nos seus logins, logo não farei. Fique a vontade para tirar as suas dúvidas quando quiser. Abraço.";
				public static final String UNKNOWN = "Desculpe, não entendi o que você falou. Você pode repetir, por favor?";
			}
			
			public static class StateEmailVerification
			{
				public static final String USER_AFFIRMATION = "Muito bom poder resolver isso pra você. Um chamado está sendo aberto para que meu supervisor tome conta pessoalmente do seu caso. Abraço!";
				public static final String USER_DENIAL = "Pelo que entendi você não deseja verificar o status do seu e-mail. Caso tenha mais alguma dúvida, sinta-se à vontade para me perguntar. Abraço!";
				public static final String UNKNOWN = "Desculpe, não entendi o que disse. Gostaria de saber se deseja verificar o status do seu e-mail?";
			}
		}
		
    }
	
}
