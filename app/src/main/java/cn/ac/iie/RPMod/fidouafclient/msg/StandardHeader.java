package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardHeader {
	
	private StandardUPV upv;
	private String op;
	private String appID;
	private String serverData;
	private StandardExtension[] exts;
	
	
	public StandardUPV getUpv() {
		return upv;
	}
	public void setUpv(StandardUPV upv) {
		this.upv = upv;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getServerData() {
		return serverData;
	}
	public void setServerData(String serverData) {
		this.serverData = serverData;
	}
	public StandardExtension[] getExts() {
		return exts;
	}
	public void setExts(StandardExtension[] exts) {
		this.exts = exts;
	}
	
}
