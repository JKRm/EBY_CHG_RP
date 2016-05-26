package cn.ac.iie.RPMod.fidouafclient.op;

import cn.ac.iie.RPMod.fidouafclient.curl.Curl;
import cn.ac.iie.RPMod.fidouafclient.util.Endpoints;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Auth {

    public String getUafMsgRequest (boolean isTrx, String appId){
        String msg = "{\"uafProtocolMessage\":\"";
        try {
            String serverResponse = getAuthRequest();
            JSONArray authReq = new JSONArray(serverResponse);
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

        res.append("#uafMessageOut");
        res.append("\n");
        res.append(decoded);
        String headerStr = "Content-Type:Application/json Accept:Application/json";
        res.append("\n");
        res.append("\n");
        res.append("\n");
        res.append("#ServerResponse");
        res.append("\n");
        String serverResponse = Curl.postInSeparateThread(Endpoints.getAuthResponseEndpoint(), headerStr , decoded);
        res.append(serverResponse);
        return res.toString();
    }

    private String getAuthRequest() {
        String url = Endpoints.getAuthRequestEndpoint();
        return Curl.getInSeparateThread(url);
    }


}
