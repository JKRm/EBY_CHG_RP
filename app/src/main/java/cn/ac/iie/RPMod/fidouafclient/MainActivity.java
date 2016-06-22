package cn.ac.iie.RPMod.fidouafclient;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;

import cn.ac.iie.RPMod.fidouafclient.msg.DeregistrationRequest;
import cn.ac.iie.RPMod.fidouafclient.msg.UAFIntentType;
import cn.ac.iie.RPMod.fidouafclient.op.ModAuth;
import cn.ac.iie.RPMod.fidouafclient.op.ModDereg;
import cn.ac.iie.RPMod.fidouafclient.op.ModReg;
import cn.ac.iie.RPMod.fidouafclient.util.Preferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends Activity {

    private static final int REG_ACTIVITY_RES_3 = 3;
    private static final int AUTH_ACTIVITY_RES_5 = 5;
    private static final int DEREG_ACTIVITY_RES_4 = 4;
    private TextView msg;
    private TextView title;
    private TextView username;
    private ModReg modReg = new ModReg();
    private ModDereg modDereg = new ModDereg();
    private ModAuth  modAuth = new ModAuth();
    private String facetID = "";
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Preferences.getSettingsParam("keyID").equals("")) {
            setContentView(R.layout.activity_main);
            findFields();
        } else {
            setContentView(R.layout.activity_registered);
            findFields();
            username.setText(Preferences.getSettingsParam("username"));
        }
        try {
            facetID = getFacetID(this.getPackageManager().getPackageInfo(this.getPackageName(), this.getPackageManager().GET_SIGNATURES));
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void findFields (){
        msg = (TextView) findViewById(R.id.textViewMsg);
        title = (TextView) findViewById(R.id.textViewTitle);
        username = (TextView) findViewById(R.id.textUsername);
    }

    public void info(View view) {

        title.setText("Discovery info");
        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        i.addCategory("android.intent.category.DEFAULT");
        i.setType("application/fido.uaf_client+json");
        List<ResolveInfo> queryIntentActivities = this.getPackageManager().queryIntentActivities(i, PackageManager.GET_META_DATA);
        Bundle data = new Bundle();
        data.putString("message", modReg.getEmptyUafMsgRegRequest());
        data.putString("UAFIntentType", UAFIntentType.DISCOVER.name());
        i.putExtras(data);
        startActivityForResult(i, 1);
        return;
    }

    public void regRequest(View view) {
        String username = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        if (username.equals ("")) {
            msg.setText("Username cannot be empty.");
            return;
        }
        Preferences.setSettingsParam("username", username);

        title.setText("Registration operation executed, Username = " + username);

        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        i.addCategory("android.intent.category.DEFAULT");
        i.setType("application/fido.uaf_client+json");
        List<ResolveInfo> queryIntentActivities = this.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
        title.setText("facetID=" + facetID);
        String appid = "https://test.noknoklabs.cn:8443/UAFSampleProxy/uaf/facets.uaf";
        String regRequest = modReg.getUafMsgRegRequest(username, facetID);
        title.setText("{regRequest}" + regRequest);

        Bundle data = new Bundle();
        data.putString("message", regRequest);
        data.putString("UAFIntentType", UAFIntentType.UAF_OPERATION.name());
        data.putString("channelBindings", regRequest);
        Log.e("reg Request", regRequest);
        i.putExtras(data);
        startActivityForResult(i, REG_ACTIVITY_RES_3);
    }

    private String getFacetID(PackageInfo paramPackageInfo) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramPackageInfo.signatures[0].toByteArray());
            Certificate certificate = CertificateFactory.getInstance("X509").generateCertificate(byteArrayInputStream);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            String facetID = "android:apk-key-hash:" + Base64.encodeToString(((MessageDigest) messageDigest).digest(certificate.getEncoded()), 3);
            return facetID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dereg(View view) {

        title.setText("Deregistration operation executed");
        String uafMessage = modDereg.getUafMsgRequest();
        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        i.addCategory("android.intent.category.DEFAULT");
        i.setType("application/fido.uaf_client+json");

        List<ResolveInfo> queryIntentActivities = this.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);

        Bundle data = new Bundle();
        data.putString("message", uafMessage);
        data.putString("UAFIntentType", "UAF_OPERATION");
        data.putString("channelBindings", uafMessage);
        i.putExtras(data);
        startActivityForResult(i, DEREG_ACTIVITY_RES_4);
    }

    public void authRequest(View view) {
        title.setText("facetID=" + facetID);
        title.setText("Authentication operation executed");
        String authRequest = modAuth.getUafMsgRequest(false, facetID);
        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        i.addCategory("android.intent.category.DEFAULT");
        i.setType("application/fido.uaf_client+json");
        Bundle data = new Bundle();
        data.putString("message", authRequest);
        data.putString("UAFIntentType", "UAF_OPERATION");
        data.putString("channelBindings", authRequest);
        Log.e("auth request", authRequest);
        i.putExtras(data);
        startActivityForResult(i, AUTH_ACTIVITY_RES_5);
    }

    public void trxRequest(View view) {
        title.setText("facetID=" + facetID);
        title.setText("Authentication operation executed");
        String authRequest = modAuth.getUafMsgRequest(true, facetID);
        Intent i = new Intent("org.fidoalliance.intent.FIDO_OPERATION");
        i.addCategory("android.intent.category.DEFAULT");
        i.setType("application/fido.uaf_client+json");
        Bundle data = new Bundle();
        data.putString("message", authRequest);
        data.putString("UAFIntentType", "UAF_OPERATION");
        data.putString("channelBindings", authRequest);
        Log.e("transaction request", authRequest);
        i.putExtras(data);
        startActivityForResult(i, AUTH_ACTIVITY_RES_5);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null){
            msg.setText("UAF Client didn't return any data. resultCode="+resultCode);
            return;
        }
        Object[] array = data.getExtras().keySet().toArray();
        StringBuffer extras = new StringBuffer();
        extras.append("[resultCode="+resultCode+"]");
        for (int i = 0; i < array.length; i++) {
            extras.append("[" + array[i] + "=");
            extras.append(""+data.getExtras().get((String) array[i]) + "]");
        }
        title.setText("extras=" + extras.toString());

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String asmResponse = data.getStringExtra("message");
                String discoveryData = data.getStringExtra("discoveryData");
                msg.setText("{message}" + asmResponse + "{discoveryData}" + discoveryData);
                //Prepare ReqResponse
                //post to server
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String asmResponse = data.getStringExtra("message");
                msg.setText(asmResponse);
                modDereg.recordKeyId(asmResponse);
                //Prepare ReqResponse
                //post to server
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == REG_ACTIVITY_RES_3) {
            if (resultCode == RESULT_OK) {
                try {
                    String uafMessage = data.getStringExtra("message");
                    Log.e("Reg message response", uafMessage);
                    msg.setText(uafMessage);
                    //Prepare ReqResponse
                    //post to server
                    String res = modReg.clientSendRegResponse(uafMessage, Preferences.getSettingsParam("username"));
                    setContentView(R.layout.activity_registered);
                    findFields();
                    title.setText("extras=" + extras.toString());
                    msg.setText(res);
                    username.setText(Preferences.getSettingsParam("username"));
                } catch (Exception e){
                    msg.setText("Registration operation failed.\n"+e);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == DEREG_ACTIVITY_RES_4) {
            if (resultCode == RESULT_OK) {
                setContentView(R.layout.activity_main);
                findFields();
                title.setText("extras=" + extras.toString());
                String message = data.getStringExtra("message");
                if (message != null) {
                    Log.e("dereg message", message);
                    String out = "Dereg done. Client msg=" + message;
                    DeregistrationRequest deregistrationRequest = gson.fromJson(message, DeregistrationRequest.class);
                    out = out + ". Sent=" + modDereg.newPost(deregistrationRequest.header.appID, deregistrationRequest.authenticators[0].keyID, Preferences.getSettingsParam("username"));
                    msg.setText(out);
                } else {
                    String deregMsg = Preferences.getSettingsParam("deregMsg");
                    String out = "Dereg done. Client msg was empty. Dereg msg = " + deregMsg;
                    Log.e("DEREGAAID", Preferences.getSettingsParam("AAID"));
                    Log.e("DEREG_MESSAGE", deregMsg);
                    out = out + ". Response=" + modDereg.newPost(Preferences.getSettingsParam("AAID"), Preferences.getSettingsParam("keyID"), Preferences.getSettingsParam("username"));
                    msg.setText(out);

                }
                Preferences.setSettingsParam("keyID", "");
                Preferences.setSettingsParam("username", "");

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == AUTH_ACTIVITY_RES_5) {
            Log.e("Auth", resultCode + data.getStringExtra("message"));
            if (resultCode == RESULT_OK) {
                String uafMessage = data.getStringExtra("message");
                if (uafMessage != null) {
                    msg.setText(uafMessage);
                    Log.e("Auth message response", uafMessage);
                    //Prepare ReqResponse
                    //post to server
                    String res = modAuth.clientSendResponse(uafMessage);
                    msg.setText("\n" + res);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent("cn.ac.iie.RPMod.fidouafclient.SettingsActivity"));
        }
        if (id == R.id.action_discover) {
            info(this.getWindow().getCurrentFocus());
        }
        if (id == R.id.action_save_message) {
            SaveMessageDialog.show(this, msg);
        }
        return super.onOptionsItemSelected(item);
    }

}
