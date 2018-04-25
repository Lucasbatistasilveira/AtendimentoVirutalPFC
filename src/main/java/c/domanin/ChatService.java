package c.domanin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import Shared.RetornoNLP;
import Shared.User;
import d.infrastructure.*;

public class ChatService implements IChatService {
	
	private NLPAgent _nlpAgent =  new NLPAgent();
	private SQLRepository _sqlAgent = new SQLRepository();
	@Override
	public RetornoNLP sendMessage(String msg,String id) {
		
		RetornoNLP result = new RetornoNLP();
		
		if(id.isEmpty() && msg.isEmpty()) {
			System.out.println("Novo Usuario.");
			return CreateIfNotExist();
			
		}else {
			// TODO : Lê infos do BD e atualiza User.
			User.setContext("");
			_sqlAgent.getUserContext(id);
			System.out.println("output: " + msg + " " + id + " " + User.getContext());
		}
		
		if(User.getContext() == null) {
			System.out.println("Contexto nulo. Setando novo contexto.");
			User.setContext("init");
		}
		
		
		
		switch(User.getContext()) {
		case "init":
			result = State_Init(msg);
			break;
		case "internet":
			result = State_Internet(msg, new JSONObject());
			break;
		case "wait_registration":
			result = State_InconsistencyMoodle(msg, new JSONObject());
			break;
		default:
			result.setMessage("Desculpe, não entendi o que você falou. Você pode repetir?");
			User.setContext("init");
		}
		
		_sqlAgent.insertUserContext();
		
		return result;
	}
	
	private RetornoNLP CreateIfNotExist() {
		UUID guid = UUID.randomUUID();
		while(_sqlAgent.ifExistUser(guid.toString())){
			guid = UUID.randomUUID();
		}
		
		User.setGuid(guid.toString());
		User.setContext("init");
		User.setMessage("");
		User.setRegistration("");
		User.setState("");
		
		_sqlAgent.insertUserContext();
		
		RetornoNLP result = new RetornoNLP();
		result.setId(guid.toString());
		result.setMessage("Olá, em que posso ajudar?");
			
		return result;
	}
	
	private RetornoNLP State_Init(String message) {
		
		JSONObject jsonWit = _nlpAgent.enviaWit(message);
		User.setMessage(message);
		RetornoNLP result = JSONtoRetornoNLP(jsonWit);
		
		switch(result.getIntent()) {
			case "Saudação":
				result.setMessage("Olá, em que posso ajudar?");
				User.setContext("init");
				break;
			case "internet":
				result = State_Internet("",jsonWit);
				break;
			case "inconsistency":
				result = State_InconsistencyMoodle("",jsonWit);
				break;
			default:
				result.setMessage("Desculpe, não entendi o que você falou. Você pode repetir?");
				User.setContext("init");
		}
		
		return result;
		
	}
	
	private RetornoNLP State_InconsistencyMoodle(String msg,JSONObject jsonWit) {
		
//		if(jsonWit.length() == 0) {
//			jsonWit = _nlpAgent.enviaWit(msg);
//		}
//		
		RetornoNLP result = new RetornoNLP();
		
		switch(User.getContext()) {
		case  "init":
			result = JSONtoRetornoNLP(jsonWit);
			result.setMessage("Identifiquei que você possui inconsistências no moodle. Você poderia me informar o seu número de matrícula?");
			User.setContext("wait_registration");
			break;
		case "wait_registration":
			System.out.println("Esperando matrícula.");
			String Reg = Get_Registration(msg);
			User.setMessage(msg);
			if(Reg == null) {
				result.setMessage("Matrícula no formato não conhecido. Por favor digite-a novamente.");
			}else {
				User.setRegistration(Reg);
				if(GetRegisterIfExist(Reg)) {
					
				}else {
					result.setMessage("Não foi identificado nenhum cadastro no minhaUFMG associado ao número de matrícula " + Reg + ". Para realizar o cadastro basta acessar o <a href=\"https://sistemas.ufmg.br/nip\" target=\"_blank\">link</a> e informar o seu CPF e senha provisória cadastrada para ter acesso à sua folha de NIPs'");
					User.setContext("init");
				}
			}
			break;
		}
		
		return result;
	}
	
	private String Get_Registration(String msg) {
		
		String charseq = "\\d+";
				  
		Pattern pattern = Pattern.compile(charseq);
		Matcher matcher = pattern.matcher(msg);
		String register = null;
		
		if(matcher.find()) {
			register = matcher.group();
		}
		
		if(register.length() == 10) return register;
		else return null;
	}
	
	private boolean GetRegisterIfExist(String registration) { 
			
		return false;
	}
	
	private RetornoNLP State_Internet(String message,JSONObject jsonWit) {
		
		if(jsonWit.length() == 0) {
			jsonWit = _nlpAgent.enviaWit(message);
		}
		JSONObject entities = jsonWit.getJSONObject("entities");
		RetornoNLP result = JSONtoRetornoNLP(jsonWit);
		
		if(entities.has("internet")) {
			String SO = entities.getJSONArray("internet").getJSONObject(0).getString("value");
			switch(SO) {
			case "windows 10":
				result.setMessage("Por favor, <a href=\"http://www.redesemfio.ufmg.br/configuracao/windows-10/\" target=\"_blank\">clique aqui</a> para visualizar as instruções para conseguir conectar à Internet no Windows 10.");
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
	
	private RetornoNLP JSONtoRetornoNLP(JSONObject jsonWit) {
		
		RetornoNLP result = new RetornoNLP();
		JSONObject son = jsonWit.getJSONObject("entities");
		
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
