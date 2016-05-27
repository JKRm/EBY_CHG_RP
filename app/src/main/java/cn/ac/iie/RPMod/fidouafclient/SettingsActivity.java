package cn.ac.iie.RPMod.fidouafclient;


import java.util.logging.Logger;

import cn.ac.iie.RPMod.fidouafclient.util.Endpoints;
import cn.ac.iie.RPMod.fidouafclient.util.NewEndpoints;
import cn.ac.iie.RPMod.fidouafclient.util.Preferences;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class SettingsActivity extends Activity {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EditText username;
	private EditText regReqEndpoint;
	private EditText regResEndpoint;
	private EditText authReqEndpoint;
	private EditText authResEndpoint;
	private EditText dereqEndpoint;
	private EditText serverEndpoint;
	private TextView msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("  [APP][SettingsActivity]  ");
        setContentView(R.layout.activity_settings);
        regReqEndpoint = (EditText) findViewById(R.id.regRequestEndpoint);
        regResEndpoint = (EditText) findViewById(R.id.regResponseEndpoint);
        authResEndpoint = (EditText) findViewById(R.id.authResponseEndpoint);
        authReqEndpoint = (EditText) findViewById(R.id.authRequestEndpoint);
        dereqEndpoint = (EditText) findViewById(R.id.deregEndpoint);
        serverEndpoint = (EditText) findViewById(R.id.server);
        username = (EditText) findViewById(R.id.username);
        msgs = (TextView) findViewById(R.id.settingsMsgs);
        populate();
    }
    
	private void populate() {
		this.username.setText(Preferences.getSettingsParam("username"));
		this.serverEndpoint.setText(NewEndpoints.getServer());
		this.authReqEndpoint.setText(NewEndpoints.getAuthRequestPath());
		this.authResEndpoint.setText(NewEndpoints.getAuthResponsePath());
		this.regReqEndpoint.setText(NewEndpoints.getRegRequestPath());
		this.regResEndpoint.setText(NewEndpoints.getRegResponsePath());
		this.dereqEndpoint.setText(NewEndpoints.getDeregPath());
	}

	public void back(View view) {
		finish();
	}
	
	public void reset(View view) {
		Endpoints.setDefaults();
		populate();
	}

	public void save(View view) {
		Preferences.setSettingsParam("username", this.username.getText().toString());
		NewEndpoints.save(
				this.serverEndpoint.getText().toString(),
				this.authReqEndpoint.getText().toString(),
				this.authResEndpoint.getText().toString(),
				this.regReqEndpoint.getText().toString(),
				this.regResEndpoint.getText().toString(),
				this.dereqEndpoint.getText().toString()
				);
		msgs.setText("Saved.");
	}   


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
