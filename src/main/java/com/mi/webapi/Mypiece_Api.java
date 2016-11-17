package com.mi.webapi;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Mypiece_Api {

	public static Context ObjContext;
	public static Mypiece_Api ObjGetService;
	public static boolean isOnline = true;
	JSONObject JObject_register;
	String Response;
	 
	private String URL_register = "http://mypiece.co/api_v3/all_api.php";
	//http://mypiece.co/api_v2/all_api.php
	//http://mypiece.co/api_v3/all_api.php
	public static Mypiece_Api GetInsatnce(Context paramContext) {
		// TODO Auto-generated method stub
		ObjContext = paramContext;
		isOnline=true;
		if (ObjGetService == null)
			ObjGetService = new Mypiece_Api();
		return ObjGetService;
	}

	public static DefaultHttpClient createClient() {

		// Methods.syso("HttpClient createClient()");

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
				HTTP.DEFAULT_CONTENT_CHARSET);
		params.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	public String Getbeacon(String method, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();

			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", method));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_codes));

			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = "http://mypiece.co/api_v3/all_api.php" + "?" + paramString;

			// Methods.syso("URL_register====" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;
				// Methods.syso("hello exception");

			}

			return "Exception==";
		}
	}

	public String GetRegistration(String method, String fnm, String lnm,
			String email, String password, String birthdate, String gender,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method==" + method + "=" + fnm + " " + lnm
			// + " " + email + " " + password + " " + birthdate + " "
			// + gender);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", method));
			nameValuePairs.add(new BasicNameValuePair("FirstName", fnm));
			nameValuePairs.add(new BasicNameValuePair("LastName", lnm));
			nameValuePairs.add(new BasicNameValuePair("Email", email));
			nameValuePairs.add(new BasicNameValuePair("Password", password));
			nameValuePairs.add(new BasicNameValuePair("BirthDate", birthdate));
			nameValuePairs.add(new BasicNameValuePair("Gender", gender));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_codes));
			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();

			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;

			}
			return "Exception==";
		}
	}

	public String GetSave_deal(String method, String user_id,
			String restaurant_id, String campaign_id, String status,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", method));
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs.add(new BasicNameValuePair("restaurant_id",
					restaurant_id));
			nameValuePairs.add(new BasicNameValuePair("campaign_id",
					campaign_id));
			nameValuePairs.add(new BasicNameValuePair("status", status));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_codes));
			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" "))
			{
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null)
			{
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Exception==";
		}
	}






	public String Getadvertisement(String method, String advertisement_id,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", method));
			nameValuePairs.add(new BasicNameValuePair("advertisement_id",
					advertisement_id));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_codes));

			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();

			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;

			}
			return "Exception==";
		}
	}
	public String PutBeacon(String method, String element_email_db,
								   String campaignid,String fbid) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("vEmail", element_email_db));
			nameValuePairs.add(new BasicNameValuePair("iCampaignId", campaignid));
			nameValuePairs.add(new BasicNameValuePair("fbid", fbid));
			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = "http://mypiece.co/webreq/" + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();

			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;

			}
			return "Exception==";
		}
	}

	public String TempAd(String GCM,String email,String fbid)
	{
		// TODO Auto-generated method stub

		try {



			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm
			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("vEmail", email));
			nameValuePairs_login.add(new BasicNameValuePair("fbid", fbid));
			nameValuePairs_login.add(new BasicNameValuePair("gcmID", GCM));
			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");
			// URL_register+="?"+paramString;
			String urltoLoad2 = "http://mypiece.co/admin/addurl";

			/*
			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null)
			{
				result.append(line);
				Log.d("ooooooooLLlllooooooooLLlllooooooooLLlll",line);
			}
			*/

			HttpClient httpclient1 = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://mypiece.co/admin/advertice/");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs_login1 = new ArrayList<NameValuePair>();
				nameValuePairs_login1.add(new BasicNameValuePair("vEmail", email));
				nameValuePairs_login1.add(new BasicNameValuePair("fbid", fbid));
				nameValuePairs_login1.add(new BasicNameValuePair("gcmID", GCM));
				Log.d("oll", GCM);
				//Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_SHORT).show();
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs_login1));

				// Execute HTTP Post Request
				HttpResponse response = httpclient1.execute(httppost);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null)
				{
					result.append(line);
					Log.d("oll",line);
				}
				HttpGet request2 = new HttpGet(urltoLoad2);

				HttpResponse response2 = httpclient.execute(request2);

				// Methods.syso("Response Code : "
				// + response.getStatusLine().getStatusCode());

				BufferedReader rd2 = new BufferedReader(new InputStreamReader(
						response2.getEntity().getContent()));

				StringBuffer result2 = new StringBuffer();
				String line2 = "";
				while ((line2 = rd2.readLine()) != null)
				{
					result2.append(line2);
					Log.d("",line2);
				}


				// Methods.syso(URL_register + "==\n" + result.toString());

				return result.toString() + " " + result2.toString();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}



		} catch (Exception e) {
			e.printStackTrace();

			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;

			}
			return "Exception==";
		}
		return "";
	}




	public String GetLogout(String method, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", method));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_codes));

			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetFilter(String method, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", method));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_codes));

			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetLogin(String method, String email, String pwd,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));

			nameValuePairs_login.add(new BasicNameValuePair("Email", email));
			nameValuePairs_login.add(new BasicNameValuePair("Password", pwd));
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;
				// Methods.syso("hello exception");

			}

			return "Exception==";
		}

	}

	public String GetMypiece_items(String method, String user_id,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));

			nameValuePairs_login
					.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

