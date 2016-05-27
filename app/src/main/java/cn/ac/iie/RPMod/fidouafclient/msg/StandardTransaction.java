package cn.ac.iie.RPMod.fidouafclient.msg;

public class StandardTransaction {
	private String contentType;
	private String content;
	private StandardtcDisplayPNGCharacteristics tcDisplayPNGCharacteristics;
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public StandardtcDisplayPNGCharacteristics getTcDisplayPNGCharacteristics() {
		return tcDisplayPNGCharacteristics;
	}
	public void setTcDisplayPNGCharacteristics(
			StandardtcDisplayPNGCharacteristics tcDisplayPNGCharacteristics) {
		this.tcDisplayPNGCharacteristics = tcDisplayPNGCharacteristics;
	}
	
	
}
