package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class loadTumblr {

    public static final String CONSUMER_KEY = "zA05B3R48EBOjJx6ykilYL3KbRE5ZZickFH5V9uOXDAIOHSbQ5";
    public static final String CONSUMER_SECRET = "0M1SCmU3NifzQ5v2Z2KYEZF00rY1k6Kgmv82o808X0TEuuqpBr";
	static int loadTumblr(Activity mLauncher,List<DoFeed_Item> FeedItemList)
	{
		try
		{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mLauncher);
		
		String token = pref.getString("TUMBLR_OAUTH_TOKEN", "");
		String secret = pref.getString("TUMBLR_OAUTH_TOKEN_SECRET", "");
   
	
		JumblrClient client = new JumblrClient(
                CONSUMER_KEY,
                CONSUMER_SECRET
        );
				client.setToken(token,secret);
				
				
				
				// Make the request
				User user = client.user();
				
				String name = user.getName();
				
				Log.w("TumblrDO","Name :" + name);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("notes_info", true);
				List<Post> posts = client.userDashboard(params);
				
				for (int i=0;i<posts.size();i++)
				{
					Log.w("TumblrDO","Post Id :" + i);

					String blogName = posts.get(i).getBlogName();
					Log.w("TumblrDO","Post BlogName :" + blogName);

					// Buggy Version of code
//					String postTitle = posts.get(i).getSlug();
//					Log.w("TumblrDO","Post postTitle :" + postTitle);
					
					String userName = posts.get(i).getBlogName();

					String postTitle = posts.get(i).getSlug();
					postTitle = postTitle.replace("-", " ");

					Log.w("TumblrDO","Post postTitle :" + postTitle);
					
					//Get Blog Photo
					String blogPhotoUrl = client.blogAvatar(posts.get(i).getBlogName()+".tumblr.com",512);
				
					
					String date = posts.get(i).getDateGMT();
					Log.w("TumblrDO","Post Date :" + date);

				  
					//Post Date :2014-08-15 12:00:49 GMT

		        		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zz",Locale.US);  
						 
					    Date date2 = null;
						try {
							date2 = format.parse(date);
						} catch (ParseException e) {
							Log.e("DoDate","Tumblr Date parse error : "+ e);
						
						}  
					  Log.d("TumblrDO", "Tumblr Post Date Parsed : " + date2);
						 
		                
					String postUrl = posts.get(i).getPostUrl();
					Log.w("TumblrDO","Post Url :" + postUrl);
					Log.w("TumblrDO", "=====================");

					DoFeed_Item df = new DoFeed_Item("tumblr",date2 ,postTitle, name, postUrl, name, blogPhotoUrl, "-", "-", null);
					df.setSourceName(userName);
					FeedItemList.add(df);
				}
		}
		catch (Exception e)
		{
			return 1;
		}
		return 0;	
	}
}
