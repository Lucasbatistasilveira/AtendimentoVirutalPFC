package c.domanin;

import org.json.JSONArray;
import org.json.JSONObject;

import Shared.RetornoNLP;
import Shared.User;
import d.infrastructure.*;


public class ChatService implements IChatService {
	
	private NLPAgent _nlpAgent =  new NLPAgent();
	private SQLRepository _sqlRepository = new SQLRepository();
	@Override
	public RetornoNLP sendMessage(String msg) {

//		User.setContext("internet");
//		User.setGuid("23k1k3j123 31o23123 1454354j54 4234j");
//		User.setState("inconsistencia");
//		User.setMessage("Dudu dudu dudu dudu dudu dudu duud");
//		User.setRegistration("2013024570");
//		
//		_sqlRepository.insertUserContext();
		
		if(User.getContext() == null) {
			System.out.println("Contexto nulo. Setando novo contexto.");
			User.setContext("init");
		}
		
		RetornoNLP result = new RetornoNLP();
		
		switch(User.getContext()) {
		case "init":
			result = State_Init(msg);
			break;
		case "internet":
			result = State_Internet(msg, new JSONObject());
			break;
		default:
			result.setMessage("Desculpe, não entendi o que você falou. Você pode repetir?");
			User.setContext("init");
		}
		

		return result;
	}
	
	private RetornoNLP State_Init(String message) {
		
		JSONObject json = _nlpAgent.enviaWit(message);
		RetornoNLP result = JSONtoRetornoNLP(json);
		
		switch(result.getIntent()) {
			case "Saudação":
				result.setMessage("Olá, em que posso ajudar?");
				User.setContext("init");
				break;
			case "internet":
				result = State_Internet("",json);
				break;
			default:
				result.setMessage("Desculpe, não entendi o que você falou. Você pode repetir?");
				User.setContext("init");
		}
		
		return result;
		
	}
	
	private RetornoNLP State_Internet(String message,JSONObject json) {
		
		if(json.length() == 0) {
			json = _nlpAgent.enviaWit(message);
		}
		JSONObject entities = json.getJSONObject("entities");
		RetornoNLP result = JSONtoRetornoNLP(json);
		
		if(entities.has("internet")) {
			String SO = entities.getJSONArray("internet").getJSONObject(0).getString("value");
			switch(SO) {
			case "windows 10":
				result.setMessage("Seu SO é o Windows 10.");
				User.setContext("init");
				break;
			default:
				result.setMessage("Seu SO não foi identificado.");
				User.setContext("init");
			}
		}else if(entities.has("intent")){
			result.setMessage("Ok. Qual é o seu Sistema Operacional?");
			User.setContext("internet");
		}else {
			result.setMessage("Desculpe, sistema operacional não conhecido, poderia repetir?");
			User.setContext("internet");
		}
		return result;
	}
	
	private RetornoNLP JSONtoRetornoNLP(JSONObject json) {
		
		RetornoNLP result = new RetornoNLP();
		JSONObject son = json.getJSONObject("entities");
		
		if(son.length() != 0) {
			if(son.has("intent")) {
				JSONArray intent = son.getJSONArray("intent");
				result.setConfidence(intent.getJSONObject(0).getDouble("confidence"));
				result.setIntent(intent.getJSONObject(0).getString("value"));
				result.setMessage("");
				result.setAction("");
			}
		}else {
			result.setConfidence(0);
			result.setIntent("");
			result.setMessage("");
			result.setAction("");
		}
		
		return result;
		
	}

}
