package com.dogaozkaraca.rotaryhome;
import java.util.List;

import twitter4j.conf.ConfigurationBuilder;

import instagram.InstagramApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

public class NetworkAdaptor extends BaseAdapter {
    private Context mContext;
    private List<NetworkItem> mListAppInfo;
	 InstagramApp mApp;
	 static final String PREFS_D = "twitterToken";
	 static final String PREFS_DE = "twittersecretToken";
	 ConfigurationBuilder cb ;
	 String NameFromTwitter = "";
	SharedPreferences settings;
    public NetworkAdaptor(Context ct, List<NetworkItem> feedItemList) {
        mContext = ct;
        mListAppInfo = feedItemList;
        
        final String INSTACLIENT_ID = "cf6a80940a984aa1b94d90fd3a550fc5";
		final String INSTACLIENT_SECRET = "23895aa86cb6447ead6643e061173057";
		final String INSTACALLBACK_URL = "instado://a";

		settings = mContext.getSharedPreferences("DONetworks", 0);

		mApp = new InstagramApp(mContext,
	    	        INSTACLIENT_ID, 
	    	        INSTACLIENT_SECRET, 
	    	        INSTACALLBACK_URL);
			final String FACEBOOK_APPID = "731265210226246";
			final String FACEBOOK_PERMISSION = "read_stream";

				
    }
 