//			 Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

//			 Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}
	public String GetDelete_deal(String method, String user_id,String restaurent_id,String campaign_id,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			
			
			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));

			nameValuePairs_login
					.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs_login
			.add(new BasicNameValuePair("restaurant_id", restaurent_id));
			nameValuePairs_login
			.add(new BasicNameValuePair("campaign_id", campaign_id));
			
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

//			 Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

//			 Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}
	public String GetSupport(String method, String user_id, String date,
			String number, String issue, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));
			nameValuePairs_login.add(new BasicNameValuePair("userid", user_id));
			nameValuePairs_login.add(new BasicNameValuePair("date", date));
			nameValuePairs_login.add(new BasicNameValuePair("callback_no",
					number));
			nameValuePairs_login.add(new BasicNameValuePair("issue", issue));
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}

			// Methods.syso("URL_register==" + urlToLoad);

			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetLocation(String method, String latitude, String longitude,
			String distance, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();

			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));
			nameValuePairs_login.add(new BasicNameValuePair("lat", latitude));
			nameValuePairs_login.add(new BasicNameValuePair("long", longitude));
			nameValuePairs_login.add(new BasicNameValuePair("distance",
					distance));
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}

//			 Methods.syso("URL_register==" + urlToLoad);
			
			HttpGet request = new HttpGet(urlToLoad);
			HttpResponse response = httpclient.execute(request);
			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			// Methods.syso(URL_register + "==\n" + result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String Getfacebook_login(String method, String fnm, String lnm,
			String email, String birthdate, String gender, String facebookid,
			String usertype, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_fb_login = new ArrayList<NameValuePair>();
			nameValuePairs_fb_login.add(new BasicNameValuePair("tag", method));
			nameValuePairs_fb_login
					.add(new BasicNameValuePair("FirstName", fnm));
			nameValuePairs_fb_login
					.add(new BasicNameValuePair("LastName", lnm));
			nameValuePairs_fb_login.add(new BasicNameValuePair("Email", email));

			nameValuePairs_fb_login.add(new BasicNameValuePair("BirthDate",
					birthdate));
			nameValuePairs_fb_login
					.add(new BasicNameValuePair("Gender", gender));

			nameValuePairs_fb_login.add(new BasicNameValuePair("Facebookid",
					facebookid));
			nameValuePairs_fb_login.add(new BasicNameValuePair("Usertype",
					usertype));
			nameValuePairs_fb_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(
					nameValuePairs_fb_login, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);

			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}

			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetDeal_info(String method, String resturant_id,
			String campaign_id, String user_id, String lang_codes) {

		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();
			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm
			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);
			List<NameValuePair> nameValuePairs_fb_login = new ArrayList<NameValuePair>();
			nameValuePairs_fb_login.add(new BasicNameValuePair("tag", method));
			nameValuePairs_fb_login.add(new BasicNameValuePair("LocationId",
					resturant_id));
			nameValuePairs_fb_login.add(new BasicNameValuePair("CampaignId",
					campaign_id));
			nameValuePairs_fb_login.add(new BasicNameValuePair("iUserId",
					user_id));
			nameValuePairs_fb_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			// nameValuePairs_fb_login.add(new BasicNameValuePair("lat",
			// latitude));
			//
			// nameValuePairs_fb_login.add(new BasicNameValuePair("long",
			// longitude));
			// nameValuePairs_fb_login.add(new BasicNameValuePair("distance",
			// distance));
			String paramString = URLEncodedUtils.format(
					nameValuePairs_fb_login, "utf-8");
			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			 //Methods.syso("URL_register==" + urlToLoad);

			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}

			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

