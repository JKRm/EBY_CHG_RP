package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardUAFRequest {
	
	private StandardHeader header;
	private String challenge;
	private String username;
	private StandardPolicy policy;
	private StandardTransaction[] transaction;
	private StandardAuthenticators[] authenticators;
	
	public StandardHeader getHeader() {
		return header;
	}
	public void setHeader(StandardHeader header) {
		this.header = header;
	}
	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public StandardPolicy getPolicy() {
		return policy;
	}
	public void setPolicy(StandardPolicy policy) {
		this.policy = policy;
	}
	public StandardTransaction[] getTransaction() {
		return transaction;
	}
	public void setTransaction(StandardTransaction[] transaction) {
		this.transaction = transaction;
	}
	public StandardAuthenticators[] getAuthenticators() {
		return authenticators;
	}
	public void setAuthenticators(StandardAuthenticators[] authenticators) {
		this.authenticators = authenticators;
	}
	
		
}
