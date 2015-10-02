package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by doga.ozkaraca on 4/4/2015.
 */

public class NewsReader extends ActionBarActivity {
    String stringfromintent;
    static String headlineArticle;
    static String articleText;
    static String postImage;
    static String postURL;
    static String postContent;
    static String postShortContent;
    ProgressBar progressBar;
    static Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_news_reader);
          ctx = this;

        ActionBar ac = getSupportActionBar();

        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            stringfromintent = extras.getString("urlofnews");
            headlineArticle = extras.getString("title");
            ac.setTitle(headlineArticle);
            postContent = extras.getString("content");
            if (extras.getString("imageofnews") != null)
            {
                postImage = extras.getString("imageofnews");
            }
            else {
                Random rand = new Random();

                int  n = rand.nextInt(10) + 1;

                postImage = "http://rotary.dogaozkaraca.com/default_feed_images/rotary_default_"+n+".jpg";
            }
            this.setTitle(headlineArticle);
            postURL = stringfromintent;
            Log.d("DO","Successfully sent extraintent "+stringfromintent);
        }
        else
        {
            Log.e("DO","intent lost");
        }




progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
       // int color = Color.parseColor("#000000");
       // int color2 = Color.parseColor("#0000FF");
       // progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
       // progressBar.getProgressDrawable().setColorFilter(color2, PorterDuff.Mode.SRC_IN);
        WebView webView = (WebView) findViewById(R.id.webView1);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomChromeViewClient());
        //webView.loadData(postContent, "text/html", "UTF-8");
        webView.loadUrl(stringfromintent);




        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;



    }

    //web view client implementation
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            NewsReader.this.progressBar.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            //do whatever you want with the url that is clicked inside the webview.
            //for example tell the webview to load that url.
            view.loadUrl(url);
            //return true if this method handled the link event
            //or false otherwise
            return true;
        }
    }
    private class CustomChromeViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            NewsReader.this.progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }



    public void shareArticle()
    {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, headlineArticle + ", " + stringfromintent);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share..."));



    }

    public static boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("Rotary_RSS_saved", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("Rotary_RSS_saved", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public static void saveArticle()
    {
        Log.w("myfeed", "--OFFLINE ARTICLE SAVE--");

        /////////////////////TITLE////////////////////////////
        //Load
        String[] titleList = loadArray("title", ctx);


        List<String> titles =new LinkedList<String>(Arrays.asList(titleList));
        //Do the things
        titles.add(headlineArticle);

        Log.w("myfeed", "added title : " + headlineArticle);
        //Convertback
        String[] simpleArray = new String[titles.size()];
        titles.toArray(simpleArray);
        //Save
        saveArray(simpleArray, "title",ctx);

        /////////////////////Article////////////////////////////
        //Load
        String[] ArticleList = loadArray("article", ctx);


        List<String> Articles =new LinkedList<String>(Arrays.asList(ArticleList));
        //Do the things
        Articles.add(headlineArticle);

        Log.w("myfeed", "added article text: " + headlineArticle);
        //Convertback
        String[] simpleArray3 = new String[Articles.size()];
        Articles.toArray(simpleArray3);
        //Save
        saveArray(simpleArray3, "article", ctx);

        ////////POST IMAGE

        //Load
        String[] imageurlList = loadArray("postimage",ctx);


        List<String> imageurls = new LinkedList<String>(Arrays.asList(imageurlList));
        //Do the things

        Log.w("myfeed", "added postimage : " + postImage);
        imageurls.add(postImage);

        //Convertback
        String[] simpleArray4 = new String[imageurls.size()];
        imageurls.toArray(simpleArray4);
        //Save
        saveArray(simpleArray4, "postimage", ctx);

        ////////POST URL

        //Load
        String[] urlList = loadArray("url",ctx);


        List<String> urls = new LinkedList<String>(Arrays.asList(urlList));
        //Do the things

        Log.w("myfeed", "added url : " + postURL);
        urls.add(postURL);

        //Convertback
        String[] simpleArray2 = new String[urls.size()];
        urls.toArray(simpleArray2);
        //Save
        saveArray(simpleArray2, "url", ctx);

        /////////////////////////

        DoWorkspace.reloadOfflineView();

        Toast.makeText(ctx,"Article succesfully saved for offline reading!",Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_share:
                shareArticle();
                return true;
            case R.id.action_save:
                saveArticle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
