package cn.ac.iie.RPMod.fidouafclient.op;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ac.iie.RPMod.fidouafclient.curl.Curl;
import cn.ac.iie.RPMod.fidouafclient.msg.AuthenticatorMessageResponse;
import cn.ac.iie.RPMod.fidouafclient.msg.Context;
import cn.ac.iie.RPMod.fidouafclient.msg.RegistrationResultResponse;
import cn.ac.iie.RPMod.fidouafclient.msg.RequestInitializer;
import cn.ac.iie.RPMod.fidouafclient.msg.StandardRequest;
import cn.ac.iie.RPMod.fidouafclient.msg.StandardResponse;
import cn.ac.iie.RPMod.fidouafclient.msg.StandardUAFRequest;
import cn.ac.iie.RPMod.fidouafclient.util.Endpoints;
import cn.ac.iie.RPMod.fidouafclient.util.NewEndpoints;
import cn.ac.iie.RPMod.fidouafclient.util.Preferences;

public class ModReg {
	
	private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	
	public String getUafMsgRegRequest (String username, String appId){
		String msg = "{\"uafProtocolMessage\":\"";
		try {
			String serverResponse = getRegRequest(username);
			Log.e("MODREG_", serverResponse);
			StandardRequest standardRequest = gson.fromJson(serverResponse, StandardRequest.class);
            String uafRequestArray = standardRequest.getUafRequest();
            JsonArray uafRequestJsonArray = (JsonArray) new JsonParser().parse(uafRequestArray);
            List<StandardUAFRequest> standardUAFRequestList = new ArrayList<StandardUAFRequest>();
            for(int i=0; i<uafRequestJsonArray.size(); i++){
                JsonObject uafRequestJson = uafRequestJsonArray.get(i).getAsJsonObject();
                StandardUAFRequest standardUAFRequest = gson.fromJson(uafRequestJson, StandardUAFRequest.class);
                standardUAFRequestList.add(standardUAFRequest);
            }
            Preferences.setSettingsParam("appID", standardUAFRequestList.get(0).getHeader().getAppID());
			JSONArray reg = new JSONArray(standardRequest.getUafRequest());
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
		RequestInitializer requestInitializer = new RequestInitializer();
		Context context = new Context();
		context.setAppID("sampleapp");
		context.setUserName(username);
		context.setPolicyName("default");
		requestInitializer.setContext(context);
		String headerStr = "Content-Type:application/fido+uaf Accept:Application/fido+uaf";
		String url = NewEndpoints.getRegRequestEndpoint();
		return Curl.postInSeparateThread(url, headerStr, gson.toJson(requestInitializer));
	}

	
	public String clientSendRegResponse (String uafMessage, String username){
		StringBuffer res = new StringBuffer();
		String decoded = null;
		try {
			JSONObject json = new JSONObject (uafMessage);
			decoded = json.getString("uafProtocolMessage").replace("\\", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}

        StandardResponse standardResponse = new StandardResponse();
        Context context = new Context();
        context.setAppID("sampleapp");
        standardResponse.setContext(context);
        standardResponse.setUafResponse(decoded);
		
		res.append("#uafMessageegOut\n"+decoded);
		String headerStr = "Content-Type:Application/json Accept:Application/json Cookie:userID=" + username;
		res.append("\n\n#ServerResponse\n");
        String serverResponse = Curl.postInSeparateThread(NewEndpoints.getRegResponseEndpoint(), headerStr, gson.toJson(standardResponse));
		res.append(serverResponse);
		saveAAIDandKeyID(serverResponse);
		return res.toString();
	}

	
	private void saveAAIDandKeyID(String res) {
		RegistrationResultResponse registrationResultResponse = gson.fromJson(res, RegistrationResultResponse.class);
		AuthenticatorMessageResponse authenticatorMessageResponse = registrationResultResponse.getDescription().getAuthenticatorsSucceeded()[0];
		Preferences.setSettingsParam("AAID", authenticatorMessageResponse.getAaid());
		Preferences.setSettingsParam("keyID", authenticatorMessageResponse.getKeyID());
	}
}
