package com.dogaozkaraca.rotaryhome;
import java.util.ArrayList;
import java.util.List;



import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TabHost.TabSpec;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Do_feed_Settings extends ActionBarActivity {
	GridView lv;
	 @Override
	 protected void onPause()
	 {
		 
			super.onPause();
			overridePendingTransition(0, R.anim.anim_right);
	 }
	 @Override
	 protected void onResume()
	 {		
		
		 
		List<NetworkItem> lst = new ArrayList<NetworkItem>();
		
		lst.add(new NetworkItem("rss"));
		//lst.add(new NetworkItem("facebook"));
		lst.add(new NetworkItem("twitter"));
		lst.add(new NetworkItem("instagram"));
		lst.add(new NetworkItem("flickr"));
		lst.add(new NetworkItem("tumblr"));
		//lst.add(new NetworkItem("linkedin"));
		lst.add(new NetworkItem("foursquare"));
		lv.setAdapter(new NetworkAdaptor(this, lst));
		 super.onResume();
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
		setContentView(R.layout.activity_dofeed_settings);
	
	//	Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ri.ttf");
	
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.dofeed_settings_main);
		rl.setBackgroundColor(Color.WHITE);
		//rl.setBackgroundColor(ColorScheme.getBackgroundColor(this));
		ActionBar ac = getSupportActionBar();

		ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		lv = (GridView) findViewById(R.id.GridView1);

		RelativeLayout tipRL = (RelativeLayout) findViewById(R.id.tiprl);
		tipRL.setBackgroundColor(ColorScheme.getBackgroundColor(this));
		TextView tipText = (TextView) findViewById(R.id.textView13);
		tipText.setTextColor(ColorScheme.getTextColor(this));
		TextView tipTextTitle = (TextView) findViewById(R.id.textView1);
		tipTextTitle.setTextColor(ColorScheme.getTextColor(this));

		TextView tipTextTitle2 = (TextView) findViewById(R.id.textView2);
		tipTextTitle2.setTextColor(ColorScheme.getTextColor(this));
		TextView tipTextTitle4 = (TextView) findViewById(R.id.textView4);
		tipTextTitle4.setTextColor(ColorScheme.getTextColor(this));
		TextView tipTextTitle5 = (TextView) findViewById(R.id.textView6);
		tipTextTitle5.setTextColor(ColorScheme.getTextColor(this));
		//SpannableString s = new SpannableString(ac.getTitle());

		//s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		//ac.setTitle(s);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
		}
		   
		  
    	}



}
