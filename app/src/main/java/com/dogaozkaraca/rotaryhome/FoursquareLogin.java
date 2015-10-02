package com.dogaozkaraca.rotaryhome;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.condesales.EasyFoursquare;
import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.criterias.CheckInCriteria;
import br.com.condesales.criterias.TipsCriteria;
import br.com.condesales.listeners.AccessTokenRequestListener;
import br.com.condesales.listeners.CheckInListener;
import br.com.condesales.listeners.ImageRequestListener;
import br.com.condesales.listeners.TipsRequestListener;
import br.com.condesales.listeners.UserInfoRequestListener;
import br.com.condesales.models.Checkin;
import br.com.condesales.models.Tip;
import br.com.condesales.models.User;
import br.com.condesales.tasks.users.UserImageRequest;

public class FoursquareLogin extends ActionBarActivity implements
        AccessTokenRequestListener, ImageRequestListener {

    private EasyFoursquareAsync async;
    private EasyFoursquare sync;
    private TextView status;
    Button loginlogout;

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
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
		overridePendingTransition(R.anim.anim_left, R.anim.anim_null);



        setContentView(R.layout.do_foursquare_login);

        android.support.v7.app.ActionBar ac = getSupportActionBar();

        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RelativeLayout tipRL = (RelativeLayout) findViewById(R.id.tiprl);
        tipRL.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        TextView tipText = (TextView) findViewById(R.id.textView13);
        tipText.setTextColor(ColorScheme.getTextColor(this));
        TextView tipTextTitle = (TextView) findViewById(R.id.textView1);
        tipTextTitle.setTextColor(ColorScheme.getTextColor(this));
       // SpannableString s = new SpannableString(ac.getTitle());
      //  s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //  ac.setTitle(s);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
        }
        status = (TextView) findViewById(R.id.textView2);
        loginlogout = (Button) findViewById(R.id.button1);
        loginlogout.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        loginlogout.setTextColor(ColorScheme.getTextColor(this));
        //ask for access
        async = new EasyFoursquareAsync(this);



      
        sync = new EasyFoursquare(this);

        if (async.isLoggedin() == true)
        {
            async.requestAccess(this);
            loginlogout.setText("Disconnect from foursquare");
        }
        else
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FoursquareLogin.this);
            pref.edit().putBoolean("foursquareAccessGranted",false).commit();
            status.setText("You're not logged into foursquare!");
            loginlogout.setText("Connect to foursquare");
        }

        
    }


    @Override
    public void onError(String errorMsg) {
        // Do something with the error message
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccessGrant(String accessToken) {
        // with the access token you can perform any request to foursquare.
        // example:
    	//FoursquareLogin.this.getActionBar().setTitle("Connected to Foursquare");
        async.getUserInfo(new UserInfoRequestListener() {

            @Override
            public void onError(String errorMsg) {
                // Some error getting user info
                Toast.makeText(FoursquareLogin.this, errorMsg, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onUserInfoFetched(User user) {
                // OWww. did i already got user!?
             //   if (user.getBitmapPhoto() == null) {
             //       UserImageRequest request = new UserImageRequest(
              //              FoursquareLogin.this, FoursquareLogin.this);
             //       request.execute(user.getPhoto());
             //   } else {
             //       userImage.setImageBitmap(user.getBitmapPhoto());
             //   }

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FoursquareLogin.this);
                pref.edit().putBoolean("foursquareAccessGranted",true).commit();
                status.setText("You're logged in as "+ user.getFirstName() + " " + user.getLastName()+" ." );
                loginlogout.setText("Disconnect from foursquare");
                Log.d("FoursquareDO", "Got it!");
            }
        });

        //for another examples uncomment lines below:
        //requestTipsNearby();
     
    }

    @Override
    public void onImageFetched(Bitmap bmp) {
       // userImage.setImageBitmap(bmp);
    }

    private void requestTipsNearby() {
        Location loc = new Location("");
        loc.setLatitude(40.4363483);
        loc.setLongitude(-3.6815703);

        TipsCriteria criteria = new TipsCriteria();
        criteria.setLocation(loc);
        async.getTipsNearby(new TipsRequestListener() {

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(FoursquareLogin.this, "error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTipsFetched(ArrayList<Tip> tips) {
                Toast.makeText(FoursquareLogin.this, tips.toString(), Toast.LENGTH_LONG).show();
            }
        }, criteria);
    }

    private void checkin() {

        CheckInCriteria criteria = new CheckInCriteria();
        criteria.setBroadcast(CheckInCriteria.BroadCastType.PUBLIC);
        criteria.setVenueId("4c7063da9c6d6dcb9798d27a");

        async.checkIn(new CheckInListener() {
            @Override
            public void onCheckInDone(Checkin checkin) {
                Toast.makeText(FoursquareLogin.this, checkin.getId(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(FoursquareLogin.this, "error", Toast.LENGTH_LONG).show();
            }
        }, criteria);
    }

    public void logout()
    {
    	
    	int i = sync.logout();
    	if (i == 0)
    	{

            //status.setText("You're not logged into foursquare!");
    	
		final SharedPreferences settings =getSharedPreferences("DONetworks" , 0);
		SharedPreferences.Editor editor = settings.edit();
		    
			 	 editor.putBoolean("SN_foursquare", false);
			 	  editor.commit();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FoursquareLogin.this);
            pref.edit().putBoolean("foursquareAccessGranted",false).commit();
            finish();

    	}
    	else if (i == 1)
    	{
        	Toast.makeText(FoursquareLogin.this, "Error, couldn't logout from foursquare !", Toast.LENGTH_LONG).show();
	
    	}
    	
    }

    public void foursquarelogin(View v)
    {
        if (async.isLoggedin() == true)
        {
                logout();
        }
        else
        {
            async.requestAccess(this);
        }



    }

}
