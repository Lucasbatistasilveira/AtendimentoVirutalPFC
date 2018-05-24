package d.infrastructure;

import java.util.List;
import java.util.UUID;

import Shared.*;

public interface ISqlRepository {
	public void getUserContext(String guid);
	public void insertUserContext(String chatmsg);
	public boolean ifExistUser(String guid);
	public boolean ifExistRegister(String reg);
	public List<Register> getLogin(String reg);
	public List<LoginDB> GetLoginFromToken(String token);
	public void SetTokenAsViewed(String token);
	public void InsertInconsistencyLogin(UUID loginGuid, String login, String register, String name, String cpf);
	public boolean[] CheckCoursesAssociated(String reg);
	public int GetLastIntentId();
	public void InsertNewIntent(String intentName);
	public void InsertNewIntentLog(int id,RetornoNLP returnNLP,String message,int intentCount,int intentUserCount);
	public int GetIntentCount(int id);
	public int GetIntentUserCount(int id);
	public List<TrainingDB> GetTrainingDB();
}
