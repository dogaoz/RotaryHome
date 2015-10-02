package com.dogaozkaraca.rotaryhome;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.markushi.ui.CircleButton;
public class Settings extends ActionBarActivity implements OnClickListener{
	public static int id;

	 CircleButton circle1;
	 CircleButton circle2;
	 CircleButton circle3;
	 CircleButton circle4;
	 CircleButton circle5;
	 CircleButton circle6;
	 TextView tv1;
	 TextView tv2;
	 TextView tv3;
	 TextView tv4;
	 TextView tv5;
	 TextView tv6;
	 RelativeLayout SettingsRelative;
	 int goingNew = 0;
	 @Override
	 protected void onPause()
	 {
		 super.onPause(); 
		 if (goingNew==1)
		 {
				overridePendingTransition(R.anim.anim_left,R.anim.anim_null);
	 
		 }
		 else if (goingNew==0)
		 {
				overridePendingTransition(0, R.anim.settings_bottom_down);
	 
		 }

	 }
	private int getDarkerColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.6f; // value component
		color = Color.HSVToColor(hsv);

		return color;
	}
	 @Override
	 protected void onResume()
	 {
		 goingNew=0;
			super.onResume();
		

			//SettingsRelative.setBackgroundColor(ColorScheme.getBackgroundColor(this));
			 circle1.setColor(ColorScheme.getBackgroundColor(this));
			  circle2.setColor(ColorScheme.getBackgroundColor(this));
			  circle3.setColor(ColorScheme.getBackgroundColor(this));
			  circle4.setColor(ColorScheme.getBackgroundColor(this));
			  circle5.setColor(ColorScheme.getBackgroundColor(this));
			  circle6.setColor(ColorScheme.getBackgroundColor(this));
			  tv1.setTextColor(getDarkerColor(ColorScheme.getBackgroundColor(this)));
			  tv2.setTextColor(getDarkerColor(ColorScheme.getBackgroundColor(this)));
			  tv3.setTextColor(getDarkerColor(ColorScheme.getBackgroundColor(this)));
			  tv4.setTextColor(getDarkerColor(ColorScheme.getBackgroundColor(this)));
			  tv5.setTextColor(getDarkerColor(ColorScheme.getBackgroundColor(this)));
			  tv6.setTextColor(getDarkerColor(ColorScheme.getBackgroundColor(this)));
		 getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));

		 if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			 Window window = getWindow();
			 window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			 window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			 window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
		 }

	 }
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.settings_bottom_up, R.anim.anim_null);
		
		setContentView(R.layout.do_settings);
	
		SettingsRelative = (RelativeLayout) findViewById(R.id.SettingsRelative);

		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		tv5 = (TextView) findViewById(R.id.textView5);
		tv6 = (TextView) findViewById(R.id.textView6);
		
		  circle1 = (CircleButton) findViewById(R.id.circleButton1);
		  circle2 = (CircleButton) findViewById(R.id.circleButton2);
		  circle3 = (CircleButton) findViewById(R.id.circleButton3);
		  circle4 = (CircleButton) findViewById(R.id.circleButton4);
		  circle5 = (CircleButton) findViewById(R.id.circleButton5);
		  circle6 = (CircleButton) findViewById(R.id.circleButton6);
		  circle1.setOnClickListener(this);
		  circle2.setOnClickListener(this);
		  circle3.setOnClickListener(this);
		  circle4.setOnClickListener(this);
		  circle5.setOnClickListener(this);
		  circle6.setOnClickListener(this);


            	   
               
               
               
              
		
		
	}
	public final void restartDOLauncher(Context context)
	{
		 android.os.Process.killProcess(android.os.Process.myPid());
    		   Intent intent = new Intent();
    		   intent.setComponent(new ComponentName("com.doga.ozkaraca.DOLauncher", "com.android.launcher3.DoLauncher.Launcher"));
    		   startActivity(intent);
    
	}
	@Override
	public void onClick(View v) {
	
        
		goingNew=1;
			if (v.getId() == circle1.getId())
	        {
	     		Intent intent = new Intent(Settings.this,Do_feed_Settings.class);
		           	 startActivity(intent);
	        }
	        else if (v.getId() == circle2.getId())
	        {
	        	  
	        	Intent intent = new Intent(Settings.this,ColorSchemeSettings.class);
	          	 startActivity(intent);
//	        	 Intent intent = new Intent(Settings.this,Themes_Settings.class);
//	           	 startActivity(intent);
	        }
	        else if (v.getId() == circle3.getId())
	        {
	     	  
		           	 Intent intent = new Intent(Settings.this,Weather_Settings.class);
		           	 startActivity(intent);
	        }
	        else if (v.getId() == circle4.getId())
	        {

	     	   Intent intent = new Intent(Settings.this,settings_more.class);
				startActivity(intent);
	     	   
	        }
	        else if (v.getId() == circle5.getId())
	        {
	     	  // Intent intent = new Intent(Settings.this,Experimental_Settings.class);
		       //    	 startActivity(intent);
				if (RotaryHome.isGooglePlayServicesMissing) {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which){
								case DialogInterface.BUTTON_NEGATIVE:

									break;
							}
						}
					};


					AlertDialog.Builder builder = new AlertDialog.Builder(this);

					builder.setMessage("Unfortunately, We found that you haven't Google Play Services installed on your device!\n ErrorCode:955")
							.setNegativeButton("Exit", dialogClickListener)
							.setCancelable(false);

					AlertDialog myAlertDialog = builder.create();
					myAlertDialog.show();
				}
				else
				{
					Intent i = new Intent(Settings.this, purchases.class);
					Settings.this.startActivity(i);
				}
	        }
	        else if (v.getId() == circle6.getId())
	        {
	     	   Intent intent = new Intent(Settings.this,AboutDO.class);
		           	 startActivity(intent);
	        }
	        else if (id == 7)
	        { 
	  	      Toast added = Toast.makeText(Settings.this, R.string.restarting,Toast.LENGTH_LONG);
	     	   added.show();
	     	  restartDOLauncher(Settings.this);
	     	 }
	}

	
	

}
