package cn.ac.iie.RPMod.fidouafclient.msg;

public class Context {
	
	private String userName;
	private String aaid;
	private String keyID;
	private String policyName;
	private String appID;
	private boolean stepup;
	private boolean transaction;
	private String price;
	private String qty;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public boolean isStepup() {
		return stepup;
	}
	public void setStepup(boolean stepup) {
		this.stepup = stepup;
	}
	public boolean isTransaction() {
		return transaction;
	}
	public void setTransaction(boolean transaction) {
		this.transaction = transaction;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getAaid() {
		return aaid;
	}
	public void setAaid(String aaid) {
		this.aaid = aaid;
	}
	public String getKeyID() {
		return keyID;
	}
	public void setKeyID(String keyID) {
		this.keyID = keyID;
	}
	

}
