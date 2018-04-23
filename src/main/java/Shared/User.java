package Shared;

public class User {
	public static String getRegistration() {
		return registration;
	}

	public static void setRegistration(String registration) {
		User.registration = registration;
	}


	public static String context;
	public static String guid;
	public static String message;
	public static String state;
	public static String registration;

	public User(String cont, String guid, String message, String state, String registration) {
		super();
		// TODO Auto-generated constructor stub
		User.context = cont;
		User.guid = guid;
		User.message = message;
		User.state =  state;
		User.registration = registration;
	}

	public static String getGuid() {
		return guid;
	}

	public static void setGuid(String guid) {
		User.guid = guid;
	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		User.message = message;
	}

	public static String getState() {
		return state;
	}

	public static void setState(String state) {
		User.state = state;
	}

	public static String getContext() {
		return context;
	}


	public static void setContext(String context) {
		User.context = context;
	}
}