    @Override
    public int getCount() {
        return mListAppInfo.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mListAppInfo.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public class ViewHolder{
        TextView ItemTitle;
        ImageButton btn;
        ImageButton fakeCheckBox;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NetworkItem article = mListAppInfo.get(position);
  
        if(convertView == null) {
        	
         LayoutInflater inflater = LayoutInflater.from(mContext);
            
       	
       
        	 ViewHolder holder  = new ViewHolder();
       	 convertView = inflater.inflate(R.layout.do_network_selector_item, null);
         holder.ItemTitle = (TextView)convertView.findViewById(R.id.textView1);
         holder.btn = (ImageButton) convertView.findViewById(R.id.button1);
         holder.fakeCheckBox = (ImageButton)  convertView.findViewById(R.id.ImageButton01);
         convertView.setTag(holder);
        
        


        }
        final ViewHolder holder = (ViewHolder)convertView.getTag();

       
        if (article.getTitle().equals("rss"))
        {
  

    		final Boolean bl = settings.getBoolean("SN_rss", true);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
			holder.btn.setImageResource(R.drawable.rss);
    	
        holder.ItemTitle.setText("RSS Feed");
       holder.btn.setOnClickListener(new OnClickListener(){

    	   	@Override
			public void onClick(View v) {
			// RSS Settings
						Intent i=new Intent(mContext,rss_feed_settings.class);
						mContext.startActivity(i);

			}});
        }
        else      if (article.getTitle().equals("tumblr"))
        {
			holder.btn.setImageResource(R.drawable.tumblr);

    		final Boolean bl = settings.getBoolean("SN_tumblr", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
    	
    	
        holder.ItemTitle.setText("Tumblr");
       holder.btn.setOnClickListener(new OnClickListener(){

    	   	@Override
			public void onClick(View v) {
			// Tumblr Settings
				if(rotaryIsPro(mContext)==true) {

					Intent i=new Intent(mContext,TumblrLogin.class);
					mContext.startActivity(i);
				}
				else {
					launchDialog("pro_or_premium",mContext);
				}

			}});
        }
        else  if (article.getTitle().equals("googleplus"))
        {
			holder.btn.setImageResource(R.drawable.googleplus);

    		final Boolean bl = settings.getBoolean("SN_googleplus", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
    	
    	
        holder.ItemTitle.setText("Google+");
       holder.btn.setOnClickListener(new OnClickListener(){

    	   	@Override
			public void onClick(View v) {
			// Google+ Settings
    	   		//Intent i=new Intent(mContext,GooglePlus.class);
				//mContext.startActivity(i);
			}});
        }
		else  if (article.getTitle().equals("flickr"))
		{
			holder.btn.setImageResource(R.drawable.flickr);

			final Boolean bl = settings.getBoolean("SN_flickr", false);

			if (bl == false)
			{
				setFakeCheckBoxFalse(holder.fakeCheckBox);
			}
			else if (bl == true)
			{
				setFakeCheckBoxTrue(holder.fakeCheckBox);

			}


			holder.ItemTitle.setText("Flickr");
			holder.btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// Flickr Settings

					if(rotaryIsPro(mContext)==true) {
						Intent i=new Intent(mContext,FlickrLogin.class);
						mContext.startActivity(i);
					}
					else {
						launchDialog("pro_or_premium",mContext);
					}
			//launchBetaDialog(mContext);
				}});
		}
        else  if (article.getTitle().equals("foursquare"))
        {
			holder.btn.setImageResource(R.drawable.foursquare);

    		final Boolean bl = settings.getBoolean("SN_foursquare", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
    	
    	
        holder.ItemTitle.setText("Foursquare");
       holder.btn.setOnClickListener(new OnClickListener(){

    	   	@Override
			public void onClick(View v) {
			// Foursquare Settings
				if(rotaryIsPro(mContext)==true) {
					Intent i = new Intent(mContext, FoursquareLogin.class);
					mContext.startActivity(i);
				}
				else {
					launchDialog("pro_or_premium",mContext);
				}
			}});
        }
        else  if (article.getTitle().equals("linkedin"))
        {
			holder.btn.setImageResource(R.drawable.linkedin);

    		final Boolean bl = settings.getBoolean("SN_linkedin", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
    	
    	
        holder.ItemTitle.setText("LinkedIn");
       holder.btn.setOnClickListener(new OnClickListener(){

    	   	@Override
			public void onClick(View v) {
			// LinkedIn Settings


				if(rotaryIsPro(mContext)==true) {

					Intent i=new Intent(mContext,LinkedInLogin.class);
					mContext.startActivity(i);
				}
				else {
					launchDialog("pro_or_premium",mContext);
				}
				//launchBetaDialog(mContext);
			}});
        }
        else  if (article.getTitle().equals("facebook"))
        {
			holder.btn.setImageResource(R.drawable.facebook500);

    		final Boolean bl = settings.getBoolean("SN_facebook", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
    		        holder.ItemTitle.setText("Facebook");

        holder.btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    			// Facebook

						Intent i = new Intent(mContext, FacebookActivity.class);
						mContext.startActivity(i);

    			}});
        }
        else  if (article.getTitle().equals("twitter"))
        {
			holder.btn.setImageResource(R.drawable.twitter);

    		final Boolean bl = settings.getBoolean("SN_twitter", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
   
	    
    	
	             holder.ItemTitle.setText("Twitter");
	    	 	
        
        holder.btn.setOnClickListener(new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    			// Twitter Settings

						Intent i = new Intent(mContext, TwitterLogin.class);
						mContext.startActivity(i);

    			}});
        }
        else  if (article.getTitle().equals("instagram"))
        {
			holder.btn.setImageResource(R.drawable.instagram);

    		final Boolean bl = settings.getBoolean("SN_instagram", false);
    		
    		if (bl == false)
    		{
    			setFakeCheckBoxFalse(holder.fakeCheckBox);
    		}
    		else if (bl == true)
    		{
    			setFakeCheckBoxTrue(holder.fakeCheckBox);
		
    		}
    	

        		holder.ItemTitle.setText("Instagram");
        		holder.btn.setOnClickListener(new OnClickListener(){
        		@Override
        		public void onClick(View v) {
        				// Instagram Settings

						Intent i = new Intent(mContext, InstagramActivity.class);
						mContext.startActivity(i);

        		}});
        }

        holder.ItemTitle.setTextColor(Color.BLACK);
        
        
        
