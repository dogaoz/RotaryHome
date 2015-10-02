package com.dogaozkaraca.rotaryhome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dogaozkaraca.rotaryhome.util.IabHelper;
import com.dogaozkaraca.rotaryhome.util.IabResult;
import com.dogaozkaraca.rotaryhome.util.Inventory;
import com.facebook.FacebookSdk;
import com.fivehundredpx.android.blur.BlurringView;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import at.markushi.ui.CircleButton;
import slidinguppanel.SlidingUpPanelLayout;


import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;



public class RotaryHome extends Activity {

    String base64EncodedPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5H3szs41ZfdsEmJqmKPFy8054WBoTBxeisytgTB5i4NJ2/Z/dn22Iydctqba41NuA5/ZitohQ8R2KEaK6bBzxpJ1OjwpCbflM4JJ0lxFU9QHOw1dyYK3Gm3IA/RkBi+6gzuPYmnMEJJFku1bhxRP0nFvmeeKRBcrrjWSEcLqivPvD9vqZzQHpyV54zXPPV5XQBcByDTQzhXeyt78f/9g4lpXB3xQgpOqlJwX2KDukNmS/utVa9HfpMCX2Z+jI9M+T7uADllRWrb0bLZsMNHXJ7RGo+mDSLuSKfDb7AHoxNJk5Cnx8Rolbg0XUF2CO5kZ5MuJYTEh/+07mqu7qDfp4wIDAQAB";
    public static RotaryView rotaryView;
    public static String connectionStatus;
    public static RelativeLayout connectionErrorLayout;
    public static TextView tv;
    public static TextView wifi;
    public static TextView folderNameText;
    public static EditText folderNameTextEdit;
    public static CircleButton folderColor;
    TelephonyManager        Tel;
    public static int rotaryStatus;
    public static int rotaryStatusFont;
    public static TextView rotarystatusTV;
    public static boolean isDarkTheme = true;
    public static boolean firstSetup = false;
    public static boolean tutorial= false;
    public static ImageButton settingsb;
    public static slidinguppanel.SlidingUpPanelLayout slidingPan;
    public static ImageButton notificationsb;
    RelativeLayout notifications_layout;
    public static boolean isFeedChangedBySettings=false;
    public static boolean wallpaperChanged = false;
    public static String rotaryStatusText= "";
    public static SharedPreferences settingsCHK;
    public static IabHelper mHelper;
    public static Context ctx;
    public static boolean isNotificationsOpened = false;
    public static boolean isDemoActive = false;
    public static boolean isWeatherChanged = false;
    public static ShowcaseView tutorialRotaryAppChooser;
    public static ShowcaseView tutorialHowToOpenFeed;
    public static ShowcaseView tutorialFeed;
    public static boolean isInAllApps = false;
    public static boolean isFolderEditMode = false;
    public static AllAppsGridView rotaryAllApps;
    public static int removeFolderIconSizeShouldbe;
    public static int getDeviceStatusBarHeight;
    FlipAnimation flipAnimation;
    TextView DoCenterWeather_TomorrowWeather_TextView;
    TextView DoCenterWeather_NextDayWeather_TextView;
    TextView weathertextDAY;
    TextView weathertextDAY2;
    ImageView imageofweatherInside;
    ImageView imageofweatherInside2;
    CircleButton weatherRF;
    RelativeLayout weatherRL;
    TextView errorDetails;
    TextView weather_details_notif;
    TextView weather_details_notif2;
    ImageView weather_today_image;
    RelativeLayout rotaryAppChooserRL;
    RelativeLayout weather_status_textRL;
    public static int deviceScreenHeight;
    public static int deviceScreenWidth;
    public static ImageButton removeFolderButton;
    public static RevealView_DragNdropView revealView;
    static RelativeLayout weatherTodayRL;
    static View slideUpRLno12;
    static View slideUpRLno2;
    public static Activity whoami;
    public static ImageButton assistantButton;
    public static boolean isGooglePlayServicesMissing = false;
    public static boolean isEditingMode = false;
    public static boolean isUninstallMode = false;
    static boolean amiDogaOzkaraca = false;

    public static boolean isFolderFirstTime = false;

