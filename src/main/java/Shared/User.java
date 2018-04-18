package Shared;

public class User {
	public static String context;

	public User(String cont) {
		super();
		// TODO Auto-generated constructor stub
		User.context = cont;
	}

	public static String getContext() {
		return context;
	}


	public static void setContext(String context) {
		User.context = context;
	}
}
