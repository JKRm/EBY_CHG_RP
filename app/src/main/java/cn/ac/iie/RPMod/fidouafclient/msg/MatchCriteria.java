package cn.ac.iie.RPMod.fidouafclient.msg;

public class MatchCriteria {
	public String[] aaid;
	//public String[] vendorID;
	public String[] keyIDs;
//	public long userVerification;
//	public int keyProtection;
	//public int matcherProtection;
	public long attachmentHint;
	//public int tcDisplay;
	//public int[] authenticationAlgorithms;
	//public String[] assertionSchemes;
	//public int[] attestationTypes;
	public int authenticatorVersion;
	public Extension[] exts;
}
