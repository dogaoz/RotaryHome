package com.dogaozkaraca.rotaryhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.tumblr.jumblr.JumblrClient;


import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class FlickrLogin extends ActionBarActivity {
	
	Boolean isAuth = false;
	Button logoutBtn;
	WebView authwebview;
	String getfirstName;
	final static String APIKEY = "77lu0ipw291g8n";
	final static String APISECRET = "JhrTZGdC8Bdy4eXM";
	final static String CALLBACK = "http://linkedinauth";
	final static String CALLBACKCANCELLED ="http://cancelledlinkedinauth";
    TextView tv2;
	 @Override
	 protected void onPause()
	 {
		 
			super.onPause();
			overridePendingTransition(0, R.anim.anim_right);
	 }
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{   super.onCreate(savedInstanceState);
		setContentView(R.layout.do_flickrlogin);
		overridePendingTransition(R.anim.anim_left, R.anim.anim_null);
		 android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
	        bar.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
		//	int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		//	TextView abTitle = (TextView) findViewById(titleId);
		//	abTitle.setTextColor(ColorScheme.getTextColor(this));
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
        }
		//SpannableString s = new SpannableString(getSupportActionBar().getTitle());
		//s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		//getSupportActionBar().setTitle(s);
			 tv2 = (TextView) findViewById(R.id.textView2);


		
		if(getToken() != null && getToken() != "" && getTokenSecret() != null && getTokenSecret() != "" && getOauthVerifier(this) != null && getOauthVerifier(this) != "")
		{
            isAuth = true;
            logoutBtn.setText("Disconnect from flickr");
            try {

                new AsyncTask<String,Void,String>()
                {
                    User user;

                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Flickr f = new Flickr("b6be3992191d2af5ac9235ab0d7853a9", "455057a0d9a22c42");
                            OAuthInterface oauthApi = f.getOAuthInterface();

                            //exchange for an AccessToken from Flickr
                            OAuth oauth = oauthApi.getAccessToken(getToken(), getTokenSecret(), getOauthVerifier(FlickrLogin.this));
                            user = oauth.getUser();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            return null;
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String result)
                    {
                        if (result !=null) {
                            tv2.setText("You're logged in as " + user.getUsername());
                        }
                        else
                        {
                            tv2.setText("There is an error occurred!");

                        }


                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            }
            catch (Exception e)
            {
                e.printStackTrace();
                tv2.setText("There is an error occurred!");
            }
		}
		else
		{
			tv2.setText("You're not logged into Flickr!");
		}

						
					

		
	}
	public void flickrbutton(View v)
    {
        if (isAuth == true)
        {

            logout();

        }
        else
        {
            // START AUTHENTICATION FOR FLICKR
            initialOauth();


        }


    }

	public void logout()
	{
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        pref.edit().putString("FLICKR_OAUTH_TOKEN", "").commit();
        pref.edit().putString("FLICKR_OAUTH_SECRET", "").commit();
        pref.edit().putString("FLICKR_OAUTH_VERIFIER", "").commit();

        finish();


    }
	@Override
	public void onResume() {
		super.onResume();




	}

    public void saveFlickrAuthToken(OAuth oauth)
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        pref.edit().putString("FLICKR_OAUTH_TOKEN", oauth.getToken().getOauthToken()).commit();
        pref.edit().putString("FLICKR_OAUTH_SECRET", oauth.getToken().getOauthTokenSecret()).commit();

    }

	public void initialOauth() {
        new AsyncTask<String, Void, String>() {

            URL oauthUrl;
            @Override
            protected String doInBackground(String... params) {
                try {
                    String callBackUrl = "oauthflow-flickr";
                    Flickr f = new Flickr("b6be3992191d2af5ac9235ab0d7853a9", "455057a0d9a22c42");
                    //get a request token from Flickr
                    OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(callBackUrl);
                    //you should save the request token and token secret to a preference store for later use.
                    saveTokenAndSecret(oauthToken);

                    //build the Authentication URL with the required permission
                    oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
                            Permission.WRITE, oauthToken);


                    //redirect user to the genreated URL.
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
        protected void onPostExecute(String abc)
            {
                createWebViewAndRedirectTo(oauthUrl);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

    public void saveTokenAndSecret(OAuthToken oAuthToken)
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        pref.edit().putString("FLICKR_OAUTH_TOKEN", oAuthToken.getOauthToken()).commit();
        pref.edit().putString("FLICKR_OAUTH_SECRET", oAuthToken.getOauthTokenSecret()).commit();

    }

    public String getToken()
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

       return pref.getString("FLICKR_OAUTH_TOKEN",null);

    }
    public String getTokenSecret()
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        return pref.getString("FLICKR_OAUTH_SECRET",null);

    }

    public static String getOauthVerifier(Context mContext)
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        return pref.getString("FLICKR_OAUTH_VERIFIER",null);

    }

    public void setAuthVerifier(String verifier)
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        pref.edit().putString("FLICKR_OAUTH_VERIFIER", verifier).commit();

    }
    public void createWebViewAndRedirectTo(URL oauthUrl)
    {
        logoutBtn.setVisibility(View.GONE);
        WebViewClient mWebClient = new WebViewClient(){
            LoadingDialog dialog = new LoadingDialog(FlickrLogin.this);

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
                String OAUTH_CALLBACK_URL = "https://m.flickr.com/oauthflow-flickr";



                if (url != null && url.startsWith(OAUTH_CALLBACK_URL))
                {
                    new AsyncTask<String, Void, String>() {

                        @Override
                        protected String doInBackground(String... params) {
                            try {


                                Uri uri = Uri.parse(url);
                                String query = uri.getQuery();
                                //the query format should be oauthToken=XXX&oauthVerifier=XXX
                                String[] data = query.split("&");
                                if (data != null && data.length == 2) {
                                    String oauthToken = data[0].substring(data[0].indexOf("=") + 1);
                                    String oauthVerifier = data[1].substring(data[1].indexOf("=") + 1);

                                    final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FlickrLogin.this);

                                    pref.edit().putString("FLICKR_OAUTH_TOKEN",oauthToken).commit();
                                    setAuthVerifier(oauthVerifier);


                                }
                                isAuth = true;

                            } catch (Exception e) {

                                e.printStackTrace();
                                return null;
                            }
                            return "";
                        }

                        @Override
                        protected void onPostExecute(String result) {


                            if (isAuth == true && result != null)
                            {

                                authwebview.setVisibility(View.GONE);
                             new AsyncTask<String, Void, String>() {

                                    @Override
                                    protected String doInBackground(String... params) {
                                        String name = "";
                                        try
                                        {
                                            //retrieve the stored request token secret in earlier step
                                            String requestTokenSecret = getTokenSecret();
                                            if (requestTokenSecret != null) {
                                                //apikey  , apisecret
                                                Flickr f = new Flickr("b6be3992191d2af5ac9235ab0d7853a9", "455057a0d9a22c42");
                                                OAuthInterface oauthApi = f.getOAuthInterface();


                                                //exchange for an AccessToken from Flickr
                                                OAuth oauth = oauthApi.getAccessToken(getToken(), requestTokenSecret, getOauthVerifier(FlickrLogin.this));

                                                User user = oauth.getUser();

                                                name = "You're logged in as " + user.getUsername();

                                                 OAuthToken token = oauth.getToken();
                                                  saveFlickrAuthToken(oauth);
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            name = "You've connected to Flickr!";
                                        }
                                        return name;
                                    }

                                    @Override
                                    protected void onPostExecute(String result) {
                                        tv2.setText(result);
                                        logoutBtn.setVisibility(View.VISIBLE);
                                        logoutBtn.setText("Disconnect from flickr");
                                    }

                                    @Override
                                    protected void onPreExecute() {}

                                    @Override
                                    protected void onProgressUpdate(Void... values) {}
                                }.execute();




                            }

                        }

                        @Override
                        protected void onPreExecute() {tv2.setText("Loading...");}

                        @Override
                        protected void onProgressUpdate(Void... values) {}
                    }.execute();

                }
                else
                {
                    view.loadUrl(url);
                    Log.w("Rotary",url);
                }
                return true;
            }
        };
        authwebview.setWebViewClient(mWebClient);
        WebSettings webSettings = authwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        authwebview.setVisibility(View.VISIBLE);
        authwebview.loadUrl(oauthUrl.toString());
    }

}
