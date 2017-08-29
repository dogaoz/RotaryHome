package com.dogaozkaraca.rotaryhome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class settings_more extends ActionBarActivity {

    Spinner fontSpinner;
    SharedPreferences settings;
    SharedPreferences settings5;
    Button iconPackbtn;
    public AlertDialog myAlertDialog;

    @Override
    protected void onPause()
    {

        super.onPause();
        overridePendingTransition(0, R.anim.anim_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_more);

        overridePendingTransition(R.anim.anim_left, R.anim.anim_null);
       // SpannableString s = new SpannableString(getSupportActionBar().getTitle());
       // s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //getSupportActionBar().setTitle(s);

        android.support.v7.app.ActionBar ac = getSupportActionBar();

        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ///

        settings = getSharedPreferences("SettingsMore", 0);
        settings5 = getSharedPreferences("font_updater", 0);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
        }

        iconPackbtn = (Button) findViewById(R.id.iconPackMainButton);
        iconPackbtn.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        iconPackbtn.setTextColor(ColorScheme.getTextColor(this));
        //Fonts
        fontSpinner = (Spinner) findViewById(R.id.font_spinner);
    fontSpinner.setBackgroundColor(ColorScheme.getActionBarColor(this));
        // dynamicSpinner.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        // dynamicSpinner.setPopupBackgroundResource(ColorScheme.getBackgroundColor(this));
        String[] items = new String[] { "Forgotten Sans","Lobster","Exo 2 Thin","Android Default"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, items);


        fontSpinner.setAdapter(adapter);


        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (RotaryHome.rotaryIsPremium(settings_more.this)) {

                    SharedPreferences.Editor editor94 = settings5.edit();

                    if (position == 0) {
                        editor94.putString("fonts", "forgottensans").commit();
                        RotaryHome.rotaryStatusFont = 1;


                    } else if (position == 1) {
                        editor94.putString("fonts", "lobster").commit();
                        RotaryHome.rotaryStatusFont = 1;


                    }
                    else if (position == 2) {
                        editor94.putString("fonts", "exo2").commit();
                        RotaryHome.rotaryStatusFont = 1;


                    }
                    else if (position == 3) {
                        editor94.putString("fonts", "android").commit();
                        RotaryHome.rotaryStatusFont = 1;


                    }

                } else {
                    updateVariables();
                    launchDialog("premium", settings_more.this, true);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        updateVariables();



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
    public void changeItemStatus(View v)
    {
        SharedPreferences.Editor editor = settings.edit();


        switch (v.getId())
        {
            case R.id.radioItemSizeNormal:

                if (RotaryHome.rotaryIsPremium(settings_more.this))
                {
                    editor.putString("itemSize","normal").commit();
                    RotaryHome.rotaryStatus=1;
                }
                else
                {
                    updateVariables();
                    launchDialog("premium",settings_more.this,true);

                }



                break;

            case R.id.radioItemSizeBig:
                if (RotaryHome.rotaryIsPremium(settings_more.this))
                {
                    editor.putString("itemSize","big").commit();
                    RotaryHome.rotaryStatus=1;
                }
                else
                {
                    updateVariables();
                    launchDialog("premium",settings_more.this,true);

                }




                break;
            case R.id.checkBox2:

                if (((CheckBox)findViewById(R.id.checkBox2)).isChecked())
                {
                    editor.putString("batteryAtLeft","enabled").commit();
                    RotaryHome.rotaryStatus=1;

                }
                else
                {
                    editor.putString("batteryAtLeft","disabled").commit();
                    RotaryHome.rotaryStatus=1;

                }

                break;
            case R.id.checkBox3:

                if (((CheckBox)findViewById(R.id.checkBox3)).isChecked())
                {
                    editor.putString("connAtRight", "enabled").commit();
                    RotaryHome.rotaryStatus=1;

                }
                else
                {
                    editor.putString("connAtRight", "disabled").commit();
                    RotaryHome.rotaryStatus=1;

                }

                break;
            case R.id.checkBox:
                if (((CheckBox)findViewById(R.id.checkBox)).isChecked())
                {
                    if (RotaryHome.rotaryIsPremium(settings_more.this))
                    {
                        editor.putBoolean("assistant",true).commit();
                        RotaryHome.rotaryStatus=1;
                    }
                    else
                    {
                        updateVariables();
                        launchDialog("premium",settings_more.this,true);

                    }


                }
                else
                {
                    if (RotaryHome.rotaryIsPremium(settings_more.this))
                    {
                        editor.putBoolean("assistant",false).commit();
                        RotaryHome.rotaryStatus=1;
                    }
                    else
                    {
                        updateVariables();
                        launchDialog("premium",settings_more.this,true);

                    }


                }

                break;




        }


    }


    private void launchDialog(String pro_or_premium, final Context mContext, final boolean cancellable) {
        if( myAlertDialog != null && myAlertDialog.isShowing() ) return;

        String ab = "upgraded";
        if (pro_or_premium.equals("pro_or_premium"))
        {
            ab = "Pro or Premium";
        }
        else {
            ab = "Premium";

        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent i=new Intent(mContext,purchases.class);
                        mContext.startActivity(i);
                        break;


                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        if (!cancellable)
                            finish();
                        break;
                }
            }
        };


         AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("This feature(s) requires " + ab + " account!")
                .setNegativeButton("Cancel", dialogClickListener)
                .setPositiveButton("Upgrade Account", dialogClickListener)
                .setCancelable(cancellable);

        myAlertDialog = builder.create();
        myAlertDialog.show();


    }

    void updateVariables()
    {
        if(settings5.getString("fonts","forgottensans").equals("forgottensans"))
        {
            fontSpinner.setSelection(0);


        }
        else if (settings5.getString("fonts","forgottensans").equals("lobster"))
        {
            fontSpinner.setSelection(1);


        }
        else if (settings5.getString("fonts","forgottensans").equals("exo2"))
        {
            fontSpinner.setSelection(2);


        }
        else if (settings5.getString("fonts","forgottensans").equals("android"))
        {
            fontSpinner.setSelection(3);


        }
        String getItemSize = settings.getString("itemSize", "normal");
        if(getItemSize.equals("normal"))
        {
            ((RadioButton) findViewById(R.id.radioItemSizeNormal)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioItemSizeBig)).setChecked(false);

        }
        else
        {
            ((RadioButton) findViewById(R.id.radioItemSizeNormal)).setChecked(false);
            ((RadioButton) findViewById(R.id.radioItemSizeBig)).setChecked(true);



        }

        boolean assistantStatus = settings.getBoolean("assistant", false);
        if(assistantStatus)
        {
            ((CheckBox) findViewById(R.id.checkBox)).setChecked(true);


        }
        else
        {
            ((CheckBox) findViewById(R.id.checkBox)).setChecked(false);




        }

        ///////
        String batteryPercentageAtLeftEnabled = settings.getString("batteryAtLeft", "enabled");
        if(batteryPercentageAtLeftEnabled.equals("enabled"))
        {
            ((CheckBox) findViewById(R.id.checkBox2)).setChecked(true);
        }
        else
        {
            ((CheckBox) findViewById(R.id.checkBox2)).setChecked(false);
        }
        ///////
        String connectionTypeAtRightEnabled = settings.getString("connAtRight", "enabled");
        if(connectionTypeAtRightEnabled.equals("enabled"))
        {
            ((CheckBox) findViewById(R.id.checkBox3)).setChecked(true);
        }
        else
        {
            ((CheckBox) findViewById(R.id.checkBox3)).setChecked(false);
        }


    }


    public void setIconPack(View v)
    {
        if (!RotaryHome.rotaryIsPremium(settings_more.this)) {
            ///Premium Check
            launchDialog("premium", settings_more.this, true);
        }
        else
        {
            List<ResolveInfo> ab = getPackageManager().queryIntentActivities(new Intent("org.adw.launcher.THEMES"), PackageManager.GET_META_DATA);

            if (ab.size() > 0) {

                final Dialog dialog2 = new Dialog(this);
                dialog2.setCancelable(true);

                final View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.choose_final_item, null);

                final ListView lv = (ListView) view.findViewById(R.id.rss_choose_detailview);
                final TextView tv = (TextView) view.findViewById(R.id.tw);
                tv.setText("Choose Icon Pack");
                tv.setBackgroundColor(ColorScheme.getActionBarColor(this));
                final ArrayList lst = new ArrayList<IconPackItem>();

                lst.add(new IconPackItem("com.dogaozkaraca.rotaryhome", dialog2));

                for (int x = 0; x < ab.size(); x++) {

                    lst.add(new IconPackItem(ab.get(x).activityInfo.packageName, dialog2));

                }
                lv.setAdapter(new IconPackAdaptor(this, lst));

                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(view);

                dialog2.show();
            } else {
                Toast.makeText(this, "We couldn't find any icon packs", Toast.LENGTH_LONG).show();
            }
        }
    }

}
