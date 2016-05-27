package cn.ac.iie.RPMod.fidouafclient.op;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ac.iie.RPMod.fidouafclient.curl.Curl;
import cn.ac.iie.RPMod.fidouafclient.msg.DeregisterAuthenticator;
import cn.ac.iie.RPMod.fidouafclient.msg.DeregistrationRequest;
import cn.ac.iie.RPMod.fidouafclient.msg.Operation;
import cn.ac.iie.RPMod.fidouafclient.msg.OperationHeader;
import cn.ac.iie.RPMod.fidouafclient.msg.Version;
import cn.ac.iie.RPMod.fidouafclient.util.Endpoints;
import cn.ac.iie.RPMod.fidouafclient.util.Preferences;

public class ModDereg {

	private Gson gson = new Gson();
	
	public String getUafMsgRequest (){
		String msg = "{\"uafProtocolMessage\":\"";
		try {
			DeregistrationRequest regResponse = getDereg();
			String forSending = getDeregUafMessage(regResponse);
			Preferences.setSettingsParam("deregMsg", forSending);
//			post(forSending);
			JSONArray deregReq = new JSONArray(forSending);
			((JSONObject)deregReq.get(0)).getJSONObject("header").put("appID", "android:apk-key-hash:bE0f1WtRJrZv/C0y9CM73bAUqiI");
			((JSONObject)deregReq.get(0)).getJSONObject("header").remove("serverData");
			JSONObject uafMsg = new JSONObject();
			uafMsg.put("uafProtocolMessage", deregReq.toString());
			return uafMsg.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		msg = msg + "\"}";
		return msg;
	}

	public void recordKeyId(String registrationsOut) {
		JSONObject asmResponse;
		try {
			asmResponse = new JSONObject(registrationsOut);

			if (asmResponse.get("responseData") == null) {
				return;
			}
			String keyId = asmResponse.getJSONObject("responseData")
					.getJSONArray("appRegs").getJSONObject(0)
					.getJSONArray("keyIDs").getString(0);
			Preferences.setSettingsParam("keyID", keyId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public DeregistrationRequest getDereg() {
		try {
			DeregistrationRequest dereg = new DeregistrationRequest();
			dereg.header = new OperationHeader();
			dereg.header.upv = new Version(1, 0);
			dereg.header.op = Operation.Dereg;
			//dereg.header.serverData = "";
			dereg.header.appID = Preferences.getSettingsParam("appID");
			dereg.authenticators = new DeregisterAuthenticator[1];
			DeregisterAuthenticator deregAuth = new DeregisterAuthenticator();
			deregAuth.aaid = Preferences.getSettingsParam("AAID");
			String tmp = Preferences.getSettingsParam("keyID");
			byte[] bytes = tmp.getBytes();
			deregAuth.keyID = 
					tmp;
					//Base64.encodeToString(bytes, Base64.NO_WRAP);
			dereg.authenticators[0] = deregAuth;

			//return post(dereg);
			return dereg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String post(String json) {
		String header = "Content-Type:Application/json Accept:Application/json";
		return Curl.postInSeparateThread(Endpoints.getDeregEndpoint(), header , json);
	}

	public String getDeregUafMessage(DeregistrationRequest regResponse) {
		DeregistrationRequest[] forSending = new DeregistrationRequest[1];
		forSending[0] = regResponse;
		String json = gson.toJson(forSending, DeregistrationRequest[].class);
		return json;
	}

	public String clientSendDeregResponse (String uafMessage) {
		StringBuffer res = new StringBuffer();
		String decoded = null;
		try {
			JSONObject json = new JSONObject(uafMessage);
			decoded = json.getString("uafProtocolMessage").replace("\\", "");
			post(decoded);
			return decoded;
		} catch (JSONException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
