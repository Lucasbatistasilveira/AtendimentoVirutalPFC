package Shared;

public class UserDB {
	public String context;
	public String guid;
	public String message;
	public String state;
	public String timestamp;



	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		User.guid = guid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		User.message = message;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		User.state = state;
	}

	public String getContext() {
		return context;
	}


	public void setContext(String context) {
		User.context = context;
	}
}
