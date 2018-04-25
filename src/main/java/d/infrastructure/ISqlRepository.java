package d.infrastructure;

import Shared.*;

public interface ISqlRepository {
	public void getUserContext(String guid);
	public void insertUserContext();
	public boolean ifExistUser(String guid);
	public boolean ifExistRegister(String reg);
}
