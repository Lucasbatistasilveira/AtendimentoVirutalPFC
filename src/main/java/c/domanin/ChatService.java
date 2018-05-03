package c.domanin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;	
import org.json.JSONObject;

import Shared.*;
import d.infrastructure.*;

public class ChatService implements IChatService {
	
	private NLPAgent _nlpAgent =  new NLPAgent();
	private SQLRepository _sqlAgent = new SQLRepository();
	private EmailAgent _emailAgent = new EmailAgent();
	@Override
	public RetornoNLP sendMessage(String msg,String id) throws AddressException, MessagingException {

		RetornoNLP result = new RetornoNLP();
		
		if(id.isEmpty() && msg.isEmpty()) {
			System.out.println("Novo Usuario.");
			return CreateIfNotExist();
			
		}else {
			// TODO : Lê infos do BD e atualiza User.
			User.setContext("");
			_sqlAgent.getUserContext(id);
			System.out.println("input: " + msg + " " + id + " " + User.getContext());
		}
		
		if(User.getContext() == null) {
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
		case "Unify_Login":
			result = State_UnifyLogin(msg, new JSONObject());
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
		result.setMessage(Util.AppConstatns.Messages.WELCOME);
			
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
		result.setId(User.getGuid());
		return result;
		
	}
	
	private RetornoNLP State_InconsistencyMoodle(String msg,JSONObject jsonWit) {
		
		RetornoNLP result = new RetornoNLP();
		
		switch(User.getContext()) {
		case  "init":
			result = JSONtoRetornoNLP(jsonWit);
			result.setMessage("Identifiquei que você possui problemas relacionados a inconsistências no moodle. Você poderia me informar o seu número de matrícula?");
			User.setContext("wait_registration");
			break;
		case "wait_registration":
			String reg = Get_Registration(msg);
			User.setMessage(msg);
			if(reg == null) {
				result.setMessage("Matrícula no formato não conhecido. Por favor digite-a novamente.");
			}else {
				User.setRegistration(reg);
				List<Register> register = new ArrayList<Register>();
				if(_sqlAgent.ifExistRegister(reg)) {
					result.setMessage("Matrícula identificada...");
					register = _sqlAgent.getLogin(reg);
					if (register.size() > 1 ) {
						if(LoginsAreDifferent(register)) {
							result.setMessage("Foram encontrados logins diferentes para a mesma pessoa."
									+ "Caso queira que eu resolva este problema, por favor, digite sim.");
							User.setContext("Unify_Login");
							User.setRegistration(reg);
						}
						
						//  TODO : Abrir chamado para unificação dos registros
					}
				}else {
					result.setMessage("Não foi identificado nenhum cadastro no minhaUFMG associado ao número de matrícula " + reg + ". Para realizar o cadastro basta acessar o <a href=\"https://sistemas.ufmg.br/nip\" target=\"_blank\">link</a> e informar o seu CPF e senha provisória cadastrada para ter acesso à sua folha de NIPs'");
					User.setContext("init");
				}
			}
			break;
		}
		result.setId(User.getGuid());
		return result;
	}
	
	private boolean LoginsAreDifferent(List<Register> register) {
		
		boolean result = false;
		
		for(Register r : register) {
			if(!r.getLogin().matches(register.get(0).getLogin())) {
				result = true;
			}
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
	
	private RetornoNLP State_UnifyLogin(String message,JSONObject jsonWit) throws AddressException, MessagingException {
		
		if(jsonWit.length() == 0) {
			jsonWit = _nlpAgent.enviaWit(message);
		}
		JSONObject entities = jsonWit.getJSONObject("entities");
		RetornoNLP result = JSONtoRetornoNLP(jsonWit);
		User.setMessage(message);
		
		switch(result.getIntent()) {
			case "afirmacao":
				result.setMessage("É muito bom me sentir prestativo! E que bom que poderei te ajudar! Estou te enviando um email para que você possa escolher um dos logins ativos. Abraço!");
				// TODO::Criar uma funcção que verifica se há chamado em aberto
				SendEmailToUnifyLogin();
//				TODO::mudar as frases de retorno da negacao tb
				break;
			case "negacao":
				result.setMessage("Pelo que parece você não quer que sejam feitas alterações nos seus logins, logo não farei. Fique a vontade para tirar as suas dúvidas quando quiser. Abraço.");
	
				break;
			default:
				result.setMessage("Desculpe, não entendi o que você falou. Você pode repetir?");
				User.setContext("Unify_Login");
				return result;
		}
		result.setId(User.getGuid());
		User.setContext("init");
		return result;
		}

	private void SendEmailToUnifyLogin() throws AddressException, MessagingException {
		List<Register> register = _sqlAgent.getLogin(User.getRegistration());
		UUID loginGuid = UUID.randomUUID();
		
		for(Register r : register) {
			//TODO::Inserir o nome!!
			//TODO::Inserir o CPF
			_sqlAgent.InsertInconsistencyLogin(loginGuid, r.getLogin(), r.getRegister(), "User_name", "User_cpf");
			_emailAgent.generateAndSendEmail("Inconsistência na Matrícula", 
											 String.format(Util.AppConstatns.EmailMessages.EMAIL_LOGIN_UNIFY_BUDY,"TODO::INSERIR O NOME",loginGuid),
											 r.getLogin()+"@ufmg.br");
		}
	}

}
