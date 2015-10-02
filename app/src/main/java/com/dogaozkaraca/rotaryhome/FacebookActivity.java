package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FacebookActivity extends ActionBarActivity {

	private TextView loginStatus;
    Button loginButtonOrg;
    Button clearCredentials;

	 @Override
	 protected void onPause()
	 {
		 
			super.onPause();
			overridePendingTransition(0, R.anim.anim_right);
	 }

	CallbackManager callbackManager;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this.getApplicationContext());

		setContentView(R.layout.do_facebook_activity);
		overridePendingTransition(R.anim.anim_left, R.anim.anim_null);

		ActionBar ac = getSupportActionBar();

		ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout tipRL = (RelativeLayout) findViewById(R.id.tiprl);
        tipRL.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        TextView tipText = (TextView) findViewById(R.id.textView13);
        tipText.setTextColor(ColorScheme.getTextColor(this));
        TextView tipTextTitle = (TextView) findViewById(R.id.textView1);
        tipTextTitle.setTextColor(ColorScheme.getTextColor(this));
		SpannableString s = new SpannableString(ac.getTitle());

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
		}
        loginStatus = (TextView)findViewById(R.id.login_status);
        loginButtonOrg = (Button) findViewById(R.id.btn_post);

        clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);


		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						// App code
						clearCredentials.setVisibility(View.GONE);
						loginButtonOrg.setVisibility(View.VISIBLE);
					}

					@Override
					public void onCancel() {
						// App code
					}

					@Override
					public void onError(FacebookException exception) {
						// App code
					}
				});

		LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setVisibility(View.GONE);
		loginButton.setReadPermissions("user_posts");
		// Other app specific specialization
		//
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code

			}

			@Override
			public void onCancel() {
				// App code
			}

			@Override
			public void onError(FacebookException exception) {
				// App code
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();


        loginButtonOrg.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        loginButtonOrg.setTextColor(ColorScheme.getTextColor(this));
        clearCredentials.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        clearCredentials.setTextColor(ColorScheme.getTextColor(this));
		//this.facebookConnector.getName(loginStatus,this);

        loginButtonOrg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                postMessage();
            }
        });

        clearCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggedIn()) {
                    Toast.makeText(FacebookActivity.this, "You cannot logout right now,you're not logged in!", Toast.LENGTH_LONG).show();


                } else if (isLoggedIn()) {

                    clearCredentials(FacebookActivity.this);
                    updateLoginStatus();
                }
            }
        });
        if (!isLoggedIn())
        {

            clearCredentials.setVisibility(View.GONE);
            loginButtonOrg.setVisibility(View.VISIBLE);
        }
        else
        {
            loginButtonOrg.setVisibility(View.GONE);
            clearCredentials.setVisibility(View.VISIBLE);
        }
		updateLoginStatus();
	}
	
	public void updateLoginStatus() {
		if (isLoggedIn())
		{

            loginStatus.setText("You have successfully connected!\nYou can enable facebook for your news feed now.");

		}
		else
		{
            loginStatus.setText("You're not logged into facebook!");
		}
		

	}

	public boolean isLoggedIn() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null;
	}
	public void postMessage() {
		
		if (isLoggedIn()) {
			//postMessageInThread();
    		Toast.makeText(FacebookActivity.this, "Already Logged In!", Toast.LENGTH_LONG).show();

		} else {
			callbackManager = CallbackManager.Factory.create();

			// Set permissions
			LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile","user_posts"));

			LoginManager.getInstance().registerCallback(callbackManager,
					new FacebookCallback<LoginResult>() {
						@Override
						public void onSuccess(LoginResult loginResult) {
							loginButtonOrg.setVisibility(View.GONE);
							clearCredentials.setVisibility(View.VISIBLE);
							loginStatus.setText("You have successfully connected!\nYou can enable facebook for your news feed now.");

						}

						@Override
						public void onCancel() {

						}

						@Override
						public void onError(FacebookException error) {

						}
					});

		}
	}


	private void clearCredentials(final Context ct) {
	
//		  Intent intent = new Intent(FacebookActivity.this,Logoutfb.class);
//        	 startActivity(intent);
		  final SharedPreferences settings =getSharedPreferences("DONetworks" , 0);
			SharedPreferences.Editor editor3 = settings.edit();
			    
				 	 editor3.putBoolean("SN_facebook",false);
				 	  editor3.commit();
		new AsyncTask<Boolean,Boolean,Boolean>()
		{

			@Override
			protected Boolean doInBackground(Boolean... params) {
				try {
					LoginManager.getInstance().logOut();
				} catch (Exception er)
				{
					er.printStackTrace();

					Log.e("Facebook", "Error! : " + er);
					return false;
				}

				return true;
			}

			@Override
			protected void onPostExecute(Boolean v)
			{
				if(v == true) {
					FacebookActivity.this.finish();
				}
				else
				{
					Toast.makeText(FacebookActivity.this, "There is an error occurred!", Toast.LENGTH_LONG).show();

				}
			}
		}.execute();


	}
}
