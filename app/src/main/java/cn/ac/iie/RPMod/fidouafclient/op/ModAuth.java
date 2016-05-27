package cn.ac.iie.RPMod.fidouafclient.op;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ac.iie.RPMod.fidouafclient.curl.Curl;
import cn.ac.iie.RPMod.fidouafclient.msg.Context;
import cn.ac.iie.RPMod.fidouafclient.msg.RequestInitializer;
import cn.ac.iie.RPMod.fidouafclient.msg.StandardRequest;
import cn.ac.iie.RPMod.fidouafclient.msg.StandardResponse;
import cn.ac.iie.RPMod.fidouafclient.util.Endpoints;
import cn.ac.iie.RPMod.fidouafclient.util.NewEndpoints;

public class ModAuth {

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String getUafMsgRequest (boolean isTrx, String appId){
        String msg = "{\"uafProtocolMessage\":\"";
        try {
            String serverResponse = getAuthRequest(isTrx);
            Log.e("MODAUTH", serverResponse);
            StandardRequest standardRequest = gson.fromJson(serverResponse, StandardRequest.class);
            JSONArray authReq = new JSONArray(standardRequest.getUafRequest());
            ((JSONObject)authReq.get(0)).getJSONObject("header").put("appID", appId);
            if (isTrx) {
                ((JSONObject) authReq.get(0)).put("transaction", getTransaction());
            }
            JSONObject uafMsg = new JSONObject();
            uafMsg.put("uafProtocolMessage", authReq.toString());
            return uafMsg.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        msg = msg + "\"}";
        return msg;
    }

    private JSONArray getTransaction (){
        JSONArray ret = new JSONArray();
        JSONObject trx = new JSONObject();

        try {
            trx.put("contentType", "image/png");
            trx.put("content", "aGVsbG8gd29ybGQh".trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret.put(trx);
        return ret;
    }

    public String clientSendResponse (String uafMessage){
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
        res.append("#uafMessageOut");
        res.append("\n");
        res.append(decoded);
        String headerStr = "Content-Type:Application/json Accept:Application/json";
        res.append("\n");
        res.append("\n");
        res.append("\n");
        res.append("#ServerResponse");
        res.append("\n");
        String serverResponse = Curl.postInSeparateThread(NewEndpoints.getAuthResponseEndpoint(), headerStr, gson.toJson(standardResponse));
        res.append(serverResponse);
        return res.toString();
    }

    private String getAuthRequest(boolean transaction) {
        RequestInitializer requestInitializer = new RequestInitializer();
        Context context = new Context();
        context.setAppID("sampleapp");
        context.setPolicyName("default");
        if(transaction){
            context.setTransaction(true);
            context.setQty("1");
            context.setPrice("100.0");
        }
        requestInitializer.setContext(context);
        String headStr = "Content-Type:application/fido+uaf Accept:Application/fido+uaf";
        String url = NewEndpoints.getAuthRequestEndpoint();
        return Curl.postInSeparateThread(url, headStr, gson.toJson(requestInitializer));
    }


}