//			 Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetDeal_info_beacon(String method, String location_id,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();
			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm
			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);
			List<NameValuePair> nameValuePairs_fb_login = new ArrayList<NameValuePair>();
			nameValuePairs_fb_login.add(new BasicNameValuePair("tag", method));
			nameValuePairs_fb_login.add(new BasicNameValuePair("LocationId",
					location_id));
			nameValuePairs_fb_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			// nameValuePairs_fb_login.add(new BasicNameValuePair("lat",
			// latitude));
			//
			// nameValuePairs_fb_login.add(new BasicNameValuePair("long",
			// longitude));
			// nameValuePairs_fb_login.add(new BasicNameValuePair("distance",
			// distance));
			String paramString = URLEncodedUtils.format(
					nameValuePairs_fb_login, "utf-8");
			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			// Methods.syso("URL_register==" + urlToLoad);

			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetForgot(String method, String email, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();

			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));

			nameValuePairs_login.add(new BasicNameValuePair("Email", email));
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;
				// Methods.syso("hello exception");

			}
			return "Exception==";
		}
	}

	public String GetFilterinfo(String method, String value_text,
			String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_login = new ArrayList<NameValuePair>();
			nameValuePairs_login.add(new BasicNameValuePair("tag", method));

			nameValuePairs_login.add(new BasicNameValuePair("category",
					value_text));
			nameValuePairs_login.add(new BasicNameValuePair("lang_code",
					lang_codes));

			String paramString = URLEncodedUtils.format(nameValuePairs_login,
					"utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String GetProfileupadte(String method, String fnm, String lnm,
			String email, String password, String birthdate, String gender,
			String user_id, String lang_codes) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_profile_upadte = new ArrayList<NameValuePair>();
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("tag",
					method));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair(
					"FirstName", fnm));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair(
					"LastName", lnm));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("Email",
					email));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair(
					"Password", password));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair(
					"BirthDate", birthdate));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("Gender",
					gender));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("UserId",
					user_id));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair(
					"lang_code", lang_codes));
			String paramString = URLEncodedUtils.format(
					nameValuePairs_profile_upadte, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception==";
		}
	}

	public String Sendterms(String method, String userid) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();

			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_profile_upadte = new ArrayList<NameValuePair>();
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("tag",
					method));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("user_id",
					userid));

			String paramString = URLEncodedUtils.format(
					nameValuePairs_profile_upadte, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;
				// Methods.syso("hello exception");
			}
			return "Exception==";
		}
	}
	public String Getnew_all_deal(String method, String lang,String latitude,String longitude) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();

			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			// Methods.syso("method=="+method+"="+fnm+" "+lnm+" "+email+" "+password+" "+birthdate+" "+gender);

			List<NameValuePair> nameValuePairs_profile_upadte = new ArrayList<NameValuePair>();
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("tag",
					method));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("lang_code",
					lang));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("latitude",
					latitude));
			nameValuePairs_profile_upadte.add(new BasicNameValuePair("longitude",
					longitude));

			String paramString = URLEncodedUtils.format(
					nameValuePairs_profile_upadte, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = "http://www.mypiece.co/api_v3/all_api.php" + "?" + paramString;

			 //Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;
				// Methods.syso("hello exception");
			}
			return "Exception==";
		}
	}
	
	public String Getnew_out_town_notify(String tag, String iUserId,
			String vLatitude,String vLongitude, String vState,
			String vCity,String lang_code) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", tag));
			nameValuePairs.add(new BasicNameValuePair("iUserId",
					iUserId));
			nameValuePairs.add(new BasicNameValuePair("vLatitude", vLatitude));
			nameValuePairs.add(new BasicNameValuePair("vLongitude",
					vLongitude));
			nameValuePairs.add(new BasicNameValuePair("vState", vState));
			nameValuePairs.add(new BasicNameValuePair("vCity",
					vCity));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_code));

			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();

			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;

			}
			return "Exception==";
		}
	}
	
	
	
			
			
	public String Getnew_in_town_notify(String tag, String iUserId,
			String vLatitude,String vLongitude, String dDate,
			String tTime,String lang_code) {
		// TODO Auto-generated method stub

		try {
			HttpClient httpclient = createClient();
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			// URL_register=URL_register+"?tag="+method+"&FirstName="+fnm

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("tag", tag));
			nameValuePairs.add(new BasicNameValuePair("iUserId",
					iUserId));
			nameValuePairs.add(new BasicNameValuePair("vLatitude", vLatitude));
			nameValuePairs.add(new BasicNameValuePair("vLongitude",
					vLongitude));
			nameValuePairs.add(new BasicNameValuePair("dDate", dDate));
			nameValuePairs.add(new BasicNameValuePair("tTime",
					tTime));
			nameValuePairs.add(new BasicNameValuePair("lang_code", lang_code));

			String paramString = URLEncodedUtils
					.format(nameValuePairs, "utf-8");

			// URL_register+="?"+paramString;
			String urlToLoad = URL_register + "?" + paramString;

			// Methods.syso("URL_register==" + urlToLoad);
			if (urlToLoad.contains(" ")) {
				urlToLoad = urlToLoad.replaceAll(" ", "%20");
			}
			HttpGet request = new HttpGet(urlToLoad);

			HttpResponse response = httpclient.execute(request);

			// Methods.syso("Response Code : "
			// + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			// Methods.syso(URL_register + "==\n" + result.toString());

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();

			if (e.toString().contains(
					"java.net.UnknownHostException: Unable to resolve host")) {
				isOnline = false;

			}
			return "Exception==";
		}
	}
}
