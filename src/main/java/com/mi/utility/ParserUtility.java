package com.mi.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParserUtility {
	
	public  String getJsonArrayFromURL(String url) {
		String result = "";
		JSONObject jsonArray = null;
		try{
			URL requestURL = new URL(url);
			URLConnection con = requestURL.openConnection();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line + "\n");
					}
					System.out.println(" before esponse string ::::::");
					result = stringBuilder.toString();
					System.out.println("response string :::::::");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public  String getURL(String url) {
		String result = "";

		try{
			URL requestURL = new URL(url);
			URLConnection con = requestURL.openConnection();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder stringBuilder = new StringBuilder();
					String line = null;
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line + "\n");
					}
					result = stringBuilder.toString();



//			URL url;
//			HttpURLConnection urlConnection = null;
//			try {
//				url = new URL(urlText);
//
//				urlConnection = (HttpURLConnection) url
//						.openConnection();
//
//				int code = urlConnection.getResponseCode();
//
//				InputStream in = urlConnection.getInputStream();
//
//				InputStreamReader isw = new InputStreamReader(in);
//
//				int data = isw.read();
//				while (data != -1) {
//					char current = (char) data;
//					data = isw.read();
//					System.out.print(current);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (urlConnection != null) {
//					urlConnection.disconnect();
//				}
//			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}
	
	
	public String performPostonData(String URL,String Data) throws IOException
	{
		byte[] postData = Data.getBytes(Charset.forName("UTF-8"));
		StringBuilder  returnValue = new StringBuilder();   
		int postDataLength = postData.length;
		URL url            = new URL( URL );

		HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/json"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );

		DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
	    wr.write( postData );
	    wr.flush();

	    if(conn.getResponseCode() == 200)
	    {
	    	String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while (( line = reader.readLine()) != null) {
				System.out.println(line);
				returnValue.append(line);
			}
			wr.close();
			reader.close(); 
	    }
		return returnValue.toString();
	}
		
		
	
	

}
