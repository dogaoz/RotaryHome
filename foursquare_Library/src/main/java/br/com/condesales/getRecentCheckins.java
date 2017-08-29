package br.com.condesales;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import br.com.condesales.constants.FoursquareConstants;

public class getRecentCheckins {

	static JSONObject venuesJson;
	
	public static int getListSize(String accessToken)
	{
	 	try {
		venuesJson = executeHttpGet("https://api.foursquare.com/v2/checkins/recent", accessToken);
			// Get return code
			int returnCode = Integer.parseInt(venuesJson.getJSONObject("meta")
					.getString("code"));
			// 200 = OK
			if (returnCode == 200) {
				Log.d("FoursquareDO","returnCode==200 - Congrats ; FullResult returned :" + venuesJson);

				JSONArray json = venuesJson.getJSONObject("response").getJSONArray("recent");

				return json.length();
			} else {
Log.e("FoursquareDO","Error return code not 200 for Do Launcher");
Log.e("FoursquareDO","here is not 200's returned json : " + venuesJson);

			return 85995;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 85995;
		}
		
	}
	
	public static JSONArray getListItem(int itemNo)
	{
		JSONArray json;
		try {
			json = venuesJson.getJSONObject("response").getJSONArray("recent");
			
//			JSONObject json2= json.getJSONObject(itemNo); 
//			String postType = json2.getString("type");
//			int createdAt = json2.getInt("createdAt");
//			
//			//Location Related
//			JSONObject json3= json2.getJSONObject("venue");
//			String placeName = json3.getString("name");
//			
//			//User Related
//			JSONObject json4= json2.getJSONObject("user");
//			String firstName = json4.getString("firstName");
//			String lastName = json4.getString("lastName");
//			String gender = json4.getString("gender");
//			String relationship = json4.getString("relationship");
//
//
//						Log.w("FoursquareDO","=======================");
//						Log.w("FoursquareDO","PostId = " +itemNo);
//
//						Log.w("FoursquareDO","PostType = " + postType);
//						Log.w("FoursquareDO","PostCreatedAt = " + createdAt);
//						Log.w("FoursquareDO","PlaceOfCheckIn = " + placeName);
//						Log.w("FoursquareDO","CheckInDidBy = " + firstName + " "+ lastName);
//						Log.w("FoursquareDO","UserGender = " + gender);
//						Log.w("FoursquareDO","relationship with this user = " + relationship);
//
//						
//						
//						
//						Log.w("FoursquareDO","=======================");
			return json;	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
	}
	
	public static void startGettingCheckins(String accessToken)
	{
	 	try {
			JSONObject venuesJson = executeHttpGet(
					"https://api.foursquare.com/v2/checkins/recent", accessToken);
			// Get return code
			int returnCode = Integer.parseInt(venuesJson.getJSONObject("meta")
					.getString("code"));
			// 200 = OK
			if (returnCode == 200) {
				Log.d("FoursquareDO","returnCode==200 - Congrats ; FullResult returned :" + venuesJson);

				JSONArray json = venuesJson.getJSONObject("response").getJSONArray("recent");

				for (int i =0;i<json.length();i++)
				{
		
		//General Post Stuff
		

				}
				
			} else {
Log.e("FoursquareDO","Error return code not 200 for Do Launcher");
Log.e("FoursquareDO","here is not 200's returned json : " + venuesJson);


			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	
	private static JSONObject executeHttpGet(String uri, String accessToken) throws Exception {
		String apiDateVersion = FoursquareConstants.API_DATE_VERSION;

		HttpGet req = new HttpGet(uri+"?oauth_token="+accessToken+"&v="+apiDateVersion);
		HttpClient client = new DefaultHttpClient();
		
		HttpResponse resCheckin = client.execute(req);
		BufferedReader r = new BufferedReader(new InputStreamReader(resCheckin
				.getEntity().getContent()));
		StringBuilder sb = new StringBuilder();
		String s = null;
		while ((s = r.readLine()) != null) {
			sb.append(s);
		}
		return new JSONObject(sb.toString());
	}
}
