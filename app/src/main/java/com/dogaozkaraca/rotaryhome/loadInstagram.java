package com.dogaozkaraca.rotaryhome;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import instagram.InstagramApp;

public class loadInstagram
{

    public static String AccessToken;
public static int loadInstagram(String type, String[] TimeLine ,String[] PostTitle ,String[] ImageURL ,String[] PostURL, List<DoFeed_Item> FeedItemList,InstagramApp instagramConnector)
{
	try
	{

		AccessToken =   instagramConnector.getAccessToken();
    	
    	String UserFeedUrl = "https://api.instagram.com/v1/users/self/feed?count=355&access_token=" + AccessToken;
   		
		   HttpClient client = new DefaultHttpClient();
           HttpGet request = new HttpGet();

           request.setURI(new URI(UserFeedUrl));
           HttpResponse response = client.execute(request);
		InputStream a = response.getEntity().getContent();
		String JsonResponse = convertInstagramStreamToString.convertStreamToString(a);
		 JSONObject respObject = null;
			try {
				respObject = new JSONObject(JsonResponse);
			} catch (JSONException e) {
				return 1;
			} 
 			    JSONArray id = null;
				
				
					id = respObject.getJSONArray("data");
					Log.w("DO-Instagram","idgotdolauncher"+id);
					String StandartResolutionUrl = null;
					String From = null;
					String Caption = null;
					String CreatedTime = null;
					String Link = null;
					String postId = null;

		for (int k = 0; k < 31; k++) {
	                    	
			        try
			        {
					StandartResolutionUrl = id.getJSONObject(k).getJSONObject("images").getJSONObject("standard_resolution").getString("url");
			        }
			        catch(Exception er)
			        {
			        	StandartResolutionUrl ="";
			        }
			        try
			        {
					Caption = id.getJSONObject(k).getJSONObject("caption").getString("text");
			        }
			        catch(Exception er)
			        {
			        	 Caption ="";
			        }
			        try
			        {
					CreatedTime = id.getJSONObject(k).getJSONObject("caption").getString("created_time");
			        }
			        catch(Exception er)
			        {
			        	 CreatedTime ="";
			        }
			        String profile_picture=null;
			        try
			        {
			        	
			        	profile_picture = id.getJSONObject(k).getJSONObject("caption").getJSONObject("from").getString("profile_picture");
			        	Log.w("doi","pr : "+profile_picture);
			        }
			        catch(Exception er)
			        {
			        	profile_picture =null;
			        	Log.w("doi","pr : "+profile_picture);

			        }
			        
			        try
			        {
				postId = id.getJSONObject(k).getString("id");
			        }
			        catch(Exception er)
			        {
			        	postId ="";
			        }
			        try
			        {
				Link = id.getJSONObject(k).getString("link");
			        }
			        catch(Exception er)
			        {
			        	Link ="";
			        }
			        try
			        {
				From = id.getJSONObject(k).getJSONObject("user").getString("username");
			        }
			        catch(Exception er)
			        {
			        	From ="";
			        }
			        
			        String HeartCount;
			        try
			        {
			        	HeartCount = id.getJSONObject(k).getJSONObject("likes").getInt("count")+"";
			        }
			        catch(Exception er)
			        {
			        	HeartCount ="";
			        }
					String InstagramPostTitle = Caption;
					String InstagramPostImage = StandartResolutionUrl;
					String InstagramPostLink = Link;
					String InstagramUserNameWhoPosted = From;
					
				try
				{
					Date date = new Date(Long.parseLong(CreatedTime)*1000);
					Log.w("DoFeed_Instagram","Post Created Time : " + date);
					
					Log.w("DoFeed_Instagram"," FRM " + k+ " : " + From);
					Log.w("DoFeed_Instagram"," CPT " + k+ " : " + Caption);
					Log.w("DoFeed_Instagram"," SRU " + k+ " : " + StandartResolutionUrl);
					Log.w("DoFeed_Instagram", " Id " + k + " : " + postId);
					Log.w("DoFeed_Instagram", " LNK " + k + " : " + Link);

					Log.w("Do-Instagram", " ===========================");

			        DoFeed_Item df = new DoFeed_Item(type, date,InstagramPostTitle, InstagramPostImage, InstagramPostLink, postId, profile_picture, HeartCount, null,false);
					df.setSourceName(InstagramUserNameWhoPosted);
					FeedItemList.add(df);
				}
				catch
				(Exception e)
				{
					//Don't add broken post
				}
					if (k == 12)
					{
						//Doga,don't forget to add pagination stuff later for longer feed
//						Log.w("Do-Instagram","Pagination");
//
//						String NextPage = respObject.getJSONObject("pagination").getString("next_url");
//						Log.w("Do-Instagram"," NPM " + k+ " : " + NextPage);
//
//				          request.setURI(new URI(NextPage));
//	    	               response = client.execute(request);
//						a = response.getEntity().getContent();
//						 JsonResponse = convertStreamToString(a);
//						
//						
//								respObject = new JSONObject(JsonResponse);
//								Log.w("Do-Instagram","Pagination Completed");
	
					}
					
		} //Loop per post ends here Doga.
	}
	catch(Exception e)
	{
		Log.e("DoFeed_Instagram","FeedLoader crashed : " + e);
		return 1;
	}
	return 0;
}

	//Don't use till get instagram permission
	// usage :  isPhotoLiked(postId,AccessToken)
    private static Boolean isPhotoLiked(String postId, String accessToken) throws URISyntaxException, IOException, JSONException {


        String url = "https://api.instagram.com/v1/media/"+postId+ "?access_token=" + accessToken;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();

        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
        InputStream a = response.getEntity().getContent();
        String JsonResponse = convertInstagramStreamToString.convertStreamToString(a);
        JSONObject respObject = new JSONObject(JsonResponse);

JSONObject id = respObject.getJSONObject("data");

        boolean userhasliked = id.getBoolean("user_has_liked");

        if(userhasliked)
        {
            return true;
        }
        else
        {
            return false;
        }


    }




}
