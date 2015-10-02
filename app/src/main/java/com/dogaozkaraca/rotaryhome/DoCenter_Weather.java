package com.dogaozkaraca.rotaryhome;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


class DoCenter_Weather extends AsyncTask<String, String, String>
{
    Activity mContex;
	ImageView mWeatherView1;
	ImageView mWeatherView2;
TextView FirstDayWeatherTV;
TextView SecondDayWeatherTV;
TextView FirstDayWeatherTV2;
TextView SecondDayWeatherTV2;
    TextView detailed_weather_today;
    ImageView weather_today_imageview;
    TextView detailed_weather_today_right;
String PREFS_WEATHERPACK = "DoWEATHER_PACK";

String WeatherPack = "pack_";



    RelativeLayout weatherRL;
    RelativeLayout statusRL;
    TextView errorDetails;



	//////////////////
	String weather_Location;
	//////
	int temp_Today;
	int temp_Tomorrow;
	int temp_NextDay;
	//
	int status_Today;
	int status_Tomorrow;
	int status_NextDay;
	//
	int temp_Tomorrow_min;
	int temp_NextDay_min;
	//Today's Details
	int pressure_today;
	int humidity_today;
	int wind_comes_from_today;
	int wind_speed_today;
	int temp_Today_max;
	int temp_Today_min;
	int cloud_percentage_today;





