package d.infrastructure;

import java.util.List;

import Shared.*;

public interface ISqlRepository {
	public void getUserContext(String guid);
	public void insertUserContext();
	public boolean ifExistUser(String guid);
	public boolean ifExistRegister(String reg);
	public List<Register> getLogin(String reg);
}
