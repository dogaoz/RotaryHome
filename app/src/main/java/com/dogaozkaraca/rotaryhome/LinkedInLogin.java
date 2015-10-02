package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LinkedInLogin extends ActionBarActivity {
	
	Boolean isAuth = false;
	Button logoutBtn;
	WebView authwebview;
	String getfirstName;
	final static String APIKEY = "77lu0ipw291g8n";
	final static String APISECRET = "JhrTZGdC8Bdy4eXM";
	final static String CALLBACK = "http://linkedinauth";
    TextView tv2;
    String getProfilePicURL;
	 @Override
	 protected void onPause()
	 {
		 
			super.onPause();
			overridePendingTransition(0, R.anim.anim_right);
	 }
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{   super.onCreate(savedInstanceState);
		setContentView(R.layout.do_linkedinlogin);
		overridePendingTransition(R.anim.anim_left, R.anim.anim_null);
		 android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
	        bar.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
		//	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		//	TextView abTitle = (TextView) findViewById(titleId);
		//	abTitle.setTextColor(ColorScheme.getTextColor(this));
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
		}
        RelativeLayout tipRL = (RelativeLayout) findViewById(R.id.tiprl);
        tipRL.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        TextView tipText = (TextView) findViewById(R.id.textView13);
        tipText.setTextColor(ColorScheme.getTextColor(this));
        TextView tipTextTitle = (TextView) findViewById(R.id.textView1);
        tipTextTitle.setTextColor(ColorScheme.getTextColor(this));
        authwebview = (WebView) findViewById(R.id.webView1);
        logoutBtn = (Button) findViewById(R.id.button1);

        logoutBtn.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        logoutBtn.setTextColor(ColorScheme.getTextColor(this));

		//SpannableString s = new SpannableString(getSupportActionBar().getTitle());
		//s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		//getSupportActionBar().setTitle(s);
			 tv2 = (TextView) findViewById(R.id.textView2);


		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		String token = pref.getString("LINKEDIN_OAUTH_TOKEN", "");
		String secret = pref.getString("LINKEDIN_OAUTH_TOKEN_SECRET", "");
		
		if(token != null && token != "" && secret != null && secret != "")
		{


	      
			authwebview.setVisibility(View.GONE);
			Log.w("LinkedInDO","LoggedInToken : "+ token);

			Log.w("LinkedInDO","LoggedInSecret : "+secret);
			isAuth = true;
			logoutBtn.setText("Disconnect from LinkedIn");
   		 new AsyncTask<String, Void, String>() {

		        @Override
		        protected String doInBackground(String... params) {
		        	String token = pref.getString("LINKEDIN_OAUTH_TOKEN", "");
		    		String secret = pref.getString("LINKEDIN_OAUTH_TOKEN_SECRET", "");

		            return "";
		        }

		        @Override
		        protected void onPostExecute(String result) {
		        	if (getfirstName != null)
		    		{
		    		  tv2.setText("You're logged in as " + getfirstName);
		    		}
		        	//if (getProfilePicURL != null)
		    		//{
				   //   Picasso.with(LinkedInLogin.this).load(getProfilePicURL).into(iv2);
		    		//}
		        }

		        @Override
		        protected void onPreExecute() {}

		        @Override
		        protected void onProgressUpdate(Void... values) {}
		    }.execute();
			
		}
		else
		{
			tv2.setText("You're not logged into LinkedIn!");

		}
        WebViewClient mWebClient = new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
    	        
    	       
           if (url != null && url.startsWith(CALLBACK))
           {


        			new AsyncTask<String, Void, String>() {

				        @Override
				        protected String doInBackground(String... params) {
							try {

								//?oauth_token=75--44f7ae68-1d40-4b28-a13d-ff4bb8e9e937&oauth_verifier=19082
								Uri uri =  Uri.parse(url);
								final String problem = uri.getQueryParameter("oauth_problem");
								if (problem == null) {
									final Editor editor = pref.edit();



									editor.putString("LINKEDIN_OAUTH_TOKEN","");
									editor.putString("LINKEDIN_OAUTH_TOKEN_SECRET","");
									editor.putString("LINKEDIN_OAUTH_TOKEN_VERIFIER","");

									editor.commit();
									
								}


						
								
						
					

								
								
								isAuth = true;
								
							} catch (Exception e)
							{
								
					   	      //  LinkedInLogin.this.getActionBar().setTitle("Error");
	Log.e("LinkedInDO","Error :" + e);
							}
				            return "";
				        }

				        @Override
				        protected void onPostExecute(String result) {
				         
				     
				        	if (isAuth == true)
				        	{
				        		if (getfirstName != null)
				        		{
				        		  tv2.setText("You're logged in as "+getfirstName);
				        		}
								else {
									tv2.setText("There is an error occurred!");

								}
								logoutBtn.setText("Disconnect from LinkedIn");

								// if (getProfilePicURL != null)
						    	//	{

								 //     Picasso.with(LinkedInLogin.this).load(getProfilePicURL).into(iv2);
						    	//	}

				        		 authwebview.setVisibility(View.GONE);
                                logoutBtn.setVisibility(View.VISIBLE);

				                
				                
				               
				        	}
				        }

				        @Override
				        protected void onPreExecute() {}

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
		
	}
	public void linkedinbutton(View v)
    {
        if (isAuth == true)
        {

            logout();

        }
        else {

			//SocialAuthAdapter adapter = new SocialAuthAdapter(new ResponseListener());
           // adapter.addCallBack(SocialAuthAdapter.Provider.LINKEDIN,"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

			//adapter.authorize(LinkedInLogin.this, SocialAuthAdapter.Provider.LINKEDIN);
		}
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        //LinkedInSessionManager.onActivityResult(this, requestCode, resultCode, data);
    }

	public void logout()
	{		
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		final Editor editor = pref.edit();

		editor.putString("LINKEDIN_OAUTH_TOKEN", "");
		editor.putString("LINKEDIN_OAUTH_TOKEN_SECRET", "");
		editor.commit();
		
		final SharedPreferences settings =getSharedPreferences("DONetworks" , 0);
		Editor editor2 = settings.edit();
		    	  editor2.putBoolean("SN_linkedin",false);
			 	  editor2.commit();	
		Toast.makeText(LinkedInLogin.this, "Logged out from LinkedIn !", Toast.LENGTH_LONG).show();
		finish();
	}
}