        holder.fakeCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (article.getTitle().equals("rss")) {
					final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_rss",true);
					if (bl == false) {
						final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
						SharedPreferences.Editor editor = settings.edit();

						editor.putBoolean("SN_rss", true);
						editor.commit();
						setFakeCheckBoxTrue(holder.fakeCheckBox);

					} else if (bl == true) {
						final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
						SharedPreferences.Editor editor = settings.edit();

						editor.putBoolean("SN_rss", false);
						editor.commit();
						setFakeCheckBoxFalse(holder.fakeCheckBox);


					}
				} else if (article.getTitle().equals("facebook")) {
					final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_facebook", false);


						if (bl == false) {

							if (isLoggedIn()) {
								final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
								SharedPreferences.Editor editor = settings.edit();

								editor.putBoolean("SN_facebook", true);
								editor.commit();
								setFakeCheckBoxTrue(holder.fakeCheckBox);

							} else
							{


								Toast.makeText(mContext, "You're not logged in to Facebook, please login by clicking settings button.", Toast.LENGTH_LONG).show();
							}
						} else if (bl == true) {
							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_facebook", false);
							editor.commit();
							setFakeCheckBoxFalse(holder.fakeCheckBox);

						}

				} else if (article.getTitle().equals("twitter")) {

					final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_twitter", false);


						if (bl == false) {
							SharedPreferences settings = mContext.getSharedPreferences(PREFS_D, 0);

							String twitterToken = settings.getString(PREFS_D, null);
							SharedPreferences settings3 = mContext.getSharedPreferences(PREFS_DE, 0);

							String twittersecretToken = settings3.getString(PREFS_DE, null);
							if (twitterToken != null || twittersecretToken != null) {
								setFakeCheckBoxTrue(holder.fakeCheckBox);


								final SharedPreferences settings2 = mContext.getSharedPreferences("DONetworks", 0);
								SharedPreferences.Editor editor = settings2.edit();

								editor.putBoolean("SN_twitter", true);
								editor.commit();
							} else if (twitterToken == null || twittersecretToken == null) {

								Toast.makeText(mContext, "You're not logged in to Twitter, please login by clicking settings button.", Toast.LENGTH_LONG).show();

							}

						} else if (bl == true) {

							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_twitter", false);
							editor.commit();
							setFakeCheckBoxFalse(holder.fakeCheckBox);


						}




				} else if (article.getTitle().equals("instagram")) {
					final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_instagram", false);
						if (bl == false) {


							if (mApp.hasAccessToken()) {
								final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
								SharedPreferences.Editor editor = settings.edit();

								editor.putBoolean("SN_instagram", true);
								editor.commit();
								setFakeCheckBoxTrue(holder.fakeCheckBox);

							} else {

								Toast.makeText(mContext, "You're not logged in to Instagram, please login by clicking settings button.", Toast.LENGTH_LONG).show();

							}
						} else if (bl == true) {
							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_instagram", false);
							editor.commit();
							setFakeCheckBoxFalse(holder.fakeCheckBox);


						}



				} else if (article.getTitle().equals("tumblr")) {
					final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_tumblr", false);

					if (rotaryIsPro(mContext) == true) {
						if (bl == false) {
							SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

							String token = pref.getString("TUMBLR_OAUTH_TOKEN", "");
							String secret = pref.getString("TUMBLR_OAUTH_TOKEN_SECRET", "");
							if (token != null && token != "" && secret != null && secret != "") {
								final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
								SharedPreferences.Editor editor = settings.edit();

								editor.putBoolean("SN_tumblr", true);
								editor.commit();
								setFakeCheckBoxTrue(holder.fakeCheckBox);

							} else {

								Toast.makeText(mContext, "You're not logged in to Tumblr, please login by clicking settings button.", Toast.LENGTH_LONG).show();

							}
						} else if (bl == true) {
							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_tumblr", false);
							editor.commit();
							setFakeCheckBoxFalse(holder.fakeCheckBox);


						}

					} else {
						launchDialog("pro_or_premium", mContext);
					}


					} else if (article.getTitle().equals("linkedin")) {
						final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

						final Boolean bl = settingsmain.getBoolean("SN_linkedin", false);

					if (rotaryIsPro(mContext) == true) {
						if (bl == false) {
							SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

							String token = pref.getString("LINKEDIN_OAUTH_TOKEN", "");
							String secret = pref.getString("LINKEDIN_OAUTH_TOKEN_SECRET", "");
							if (token != null && token != "" && secret != null && secret != "") {
								final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
								SharedPreferences.Editor editor = settings.edit();

								editor.putBoolean("SN_linkedin", true);
								editor.commit();
								setFakeCheckBoxTrue(holder.fakeCheckBox);

							} else {

								Toast.makeText(mContext, "You're not logged in to LinkedIn, please login by clicking settings button.", Toast.LENGTH_LONG).show();

							}
						} else if (bl == true) {
							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_linkedin", false);
							editor.commit();
							setFakeCheckBoxFalse(holder.fakeCheckBox);


						}

					} else {
						launchDialog("pro_or_premium", mContext);
						//launchBetaDialog(mContext);
					}


					}
				else if (article.getTitle().equals("flickr")) {
					final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_flickr", false);

					SharedPreferences.Editor editor = settingsmain.edit();

					if (rotaryIsPro(mContext) == true) {

						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
						String token = pref.getString("FLICKR_OAUTH_TOKEN", "");
						String secret = pref.getString("FLICKR_OAUTH_SECRET", "");
						if (token != null && secret !=null && !token.equals("") && !secret.equals("")) {
							if (bl == false) {

								editor.putBoolean("SN_flickr", true);
								editor.commit();
								setFakeCheckBoxTrue(holder.fakeCheckBox);


							} else if (bl == true) {
								editor.putBoolean("SN_flickr", false);
								editor.commit();
								setFakeCheckBoxFalse(holder.fakeCheckBox);


							}
						}
						else
						{
							Toast.makeText(mContext, "You're not logged in to Flickr, please login by clicking settings button.", Toast.LENGTH_LONG).show();

						}

					} else {
						launchDialog("pro_or_premium", mContext);
						//launchBetaDialog(mContext);
					}


				}
				else if (article.getTitle().equals("foursquare")) {
						final SharedPreferences settingsmain = mContext.getSharedPreferences("DONetworks", 0);

					final Boolean bl = settingsmain.getBoolean("SN_foursquare", false);

					if (rotaryIsPro(mContext) == true) {
						if (bl == false) {
							SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
							boolean isloggedinFoursquare = pref.getBoolean("foursquareAccessGranted",false);
							if(isloggedinFoursquare)
							{
							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_foursquare", true);
							editor.commit();
							setFakeCheckBoxTrue(holder.fakeCheckBox);

							}
							else
							{

								 Toast.makeText(mContext, "You're not logged in to Foursquare, please login by clicking settings button.", Toast.LENGTH_LONG).show();

							}
						} else if (bl == true) {
							final SharedPreferences settings = mContext.getSharedPreferences("DONetworks", 0);
							SharedPreferences.Editor editor = settings.edit();

							editor.putBoolean("SN_foursquare", false);
							editor.commit();
							setFakeCheckBoxFalse(holder.fakeCheckBox);


						}
					} else {
						launchDialog("pro_or_premium", mContext);
					}


					}

					Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
				 holder.fakeCheckBox.startAnimation(shake);
			}});
        
        
        return convertView;
    }
	public static boolean isLoggedIn() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null;
	}
	private void launchDialog(String pro_or_premium, final Context mContext) {
			String ab = "upgraded";
		if (pro_or_premium.equals("pro_or_premium"))
		{
			ab = "Pro or Premium";
		}
		else {
			ab = "Premium";

		}
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						Intent i=new Intent(mContext,purchases.class);
						mContext.startActivity(i);
						break;


					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("This action requires " + ab + " account!")
				.setNegativeButton("Cancel", dialogClickListener)
				.setPositiveButton("Upgrade Account", dialogClickListener)
				.show();


	}

	private void launchBetaDialog(final Context mContext) {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						break;


					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("This action is not available on Beta!\nIt will be available soon...")
				.setNegativeButton("OK", dialogClickListener)
				.setTitle("RotaryHome Beta")
				.show();


	}

	public void setFakeCheckBoxFalse(ImageButton btn)
   {
		btn.setImageResource(R.drawable.fake_checkbox_bg_false);
	   RotaryHome.isFeedChangedBySettings = true;


   }
   public void setFakeCheckBoxTrue(ImageButton btn)
   {
		btn.setImageResource(R.drawable.fake_checkbox_bg_true);
	   RotaryHome.isFeedChangedBySettings = true;


   }

	public static boolean rotaryIsPro(Context ctx)
	{
		SharedPreferences settingsCH = ctx.getSharedPreferences("RotaryCheck", 0);
		return  settingsCH.getBoolean("isPro", false);
	}

	public static boolean rotaryIsPremium(Context ctx)
	{
		SharedPreferences settingsCH = ctx.getSharedPreferences("RotaryCheck", 0);
		return settingsCH.getBoolean("isPremium", false);
	}
}
