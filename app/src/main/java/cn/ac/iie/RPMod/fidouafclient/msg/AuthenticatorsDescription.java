package cn.ac.iie.RPMod.fidouafclient.msg;

public class AuthenticatorsDescription {
	
	private AuthenticatorMessageResponse[] authenticatorsSucceeded;
	private AuthenticatorMessageResponse[] authenticators;
	private String statusMsg;
	
	public AuthenticatorMessageResponse[] getAuthenticatorsSucceeded() {
		return authenticatorsSucceeded;
	}
	public void setAuthenticatorsSucceeded(
			AuthenticatorMessageResponse[] authenticatorsSucceeded) {
		this.authenticatorsSucceeded = authenticatorsSucceeded;
	}
	
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public AuthenticatorMessageResponse[] getAuthenticators() {
		return authenticators;
	}
	public void setAuthenticators(AuthenticatorMessageResponse[] authenticators) {
		this.authenticators = authenticators;
	}
	
	
	
}
