package com.dogaozkaraca.rotaryhome;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorSchemeSettings extends ActionBarActivity {
	
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
					overridePendingTransition(0, R.anim.anim_right);
		 
			 }
			
	 }
	 @Override
	 protected void onResume()
	 {
		 super.onResume();
		 goingNew=0;
	 }
	private SharedPreferences sharedpreferences;
	private View revealView;
	ActionBar ac;
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
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_scheme_settings);
		revealView = findViewById(R.id.reveal_view);
		revealView.setBackgroundColor(ColorScheme.getBackgroundColor(this));
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.css_rl);

		Button btn = (Button) findViewById(R.id.button1);
        btn.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        btn.setTextColor(ColorScheme.getTextColor(this));
		rl.setBackgroundColor(Color.WHITE);
         ac = getSupportActionBar();
        RelativeLayout tiprl = (RelativeLayout) findViewById(R.id.tiprl);
        tiprl.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        TextView tipTextTitle = (TextView) findViewById(R.id.textView1);
        tipTextTitle.setTextColor(ColorScheme.getTextColor(this));
        TextView tipText = (TextView) findViewById(R.id.textView13);
        tipText.setTextColor(ColorScheme.getTextColor(this));

        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//SpannableString s = new SpannableString(getSupportActionBar().getTitle());
		//s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		//getSupportActionBar().setTitle(s);

		sharedpreferences = getSharedPreferences("materialColors", Context.MODE_PRIVATE);


		// Views
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ColorScheme.getStatusBarColor(ColorSchemeSettings.this));
	if (getIntent().getBooleanExtra("animate", false)) {
		// Show the unReveal effect
		final int cx = sharedpreferences.getInt("x", 0);
		final int cy = sharedpreferences.getInt("y", 0);

		startHideRevealEffect(cx, cy);
	}
	}
}

	public void setColorSchemePremium(final View v)
	{
        if (RotaryHome.rotaryIsPremium(this)) {
            final SharedPreferences settings = getSharedPreferences("DO_StatusBarColors", 0);

            ///Set Background Colors
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, ColorScheme.getBackgroundColor(ColorSchemeSettings.this), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    // SetTextColor

                    SharedPreferences.Editor editor = settings.edit();

					editor.putString("DO_StatusBarColor", String.format("#%06X", (0xFFFFFF & color)));
					editor.putString("DO_StatusBarTextColor", ColorScheme.makeTextColor(color));
					editor.commit();
                    final String statusbarcolor = settings.getString("DO_StatusBarColor", "#FFFFFFFF");
                    RotaryHome.rotaryStatus=1;
					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
					if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
						int[] location = new int[2];
						revealView.setBackgroundColor(ColorScheme.getBackgroundColor(ColorSchemeSettings.this));
						v.getLocationOnScreen(location);

						int cx = (location[0] + (v.getWidth() / 2));
						int cy = location[1] + (GUIUtils.getStatusBarHeight(ColorSchemeSettings.this) / 2);

						SharedPreferences.Editor ed = sharedpreferences.edit();
						ed.putInt("x", cx);
						ed.putInt("y", cy);
						ed.apply();

						hideNavigationStatus();
						//ac.hide();
						GUIUtils.showRevealEffect(revealView, cx, cy, revealAnimationListener);
					}
					else
					{
						finish();
					}


                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    // cancel was selected by the user
                }
            });

            dialog.show();
        }
        else {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent i=new Intent(ColorSchemeSettings.this,purchases.class);
                            startActivity(i);
                            break;


                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This action requires " + "Premium" + " account!")
                    .setNegativeButton("Cancel", dialogClickListener)
                    .setPositiveButton("Upgrade Account", dialogClickListener)
                    .show();


        }

	}

	 public void setColorScheme(View v)
	    {
	    	String id = getResources().getResourceEntryName(v.getId());

	final SharedPreferences settings =getSharedPreferences("DO_StatusBarColors" , 0);

	   SharedPreferences.Editor editor = settings.edit();
	    	if (id.equals("carbonblack"))
	    	{
	    		// #FFFF1F1E1E
	    	
	      	   
		 	 editor.putString("DO_StatusBarColor","#FF1F1E1E");
		 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
		 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("dowhite"))
	    	{
	    		// #FFEFF0FB
	   	 	 editor.putString("DO_StatusBarColor","#FFEFF0FB");
	   	 	editor.putString("DO_StatusBarTextColor","#FF000000");
		 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("coffeebrown"))
	    	{
	    		// #FF7C5852
	    		editor.putString("DO_StatusBarColor","#FF7C5852");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("lightblue"))
	    	{
	    		// #FF0D98FD
	    		editor.putString("DO_StatusBarColor","#FF0D98FD");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("darkblue"))
	    	{
	    		// #FF374F6F
	    		editor.putString("DO_StatusBarColor","#FF374F6F");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("blue"))
	    	{
	    		// #FF00B4CC
	    		editor.putString("DO_StatusBarColor","#FF00B4CC");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("treegreen"))
	    	{
	    		// #FF75A801
	    		editor.putString("DO_StatusBarColor","#FF75A801");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("red"))
	    	{
	    		// #FFB11623
	    		editor.putString("DO_StatusBarColor","#FFB11623");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("darkred"))
	    	{
	    		// #FF9F111B
	    		editor.putString("DO_StatusBarColor","#FF9F111B");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("yellow"))
	    	{
	    		// #FFF8CA00
	    		editor.putString("DO_StatusBarColor","#FFF8CA00");
	       	 	editor.putString("DO_StatusBarTextColor","#FF000000");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("pink"))
	    	{
	    		// #FFFF3D7F
	    		editor.putString("DO_StatusBarColor","#FFFF3D7F");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("purple"))
	    	{
	    		// #FF480048
	    		editor.putString("DO_StatusBarColor","#FF480048");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("orange"))
	    	{
	    		// #FFFA6900
	    		editor.putString("DO_StatusBarColor","#FFFA6900");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("materialblue"))
	    	{
	    		// #FF00BCD4
	    		editor.putString("DO_StatusBarColor","#FF00BCD4");
	       	 	editor.putString("DO_StatusBarTextColor","#FF000000");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("materialgreen"))
	    	{
	    		// #FF179B5E
	    		editor.putString("DO_StatusBarColor","#FF179B5E");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("materialpink"))
	    	{
	    		// #FFE91E63
	    		editor.putString("DO_StatusBarColor","#FFE91E63");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("materialpurple"))
	    	{
	    		// #FF716BBE
	    		editor.putString("DO_StatusBarColor","#FF716BBE");
	       	 	editor.putString("DO_StatusBarTextColor","#FFFFFFFF");
	    	 	  editor.commit();
	    		
	    	}
	    	else if (id.equals("materiallightgreen"))
	    	{
	    		// #FF4DB6AC
	    		editor.putString("DO_StatusBarColor","#FF4DB6AC");
	       	 	editor.putString("DO_StatusBarTextColor","#FF000000");
	    	 	  editor.commit();
	    	}

	    	final String statusbarcolor = settings.getString("DO_StatusBarColor", "#FFFFFFFF");
	//finish();

	 
	 RotaryHome.rotaryStatus=1;

			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
				int[] location = new int[2];
				revealView.setBackgroundColor(ColorScheme.getBackgroundColor(this));
				v.getLocationOnScreen(location);

				int cx = (location[0] + (v.getWidth() / 2));
				int cy = location[1] + (GUIUtils.getStatusBarHeight(this) / 2);

				SharedPreferences.Editor ed = sharedpreferences.edit();
				ed.putInt("x", cx);
				ed.putInt("y", cy);
				ed.apply();

				hideNavigationStatus();
				//ac.hide();
				GUIUtils.showRevealEffect(revealView, cx, cy, revealAnimationListener);
			}
			else
			{
				finish();
			}

	    }
	 
		public void WidgetLayout(View view)
		{
			goingNew=1;
	    	//Intent intent = new Intent(ColorSchemeSettings.this,OnlineContentPersonalization.class);
	      	// startActivity(intent);
            Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
            startActivity(Intent.createChooser(intent, "Choose Wallpaper"));
	      	 
		}


	private void startHideRevealEffect(final int cx, final int cy) {

		if (cx != 0 && cy != 0) {
			// Show the unReveal effect when the view is attached to the window
			revealView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
				@Override
				public void onViewAttachedToWindow(View v) {

					// Get the accent color
					//TypedValue outValue = new TypedValue();
					//getTheme().resolveAttribute(android.R.attr.colorPrimary, outValue, true);
					revealView.setBackgroundColor(ColorScheme.getBackgroundColor(ColorSchemeSettings.this));

					GUIUtils.hideRevealEffect(revealView, cx, cy, 1920);
				}

				@Override
				public void onViewDetachedFromWindow(View v) {}
			});
		}
	}


	private void hideNavigationStatus() {

		View decorView = getWindow().getDecorView();

		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		decorView.setSystemUiVisibility(uiOptions);
	}


	Animator.AnimatorListener revealAnimationListener = new Animator.AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {}

		@Override
		public void onAnimationEnd(Animator animation) {

			Intent i = new Intent(ColorSchemeSettings.this, ColorSchemeSettings.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			i.putExtra("animate", true);
			startActivity(i);
			overridePendingTransition(0, 0);
			finish();
		}

		@Override
		public void onAnimationCancel(Animator animation) {}

		@Override
		public void onAnimationRepeat(Animator animation) {}
	};
}
