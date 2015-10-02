package com.dogaozkaraca.rotaryhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.frakbot.imageviewex.Converters;
import net.frakbot.imageviewex.ImageViewEx;

/**
 * Created by doga on 10/05/15.
 */
public class LoadingDialog extends ProgressDialog implements DialogInterface.OnDismissListener {
    public LoadingDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);

        ImageViewEx loading = (ImageViewEx) findViewById(R.id.loadingAnimated);
        RelativeLayout loadingRL = (RelativeLayout) findViewById(R.id.loadingRelativeLayout);
        TextView loadingText = (TextView)	findViewById(R.id.loadingText);
        // Sets the sources of ImageViewExs as byte arrays
        loading.setSource(Converters.assetToByteArray(getContext().getAssets(), "loading.gif"));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
       // mAdView = (AdView) findViewById(R.id.ad_view);
     //   if (RotaryHome.isAdFree(getContext()) == true)
     //   {
    //        mAdView.setVisibility(View.GONE);
     //   }
     //   else {
            // Start loading the ad in the background.
            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
       //     AdRequest adRequest = new AdRequest.Builder().build();

            // Start loading the ad in the background.
         //   mAdView.loadAd(adRequest);
      //  }


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // Do whatever

       // if (mAdView != null) {
       //     mAdView.destroy();
      //  }
    }


}
