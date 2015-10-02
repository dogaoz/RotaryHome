package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import br.com.condesales.EasyFoursquare;
import br.com.condesales.listeners.AccessTokenRequestListener;

public class loadFoursquare {
	static int ifyes = 0;
	static int loadFoursquare(final Activity mLauncher,final List<DoFeed_Item> FeedItemList)
	{
		try
		{

	

		final EasyFoursquare sync = new EasyFoursquare(mLauncher);
		
		sync.requestAccess(new AccessTokenRequestListener(){

			@Override
			public void onError(String errorMsg) {
Log.e("FoursquareDO","ErrorOnloadFoursquareToDofeed : " + errorMsg);
ifyes = 0;
			}

			@Override
			public void onAccessGrant(String accessToken) {
			Log.w("FoursquareDO","Grant : " + accessToken);

			ifyes = 2;
			}
			
		});
		
	
	int CheckInRequestResult = sync.getcheckInsForDO();
		if (CheckInRequestResult != 85995)
		{
		 for (int i=0;i<CheckInRequestResult;i++)
		 {
				JSONArray postJSON = sync.getCheckInItem(i);
	
				try
				{
				JSONObject json2= postJSON.getJSONObject(i); 
				String postType = json2.getString("type");
				int createdAt = json2.getInt("createdAt");

				//Location Related
				JSONObject json3= json2.getJSONObject("venue");
				String placeName = json3.getString("name");
				String url = null;
				try
				{
				url = json3.getString("url");
				}
				catch(Exception e)
				{
				url = "";	
				}

				//User Related
				JSONObject json4= json2.getJSONObject("user");
				String firstName = json4.getString("firstName");
				String lastName = json4.getString("lastName");
				String gender = json4.getString("gender");
				String relationship = json4.getString("relationship");

				//User Photo
				JSONObject json5= json4.getJSONObject("photo");
				String PrefixIMG = json5.getString("prefix");
				String SuffixIMG = json5.getString("suffix");

				String imageURL = PrefixIMG + "original" + SuffixIMG;


				Date date = new Date((long)createdAt*1000);
			
				String postTitle = "checked in at "+placeName;
				
							Log.w("FoursquareDO","=======================");
							Log.w("FoursquareDO","PostId = " +i);

							Log.w("FoursquareDO","PostType = " + postType);
							Log.w("FoursquareDO","PostCreatedAt = " + createdAt);
							Log.w("FoursquareDO","PostCreatedAtInDate = " + date);

							Log.w("FoursquareDO","PlaceOfCheckIn = " + placeName);
							Log.w("FoursquareDO","CheckInDidBy = " + firstName + " "+ lastName);
							Log.w("FoursquareDO","UserGender = " + gender);
							Log.w("FoursquareDO","UserImageURLofPostAuthor = " + imageURL);
							Log.w("FoursquareDO", "relationship with signedin user = " + relationship);
							
							
					DoFeed_Item df = new DoFeed_Item("foursquare", date, postTitle, imageURL, url, null, null, null, null, null);
					df.setSourceName(firstName + " "+ lastName );
					FeedItemList.add(df);
							
							Log.w("FoursquareDO","=======================");
				}
				catch (Exception e)
				{
					Log.e("FoursquareDO","Error in loadFoursquare : " +  e);
				}
						 
		 }

		}	
		else
		{
			return 1;
		}
			

		}
		catch (Exception e)
		{
			Log.e("FoursquareDO","Error Main : " + e);

			return 1;
		}
		return 0;	
	}
}
