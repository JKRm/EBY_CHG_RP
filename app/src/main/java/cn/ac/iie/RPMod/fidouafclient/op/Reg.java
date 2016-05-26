package cn.ac.iie.RPMod.fidouafclient.op;

import cn.ac.iie.RPMod.fidouafclient.curl.Curl;
import cn.ac.iie.RPMod.fidouafclient.util.Endpoints;
import cn.ac.iie.RPMod.fidouafclient.util.Preferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Reg {
	
	private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	
	public String getUafMsgRegRequest (String username, String appId){
		String msg = "{\"uafProtocolMessage\":\"";
		try {
			String serverResponse = getRegRequest(username);
			JSONArray reg = new JSONArray(serverResponse);
			((JSONObject)reg.get(0)).getJSONObject("header").put("appID", appId);
			JSONObject uafMsg = new JSONObject();
			uafMsg.put("uafProtocolMessage", reg.toString());
			return uafMsg.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
	
	public String getEmptyUafMsgRegRequest (){
		String msg = "{\"uafProtocolMessage\":";
		msg = msg + "\"\"";
		msg = msg + "}";
		return msg;
	}
	
	public String getRegRequest (String username){
		String url = Endpoints.getRegRequestEndpoint()+username;
		return Curl.getInSeparateThread(url);
	}

	
	public String clientSendRegResponse (String uafMessage){
		StringBuffer res = new StringBuffer();
		String decoded = null;
		try {
			JSONObject json = new JSONObject (uafMessage);
			decoded = json.getString("uafProtocolMessage").replace("\\", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		res.append("#uafMessageegOut\n"+decoded);
		String headerStr = "Content-Type:Application/json Accept:Application/json";
		res.append("\n\n#ServerResponse\n");
		String serverResponse = Curl.postInSeparateThread(Endpoints.getRegResponseEndpoint(), headerStr , decoded);
		res.append(serverResponse);
		saveAAIDandKeyID(serverResponse);
		return res.toString();
	}

	
	private void saveAAIDandKeyID(String res) {
		try{
			JSONArray regRecord = new JSONArray(res);
			JSONObject authenticator = regRecord.getJSONObject(0).getJSONObject("authenticator");
			Preferences.setSettingsParam("AAID", authenticator.getString("AAID"));
			Preferences.setSettingsParam("keyID", authenticator.getString("KeyID"));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
