package Shared;

import java.security.Timestamp;

public class LoginDB {
	public String register;
	public String loginName;
	public String viewed;
	public String openedChamado;
	public String loginGuid;
	public Timestamp timestamp;
	public String userName;
	

	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginname) {
		this.loginName = loginname;
	}
	public String getOpenedChamado() {
		return openedChamado;
	}
	public void setOpenedChamado(String openedChamado) {
		this.openedChamado = openedChamado;
	}
	public String getLoginGuid() {
		return loginGuid;
	}
	public void setLoginGuid(String loginGuid) {
		this.loginGuid = loginGuid;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getViewed() {
		return viewed;
	}
	public void setViewed(String viewed) {
		this.viewed = viewed;
	}
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}

}