   public DoCenter_Weather(
                            TextView detailed_weather_today2,
                            TextView detailed_weather_today_right2,
                            ImageView weather_today_imageview2,
                            RelativeLayout statusLayout,
                           RelativeLayout weatherRLfrom,
                           TextView errorDetailsfrom,
                           Activity mLauncher,
                           TextView doCenterWeather_TomorrowWeather_TextView,
                           TextView doCenterWeather_NextDayWeather_TextView,
                           ImageView imageofweatherInside,
                           ImageView imageofweatherInside2,
                           TextView weathertextDAY,
                           TextView weathertextDAY2
   ) {
	// TODO Auto-generated constructor stub
       this.detailed_weather_today = detailed_weather_today2;
       this.detailed_weather_today_right = detailed_weather_today_right2;
       this.weather_today_imageview = weather_today_imageview2;
	   this.mWeatherView1 = imageofweatherInside;
	   this.mWeatherView2 = imageofweatherInside2;
	   this.FirstDayWeatherTV = doCenterWeather_TomorrowWeather_TextView;
	   this.SecondDayWeatherTV = doCenterWeather_NextDayWeather_TextView;
	   this.FirstDayWeatherTV2 =  weathertextDAY;
	   this.SecondDayWeatherTV2 =  weathertextDAY2;
	  this.weatherRL = weatherRLfrom;
       this.errorDetails = errorDetailsfrom;
       this.statusRL = statusLayout;
this.mContex= mLauncher;

final SharedPreferences settings2 = mContex.getSharedPreferences(PREFS_WEATHERPACK, 0);

final String weather_pack = settings2.getString(PREFS_WEATHERPACK, "default_wp");

WeatherPack = "pack_";

   }

@Override
   protected void onPreExecute()
   {
       connecting(weatherRL,errorDetails,statusRL);
       //set connection message



	
}
   protected String doInBackground(String... params)
    {

			
		try {

			
			
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String locality = null;
            try {
            	
            	
            	SharedPreferences settings = mContex.getSharedPreferences("Location", 0);
		     	final String locationcityname =  settings.getString("Location", "Izmir");
		     	
		     	
		    	SharedPreferences settings87 = mContex.getSharedPreferences("DoWEATHER", 0);
		     	locality =  settings87.getString("DoWEATHER", "metric");
		     	
		     	
           
				request.setURI(new URI("http://api.openweathermap.org/data/2.5/forecast/daily?q="+locationcityname +"&mode=json&units="+locality+"&cnt=3"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            HttpResponse response = client.execute(request);
            
			 HttpEntity entity = response.getEntity();

			 if(entity != null){

				try {
			    JSONObject respObject = new JSONObject(EntityUtils.toString(entity));
			    //String active = respObject.getString("active");   
			   // String name = respObject.getString("name"); 
			  //  Log.d("DOGA","CUR LOCATION : " + name);
			    String temp =null;
			   // Workspace.CUR_LOCATION = name;
			    try
			    {


			    //	temp = respObject.getString(); 
			    	 JSONArray respObject2 = respObject.getJSONArray("list");
                    JSONObject tempToday = null;
                    JSONObject tempTomorrow = null;
					JSONObject tempNextDay = null;


                    tempToday = respObject2.getJSONObject(0);

                    tempTomorrow = respObject2.getJSONObject(1);

                    tempNextDay = respObject2.getJSONObject(2);



                    JSONObject a = tempToday.getJSONObject("temp");
                    temp_Today_max = a.getInt("max");

                    temp_Today = a.getInt("day");

                    temp_Today_min = a.getInt("min");

                    pressure_today = tempToday.getInt("pressure");

                    humidity_today = tempToday.getInt("humidity");

                    wind_comes_from_today = tempToday.getInt("deg");
                    wind_speed_today = tempToday.getInt("speed");

                    cloud_percentage_today = tempToday.getInt("clouds");

                    JSONArray h = tempToday.getJSONArray("weather");
                    JSONObject g = h.getJSONObject(0);

                    status_Today = g.getInt("id");


                    //json = tempToday.getInt("dt"); DATE


                    JSONObject b = tempTomorrow.getJSONObject("temp");
			    	 temp_Tomorrow = b.getInt("day");

					 temp_Tomorrow_min = b.getInt("min");

                    JSONArray o = tempTomorrow.getJSONArray("weather");
                    JSONObject l = o.getJSONObject(0);

                    status_Tomorrow = l.getInt("id");

                    //json = tempTomorroww.getInt("dt"); DATE


                    JSONObject c = tempNextDay.getJSONObject("temp");
                    temp_NextDay = c.getInt("day");

                    temp_NextDay_min = c.getInt("min");

                    JSONArray p = tempNextDay.getJSONArray("weather");
                    JSONObject r = p.getJSONObject(0);

                    status_NextDay = r.getInt("id");

                    //json = tempNextDayy.getInt("dt"); DATE



			    	
			    
			    }
			    catch(Exception tempErr)
			    {
			    	Log.e("DO-WEATHER","ERROR" + tempErr);
			
			
			    }


				 }
				 catch(Exception eer3)
				 {

					 //Workspace.CUR_LOCATION = "Weather Error!";
					

					 Log.e("DO-WEATHER","WEATHER TASK FAILED :" + eer3);
					 eer3.printStackTrace();
				 }
			    //.... 
			  }
			else{
			       //Do something here...
			    }
			
			
		} catch (IOException e) {
			Log.e("DO-WEATHER","ASYNCTASK WEATHER TASK FAILED");
			e.printStackTrace();
		}


	   
	   return null;
    }

   @Override
    protected void onPostExecute(String result) {


	SharedPreferences settings87 = mContex.getSharedPreferences("DoWEATHER", 0);
 	String locality = settings87.getString("DoWEATHER", "metric");
 	
Calendar calendar = Calendar.getInstance();
calendar.add(Calendar.DAY_OF_YEAR, 1);
Date tomorrow = calendar.getTime();


calendar.add(Calendar.DAY_OF_YEAR, 1);
Date today = calendar.getTime();
SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");


String tomorrowAsString =  "Tomorrow";// dateFormat.format(tomorrow);
	 String nextDayAsString = "Next Day";// dateFormat.format(today);


// Weather Today Details
       //detailed_weather_today

       if (locality.equals("metric"))
       {
           wind_speed_today = (int)(wind_speed_today*3.6/1.852);
           detailed_weather_today.setText("Pressure | "+pressure_today +"hPa"+
                           "\nWind | "+wind_comes_from_today+"°"+
                           "\nWind Speed | "+wind_speed_today+"knots"

           );

           detailed_weather_today_right.setText("Max | " + temp_Today_max+
                                        "\nMin | " + temp_Today_min +
                                        "\nHumidity | %"+humidity_today+
                                        "\nClouds | %" + cloud_percentage_today




                                            );

       }
       else
       {
           detailed_weather_today.setText("Pressure | "+pressure_today +"hPa"+
                           "\nWind | "+wind_comes_from_today+"°"+
                           "\nWind Speed | "+wind_speed_today+"mph"

           );

           detailed_weather_today_right.setText("Max | " + temp_Today_max+
                           "\nMin | " + temp_Today_min +
                           "\nHumidity | %"+humidity_today+
                           "\nClouds | %" + cloud_percentage_today




           );
       }

//Set Small Weather minimum Texts

       if (locality.equals("metric"))
       { // °C  °F = \-u-0-0-b-0  -leri sil

           FirstDayWeatherTV2.setText(tomorrowAsString+"  |  "+"Min: "+temp_Tomorrow_min+"°C");
           SecondDayWeatherTV2.setText(nextDayAsString+"  |  "+"Min: "+temp_NextDay_min+"°C");

       }
       else
       {
           FirstDayWeatherTV2.setText(tomorrowAsString+"  |  "+"Min: "+temp_Tomorrow_min+"°F");
           SecondDayWeatherTV2.setText(nextDayAsString+"  |  "+"Min: "+temp_NextDay_min+"°F");


       }


//Set Big Weather in Day Texts
       if (locality.equals("metric"))
       { // °C  °F
           FirstDayWeatherTV.setText(temp_Tomorrow+"°C");
           SecondDayWeatherTV.setText(temp_NextDay+"°C");

       }
       else
       {
           FirstDayWeatherTV.setText(temp_Tomorrow+"°F");
           SecondDayWeatherTV.setText(temp_NextDay+"°F");

       }


//Set connection is ok. If something wrong, status will throw an error in weatherNA;
       connected(weatherRL,statusRL);
//Set Images

       weather_today_imageview.clearAnimation();
       mWeatherView1.clearAnimation();
       mWeatherView2.clearAnimation();

       setStatusImage(status_Today, weather_today_imageview);
       setStatusImage(status_Tomorrow,mWeatherView1);
       setStatusImage(status_NextDay,mWeatherView2);


   }
    		 
    	 
 public static void setConnectionError(RelativeLayout weatherRL, TextView details,String errorText,RelativeLayout detailsRL)
 {
	 weatherRL.setVisibility(View.GONE);
	 detailsRL.setVisibility(View.VISIBLE);
	 details.setVisibility(View.VISIBLE);
     details.setText(errorText);

 }

public static void connected(RelativeLayout weatherRL, RelativeLayout details)
{

		weatherRL.setVisibility(View.VISIBLE);
		details.setVisibility(View.GONE);



}

public static void connecting(RelativeLayout weatherRL, TextView details,RelativeLayout detailsRL)
{

        weatherRL.setVisibility(View.GONE);
	detailsRL.setVisibility(View.VISIBLE);

	details.setVisibility(View.VISIBLE);
        details.setText("Please wait while we are loading things for you!");


}
            
      

   
   public static int getResId(String resourceName, Class<?> c) {

	    try {
	    	 Field idField = c.getDeclaredField(resourceName);
	         return idField.getInt(idField);
	    } catch (Exception e) {
	        Log.e("DO_ERROR","WeatherLoaderIcons cannot get");
	        return -1;
	    } 
	}



    public void setStatusImage(int tempStatus, ImageView imageofweather)
    {
        ////WEATHER STATUS
        if (200<=tempStatus && tempStatus<250)
        {
            if (tempStatus==200)
            {

                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));



            }
            else if (tempStatus==201)
            {

                //thunderstorm with rain
                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));


            }
            else if (tempStatus==202)
            {

                //thunderstorm with heavy rain
                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));


            }
            else if (tempStatus==210)
            {

                //light thunderstorm
                imageofweather.setImageResource(getResId(WeatherPack+"17", R.drawable.class));



            }
            else if (tempStatus==211)
            {

                //thunderstorm
                imageofweather.setImageResource(getResId(WeatherPack+"09", R.drawable.class));


            }
            else if (tempStatus==212)
            {
                //heavy thunderstorm
                imageofweather.setImageResource(getResId(WeatherPack+"09", R.drawable.class));



            }
            else if (tempStatus==221)
            {

                //ragged thunderstorm
                imageofweather.setImageResource(getResId(WeatherPack+"09", R.drawable.class));



            }
            else if (tempStatus==230)
            {
                //thunderstorm with light drizzle
                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));



            }
            else if (tempStatus==231)
            {

                //thunderstorm with drizzle
                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));


            }
            else if (tempStatus==232)
            {

                //thunderstorm with heavy drizzle
                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));


            }


        }
        else if (300<=tempStatus && tempStatus<350)
        {
            if (tempStatus==300)
            {

                //light intensity drizzle
                imageofweather.setImageResource(getResId(WeatherPack+"15", R.drawable.class));



            }
            else if (tempStatus==301)
            {

                //drizzle
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));


            }
            else if (tempStatus==302)
            {

                //heavy intensity drizzle rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));


            }
            else if (tempStatus==310)
            {

                //light intensity drizzle rain
                imageofweather.setImageResource(getResId(WeatherPack+"15", R.drawable.class));



            }
            else if (tempStatus==311)
            {

                //drizzle rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));



            }
            else if (tempStatus==312)
            {

                //heavy intensity drizzle rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));



            }
            else if (tempStatus==321)
            {

                //shower drizzle
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));



            }
        }
        else if (500<=tempStatus && tempStatus<550)
        {
            if (tempStatus==500)
            {

                //light rain
                imageofweather.setImageResource(getResId(WeatherPack+"15", R.drawable.class));


            }
            else if (tempStatus==501)
            {

                //moderate rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));


            }
            else if (tempStatus==502)
            {

                //heavy intensity rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));


            }
            else if (tempStatus==503)
            {

                //very heavy rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));



            }
            else if (tempStatus==504)
            {

                //extreme rain
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));



            }
            else if (tempStatus==511)
            {

                //freezing rain
                imageofweather.setImageResource(getResId(WeatherPack+"06", R.drawable.class));


            }
            else if (tempStatus==520)
            {

                //light intensity shower rain
                imageofweather.setImageResource(getResId(WeatherPack+"05", R.drawable.class));


            }
            else if (tempStatus==521)
            {

                //shower rain
                imageofweather.setImageResource(getResId(WeatherPack+"05", R.drawable.class));



            }
            else if (tempStatus==522)
            {

                //heavy intensity shower rain
                imageofweather.setImageResource(getResId(WeatherPack+"05", R.drawable.class));



            }


        }
        else if (600<=tempStatus && tempStatus<650)
        {
            if (tempStatus==600)
            {

                //light snow
                imageofweather.setImageResource(getResId(WeatherPack+"19", R.drawable.class));

            }
            else if (tempStatus==601)
            {


                //snow
                imageofweather.setImageResource(getResId(WeatherPack+"14", R.drawable.class));


            }
            else if (tempStatus==602)
            {


                //heavy snow
                imageofweather.setImageResource(getResId(WeatherPack+"13", R.drawable.class));


            }
            else if (tempStatus==611)
            {

                //sleet
                imageofweather.setImageResource(getResId(WeatherPack+"06", R.drawable.class));


            }
            else if (tempStatus==621)
            {

                //shower snow
                imageofweather.setImageResource(getResId(WeatherPack+"13", R.drawable.class));


            }
        }
        else if (700<=tempStatus && tempStatus<750)
        {
            if (tempStatus==701)
            {

                //mist
                imageofweather.setImageResource(getResId(WeatherPack+"02", R.drawable.class));


            }
            else if (tempStatus==711)
            {

                //smoke
                imageofweather.setImageResource(getResId(WeatherPack+"02", R.drawable.class));


            }
            else if (tempStatus==721)
            {

                //haze
                imageofweather.setImageResource(getResId(WeatherPack+"02", R.drawable.class));


            }
            else if (tempStatus==731)
            {

                //sand/dust whirls
                imageofweather.setImageResource(getResId(WeatherPack+"16", R.drawable.class));


            }
            else if (tempStatus==741)
            {

                ///fog
                imageofweather.setImageResource(getResId(WeatherPack+"02", R.drawable.class));



            }


        }
        else if (800<=tempStatus && tempStatus<850)
        {
            if (tempStatus==800)
            {


                //sky is clear
                imageofweather.setImageResource(getResId(WeatherPack+"01", R.drawable.class));


                SharedPreferences settings56472 = mContex.getSharedPreferences("AnimationsDO", 0);
                String enabledanimations = settings56472.getString("AnimationsDO", "enabled");
                if(enabledanimations.equals("enabled"))
                {
                    RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//
////Setup anim with desired properties
//anim.setInterpolator(new LinearInterpolator());
                    anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
//anim.setRepeatCount(2); //Repeat animation indefinitely
//
                    anim.setDuration(7000); //Put desired duration per anim cycle here, in milliseconds
//
//
//   			 // Start animating the image
                    imageofweather.startAnimation(anim);
                }
                else
                {
                }



            }
            else if (tempStatus==801)
            {

                //few clouds
                imageofweather.setImageResource(getResId(WeatherPack+"04", R.drawable.class));


            }
            else if (tempStatus==802)
            {

                //scattered clouds
                imageofweather.setImageResource(getResId(WeatherPack+"12", R.drawable.class));



            }
            else if (tempStatus==803)
            {
                //broken clouds

                imageofweather.setImageResource(getResId(WeatherPack+"10", R.drawable.class));



            }
            else if (tempStatus==804)
            {

                //overcast clouds

                imageofweather.setImageResource(getResId(WeatherPack+"11", R.drawable.class));

            }
        }
        else if (900<=tempStatus && tempStatus<950)
        {
            if (tempStatus==900)
            {

                //tornado
                imageofweather.setImageResource(getResId(WeatherPack+"20", R.drawable.class));


            }
            else if (tempStatus==901)
            {

                //tropical storm
                imageofweather.setImageResource(getResId(WeatherPack+"03", R.drawable.class));

            }

            else if (tempStatus==902)
            {

                //hurricane
                imageofweather.setImageResource(getResId(WeatherPack+"20", R.drawable.class));

            }
            else if (tempStatus==903)
            {

                //cold
                imageofweather.setImageResource(getResId(WeatherPack+"17", R.drawable.class));



            }
            else if (tempStatus==904)
            {


                //hot
                imageofweather.setImageResource(getResId(WeatherPack+"08", R.drawable.class));



            }
            else if (tempStatus==905)
            {

                //windy
                imageofweather.setImageResource(getResId(WeatherPack+"16", R.drawable.class));


            }
            else if (tempStatus==906)
            {

                //hail
                imageofweather.setImageResource(getResId(WeatherPack+"07", R.drawable.class));



            }
        }
        else
        {

            imageofweather.setImageResource(getResId(WeatherPack + "na", R.drawable.class));
            if(DoWorkspace.isNetworkAvailable()) {
                setConnectionError(weatherRL, errorDetails, "We couldn't connect to the weather service!\nPlease try again in a while.",statusRL);
            }
            else
            {
                setConnectionError(weatherRL, errorDetails, "We couldn't connect to the weather service!\nPlease check your network connection and try again.",statusRL);

            }

        }
    }




    public String convertDegreesToWays(int degree)
    {
        String lo = "degree";

        if (degree >= 345 && degree <= 15)
        {

            lo = "N";
        }

        if (degree > 15 && degree <= 30)
        {

            lo = "NE";
        }

        if (degree > 30 && degree <= 60)
        {

            lo = "E";
        }
        if (degree > 60 && degree <= 60)
        {

            lo = "E";
        }


        return lo;
    }




}