    public static ArrayList packageNameListForNewFolder;
    public static boolean newFolderCreationStarted;
    public static int folderItemLimit = 8;


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            tv.setText("%"+String.valueOf(level) );
        }
    };
    //private ScaleGestureDetector SGD;
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.rotary_home);

        if(BuildConfig.DEBUG)
        {
            amiDogaOzkaraca = true;
        }
        ctx = this;
        getSharedPreferences("rotary", 0).edit().putBoolean("iNeedRefresh", true).commit();
        rotaryView.refreshAppsWithLoadingScreen(this);
        whoami = this;
        if (!getSharedPreferences("Rotary_Online", 0).getString("Rotary_loggedin","no").equals("yes"))
        {
            if(!BuildConfig.DEBUG) {
                firstSetup = true;

                //Intent i = new Intent(this, RotaryHome_FirstSetup.class);
               // startActivity(i);
                SharedPreferences settings = getSharedPreferences("Rotary_Online", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Rotary_loggedin", "yes");

                editor.putString("Rotary_Name", "Rotary User");

                editor.putString("Rotary_Email", "rotaryuser@rotaryhome.com");


                // Commit the edits!
                editor.commit();
                SharedPreferences settings2 = getSharedPreferences("Rotary_Online", 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putString("Rotary_loggedin", "yes");
                editor2.commit();

            }
        }
        if (getSharedPreferences("rotaryFolders", 0).getBoolean("isFolderFirstTime",true))
        {
            isFolderFirstTime = true;
        }
       slideUpRLno12 = findViewById(R.id.slideUpRLno12);
        slideUpRLno2 = findViewById(R.id.slideUpRLno2);
        DoCenterWeather_TomorrowWeather_TextView = (TextView) findViewById(R.id.textView195);
        DoCenterWeather_NextDayWeather_TextView = (TextView) findViewById(R.id.textView196);
        weathertextDAY = (TextView) findViewById(R.id.weathertextDAY);
        weathertextDAY2 = (TextView) findViewById(R.id.weathertextDAY2);
        imageofweatherInside = (ImageView) findViewById(R.id.imageofweatherInside);
        imageofweatherInside2 = (ImageView) findViewById(R.id.imageofweatherInside2);
        slidingPan = (slidinguppanel.SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT
            );
            getDeviceStatusBarHeight = getStatusBarHeight();
            params.setMargins(0,getDeviceStatusBarHeight, 0,0);
            slidingPan.setLayoutParams(params);


        }


        notifications_layout = (RelativeLayout) findViewById(R.id.notifications_dialog);
        tv = (TextView) findViewById(R.id.signal);
        wifi = (TextView) findViewById(R.id.wifi);
        rotarystatusTV= (TextView) findViewById(R.id.textView10);
        rotaryView = (RotaryView) findViewById(R.id.rotaryView);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        rotaryView.getLayoutParams().height = size.x + (int)rotaryView.convertDpToPixel(15);
        deviceScreenWidth = size.x;
        deviceScreenHeight = size.y;
        settingsb = (ImageButton) findViewById(R.id.imageButton);
        notificationsb = (ImageButton) findViewById(R.id.imageButton2);
        rotaryAppChooserRL = (RelativeLayout) findViewById(R.id.rotaryAppChooserRL);
        weatherRF = (CircleButton) findViewById(R.id.circleButtonRF);
        weatherRL = (RelativeLayout) findViewById(R.id.weatherRL_ro);
        errorDetails = (TextView) findViewById(R.id.errordetails);
        weather_status_textRL = (RelativeLayout) findViewById(R.id.weather_status_textRL);
        weather_details_notif = (TextView) findViewById(R.id.textView17);
        weather_details_notif2 = (TextView) findViewById(R.id.textView19);
        weather_today_image = (ImageView) findViewById(R.id.imageofweatherInsideToday);
        weatherTodayRL = (RelativeLayout) findViewById(R.id.weatherTodayRL);
        connectionErrorLayout = (RelativeLayout) findViewById(R.id.connerr);
        assistantButton = (ImageButton) findViewById(R.id.assistantButton);
        rotaryAllApps = (AllAppsGridView) findViewById(R.id.rotary_allapps_gridview);

        revealView = (RevealView_DragNdropView) findViewById(R.id.reveal_viewF);
        removeFolderButton = (ImageButton) findViewById(R.id.removeFolderButton);
        folderNameText = (TextView) findViewById(R.id.folderNameText);
        folderNameTextEdit = (EditText) findViewById(R.id.folderNameEditText);
        folderColor =(CircleButton) findViewById(R.id.folderColor);

        removeFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderManager.removeFolder(rotaryView.page,false);
                rotaryView.loadFoldersInRotary();
                rotaryView.invalidate();
            }
        });
       revealView.setBackgroundColor(Color.parseColor("#80000000"));


        rotaryAllApps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!RotaryHome.revealView.isIndragNdrop) {
                    return false;
                }
                else {
                    return RotaryHome.revealView.dispatchTouchEvent(event);
                }
            }
        });


        folderNameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isFolderEditMode) {
                    FolderManager.editFolderName(rotaryView.page, s.toString());
                    Log.e("FolderManager", "Folder Name Updated to : " + s.toString());
                }
            }
        });


        if(getSharedPreferences("SettingsMore", 0).getBoolean("assistant",false)) {
            assistantButton.setVisibility(View.VISIBLE);
        }
        else
        {
            assistantButton.setVisibility(View.GONE);

        }

        connectionStatus =getNetworkClass(RotaryHome.this);


        settingsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newFolderCreationStarted)
                {
                    FolderManager.finalizeFolderCreation();

                }
                else if (isFolderEditMode)
                {
                    deActivateFolderEditMode();
                }
                else if (isInAllApps) {

                    activateUninstallMode();

                } else {
                    Intent intent = new Intent(RotaryHome.this, Settings.class);
                    startActivity(intent);
                }

            }
        });


        rotaryStatus = 0;
        rotaryStatusFont = 0;
        settingsCHK = getSharedPreferences("RotaryCheck", 0);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        DoWorkspace.load(RotaryHome.this, RotaryHome.this, null);
        restoreButtonsTillLoadItLoads();

        weatherRF.setColor(ColorScheme.getBackgroundColor(this));
        notifications_layout.setVisibility(View.GONE);

        try {

            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        Log.e("DO", "Problem setting up In-app Billing: " + result);
                        // is_Setup[0] = false;
                        SharedPreferences.Editor editor = settingsCHK.edit();
                        editor.putBoolean("isPremium", false);
                        editor.putBoolean("isPro", false);
                        editor.commit();
                        RotaryHome.rotaryStatusText = "Can't Connect to Google !";
                        RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);
                        Toast.makeText(RotaryHome.this, "Server connection failed, Premium or Pro Features may not work!", Toast.LENGTH_LONG).show();

                    } else {
                        //Log.d("DO", "onIabSetupFinished " + result.getResponse());
                        //Initiate CHECKING PROCESS
                        if (mHelper != null) mHelper.flagEndAsync();
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                        //RotaryHome.rotaryStatusText = "Welcome back!";
                        RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);


                    }
                }
            });


        }catch(Exception e)
        {
            e.printStackTrace();
            Log.e("Rotary", "There is an error occurred on iAP Helper");
            isGooglePlayServicesMissing = true;
        }




        slidingPan.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {


            }

            @Override
            public void onPanelExpanded(View panel) {

                // Hide status bar
               // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                if (!getSharedPreferences("Rotary_Online", 0).getString("Rotary_tutorial2","no").equals("yes"))
                {
                    if(tutorialHowToOpenFeed != null) {
                        tutorialHowToOpenFeed.hide();
                        tutorialHowToOpenFeed = null;
                    }
                    final ViewTarget target3 = new ViewTarget(R.id.circleButton, RotaryHome.this);

                    new ShowcaseView.Builder(RotaryHome.this)
                        .setTarget(target3)
                        .setContentTitle("Tap to see saved articles,hold to go to top of the timeline!")
                        .setContentText("")
                        .hideOnTouchOutside()
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .setShowcaseEventListener(new OnShowcaseEventListener() {
                            @Override
                            public void onShowcaseViewHide(ShowcaseView showcaseView) {

                            }

                            @Override
                            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                SharedPreferences settings = getSharedPreferences("Rotary_Online", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("Rotary_tutorial2", "yes");
                                editor.commit();
                            }

                            @Override
                            public void onShowcaseViewShow(ShowcaseView showcaseView) {
                                showcaseView.hideButton();

                                tutorialFeed = showcaseView;
                            }

                        }).build();
            }

            }

            @Override
            public void onPanelCollapsed(View panel) {

                // Show status bar
              //  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
        slidingPan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (DoWorkspace.feed.getChildCount() == 0) {
                    DoWorkspace.refreshTheFeed();

                }
                return false;
            }
        });

        wifi.setText(connectionStatus);
        tv.setText("");



        File dir = new File(Environment.getExternalStorageDirectory() + "/RotaryHome");
        if(dir.exists() && dir.isDirectory()) {
            // do something here
        }
        else
        {
            dir.mkdir();
            File output = new File(dir, ".nomedia");
            try {
                output.createNewFile();
            } catch (IOException e) {}
        }

        RotaryView.loadWeather(this, false);
        createWeatherView();
        updateLastUpdatedWeather(this);

        //Tutorial
        if (!getSharedPreferences("Rotary_Online", 0).getString("Rotary_tutorial","no").equals("yes"))
        {
            tutorial=true;
            runFirstSetup();
            final ViewTarget target = new ViewTarget(R.id.hidden, this);
            final ViewTarget target2 = new ViewTarget(R.id.time, this);

            new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setContentTitle("Hello, this is Rotary!\nTap & Hold Center and drag onto pages to change current page.")
                    .setContentText("")
                    .hideOnTouchOutside()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            new ShowcaseView.Builder(RotaryHome.this)
                                    .setTarget(target2)
                                    .setContentTitle("Swipe it up to see your timeline,swipe it down to see rotary!")
                                    .setContentText("")
                                    .hideOnTouchOutside()
                                    .setStyle(R.style.CustomShowcaseTheme2)
                                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                                        @Override
                                        public void onShowcaseViewHide(ShowcaseView showcaseView) {

                                        }

                                        @Override
                                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                            SharedPreferences settings = getSharedPreferences("Rotary_Online", 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("Rotary_tutorial", "yes");
                                            editor.commit();
                                        }

                                        @Override
                                        public void onShowcaseViewShow(ShowcaseView showcaseView) {
                                            showcaseView.hideButton();
                                            tutorialHowToOpenFeed = showcaseView;
                                        }

                                    }).build();

                        }

                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {
                            showcaseView.hideButton();
                            tutorialRotaryAppChooser = showcaseView;

                        }

                    }).build();


        }


    }

    private void activateUninstallMode() {

        isUninstallMode = true;
        RotaryHome.settingsb.setVisibility(View.INVISIBLE);
        RotaryHome.rotarystatusTV.setText("Choose to Uninstall");


    }

    private void DeactivateUninstallMode() {

        RotaryHome.rotarystatusTV.setText("All Apps");
        RotaryHome.settingsb.setVisibility(View.VISIBLE);

        isUninstallMode = false;


    }


    public static void activateFolderEditMode() {
        isFolderEditMode= true;
        folderNameText.setVisibility(View.GONE);
        folderNameTextEdit.setVisibility(View.VISIBLE);
        folderNameTextEdit.setText(folderNameText.getText());
        removeFolderButton.setVisibility(View.VISIBLE);
        removeFolderButton.getLayoutParams().height = removeFolderIconSizeShouldbe;
        removeFolderButton.getLayoutParams().width = removeFolderIconSizeShouldbe;
        notificationsb.setVisibility(View.INVISIBLE);
        if (RotaryHome.isDarkTheme)
        {
            settingsb.setImageResource(R.drawable.tickb_white);
        }
        else
        {
            settingsb.setImageResource(R.drawable.tickb);

        }
        folderColor.setVisibility(View.VISIBLE);
        folderColor.setColor(Color.WHITE);
        RotaryHome.rotarystatusTV.setText("Edit Apps");

    }

    public static void deActivateFolderEditMode() {
        isFolderEditMode= false;
        folderNameTextEdit.setVisibility(View.GONE);
        folderColor.setVisibility(View.GONE);
        folderNameText.setVisibility(View.VISIBLE);
        notificationsb.setVisibility(View.VISIBLE);
        if (RotaryHome.isDarkTheme)
        {
            settingsb.setImageResource(R.drawable.settingsb_white);
        }
        else
        {
            settingsb.setImageResource(R.drawable.settingsb);

        }
        removeFolderButton.setVisibility(View.GONE);
        rotaryView.holdingpage =false;
        rotaryView.invalidate();
        folderNameText.setText(folderNameTextEdit.getText());
        RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);

    }

    public void runFirstSetup()
    {
       // WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
      //  try {
      //      myWallpaperManager.suggestDesiredDimensions(1080,1980);
      //      myWallpaperManager.setResource(+ R.drawable.rotary_default_wallpaper);

      //  } catch (IOException e) {
      //      // TODO Auto-generated catch block
      //      e.printStackTrace();
      //  }
        final SharedPreferences settings =getSharedPreferences("DO_StatusBarColors", 0);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("DO_StatusBarColor","#FF0D98FD");
        editor.putString("DO_StatusBarTextColor", "#FFFFFFFF");
        editor.commit();


    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public static boolean isColorDark(int color){
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
    /* Called when the application resumes */
    @Override
    protected void onResume()
    {
        super.onResume();

        if (isInAllApps)
        {
            hideAllApps(true,-99);

        }

        if (rotaryStatusText.startsWith("Connection Failed"))
        {
            RotaryView.loadWeather(this, false);
            createWeatherView();
            updateLastUpdatedWeather(this);
        }


//if (slidingPan.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
//{
 //   slidingPan.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//}

        if(getSharedPreferences("rotary", 0).getBoolean("iNeedRefresh",true))
        {
            rotaryView.refreshApps(this);
        }
        else
        {
            if (isUninstallMode)
            {
                DeactivateUninstallMode();
            }

            if (isInAllApps)
            {
                hideAllApps(true,-99);
            }
        }
        //F
        if (isFolderEditMode)
        {
            deActivateFolderEditMode();
        }


if (isFeedChangedBySettings == true)
{
    DoWorkspace.refreshTheFeed();
    isFeedChangedBySettings =false;
}



        if (rotaryStatus == 1)
        {

            DoWorkspace.reloadVariables();

            if(getSharedPreferences("SettingsMore", 0).getBoolean("assistant",false) ) {
                assistantButton.setVisibility(View.VISIBLE);
            }
            else
            {
                assistantButton.setVisibility(View.GONE);

            }
            weatherRF.setColor(ColorScheme.getBackgroundColor(this));
            rotaryStatus =0;
        }

        if (rotaryStatusFont == 1)
        {

            onCreate(null);
            rotaryStatusFont =0;
        }

        DoWorkspace.reloadOfflineView();

        if (isNotificationsOpened)
        {
            setNotifications(null);
        }


        rotaryView.refresh();

        if(shouldiUpdateWeather(this))
        {
            RotaryView.loadWeather(this, false);
            createWeatherView();
            updateLastUpdatedWeather(this);
        }
        if(isWeatherChanged) {
            RotaryView.loadWeather(this,false);
            createWeatherView();
            updateLastUpdatedWeather(this);

        }


        registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info==null || !info.isConnected())
            return "-"; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }



    public void changeAngle(View v)
    {
       // RotaryView.x = RotaryView.x - 10;
       // rotaryView.invalidate();
    }
    @Override
    public void onBackPressed() {

        if (newFolderCreationStarted)
        {
            FolderManager.cancelFolderCreation();
        }
        else if (isInAllApps && isUninstallMode)
        {
            DeactivateUninstallMode();
        }
        else if (isInAllApps && !newFolderCreationStarted)
        {
            hideAllApps(true,-99);
        }
        else if (isFolderEditMode)
        {
            deActivateFolderEditMode();
        }

        // do nothing.
        if(slidingPan.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
        {
            slidingPan.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        if (notifications_layout.getVisibility() == View.VISIBLE)
        {
            setNotifications(null);
        }
    }
    public void restoreButtonsTillLoadItLoads()
    {

        if (getSharedPreferences("colors", 0).getBoolean("isDarkTheme", true)) {
            settingsb.setImageResource(R.drawable.settingsb_white);

            if (isNotificationsOpened == false)
            {
                notificationsb.setImageResource(R.drawable.notificationsb_white);


            }
            else
            {

                notificationsb.setImageResource(R.drawable.closenotificationsb_white);


            }
            RotaryHome.rotarystatusTV.setTextColor(Color.WHITE);
            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.BLACK);
            RotaryHome.folderNameText.setTextColor(Color.WHITE);
            RotaryHome.folderNameText.setShadowLayer(3, 2, 1, Color.BLACK);

            wifi.setTextColor(Color.WHITE);
            wifi.setShadowLayer(3, 2, 1, Color.BLACK);
            tv.setTextColor(Color.WHITE);
            tv.setShadowLayer(3, 2, 1, Color.BLACK);
            DoWorkspace.clock.setTextColor(Color.WHITE);
            DoWorkspace.clock.setShadowLayer(3, 2, 1, Color.BLACK);
            assistantButton.setImageResource(R.drawable.assistantlight);

        }
        else
        {
            settingsb.setImageResource(R.drawable.settingsb);
            if (isNotificationsOpened == false)
            {
                notificationsb.setImageResource(R.drawable.notificationsb);


            }
            else
            {

                notificationsb.setImageResource(R.drawable.closenotificationsb);




            }
            RotaryHome.rotarystatusTV.setTextColor(Color.BLACK);
            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.WHITE);
            wifi.setTextColor(Color.BLACK);
            wifi.setShadowLayer(3, 2, 1, Color.WHITE);
            tv.setTextColor(Color.BLACK);
            RotaryHome.folderNameText.setTextColor(Color.BLACK);
            RotaryHome.folderNameText.setShadowLayer(3, 2, 1, Color.WHITE);
            tv.setShadowLayer(3, 2, 1, Color.WHITE);
            DoWorkspace.clock.setTextColor(Color.BLACK);
            DoWorkspace.clock.setShadowLayer(3, 2, 1, Color.WHITE);
            assistantButton.setImageResource(R.drawable.assistantdark);

        }


    }
    public static void loadCorrectButtons()
    {

        if (isDarkTheme ==true)
        {
            settingsb.setImageResource(R.drawable.settingsb_white);

            if (isNotificationsOpened == false)
            {
                notificationsb.setImageResource(R.drawable.notificationsb_white);


            }
            else
            {

                notificationsb.setImageResource(R.drawable.closenotificationsb_white);


            }
            RotaryHome.rotarystatusTV.setTextColor(Color.WHITE);
            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.BLACK);
            wifi.setTextColor(Color.WHITE);
            wifi.setShadowLayer(3, 2, 1, Color.BLACK);
            RotaryHome.folderNameText.setTextColor(Color.WHITE);
            RotaryHome.folderNameText.setShadowLayer(3, 2, 1, Color.BLACK);
            tv.setTextColor(Color.WHITE);
            tv.setShadowLayer(3, 2, 1, Color.BLACK);
            DoWorkspace.clock.setTextColor(Color.WHITE);
            DoWorkspace.clock.setShadowLayer(3, 2, 1, Color.BLACK);
            assistantButton.setImageResource(R.drawable.assistantlight);

        }
        else
        {
            settingsb.setImageResource(R.drawable.settingsb);
            if (isNotificationsOpened == false)
            {
                notificationsb.setImageResource(R.drawable.notificationsb);


            }
            else
            {

                notificationsb.setImageResource(R.drawable.closenotificationsb);




            }
            RotaryHome.rotarystatusTV.setTextColor(Color.BLACK);
            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.WHITE);
            wifi.setTextColor(Color.BLACK);
            wifi.setShadowLayer(3, 2, 1, Color.WHITE);
            RotaryHome.folderNameText.setTextColor(Color.BLACK);
            RotaryHome.folderNameText.setShadowLayer(3, 2, 1, Color.WHITE);
            tv.setTextColor(Color.BLACK);
            tv.setShadowLayer(3, 2, 1, Color.WHITE);
            DoWorkspace.clock.setTextColor(Color.BLACK);
            DoWorkspace.clock.setShadowLayer(3, 2, 1, Color.WHITE);
            assistantButton.setImageResource(R.drawable.assistantdark);


        }


    }
    public static void showAllApps(boolean b,int fromX,int fromY) {
        int from_X;
        int from_Y;
        if (b)
        {
         from_X = fromX;
            from_Y = fromY;
        }
        else
        {
            from_X = deviceScreenWidth / 2;
            from_Y = deviceScreenHeight / 2;
        }

        DoWorkspace.allAppsButton.setImageResource(R.drawable.uninstallb_white);
        RotaryHome.notificationsb.setVisibility(View.INVISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Animator anim = ViewAnimationUtils.createCircularReveal(
                    RotaryHome.revealView, from_X, from_Y, 0, RotaryHome.deviceScreenHeight);

            anim.setDuration(400);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    RotaryHome.isInAllApps = true;

                    RotaryHome.revealView.setVisibility(View.VISIBLE);
                    RotaryHome.rotarystatusTV.setText("All Apps");
                    //    RotaryHome.notificationsb.setImageResource(R.drawable.backb_white);
                        RotaryHome.settingsb.setImageResource(R.drawable.uninstallb_white);
                    RotaryHome.rotarystatusTV.setTextColor(Color.WHITE);
                    RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.BLACK);



                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    Animation a2 = DoWorkspace.fadeInDO(400);
                    a2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            RotaryHome.rotaryAllApps.setVisibility(View.VISIBLE);
                            RotaryHome.folderNameText.setVisibility(View.GONE);
                            RotaryHome.rotaryView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    RotaryHome.rotaryAllApps.startAnimation(a2);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        } else {
            Animation aa = DoWorkspace.fadeInDO(300);
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    RotaryHome.revealView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    Animation a2 = DoWorkspace.fadeInDO(200);
                    a2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            RotaryHome.rotaryAllApps.setVisibility(View.VISIBLE);
                            RotaryHome.folderNameText.setVisibility(View.GONE);
                            RotaryHome.rotaryView.setVisibility(View.GONE);
                            RotaryHome.rotarystatusTV.setText("All Apps");
                        //    RotaryHome.notificationsb.setImageResource(R.drawable.backb_white);
                            RotaryHome.settingsb.setImageResource(R.drawable.uninstallb_white);
                            RotaryHome.rotarystatusTV.setTextColor(Color.WHITE);
                            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.BLACK);


                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    RotaryHome.rotaryAllApps.startAnimation(a2);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            RotaryHome.revealView.startAnimation(aa);


        }
    }
    public static void hideAllApps(boolean doAnimation,int andGotoPage)
    {
        rotarystatusTV.setText(RotaryHome.rotaryStatusText);
        DoWorkspace.allAppsButton.setImageResource(R.drawable.all_apps_icon);
        RotaryHome.notificationsb.setVisibility(View.VISIBLE);

        if (isDarkTheme)
        {
           // notificationsb.setImageResource(R.drawable.notificationsb_white);
            settingsb.setImageResource(R.drawable.settingsb_white);
            RotaryHome.rotarystatusTV.setTextColor(Color.WHITE);
            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.BLACK);



        }
        else
        {
           // notificationsb.setImageResource(R.drawable.notificationsb);
            settingsb.setImageResource(R.drawable.settingsb);
            RotaryHome.rotarystatusTV.setTextColor(Color.BLACK);
            RotaryHome.rotarystatusTV.setShadowLayer(3, 2, 1, Color.WHITE);




        }
        if (doAnimation) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animation a2 = DoWorkspace.fadeOutDO(200);
                a2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isInAllApps = false;


                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        RotaryHome.rotaryAllApps.setVisibility(View.GONE);

                        Animator anim = ViewAnimationUtils.createCircularReveal(
                                revealView, deviceScreenWidth / 2, deviceScreenHeight / 2, deviceScreenHeight, 0);

                        anim.setDuration(400);

                        // make the view invisible when the animation is done
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);



                            }
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                revealView.setVisibility(View.GONE);

                                Animation a3 = DoWorkspace.fadeInDO(400);
                                a3.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        RotaryHome.rotaryView.setVisibility(View.VISIBLE);
                                    }

                                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        Animation a2 = DoWorkspace.fadeInDO(200);
                                        a2.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {

                                                RotaryHome.folderNameText.setVisibility(View.VISIBLE);                                            }

                                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                RotaryHome.folderNameText.setVisibility(View.VISIBLE);

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        RotaryHome.folderNameText.startAnimation(a2);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                RotaryHome.rotaryView.startAnimation(a3);


                            }
                        });

                        anim.start();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                RotaryHome.rotaryAllApps.startAnimation(a2);


            } else {
                Animation a2 = DoWorkspace.fadeInDO(200);
                a2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        RotaryHome.rotaryAllApps.setVisibility(View.GONE);
                        Animation aa = DoWorkspace.fadeOutDO(300);
                        aa.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                RotaryHome.rotaryView.setVisibility(View.VISIBLE);
                                RotaryHome.folderNameText.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                revealView.setVisibility(View.GONE);
                                isInAllApps = false;


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        RotaryHome.revealView.startAnimation(aa);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                RotaryHome.rotaryAllApps.startAnimation(a2);

            }
        }
        else
        {
            revealView.setVisibility(View.GONE);
            RotaryHome.rotaryAllApps.setVisibility(View.GONE);
            RotaryHome.rotaryView.setVisibility(View.VISIBLE);
            RotaryHome.folderNameText.setVisibility(View.VISIBLE);

            isInAllApps = false;
        }
        if (andGotoPage != -99)
        {
            rotaryView.page = andGotoPage;
        }
        rotaryView.invalidate();
    }
    public void refreshWeather(View v)
    {
        RotaryView.loadWeather(RotaryHome.this,true);
        createWeatherView();
        setAnimWeatherView(this);
        updateLastUpdatedWeather(this);

    }

    public static void setAnimWeatherView(Context ctx)
    {
        SharedPreferences settings56472 = ctx.getSharedPreferences("AnimationsDO", 0);
        String enabledanimations = settings56472.getString("AnimationsDO", "enabled");
        if(enabledanimations.equals("enabled"))
        {

            weatherTodayRL.setVisibility(View.INVISIBLE);
            slideUpRLno12.setVisibility(View.INVISIBLE);
            slideUpRLno2.setVisibility(View.INVISIBLE);
            final Animation anim1= AnimationUtils.loadAnimation(ctx, R.anim.zoom);
            final Animation anim2= AnimationUtils.loadAnimation(ctx, R.anim.zoom);
            final Animation anim3= AnimationUtils.loadAnimation(ctx, R.anim.zoom);

            weatherTodayRL.setVisibility(View.VISIBLE);
            weatherTodayRL.startAnimation(anim1);

            anim1.setAnimationListener(new Animation.AnimationListener() {
                int animed = 0;
                int animed2 = 0;

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub


                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animed != 1) {
                        slideUpRLno12.setVisibility(View.VISIBLE);
                        slideUpRLno12.startAnimation(anim2);


                        animed = 1;
                    }
                    // pass the intent to switch to other activity

                }
            });

            anim2.setAnimationListener(new Animation.AnimationListener() {
                int animed = 0;
                int animed2 = 0;

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub


                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animed != 1) {
                        slideUpRLno2.setVisibility(View.VISIBLE);
                        slideUpRLno2.startAnimation(anim3);


                        animed = 1;
                    }
                    // pass the intent to switch to other activity

                }
            });

        }
        else
        {
            slideUpRLno2.setVisibility(View.VISIBLE);
            slideUpRLno12.setVisibility(View.VISIBLE);
        }
    }

    public void closeNotifications(View v)
    {
        rotaryView.setVisibility(View.VISIBLE);
        notifications_layout.setVisibility(View.GONE);
    }
    public void setNotifications(View v)
    {


        if (newFolderCreationStarted)
        {
            FolderManager.cancelFolderCreation();
        }
        else if (isInAllApps && isUninstallMode)
        {
            DeactivateUninstallMode();
        }
        else if (isInAllApps && !newFolderCreationStarted)
        {

            //hideAllApps(true,-99);

        }
        else if (isFolderEditMode)
        {//Incase of bug
            deActivateFolderEditMode();
        }
        else {
            if (isNotificationsOpened == false) {

                Animation a = new AlphaAnimation(1.0f, 0.0f);
                a.setDuration(100);
                a.setFillAfter(true);
                rotaryView.startAnimation(a);
                folderNameText.setVisibility(View.GONE);



                a.setAnimationListener(new Animation.AnimationListener() {

                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                        Animation a2 = DoWorkspace.fadeInDO(100);
                        a2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        notifications_layout.startAnimation(a2);

                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    public void onAnimationEnd(Animation animation) {
                        Animation ab = new AlphaAnimation(0.0f, 1.0f);
                        ab.setDuration(100);
                        ab.setFillAfter(true);
                        notifications_layout.startAnimation(ab);
                        ab.setAnimationListener(new Animation.AnimationListener() {

                            public void onAnimationStart(Animation animation) {
                                // TODO Auto-generated method stub
                                isNotificationsOpened = true;

                                if (isDarkTheme == true) {
                                    notificationsb.setImageResource(R.drawable.closenotificationsb_white);

                                } else {
                                    notificationsb.setImageResource(R.drawable.closenotificationsb);

                                }

                            }

                            public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            public void onAnimationEnd(Animation animation) {
                                notifications_layout.clearAnimation();
                                rotaryView.clearAnimation();
                                notifications_layout.setVisibility(View.VISIBLE);
                                rotaryView.setVisibility(View.GONE);
                                setAnimWeatherView(RotaryHome.this);


                            }
                        });


                    }
                });


            } else {


                Animation a = new AlphaAnimation(1.0f, 0.0f);
                a.setDuration(100);
                a.setFillAfter(true);
                notifications_layout.startAnimation(a);


                a.setAnimationListener(new Animation.AnimationListener() {

                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    public void onAnimationEnd(Animation animation) {

                        Animation ab = new AlphaAnimation(0.0f, 1.0f);
                        ab.setDuration(100);
                        ab.setFillAfter(true);
                        rotaryView.startAnimation(ab);
                        ab.setAnimationListener(new Animation.AnimationListener() {

                            public void onAnimationStart(Animation animation) {

                                // TODO Auto-generated method stub
                                isNotificationsOpened = false;

                                if (isDarkTheme == true) {
                                    notificationsb.setImageResource(R.drawable.notificationsb_white);

                                } else {
                                    notificationsb.setImageResource(R.drawable.notificationsb);

                                }

                            }

                            public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            public void onAnimationEnd(Animation animation) {
                                notifications_layout.clearAnimation();
                                rotaryView.clearAnimation();
                                weatherTodayRL.clearAnimation();
                                slideUpRLno12.clearAnimation();
                                slideUpRLno2.clearAnimation();
                                folderNameText.setVisibility(View.VISIBLE);

                                notifications_layout.setVisibility(View.GONE);
                                weatherTodayRL.setVisibility(View.INVISIBLE);
                                slideUpRLno12.setVisibility(View.INVISIBLE);
                                slideUpRLno2.setVisibility(View.INVISIBLE);
                                rotaryView.setVisibility(View.VISIBLE);


                            }
                        });

                    }
                });
                //notifications_layout.setVisibility(View.GONE);

            }

        }
    }



    public void fixNotifications()
    {
        if (isNotificationsOpened == false)
        {
            if (isDarkTheme ==true)
            {
                notificationsb.setImageResource(R.drawable.closenotificationsb_white);

            }
            else
            {
                notificationsb.setImageResource(R.drawable.closenotificationsb);

            }

        }
        else
        {

            if (isDarkTheme ==true)
            {
                notificationsb.setImageResource(R.drawable.notificationsb_white);

            }
            else
            {
                notificationsb.setImageResource(R.drawable.notificationsb);

            }

        }


    }





