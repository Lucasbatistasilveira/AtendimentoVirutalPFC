package c.domanin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;	
import org.json.JSONObject;

import Shared.*;
import Shared.Util.AppConstatns.StateMessages.StateInit;
import Shared.Util.AppConstatns.StateMessages.StateInternet;
import Shared.Util.AppConstatns.StateMessages.StateMoodleInconsistency;
import Shared.Util.AppConstatns.StateMessages.StateSetup;
import Shared.Util.AppConstatns.StateMessages.StateUnifyLogin;
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
			System.out.println("input: " + RemoveAccent(msg) + " " + id + " " + User.getContext());
			msg = RemoveAccent(msg);
		}
		
		if(User.getContext() == null) {
			User.setContext("init");
		}
		
		switch(User.getContext()) {
		case "init":
			result = State_Init(msg);
			break;
		case "internet":
			result = State_Internet(msg, new JSONObject(),"");
			break;
		case "wait_registration":
			result = State_InconsistencyMoodle(msg, new JSONObject());
			break;
		case "Unify_Login":
			result = State_UnifyLogin(msg, new JSONObject());
			break;
		case "email_verification":
			result = State_EmailVerification(msg);
			break;
		default:
			result.setMessage(StateSetup.UNKNOWN);
			
		}
		
		_sqlAgent.insertUserContext(result.getMessage());
		
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
		User.setCpf("");
		User.setName("");
		
		_sqlAgent.insertUserContext("");
		
		RetornoNLP result = new RetornoNLP();
		result.setId(guid.toString());
		result.setMessage(Util.AppConstatns.Messages.WELCOME);
			
		return result;
	}
	
	private RetornoNLP State_Init(String message) {
		
		JSONObject jsonWit = _nlpAgent.enviaWit(message);
		User.setMessage(message);
		RetornoNLP result = JSONtoRetornoNLP(jsonWit);
		
		if(result.getConfidence() > Util.AppConstatns.CONFIDENCE_THRESHOLD) {
			switch(result.getIntent()) {
				case "saudacao":
					result.setMessage(String.format(StateInit.GREATING, GetGreeting()));
					User.setContext("init");
					break;
				case "internet":
					result = State_Internet("",jsonWit,"");
					break;
				case "inconsistencia":
					result = State_InconsistencyMoodle("",jsonWit);
					break;
				default:
					result.setMessage(StateInit.UNKNOWN);
					CheckUnknowledge(message);
					User.setContext("init");
			}
		}else {
			result.setMessage(StateInit.UNKNOWN);
			CheckUnknowledge(message);
			User.setContext("init");
		}
		result.setId(User.getGuid());
		return result;
		
	}

	private RetornoNLP State_InconsistencyMoodle(String msg,JSONObject jsonWit) {
		
		RetornoNLP result = new RetornoNLP();
		boolean unifyLogin = true;
		
		switch(User.getContext()) {
		case  "init":
			result = JSONtoRetornoNLP(jsonWit);
			result.setMessage(StateMoodleInconsistency.ASK_ABOUT_REGISTER);
			User.setContext("wait_registration");
			break;
		case "wait_registration":
			String reg = Get_Registration(msg);
			User.setMessage(msg);
			if(reg == null) {
				jsonWit = _nlpAgent.enviaWit(msg);
				result = JSONtoRetornoNLP(jsonWit);
				
				switch(result.getIntent()) {
				case "negacao" :
					result.setMessage(StateMoodleInconsistency.REGISTER_DENIAL);
					User.setState("init");
					break;
				case "internet":
					System.out.println("Envia ao estado de internet");
					result = State_Internet("", jsonWit,StateMoodleInconsistency.STATE_CHANGE);
					break;
				default:
					result.setMessage(StateMoodleInconsistency.WRONG_REGISTER_FORMAT);	
				}
				
				
			}else {
				User.setRegistration(reg);
				List<Register> register = new ArrayList<Register>();
				if(_sqlAgent.ifExistRegister(reg)) {
					register = _sqlAgent.getLogin(reg);
					if (register.size() > 1 ) {
						if(LoginsAreDifferent(register)) {
							unifyLogin = false;
							result.setMessage(StateMoodleInconsistency.DIFFERENT_LOGIN_FOUND);
							User.setContext("Unify_Login");
							User.setRegistration(reg);
						}
					}
					
					if(unifyLogin) {
						boolean[] checkcourses = _sqlAgent.CheckCoursesAssociated(reg);
						if(!checkcourses[0]) {
							result.setMessage(StateMoodleInconsistency.COURSES_NOT_FOUND);
						}else if(!checkcourses[1]) {
							result.setMessage(StateMoodleInconsistency.COURSES_UNDER_TWO_DAYS);
						}else {
							result.setMessage(StateMoodleInconsistency.SUCCESS);
							User.setContext("email_verification");
						}
					}
				}else {
					result.setMessage(String.format(StateMoodleInconsistency.LOGIN_NOT_FOUND, reg)); 
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
		
		if(register != null) {
			if(register.length() == 10) return register;
			else return null;
		}
		
		return null;
	}
	
	private RetornoNLP State_Internet(String message,JSONObject jsonWit,String prevStateMessage) {
		
		if(jsonWit.length() == 0) {
			jsonWit = _nlpAgent.enviaWit(message);
		}
		JSONObject entities = jsonWit.getJSONObject("entities");
		RetornoNLP result = JSONtoRetornoNLP(jsonWit);
		
		if(entities.has("internet")) {
			String SO = entities.getJSONArray("internet").getJSONObject(0).getString("value");
			switch(SO) {
			case "windows 10":
				result.setMessage(StateInternet.SO.WINDOWS_10);
				User.setContext("init");
				break;
			case "windows 8":
				result.setMessage(StateInternet.SO.WINDOWS_8);
				User.setContext("init");
				break;
			case "windows 7":
				result.setMessage(StateInternet.SO.WINDOWS_7);
				User.setContext("init");
				break;
			case "windows vista":
				result.setMessage(StateInternet.SO.WINDOWS_VISTA);
				User.setContext("init");
				break;
			case "windows xp":
				result.setMessage(StateInternet.SO.WINDOWS_XP);
				User.setContext("init");
				break;
			case "mac os":
				result.setMessage(StateInternet.SO.MAC_OS);
				User.setContext("init");
				break;
			case "ios":
				result.setMessage(StateInternet.SO.IOS);
				User.setContext("init");
				break;
			case "linux":
				result.setMessage(StateInternet.SO.LINUX);
				User.setContext("init");
				break;
			default:
				result.setMessage(StateInternet.UNKNOWN);
				User.setContext("init");
			}
		}else if(entities.has("intent")){
			
				
			switch(result.getIntent()) {
			case "negacao" :
				result.setMessage(StateInternet.UNKNOWN_SO);
				User.setContext("init");
				break;
			case "saudacao":
				result.setMessage(String.format(StateInternet.GREATING, GetGreeting()));
				User.setContext("internet");
				break;
			default:
				result.setMessage(StateInternet.ASK_ABOUT_SO);
				User.setContext("internet");	
			}
			
			

		}else {
			result.setMessage(StateInternet.UNKNOWN);
			User.setContext("internet");
		}
		result.setMessage(String.format(result.getMessage(), prevStateMessage));
		
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
		
		if(result.getConfidence() > Util.AppConstatns.CONFIDENCE_THRESHOLD) {
			switch(result.getIntent()) {
				case "afirmacao":
					result.setMessage(StateUnifyLogin.USER_AFFIRMATION);
					SendEmailToUnifyLogin();
					break;
				case "negacao":
					result.setMessage(StateUnifyLogin.USER_DENIAL);
					break;
				default:
					result.setMessage(StateUnifyLogin.UNKNOWN);
					CheckUnknowledge(message);
					User.setContext("Unify_Login");
					return result;
			}
			result.setId(User.getGuid());
			User.setContext("init");
		}else {
			result.setMessage(StateUnifyLogin.UNKNOWN);
			CheckUnknowledge(message);
			User.setContext("Unify_Login");
			return result;
		}
		return result;
		}

	private void SendEmailToUnifyLogin() throws AddressException, MessagingException {
		List<Register> register = _sqlAgent.getLogin(User.getRegistration());
		UUID loginGuid = UUID.randomUUID();
		
		for(Register r : register) {
			_sqlAgent.InsertInconsistencyLogin(loginGuid, r.getLogin(), r.getRegister(), "User_name", "User_cpf");
			_emailAgent.generateAndSendEmail("Inconsistência na Matrícula", 
											 String.format(Util.AppConstatns.EmailMessages.EMAIL_LOGIN_UNIFY_BUDY,"TODO::INSERIR O NOME",loginGuid),
											 r.getLogin()+"@ufmg.br");
		}
	}
	
	private RetornoNLP State_EmailVerification(String msg) {
		RetornoNLP result = new RetornoNLP();
		
		
		switch(User.getContext()) {
		case "email_verification":
			JSONObject jsonWit = _nlpAgent.enviaWit(msg);
			User.setMessage(msg);
			result = JSONtoRetornoNLP(jsonWit);
			if(result.getConfidence() > Util.AppConstatns.CONFIDENCE_THRESHOLD) {
				switch(result.getIntent()) {
				case "afirmacao":
					result.setMessage(Util.AppConstatns.StateMessages.StateEmailVerification.USER_AFFIRMATION);
					User.setContext("init");
					break;
				case "negacao":
					result.setMessage(Util.AppConstatns.StateMessages.StateEmailVerification.USER_DENIAL);
					User.setContext("init");
					break;
				default:
					CheckUnknowledge(msg);
					result.setMessage(Util.AppConstatns.StateMessages.StateEmailVerification.UNKNOWN);
				}
			}else {
				CheckUnknowledge(msg);
				result.setMessage(Util.AppConstatns.StateMessages.StateEmailVerification.UNKNOWN);
			}
			
			
			
			break;
		}
		return result;
	}
	
	private void CheckUnknowledge(String msg) {
		
		JSONObject wit = new JSONObject();
		wit = _nlpAgent.SendWitSecondary(msg);		
		
		RetornoNLP result = JSONtoRetornoNLP(wit);
		
		int intentId = 0;
		String intent = "";
		
		if(result.getIntent() == "") {
			
			intentId = _sqlAgent.GetLastIntentId() + 1;
			intent = String.format("Intencao%d", intentId);
			
			_sqlAgent.InsertNewIntent(intent);
			_sqlAgent.InsertNewIntentLog(intentId, result, msg,1,1);
			
			for(int i = 0; i < 5; i++) {
				_nlpAgent.CreateNewIntent(msg,intent);
			}
			
			
			System.out.println("Cria nova intenção. Id nome : " + intent);	
		}else {
			
			intent = result.getIntent();
			intentId = _sqlAgent.GetIntentId(intent);
			_sqlAgent.InsertNewIntentLog(intentId, result, msg,_sqlAgent.GetIntentCount(intentId) + 1,_sqlAgent.GetIntentUserCount(intentId) + _sqlAgent.CheckIntentPlusUser(intentId));
			_nlpAgent.CreateNewIntent(msg, intent);
			System.out.println("Adiciona valor para intenção identificada." + intent);
					
		}
	}
	
	private String GetGreeting() {
		Calendar c = Calendar.getInstance();
		String greeting =  null;				
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
		if(timeOfDay >= 0 && timeOfDay < 12){
			greeting= "bom dia";
		}else if(timeOfDay >= 12 && timeOfDay < 17){
			greeting = "boa tarde";
		}else if(timeOfDay >= 17 && timeOfDay < 24){
			greeting = "boa noite";
		}
		return greeting;
	}
	
	private String RemoveAccent(String message) {
		String strWithoutAccent = Normalizer.normalize(message,Normalizer.Form.NFD);
		strWithoutAccent = strWithoutAccent.replaceAll("[^\\p{ASCII}]", "");
		return strWithoutAccent; 
	}

}
