package Shared;

public class TrainingDB {
	String intent;
	String message;
	Double confident;
	int intentCount;
	int userCount;
	boolean newIntent;
	String state;
	
	public int getIntentCount() {
		return intentCount;
	}
	public void setIntentCount(int intentCount) {
		this.intentCount = intentCount;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Double getConfident() {
		return confident;
	}
	public void setConfident(Double confident) {
		this.confident = confident;
	}
	public boolean isNewIntent() {
		return newIntent;
	}
	public void setNewIntent(boolean newIntent) {
		this.newIntent = newIntent;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}

// LIST : 	Intenção
//			Mensagem
//			Confiança
//			flag new
//			Estado
