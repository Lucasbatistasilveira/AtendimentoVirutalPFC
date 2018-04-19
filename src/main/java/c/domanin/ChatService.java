package c.domanin;

import org.json.JSONArray;
import org.json.JSONObject;

import Shared.RetornoNLP;
import Shared.User;
import d.infrastructure.NLPAgent;

public class ChatService implements IChatService {
	
	private NLPAgent _nlpAgent =  new NLPAgent();
	@Override
	public RetornoNLP sendMessage(String msg) {

		if(User.getContext() == null) {
			System.out.println("Contexto nulo. Setando novo contexto.");
			User.setContext("init");
		}
		
		RetornoNLP result = JSONtoRetornoNLP(_nlpAgent.enviaWit(msg));
		
		switch(result.getIntent()) {
			case "Saudação":
				result.setMessage("Olá, em que posso ajudar?");
				break;
			default:
				result.setMessage("Desculpe, não entendi o que você falou. Você pode repetir?");
		}

		return result;
	}
	
	private RetornoNLP JSONtoRetornoNLP(JSONObject json) {
		
		RetornoNLP result = new RetornoNLP();
		JSONObject son = json.getJSONObject("entities");
		
		if(son.length() != 0) {
			JSONArray intent = son.getJSONArray("intent");
			result.setConfidence(intent.getJSONObject(0).getDouble("confidence"));
			result.setIntent(intent.getJSONObject(0).getString("value"));
			result.setMessage("");
			result.setAction("");
		}else {
			result.setConfidence(0);
			result.setIntent("");
			result.setMessage("");
			result.setAction("");
		}
		
		return result;
		
	}

}
