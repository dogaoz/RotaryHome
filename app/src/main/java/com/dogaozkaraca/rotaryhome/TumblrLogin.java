package com.dogaozkaraca.rotaryhome;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.User;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class TumblrLogin extends ActionBarActivity
{
	public static final String CONSUMER_KEY = "zA05B3R48EBOjJx6ykilYL3KbRE5ZZickFH5V9uOXDAIOHSbQ5";
	public static final String CONSUMER_SECRET = "0M1SCmU3NifzQ5v2Z2KYEZF00rY1k6Kgmv82o808X0TEuuqpBr";
	
	public static final String REQUEST_URL = "http://www.tumblr.com/oauth/request_token";
	public static final String ACCESS_URL = "http://www.tumblr.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://www.tumblr.com/oauth/authorize";

	public static final String	OAUTH_CALLBACK_SCHEME	= "oauthflow-tumblr";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

	private Button loginorout;
	
	private static Intent newIntent = null;
	
	private static SharedPreferences pref = null;
	
	private static String debug, token, secret, authURL, uripath;
	
	private static CommonsHttpOAuthConsumer consumer ;
    private static CommonsHttpOAuthProvider provider ;
    TextView tv1;
    private static boolean auth = false, browser = false, browser2 = false;//These booleans determine which code is run every time onResume is executed.
    private static boolean loggedin = false;
	WebView authwebview;
	
	 @Override
	 protected void onPause()
	 {
		 
			super.onPause();
			overridePendingTransition(0, R.anim.anim_right);
	 }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{   
		super.onCreate(savedInstanceState);
        setContentView(R.layout.do_tumblrlogin);

        ActionBar ac = getSupportActionBar();

        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
		}
		overridePendingTransition(R.anim.anim_left, R.anim.anim_null);
      tv1 = (TextView) findViewById(R.id.textView19);
        loginorout = (Button) findViewById(R.id.loginorout);

        RelativeLayout tipRL = (RelativeLayout) findViewById(R.id.tiprl);
        tipRL.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        TextView tipText = (TextView) findViewById(R.id.textView13);
        tipText.setTextColor(ColorScheme.getTextColor(this));
        TextView tipTextTitle = (TextView) findViewById(R.id.textView1);
        tipTextTitle.setTextColor(ColorScheme.getTextColor(this));


        loginorout.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        loginorout.setTextColor(ColorScheme.getTextColor(this));
       // SpannableString s = new SpannableString(ac.getTitle());
		//s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		//s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // ac.setTitle(s);

        authwebview = (WebView) findViewById(R.id.webView1);
        WebViewClient mWebClient = new WebViewClient(){
            LoadingDialog dialog = new LoadingDialog(TumblrLogin.this);

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.hide();
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                try {

                    dialog.show();
                }
                catch(Exception e){}
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
           if (url != null && url.startsWith(OAUTH_CALLBACK_URL))
           {
        			new AsyncTask<String, Void, String>() {

				        @Override
				        protected String doInBackground(String... params) {
							try {

								
								String segments[] = url.split(Pattern.quote("?"));
								Log.w("TumblrDO","getAuthSegments[1] :"+ segments[1]);
								String finalvalue[] = segments[1].split(Pattern.quote("="));
								Log.w("TumblrDO","finalvalue[1] :"+ finalvalue[1]);

								String finalvalue2[] = url.split(Pattern.quote("oauth_verifier="));
								Log.w("TumblrDO","finalvalue2[1] :"+ finalvalue2[1]);

								String finalvalue3[] = finalvalue2[1].split(Pattern.quote("#"));
								Log.w("TumblrDO","finalvalue3[1] :"+ finalvalue3[0]);
								
								provider.retrieveAccessToken(consumer,finalvalue3[0] );
						
								token = consumer.getToken();
								Log.w("TumblrDO","Token : "+ token);

								secret = consumer.getTokenSecret();
								Log.w("TumblrDO","Secret : "+ secret);

								final Editor editor = pref.edit();
								editor.putString("TUMBLR_OAUTH_TOKEN", token);
								editor.putString("TUMBLR_OAUTH_TOKEN_SECRET", secret);
								editor.commit();
								
								auth = true;
								loggedin = true;
								
							} catch (OAuthMessageSignerException e) 
							{
								e.printStackTrace();
							} catch (OAuthNotAuthorizedException e) 
							{
								e.printStackTrace();
							} catch (OAuthExpectationFailedException e) 
							{
								e.printStackTrace();
							} catch (OAuthCommunicationException e) 
							{
								e.printStackTrace();
							}
				            return authURL;
				        }

				        @Override
				        protected void onPostExecute(String result) {
				         
				     
				        	if (auth == true && loggedin == true)
				        	{

				        		 authwebview.setVisibility(View.GONE);





				                
									new AsyncTask<String, Void, String>() {

								        @Override
								        protected String doInBackground(String... params) {
								        	String name = "";
								try
								{
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(TumblrLogin.this);
									
									String token = pref.getString("TUMBLR_OAUTH_TOKEN", "");
									String secret = pref.getString("TUMBLR_OAUTH_TOKEN_SECRET", "");
							   
								
									JumblrClient client = new JumblrClient(
                                            CONSUMER_KEY,CONSUMER_SECRET
											);
											client.setToken(token,secret);
											
											
											
											// Make the request
											User user = client.user();

                                    name =  "You're logged in as "+ user.getName();
								}
								catch(Exception e)
								{
									name = "You've connected to tumblr!";
								}
								            return name;
								        }

								        @Override
								        protected void onPostExecute(String result) {
								        	tv1.setText(result);
                                            loginorout.setVisibility(View.VISIBLE);
                                            loginorout.setText("Disconnect from tumblr");
								        }

								        @Override
								        protected void onPreExecute() {}

								        @Override
								        protected void onProgressUpdate(Void... values) {}
								    }.execute();
				                

				        	}

				        }

				        @Override
				        protected void onPreExecute() {loginorout.setText("Loading...");}

				        @Override
				        protected void onProgressUpdate(Void... values) {}
				    }.execute();
				
           }
           else
           {
               view.loadUrl(url);
           }
                    return true;
                }
           };
           authwebview.setWebViewClient(mWebClient);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		token = pref.getString("TUMBLR_OAUTH_TOKEN", "");
		secret = pref.getString("TUMBLR_OAUTH_TOKEN_SECRET", "");
			
		if(token != null && token != "" && secret != null && secret != "")
		{
			auth = true;
			loggedin = true;
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... params) {
                    String name = "";
                    try
                    {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(TumblrLogin.this);

                        String token = pref.getString("TUMBLR_OAUTH_TOKEN", "");
                        String secret = pref.getString("TUMBLR_OAUTH_TOKEN_SECRET", "");


                        JumblrClient client = new JumblrClient(
                                CONSUMER_KEY,
                                CONSUMER_SECRET
                        );
                        client.setToken(token,secret);



                        // Make the request
                        User user = client.user();

                        name =  "You're logged in as "+ user.getName();
                    }
                    catch(Exception e)
                    {
                        name = "You're connected to tumblr!";
                    }
                    return name;
                }

                @Override
                protected void onPostExecute(String result) {
                    tv1.setText(result);


                }

                @Override
                protected void onPreExecute() { loginorout.setVisibility(View.VISIBLE);
                    loginorout.setText("Disconnect from tumblr");}

            }.execute();
		}
        else
        {

            tv1.setText("You're not logged into tumblr!");


        }


		
		
 
    			
	}
	
	@Override
	public void onResume() {
		super.onResume();
	       String TumblrGetRequestUrl = "https://api.tumblr.com/v2/user/dashboard";

	       OAuthRequest request = new OAuthRequest(Verb.GET, TumblrGetRequestUrl);
	       Log.w("TumblrDO","Getting Content");

	       Log.w("TumblrDO",request.getBodyContents()+ "");
        authwebview.getSettings().setJavaScriptEnabled(true);
        authwebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if(auth == false)
        {
            tv1.setText("You're not logged into tumblr!");
        }
        else if (auth == true)
        {


        }


	}
	
	//Grabs the authorization URL from OAUTH and sets it to the String authURL member
	private void setAuthURL()
	{
		 authwebview.setVisibility(View.VISIBLE);
		 new AsyncTask<String, Void, String>() {

		        @Override
		        protected String doInBackground(String... params) {

		        	try 
		    		{
		        		Log.w("TumblrDO","Asynctaskstarted");
		        		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		        		 String ww2 = consumer.getConsumerKey();
		        		provider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
		        		
		        		 provider.setOAuth10a(true);
		        		 String ww =provider.getAuthorizationWebsiteUrl();
			        		Log.w("TumblrDO","authweburl : "+ww);

			        		Log.w("TumblrDO","consumerkey : "+ww2);

//		        	if((token == null || token == "") && (secret == null || secret == "") && auth == false && browser == false)
					
			        		
			        		authURL = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
			        		Log.w("TumblrDO","authURL : "+authURL);

		    		} catch (OAuthMessageSignerException e) 
		    		{
		        		Log.e("TumblrDO","error : "+e);

		    			e.printStackTrace();
		    			authURL= "error " + e;
		    		} catch (OAuthNotAuthorizedException e) 
		    		{
		        		Log.e("TumblrDO","error : "+e);

		    			e.printStackTrace();
		    			authURL= "error " + e;

		    		} catch (OAuthExpectationFailedException e) 
		    		{
		        		Log.e("TumblrDO","error : "+e);

		    			e.printStackTrace();
		    			authURL= "error " + e;

		    		} catch (OAuthCommunicationException e) 
		    		{
		        		Log.e("TumblrDO","error : "+e);

		    			e.printStackTrace();
		    			authURL= "error " + e;

		    		}
	        		Log.w("TumblrDO","Asynctaskreturned");

		            return authURL;
		        }

		        @Override
		        protected void onPostExecute(String result) {
		        	authwebview.setVisibility(View.VISIBLE);
		    		authwebview.loadUrl( authURL);
		        }

		        @Override
		        protected void onPreExecute() {}

		        @Override
		        protected void onProgressUpdate(Void... values) {}
		    }.execute();
		
		
	}
	

	
	//Removes the shared preferences values and sets the current token and secret to null essentially logging the user out of Tumblr
	private void logout() 
	{
		final Editor edit = pref.edit();
		edit.remove("TUMBLR_OAUTH_TOKEN");
		edit.remove("TUMBLR_OAUTH_TOKEN_SECRET");
        edit.remove("TUMBLR_BLOG_NAME");
		edit.commit();
		
		token = null;
		secret = null;
		
		consumer = null;
		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	    provider = null;
	    provider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
	    
	    debug = "Access Token: " + token + "\n\nAccess Token Secret: " + secret + "\n\n";
        auth=false;
    	loggedin = false;
        loginorout.setText("Connect to tumblr");
	}
	

	




    public void tumblrAction(View v)
    {
        if(auth == false)
        {


                loginorout.setVisibility(View.GONE);
                setAuthURL();




        }
        else
        {



            logout();
            finish();

        }


    }
}
