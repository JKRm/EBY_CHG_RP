package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardRequest {
	
	private String description;
	private int statusCode;
	private String uafRequest; 
	private int lifetimeMillis;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getUafRequest() {
		return uafRequest;
	}
	public void setUafRequest(String uafRequest) {
		this.uafRequest = uafRequest;
	}
	public int getLifetimeMillis() {
		return lifetimeMillis;
	}
	public void setLifetimeMillis(int lifetimeMillis) {
		this.lifetimeMillis = lifetimeMillis;
	}
	
	
}
