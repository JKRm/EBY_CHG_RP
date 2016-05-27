package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardExtension {
	
	private String id;
	private String data;
	private boolean fail_if_unknown;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public boolean isFail_if_unknown() {
		return fail_if_unknown;
	}
	public void setFail_if_unknown(boolean fail_if_unknown) {
		this.fail_if_unknown = fail_if_unknown;
	}
	
	
}
