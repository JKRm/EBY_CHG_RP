package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardPolicy {
	private StandardMatchCriteria[][] accepted;
	private StandardMatchCriteria[] disallowed;
	
	public StandardMatchCriteria[][] getAccepted() {
		return accepted;
	}
	public void setAccepted(StandardMatchCriteria[][] accepted) {
		this.accepted = accepted;
	}
	public StandardMatchCriteria[] getDisallowed() {
		return disallowed;
	}
	public void setDisallowed(StandardMatchCriteria[] disallowed) {
		this.disallowed = disallowed;
	}
	
	
	
}