public class MyScaleGestures implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;


    public MyScaleGestures(Context c){
        gestureScale = new ScaleGestureDetector(c, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        gestureScale.onTouchEvent(event);
        return true;
    }

    float startScale;
    float endScale;
    float scale;
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.i("onScale", "onScale");
        scale *= detector.getScaleFactor();
        scale = Math.max(0.1f, Math.min(scale, 5.0f));
        if (scale > 0.1f) {
            Log.i("onScaleEnd", "Pinch Dection");
            // onPinch();
        } else if (scale > 2.5f) {
            Log.i("onScaleEnd", "Zoom Dection");
            // onZoom();
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        startScale *= detector.getScaleFactor();
        Log.i("onScaleBegin", "begin : "+startScale );

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        endScale *= detector.getScaleFactor();
        Log.i("onScaleEnd", "end: " + endScale);

      //  if ((startScale - endScale) > 0.0100
         //       || (endScale - startScale) > 0.0100) {

            if (startScale > endScale) {
                Log.i("onScaleEnd", "Pinch Dection");
                // onPinch();
            } else if (startScale < endScale) {
                Log.i("onScaleEnd", "Zoom Dection");
                // onZoom();
            }

       // }
    }
}




    /////////////
    ////////PURCHASES
    //////////////
    //////////////
    ///////////
    /////////////
    /////////////
    ///////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    // Get already purchased response
    private static IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            boolean mIsPremium = false;
            boolean mIsPro = false;
            String ITEM_SKU_buy_pro = "basic_social_networks_bundle";

            String ITEM_SKU_upgrade_to_premium = "more_social_bundle";

            String ITEM_SKU3_buy_premium = "rotary_premium";

            if (result.isFailure()) {
                // handle error here
                Log.e("DO", "Error checking inventory: "+result);
                mIsPremium = false;
                mIsPro = false;
                SharedPreferences.Editor editor = settingsCHK.edit();
                editor.putBoolean("isPremium", mIsPremium);
                editor.putBoolean("isPro", mIsPro);
                editor.commit();
            }
            else {
                // does the user have the premium upgrade?
                //mIsPremium = inventory.hasPurchase(ITEM_SKU3_buy_premium);



                if(inventory.hasPurchase(ITEM_SKU3_buy_premium) == true || inventory.hasPurchase(ITEM_SKU_upgrade_to_premium) == true)
                {
                    //Premium is True
                    mIsPremium = true;


                    SharedPreferences.Editor editor = settingsCHK.edit();
                    editor.putBoolean("isPremium", mIsPremium);
                    editor.commit();

            Toast.makeText(ctx, "Premium Account has been enabled\nThank you for your support!", Toast.LENGTH_LONG).show();

                }
                else if (inventory.hasPurchase(ITEM_SKU_buy_pro) == true )
                {

                    mIsPro = true;

                    SharedPreferences.Editor editor = settingsCHK.edit();
                    editor.putBoolean("isPro", mIsPro);
                    editor.commit();
    Toast.makeText(ctx, "Pro Account has been enabled\nThank you for your support!", Toast.LENGTH_LONG).show();


                }
                else
                {
                    mIsPremium = false;
                    mIsPro = false;
                    SharedPreferences.Editor editor = settingsCHK.edit();
                    editor.putBoolean("isPremium", mIsPremium);
                    editor.putBoolean("isPro", mIsPro);
                    editor.commit();

                }
                Log.d("DO","purchase_status iab: "+"isPro ="+mIsPro + "--isPremium =" + mIsPremium );

            }

            if (amiDogaOzkaraca)
            {
                mIsPremium = true;
                mIsPro = true;
                SharedPreferences.Editor editor = settingsCHK.edit();
                editor.putBoolean("isPremium", mIsPremium);
                editor.putBoolean("isPro", mIsPro);
                editor.commit();
            }
        }
    };


    public static boolean rotaryIsPro(Context ctx)
    {

        SharedPreferences settingsCH = ctx.getSharedPreferences("RotaryCheck", 0);
        return  settingsCH.getBoolean("isPro", false);
    }

    public static boolean rotaryIsPremium(Context ctx)
    {
        SharedPreferences settingsCH = ctx.getSharedPreferences("RotaryCheck", 0);
        return settingsCH.getBoolean("isPremium", false);
    }

    public static boolean isAdFree(Context ctx)
    {
        boolean isAdFree = false;

        SharedPreferences settingsCH = ctx.getSharedPreferences("RotaryCheck", 0);
        if( settingsCH.getBoolean("isPremium", false) == true || settingsCH.getBoolean("isPro", false) == true) {
            isAdFree = true;
        }
        return isAdFree;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {        super.onActivityResult(requestCode, resultCode, data);

       Log.d("DO", "onActivityResult(" + requestCode + "," + resultCode + ","+ data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d("DO", "onActivityResult handled by IABUtil.");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;

        try {
            unregisterReceiver(this.mBatInfoReceiver);
        }
        catch(Exception e)
        {}

    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();

        try {
            unregisterReceiver(this.mBatInfoReceiver);
        }
        catch(Exception e)
        {}
    }


    public void createWeatherView()
    {


        //Get Weather
        new DoCenter_Weather(weather_details_notif,weather_details_notif2,weather_today_image,weather_status_textRL,weatherRL,errorDetails,RotaryHome.this,DoCenterWeather_TomorrowWeather_TextView,DoCenterWeather_NextDayWeather_TextView,imageofweatherInside,imageofweatherInside2,weathertextDAY,weathertextDAY2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
    public static boolean minutesDiff(Date earlierDate, Date laterDate,int isDifferenceBiggerThan)
    {
    

        if ((int)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000)) > isDifferenceBiggerThan)
        {
           return true;

        }
        else
        {
            return false;
        }
    }

    public static Date GetItemDate(final String date)
    {
        final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setCalendar(cal);

        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public void updateLastUpdatedWeather(Context ctx)
    {
        final SharedPreferences settings = ctx.getSharedPreferences("weather_update", 0);

        SharedPreferences.Editor editor = settings.edit();
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String newDateStr = formatter.format(now);
        editor.putString("lastupdated", newDateStr).commit();

    }
public static boolean shouldiUpdateWeather(Context ctx)
    {
        Date now = new Date();

        final SharedPreferences settings = ctx.getSharedPreferences("weather_update", 0);

        Date lastUpdated = GetItemDate(settings.getString("lastupdated", "2015-06-25 14:06"));


        if(settings.getString("time","manual").equals("every15"))
        {
            if(minutesDiff(now,lastUpdated,15))
            {

                return true;
            }
        }
        else if (settings.getString("time","manual").equals("every30"))
        {
            if(minutesDiff(now,lastUpdated,30))
            {
                return true;
            }
        }
        else if (settings.getString("time","manual").equals("everyHour"))
        {
            if(minutesDiff(now,lastUpdated,60))
            {
                return true;
            }
        }
        else if (settings.getString("time","manual").equals("fourTimes"))
        {
            if(minutesDiff(now,lastUpdated,360))
            {
                return true;
            }
        }
        else if (settings.getString("time","manual").equals("twoTimes"))
        {
            if(minutesDiff(now,lastUpdated,720))
            {
                return true;
            }
        }
        else if (settings.getString("time","manual").equals("everyDay"))
        {
            if(minutesDiff(now,lastUpdated,1440))
            {
                return true;
            }
        }
        else if (settings.getString("time","manual").equals("manual"))
        {
            return false;
        }


            return false;



    }

    public void launchAssistant(View v)
    {
        Intent intent = new Intent(Intent.ACTION_ASSIST);
        startActivity(intent);
    }

}
