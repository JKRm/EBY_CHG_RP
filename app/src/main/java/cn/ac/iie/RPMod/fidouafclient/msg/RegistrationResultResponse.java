package cn.ac.iie.RPMod.fidouafclient.msg;

public class RegistrationResultResponse {
//	private AuthenticatorMessageResponse[] authenticatorsSucceeded;
	private AuthenticatorsDescription description;
	private int statusCode;
	
	public AuthenticatorsDescription getDescription() {
		return description;
	}
	public void setDescription(AuthenticatorsDescription description) {
		this.description = description;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
