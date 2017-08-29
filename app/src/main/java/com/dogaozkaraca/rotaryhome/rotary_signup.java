package com.dogaozkaraca.rotaryhome;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.User;

import java.util.regex.Pattern;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;


public class rotary_signup extends ActionBarActivity {

    WebView signupV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_signup);



        signupV = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = signupV.getSettings();
        webSettings.setJavaScriptEnabled(true);
        signupV.loadUrl("http://rotary.dogaozkaraca.com/signup.php");
        final WebViewClient mWebClient = new WebViewClient(){
            LoadingDialog dialog = new LoadingDialog(rotary_signup.this);

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
                if (url != null && (url.startsWith("http://rotary.dogaozkaraca.com/signedup") || url.startsWith("rotary.dogaozkaraca.com/signedup") || url.startsWith("http://dogaozkaraca.com")|| url.startsWith("dogaozkaraca.com")  ))
                {
                    rotary_signup.this.finish();

                }
                else
                {
                    view.loadUrl(url);
                }
                return true;
            }
        };
        signupV.setWebViewClient(mWebClient);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rotary_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
