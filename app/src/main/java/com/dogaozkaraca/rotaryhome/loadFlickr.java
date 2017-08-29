package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.activity.Event;
import com.googlecode.flickrjandroid.activity.Item;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.photos.PhotoList;

import java.util.Date;
import java.util.List;


public class loadFlickr {
	static int loadFlickr(Activity mLauncher,List<DoFeed_Item> FeedItemList)
	{
		try
		{
			
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mLauncher);
		String token = pref.getString("FLICKR_OAUTH_TOKEN", "");
		String secret = pref.getString("FLICKR_OAUTH_SECRET", "");

			Flickr f = new Flickr("b6be3992191d2af5ac9235ab0d7853a9", "455057a0d9a22c42");

			RequestContext requestContext = RequestContext.getRequestContext();
			OAuth auth = new OAuth();
			auth.setToken(new OAuthToken(token, secret));
			requestContext.setOAuth(auth);
			//String timeFrame = "100d";
			//int page = 1;
			PhotoList flickrBro = f.getPhotosInterface().getContactsPhotos(200, false,false,false);
            int page =0;
            int maxPages=5;
			for (int i=0;i<flickrBro.size();i++) {
					Date pTime=flickrBro.get(i).getDatePosted();
					String author =flickrBro.get(i).getOwner().getUsername();
					String title = flickrBro.get(i).getTitle();
					int favcount = flickrBro.get(i).getFavorites();
					String id = flickrBro.get(i).getId();
					String imageUrl = flickrBro.get(i).getLargeUrl();
					String postUrl = flickrBro.get(i).getUrl();
					String userImageUrl = flickrBro.get(i).getOwner().getBuddyIconUrl();
					int commentedCount = flickrBro.get(i).getComments();
					boolean isFaved = flickrBro.get(i).isFavorite();
					DoFeed_Item df = new DoFeed_Item("flickr", pTime, title, imageUrl, postUrl,id, userImageUrl, favcount+"", commentedCount+"", isFaved);
					df.setSourceName(author);
					FeedItemList.add(df);
               //if(i==flickrBro.size()-1 && page < maxPages)
                //{
                //    f.getPhotosInterface().getContactsPhotos(200, false,false, true).setPage(page+1);
               //     i=0;
               // }
                Log.w("RotaryFlickr", "============================");
                Log.w("RotaryFlickr","item "+i );
                Log.w("RotaryFlickr","postId = "+id);
                Log.w("RotaryFlickr","title = " + title);
                Log.w("RotaryFlickr","DatePosted = "+ pTime);
                Log.w("RotaryFlickr","photoUrl = "+imageUrl);
                Log.w("RotaryFlickr", "url = " + postUrl);
                Log.w("RotaryFlickr", "postedBy = " + author);

                Log.w("RotaryFlickr","============================");


            }






		}
		catch(Exception e)
		{
			return 1;
		}


		return 0;
	}

}
