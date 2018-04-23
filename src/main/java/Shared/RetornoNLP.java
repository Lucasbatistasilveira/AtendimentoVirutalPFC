package Shared;

public class RetornoNLP {
	private String Message;
	private String Action;
	private String Intent;
	private double Confidence;
	private String Id;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public double getConfidence() {
		return Confidence;
	}
	public void setConfidence(double Confidence) {
		this.Confidence = Confidence;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String mensagem) {
		Message = mensagem;
	}
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	public String getIntent() {
		return Intent;
	}
	public void setIntent(String intent) {
		Intent = intent;
	}


}
