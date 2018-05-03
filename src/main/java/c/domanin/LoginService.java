package c.domanin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Shared.LoginDB;
import Shared.Register;
import a.host.RequestLogin;
import a.host.ResponseLogin;
import d.infrastructure.SQLRepository;

public class LoginService implements ILoginService {
	SQLRepository _sqlRepository =  new SQLRepository();
	@Override
	public Map<String, String> GetUserByToken(String token) {
		Map<String, String> userData = new HashMap<String, String>();
		List<LoginDB> userLogin = _sqlRepository.GetLoginFromToken(token);
		
		if(userLogin.size() > 0) {
//			//_sqlRepository.SetTokenAsViewed(token);
			int contadorLogin = 1;
			for( LoginDB login : userLogin) {
				userData.put("login" + String.valueOf(contadorLogin), login.getLoginName());
				userData.put("register" + String.valueOf(contadorLogin), login.getRegister());
				contadorLogin++;
			}
			userData.put("status", "true");
			
		} else {
			userData.put("status", "false");
		}
		
		userData.put("token", token);
		return  userData;
	}
	public ResponseLogin SetUserLoginAsUnique(RequestLogin userResponse) {
		// criar um chamado
		// caso dê certo retornar sucesso para o usuário e um email também
		// caso dê errado, informar que estamo com problemas temporários
		ResponseLogin responseLogin = new ResponseLogin();
		return responseLogin;
	}

}
