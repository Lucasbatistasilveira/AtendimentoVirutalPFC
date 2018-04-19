package Shared;

public class RetornoNLP {
	private String Mensagem;
	private String Action;
	private String Intent;
	private double Confidence;
	
	public double getConfidence() {
		return Confidence;
	}
	public void setConfidence(double Confidence) {
		this.Confidence = Confidence;
	}
	public String getMessage() {
		return Mensagem;
	}
	public void setMessage(String mensagem) {
		Mensagem = mensagem;
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
