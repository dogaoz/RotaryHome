package com.dogaozkaraca.rotaryhome;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dogaozkaraca.SwipeRefreshLayoutRotary;


public class rss_feed_settings extends ActionBarActivity {
    GridView lv;
    List<CountryItem> lst;
    static ListView LA;
    static Context ctx;
    AsyncTask<Void, Void, Void> search;
    ArrayList[] lst5;
    ListView lv5;
    RelativeLayout searchRL;
    TextView tvSearchStatus;
    Button sendRequestToUs;
    SearchView searchView;
    WebView blurCommWebView;
    SwipeRefreshLayoutRotary mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed_settings);
        ActionBar ac = getSupportActionBar();
        ctx = this;
        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
        lv = (GridView) findViewById(R.id.GridView2);

        TextView tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setTextColor(ColorScheme.getActionBarColor(this));
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setTextColor(ColorScheme.getActionBarColor(this));
       // SpannableString s = new SpannableString(getSupportActionBar().getTitle());
       // s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        ac.setDisplayHomeAsUpEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
        }
        lv5 = (ListView) findViewById(R.id.listViewSearcher);
        lst5 = new ArrayList[1];
        searchRL = (RelativeLayout) findViewById(R.id.noresultsfound);


        tvSearchStatus = (TextView) findViewById(R.id.noresultsfound_text);
        sendRequestToUs = (Button) findViewById(R.id.noresultsfound_button);
        sendRequestToUs.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        sendRequestToUs.setTextColor(ColorScheme.getTextColor(this));
        blurCommWebView = (WebView) findViewById(R.id.webView1);
        mSwipeRefreshLayout = (SwipeRefreshLayoutRotary) findViewById(R.id.swipe_search_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutRotary.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        blurCommWebView.getSettings().setJavaScriptEnabled(true);
        blurCommWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("http://rotaryhome.com/?cancelled")) {

                    blurCommWebView.stopLoading();
                    blurCommWebView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);

                } else if (url.equals("http://rotaryhome.com/?done")) {
                    blurCommWebView.stopLoading();
                    blurCommWebView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);


                } else {
                    view.loadUrl(url);
                }

                return true;
            }
        });

        sendRequestToUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                searchView.setIconified(true);
                searchView.setIconified(true);
                mSwipeRefreshLayout.setRefreshing(false);
                lv5.setVisibility(View.GONE);
                blurCommWebView.setVisibility(View.VISIBLE);
                blurCommWebView.loadUrl("http://rotary.dogaozkaraca.com/news_source_request.php");
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);


            }
        });


      //  getSupportActionBar().setTitle(s);
        lst = new ArrayList<CountryItem>();




        new AsyncTask<String,String, String[]>(){

            LoadingDialog dialog = new LoadingDialog(rss_feed_settings.this);
            protected void onPreExecute() {
                this.dialog.setMessage("Please Wait...");
                this.dialog.setCancelable(false);
                try {

                    this.dialog.show();
                }
                catch(Exception e){}
            }
            @Override
            protected String[] doInBackground(String... params) {
                // TODO: attempt authentication against a network service.


                try
                {

                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI("http://rotary.dogaozkaraca.com/getCountryList.php"));

                    HttpResponse response = client.execute(request);
                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));

                    String aa = in.readLine();
                    if (aa == null) {
                        return null;
                    }
                    String[] parts = aa.split("SPACE");

                    return parts;
                }
                catch (Exception er)
                {

                }


                return new String[]{"Error"};
            }

            @Override
            protected void onPostExecute(final String[] result) {


                if(!result[0].equals("Error"))
                {

                    for (int x2=0; x2<result.length;x2++)
                    {
                        lst.add(new CountryItem(result[x2],rss_feed_settings.this));
                    }


                }
                lv.setAdapter(new CountryAdaptor(rss_feed_settings.this, lst));
                this.dialog.hide();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);






    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rss_feed_settings, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lv5.setVisibility(View.GONE);
                searchRL.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);

                return false;
            }
        });

searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        queryOnServer(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
            queryOnServer(newText);
        return true;
    }
});


        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (!searchView.isIconified())
                {
                    searchRL.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);

                    lv5.setVisibility(View.GONE);

                    searchView.setIconified(true);
                    mSwipeRefreshLayout.setRefreshing(false);

                }
                else
                {
                    if(blurCommWebView.getVisibility() == View.VISIBLE)
                    {
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        blurCommWebView.setVisibility(View.GONE);
                    }
                    else {
                        finish();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void queryOnServer(final String query) {

        if (search != null)
            search.cancel(true);

        search = new blurSearch(query);
        search.execute();
    }



    public class blurSearch extends AsyncTask<Void, Void, Void> {
        String query;
        public blurSearch(String query) {
            super();
            this.query = query;
        }

        String[] result;
        protected void onPreExecute() {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);

            mSwipeRefreshLayout.setRefreshing(true);

            lv5.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                Log.w("RO_country", "try started");
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                String searchString = query;
                searchString = searchString.replaceAll(" ", "%20");
                searchString = searchString.replaceAll("\\?", "");
                searchString = searchString.replaceAll("\\<", "");
                searchString = searchString.replaceAll("\\>", "");

                request.setURI(new URI("http://rotary.dogaozkaraca.com/searchOnTheDatabase.php?string=" + searchString));
                Log.w("RO_country", "response getting");
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));

                String aa = in.readLine();
                Log.w("RO_country", "text got");

                if (aa == null) {
                    Log.w("RO_country", "null will be returned");

                }
                result = aa.split("SPACE");

                Log.w("RO_country", "space splitted");


            } catch (Exception er) {
                er.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mSwipeRefreshLayout.setRefreshing(false);

            lst5[0] = new ArrayList<RSS_source_item>();

                if (result != null) {
                    searchRL.setVisibility(View.GONE);

                    for (int x2 = 0; x2 < result.length; x2++) {
                        String[] feedProps = result[x2].split("DORO");


                        lst5[0].add(new RSS_source_item(feedProps[0], feedProps[1]));


                        Log.w("RO_country", feedProps[0] + " and " + feedProps[1]);

                    }


                } else {
                    searchRL.setVisibility(View.VISIBLE);

                    tvSearchStatus.setVisibility(View.VISIBLE);
                    tvSearchStatus.setText("No results found!\nBut, we can add missing news source for you.");
                    sendRequestToUs.setVisibility(View.VISIBLE);

                }
                lv5.setAdapter(new RSS_source_adaptor(rss_feed_settings.this, lst5[0]));



        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LA= (ListView) findViewById(R.id.ListView1);
        refreshMyFeedList();

    }

    public static void refreshMyFeedList()
    {

        List<RSS_source_item> myfeed = new ArrayList<RSS_source_item>();


        //Load
        String[] titleList = loadArray("title", ctx);
        String[] urlList = loadArray("url", ctx);
        for (int x=0;x<titleList.length;x++) {
            myfeed.add(new RSS_source_item(titleList[x], urlList[x]));

        }

        LA.setAdapter(new RSS_myfeed_source_adaptor(ctx, myfeed));


    }
    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("Rotary_RSS", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

@Override
public void onBackPressed()
{

    if (!searchView.isIconified())
    {
        searchRL.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);

        lv5.setVisibility(View.GONE);
        searchView.setIconified(true);
        mSwipeRefreshLayout.setRefreshing(false);

    }
    else
    {
        if(blurCommWebView.getVisibility() == View.VISIBLE)
        {
            mSwipeRefreshLayout.setVisibility(View.GONE);

            blurCommWebView.setVisibility(View.GONE);
        }
        else {
            finish();
        }
    }

}




}
