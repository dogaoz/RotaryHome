package com.dogaozkaraca.rotaryhome;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import at.markushi.ui.CircleButton;
import twitter4j.TwitterException;

/**
 * Created by doga on 26/06/15.
 */

@SuppressLint("ValidFragment")
public class BlurCommWithDogaOzkaraca extends DialogFragment {

    String reason;
    public BlurCommWithDogaOzkaraca(String reason)
    {
        super();
        this.reason = reason;


    }
    public BlurCommWithDogaOzkaraca(){
        super();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(130);
        dialog.getWindow().setBackgroundDrawable(d);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    @Override
    public void onResume() {

        super.onResume();

        Window window = getDialog().getWindow();
        Display display = DoWorkspace.mLauncher.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_Panel);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.blur_comm, null);

        final WebView blurCommWebView = (WebView) v.findViewById(R.id.webView2);



        blurCommWebView.getSettings().setJavaScriptEnabled(true);
        blurCommWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url)
            {
                if (url.equals("http://rotaryhome.com/?cancelled"))
                {

                    blurCommWebView.stopLoading();
                    BlurCommWithDogaOzkaraca.this.dismiss();

                }
                else  if (url.equals("http://rotaryhome.com/?done"))
                {
                    blurCommWebView.stopLoading();
                    BlurCommWithDogaOzkaraca.this.dismiss();

                }
                else
                {
                    view.loadUrl(url);
                }

                return true;
            }
        });
        if(reason.equals("source_req"))
            blurCommWebView.loadUrl("http://rotary.dogaozkaraca.com/news_source_request.php");

        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (this != null) {
            this.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);


    }


    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_fade);
    }




}
