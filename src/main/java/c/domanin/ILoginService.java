package c.domanin;

import java.util.Map;

import a.host.RequestLogin;
import a.host.ResponseLogin;

public interface ILoginService {
	public Map<String, String> GetUserByToken(String token);
	public ResponseLogin SetUserLoginAsUnique(RequestLogin userResponse);
}
