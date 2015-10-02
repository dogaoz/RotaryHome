package com.dogaozkaraca.rotaryhome;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class Weather_Settings extends ActionBarActivity {

    TextView tv;
    Button changeLocationBTN;
	String PREFS_WEATHERPACK = "DoWEATHER_PACK";
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_settings);

		//SpannableString s = new SpannableString(getSupportActionBar().getTitle());
		//s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //getSupportActionBar().setTitle(s);

        android.support.v7.app.ActionBar ac = getSupportActionBar();

        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv = (TextView) findViewById(R.id.textView16);
        TextView tv2 = (TextView) findViewById(R.id.textView199);
        TextView tv3 = (TextView) findViewById(R.id.textView12);
        TextView tv4 = (TextView) findViewById(R.id.textView121);

        tv2.setTextColor(ColorScheme.getActionBarColor(this));
        tv3.setTextColor(ColorScheme.getActionBarColor(this));
        tv4.setTextColor(ColorScheme.getActionBarColor(this));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
        }
        final SharedPreferences settings2 = getSharedPreferences(PREFS_WEATHERPACK, 0);

		final String weather_pack = settings2.getString(PREFS_WEATHERPACK, "default_wp");

        SharedPreferences settings3 = getSharedPreferences("Location", 0);
        final String locationcityname2 =  settings3.getString("Location_shown", "none");
        if (locationcityname2.equals("none"))
        {
            tv.setText("You didn't set any location yet.\nYou're using Izmir,Turkey as default weather location");

        }
        else {
            tv.setText("Weather location is : " + locationcityname2);
        }

        changeLocationBTN = (Button) findViewById(R.id.button1);



        changeLocationBTN.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        changeLocationBTN.setTextColor(ColorScheme.getTextColor(this));
		String PREFS_WEATHER = "DoWEATHER";
		final SharedPreferences settings5 = getSharedPreferences(PREFS_WEATHER, 0);

		final String locality = settings5.getString(PREFS_WEATHER, "metric");
		final	RadioButton radiobutton0 = (RadioButton) findViewById(R.id.radio0);
		final	RadioButton radiobutton1 = (RadioButton) findViewById(R.id.radio1);
		 //Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ri.ttf");
	     //radiobutton0.setTypeface(font);
	     //radiobutton1.setTypeface(font);
		if (locality == "metric")
			{
				radiobutton0.setChecked(true);
				
			}
		else if (locality == "imperial")
			{
			radiobutton1.setChecked(true);
			}
		else
		{
			Log.e("DOGA","WEATHER SETTINGS PROBLEM DETECTED");
		}






        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        dynamicSpinner.setBackgroundColor(ColorScheme.getActionBarColor(this));
        //dynamicSpinner.setPopupBackgroundResource(ColorScheme.getDarkerColor(ColorScheme.getBackgroundColor(this)));
        String[] items = new String[] { "Every 15 Minutes", "Every 30 Minutes", "Every Hour","Four times a day","Twice a day","Every Day","Manually" };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, items);


        dynamicSpinner.setAdapter(adapter);


        final SharedPreferences settings = getSharedPreferences("weather_update", 0);


        if(settings.getString("time","manual").equals("every15"))
        {
            dynamicSpinner.setSelection(0);


        }
        else if (settings.getString("time","manual").equals("every30"))
        {
            dynamicSpinner.setSelection(1);


        }
        else if (settings.getString("time","manual").equals("everyHour"))
        {
            dynamicSpinner.setSelection(2);


        }
        else if (settings.getString("time","manual").equals("fourTimes"))
        {
            dynamicSpinner.setSelection(3);


        }
        else if (settings.getString("time","manual").equals("twoTimes"))
        {
            dynamicSpinner.setSelection(4);


        }
        else if (settings.getString("time","manual").equals("everyDay"))
        {
            dynamicSpinner.setSelection(5);

        }
        else if (settings.getString("time","manual").equals("manual"))
        {
            dynamicSpinner.setSelection(6);

        }


        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                SharedPreferences.Editor editor94 = settings.edit();

                if (position == 0)
               {
                   editor94.putString("time", "every15");


               }
               else if (position == 1)
               {
                   editor94.putString("time", "every30");

               }
               else if (position == 2)
               {
                   editor94.putString("time", "everyHour");

               }
               else if (position == 3)
               {
                   editor94.putString("time", "fourTimes");

               }
               else if (position == 4)
               {
                   editor94.putString("time", "twoTimes");

               }
               else if (position == 5)
               {
                   editor94.putString("time", "everyDay");

               }
               else if (position == 6)
               {
                   editor94.putString("time", "manual");

               }
                // Commit the edits!
                editor94.commit();
                RotaryHome.isWeatherChanged = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
		
		
	}
	
	public void setWeatherLocation(View view)
	{
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
        input.setGravity(Gravity.CENTER);
		new AlertDialog.Builder(Weather_Settings.this)
	    .setTitle(getResources().getString(R.string.WeatherLocation))
	    .setMessage(getResources().getString(R.string.EnterYourLocation))
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        @Override
			public void onClick(DialogInterface dialog, int whichButton) {
	            Editable value = input.getText(); 
	            String myString = value.toString();
	            myString = myString.replaceAll(" ", "%20");
                isWeatherLocationExist(myString);


           //     RotaryHome.rotaryStatus=1;
	        	   Intent i;
	        	   PackageManager manager = getPackageManager();

	            
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        @Override
			public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
	}

	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
		String PREFS_WEATHER = "DoWEATHER";
		final SharedPreferences settings = getSharedPreferences(PREFS_WEATHER, 0);
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio0:
	            if (checked)
	                // Pirates are the best
	            	
	            	
	            	 Log.d("DOGASettings","metric");
		        	 	      SharedPreferences.Editor editor94 = settings.edit();
		        	 	 	  editor94.putString("DoWEATHER", "metric");

		        	   	      // Commit the edits!
		        	   	      editor94.commit();
                RotaryHome.isWeatherChanged = true;

                Log.d("DOGASettings","metriccomp");
		        	
		        
		        
	            break;
	        case R.id.radio1:
	            if (checked)
	                // Ninjas rule
	            	 Log.d("DOGASettings","imperial");
      		  SharedPreferences.Editor editor3 = settings.edit();
  	 	 	  editor3.putString("DoWEATHER", "imperial");

  	   	      // Commit the edits!
  	   	      editor3.commit();
                RotaryHome.isWeatherChanged = true;
  	   	      Log.d("DOGASettings","imperialcomp");
	            break;
	    }
	}

	public void isWeatherLocationExist(final String queryOfCityName)
    {
        final String degreeSymbol = "\u00b0";
        final String[] C_or_F = {"C"};
        final String[] Location = {"Error"};
        final int[] currentTemp = {1};
        final int[] tempStatus = new int[1];

        new AsyncTask<String,String,String>()
        {

            LoadingDialog dialog = new LoadingDialog(Weather_Settings.this);

            @Override
            protected void onPreExecute()
            {

                this.dialog.setMessage("Please Wait...");
                this.dialog.setCancelable(false);
                try {

                    this.dialog.show();
                }
                catch(Exception e){}

            }
            @Override
            protected String doInBackground(String... params) {

                try {



                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    try {



                        SharedPreferences settings87 = getSharedPreferences("DoWEATHER", 0);
                        final String locality =  settings87.getString("DoWEATHER", "metric");
                        if (locality.equals("metric"))
                        {
                            C_or_F[0] = "C";
                        }
                        else
                        {
                            C_or_F[0] = "F";
                        }


                        request.setURI(new URI("http://api.openweathermap.org/data/2.5/weather?q="+queryOfCityName+"&mode=json&units="+locality+""));
                    } catch (URISyntaxException e) {

                        return "error";
                    }
                    HttpResponse response = client.execute(request);

                    HttpEntity entity = response.getEntity();

                    if(entity != null){
                        try {
                            JSONObject respObject = new JSONObject(EntityUtils.toString(entity));
                            //String active = respObject.getString("active");
                            String name = respObject.getString("name");
                            Log.d("DOGA","CUR LOCATION : " + name);
                            String temp =null;
                            Location[0] = name;
                            try
                            {

                                temp = respObject.getString("main");
                                JSONObject respObject2 = new JSONObject(temp);
                                int temp2 = respObject2.getInt("temp");
                                Log.d("DO","weather : " + name + ", " + temp2);
                                currentTemp[0] = temp2;
                            }
                            catch(Exception tempErr)
                            {
                                Log.e("DOGA_TEMP","ERROR");
                                currentTemp[0] = 00;
                                return "error";
                            }

                            try
                            {
                                Log.d("DO","WEATHER STATUS STARTED");


                                temp = respObject.getString("weather");

                                JSONArray respObject2 = new JSONArray();

                                // contacts JSONArray
                                JSONArray weather = null;
                                weather = respObject.getJSONArray("weather");
                                JSONObject c = weather.getJSONObject(0);

                                tempStatus[0] = c.getInt("id");
                                Log.d("DO","WEATHER STATUS TASK ID GOT : "+ tempStatus[0]);




                            }
                            catch(Exception eertew)
                            {
                                Log.e("DogaOzkaraca","Weather status failed");
                                Log.e("DO","WEATHER STATUS TASK ID FAILED : "+ eertew);
                                return "error";
                            }
                            Location[0] = name;

                            //  String tab1_text = respObject.getString("tab1_text");
                        }
                        catch(Exception eer3)
                        {
                            Location[0] = "Weather Error";
                            currentTemp[0] = 00;
                            return "nolocationfound";
                        }
                        //....
                    }
                    else{
                        //Do something here...
                    }


                } catch (IOException e) {
                    Log.e("DO","ASYNCTASK WEATHER TASK FAILED");
                    return "error";
                }


                return null;
            }
            @Override
            protected void onPostExecute(String result) {

                String text;
                if (result != null && result.equals("error"))
                {
                    this.dialog.hide();
                    text = "Connection Failed!\nPlease try again after checking your connection.";
                    new AlertDialog.Builder(Weather_Settings.this)
                            .setTitle("Error")
                            .setMessage(text)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // whatever...

                                }
                            }).create().show();
                }
                else if (result != null && result.equals("nolocationfound")){
                    this.dialog.hide();
                    text = "We couldn't find this location.\nPlease try again with different city name.";
                    new AlertDialog.Builder(Weather_Settings.this)
                            .setTitle("No Location Found")
                            .setMessage(text)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // whatever...

                                }
                            }).create().show();
                } else {
                    text = "Location : " + Location[0];
                    SharedPreferences settings = getSharedPreferences("Location", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("Location", queryOfCityName);
                    // Commit the edits!
                    editor.putString("Location_shown", Location[0]);
                    // Commit the edits!
                    editor.commit();
                    updateWeatherLocation();
                    RotaryHome.isWeatherChanged = true;
                    this.dialog.hide();
                }

            }
        }.execute();










    }


    public void updateWeatherLocation()
    {
        SharedPreferences settings3 = getSharedPreferences("Location", 0);
        final String locationcityname =  settings3.getString("Location_shown", "none");
        if (locationcityname.equals("none"))
        {
            tv.setText("You didn't set any location yet.\nYou're using Izmir,Turkey as default weather location");

        }
        else {
            tv.setText("Weather location is : " + locationcityname);
        }

    }

}
