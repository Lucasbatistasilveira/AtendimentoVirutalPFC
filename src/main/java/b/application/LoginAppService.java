package b.application;

import java.util.Map;

import a.host.RequestLogin;
import a.host.ResponseLogin;
import c.domanin.LoginService;

public class LoginAppService implements ILoginAppService {
	private LoginService _loginService =  new LoginService();
	@Override
	public Map<String, String> GetUserByToken(String token) {
		
		return _loginService.GetUserByToken(token);
	}
	public ResponseLogin SetUserLoginAsUnique(RequestLogin userResponse) {
		// TODO Auto-generated method stub
		return _loginService.SetUserLoginAsUnique(userResponse);
	}

	
}
