package cn.ac.iie.RPMod.fidouafclient.curl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class Curl {

	private static String cookies;
	private static HashMap<String,String>  CookieContiner=new HashMap<String,String>() ;

	public static void SaveCookies(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getHeaders("Set-Cookie");
		String headerstr=headers.toString();
		if (headers == null)
			return;

		for(int i=0;i<headers.length;i++)
		{
			String cookie=headers[i].getValue();
			String[]cookievalues=cookie.split(";");
			for(int j=0;j<cookievalues.length;j++)
			{
				String[] keyPair=cookievalues[j].split("=");
				String key=keyPair[0].trim();
				String value=keyPair.length>1?keyPair[1].trim():"";
				CookieContiner.put(key, value);
			}
		}
	}

	public static void AddCookies(HttpPost request) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			String val = entry.getValue().toString();
			sb.append(key);
			sb.append("=");
			sb.append(val);
			sb.append(";");
		}
		request.addHeader("cookie", sb.toString());
	}

	public static String toStr(HttpResponse response) {
		String result = "";
		try {
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}
			in.close();
			result = str.toString();
		} catch (Exception ex) {
			result = "Error";
		}
		return result;
	}
	
	public static String getInSeparateThread(String url) {
		GetAsyncTask async = new GetAsyncTask();
		async.execute(url);
		while (!async.isDone()){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return async.getResult();
	}
	
	public static String postInSeparateThread(String url, String header, String data) {
		PostAsyncTask async = new PostAsyncTask();
		async.execute(url, header, data);
		while (!async.isDone()){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return async.getResult();
	}
	
	public static String get(String url) {
		return get(url,null);
	}

	public static String get(String url, String[] header) {
		String ret = "";
		try {

			HttpClient httpClient = getClient(url);

			HttpGet request = new HttpGet(url);
			try {
				if (header != null){
					for (String h : header){
						String[] split = h.split(":");
						request.addHeader(split[0], split[1]);
					}
				}
				HttpResponse response = httpClient.execute(request);
				ret = Curl.toStr(response);
				Header[] headers = response.getAllHeaders();

			} catch (Exception ex) {
				ex.printStackTrace();
				ret = "{'error_code':'connect_fail','url':'" + url + "'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = "{'error_code':'connect_fail','e':'" + e + "'}";
		}

		return ret;
	}
	
	public static String post(String url, String header, String data) {
		return post (url, header.split(" "), data);
	}


	public static String post(String url, String[] header, String data) {
		String ret = "";
		try {

			HttpClient httpClient = getClient(url);

			HttpPost request = new HttpPost(url);
			AddCookies(request);
			if (header != null){
				for (String h : header){
					String[] split = h.split(":");
					request.addHeader(split[0], split[1]);
				}
			}
//			if(!TextUtils.isEmpty(cookies)){
//				request.addHeader("Cookie", cookies);
//			}

			request.setEntity(new StringEntity(data));
			try {
				HttpResponse response = httpClient.execute(request);
				ret = Curl.toStr(response);
				SaveCookies(response);
				Header[] headers = response.getAllHeaders();
				for(int i=0; i<headers.length; i++){
					Log.e("HEADER", headers[i].getName() + " " + headers[i].getValue());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ret = "{'error_code':'connect_fail','url':'" + url + "'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = "{'error_code':'connect_fail','e':'" + e + "'}";
		}

		return ret;
	}

	private static HttpClient getClient(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		if (url.toLowerCase().startsWith("https")) {
			httpClient = createHttpsClient();
		}
		return httpClient;
	}

	private static HttpClient createHttpsClient() {
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 8443));
		HttpClient client = new DefaultHttpClient();
		SingleClientConnManager mgr = new SingleClientConnManager(
				client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
				client.getParams());
		return httpClient;
	}

}

class GetAsyncTask extends AsyncTask<String, Integer, String>{

	private String result = null;
	private boolean done = false;
	public boolean isDone() {
		return done;
	}
	public String getResult() {
		return result;
	}
	@Override
	protected String doInBackground(String... args) {
		result = Curl.get(args[0]);
		done = true;
		return result;
	}
	protected void onProgressUpdate(Integer... progress) {
    }
    protected void onPostExecute(String result) {
		this.result = result;
		done = true;
	}
}

class PostAsyncTask extends AsyncTask<String, Integer, String>{

	private String result = null;
	private boolean done = false;
	public boolean isDone() {
		return done;
	}
	public String getResult() {
		return result;
	}
	@Override
	protected String doInBackground(String... args) {
		result = Curl.post(args[0],args[1],args[2]);//(url, header, data)
		done = true;
		return result;
	}
	protected void onProgressUpdate(Integer... progress) {
    }
	@Override
	protected void onPostExecute(String result) {
		this.result = result;
		done = true;
	}
}
