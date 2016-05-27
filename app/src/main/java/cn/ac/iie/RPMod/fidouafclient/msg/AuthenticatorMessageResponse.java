package cn.ac.iie.RPMod.fidouafclient.msg;

public class AuthenticatorMessageResponse {
	
	private String description;
	private String userName;
	private String keyID;
	private String uvi;
	private short uviStatus;
	private String aaid;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getKeyID() {
		return keyID;
	}
	public void setKeyID(String keyID) {
		this.keyID = keyID;
	}
	public String getUvi() {
		return uvi;
	}
	public void setUvi(String uvi) {
		this.uvi = uvi;
	}
	public short getUviStatus() {
		return uviStatus;
	}
	public void setUviStatus(short uviStatus) {
		this.uviStatus = uviStatus;
	}
	public String getAaid() {
		return aaid;
	}
	public void setAaid(String aaid) {
		this.aaid = aaid;
	}
	
	
}
