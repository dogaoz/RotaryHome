package com.dogaozkaraca.rotaryhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dogaozkaraca.rotaryhome.util.IabHelper;
import com.dogaozkaraca.rotaryhome.util.IabResult;
import com.dogaozkaraca.rotaryhome.util.Inventory;
import com.dogaozkaraca.rotaryhome.util.Purchase;


public class purchases extends ActionBarActivity {

    IabHelper mHelper;
    String ITEM_SKU_buy_pro = "basic_social_networks_bundle";

    String ITEM_SKU_upgrade_to_premium = "more_social_bundle";

    String ITEM_SKU3_buy_premium = "rotary_premium";
    public static SharedPreferences settingsCHK;
    RelativeLayout item_fb_tw_ins;
    RelativeLayout item_more_social;
    RelativeLayout item_premium;
    TextView accountType;
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
        setContentView(R.layout.activity_purchases);



            settingsCHK = getSharedPreferences("RotaryCheck", 0);



        //SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        //s.setSpan(new TypefaceSpanRo(this, "fonts/MainClockFont.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //s.setSpan(new ForegroundColorSpan(ColorScheme.getTextColor(this)), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //getSupportActionBar().setTitle(s);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlback);
        rl.setBackgroundColor(Color.WHITE);
        //rl.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        android.support.v7.app.ActionBar ac = getSupportActionBar();

        ac.setBackgroundDrawable(new ColorDrawable(ColorScheme.getActionBarColor(this)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ColorScheme.getStatusBarColor(this));
        }
        Button btn = (Button) findViewById(R.id.button4);
        btn.setBackgroundColor(ColorScheme.getBackgroundColor(this));
        btn.setTextColor(ColorScheme.getTextColor(this));

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5H3szs41ZfdsEmJqmKPFy8054WBoTBxeisytgTB5i4NJ2/Z/dn22Iydctqba41NuA5/ZitohQ8R2KEaK6bBzxpJ1OjwpCbflM4JJ0lxFU9QHOw1dyYK3Gm3IA/RkBi+6gzuPYmnMEJJFku1bhxRP0nFvmeeKRBcrrjWSEcLqivPvD9vqZzQHpyV54zXPPV5XQBcByDTQzhXeyt78f/9g4lpXB3xQgpOqlJwX2KDukNmS/utVa9HfpMCX2Z+jI9M+T7uADllRWrb0bLZsMNHXJ7RGo+mDSLuSKfDb7AHoxNJk5Cnx8Rolbg0XUF2CO5kZ5MuJYTEh/+07mqu7qDfp4wIDAQAB";


        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               Log.d("", "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d("", "In-app Billing is set up OK");
                                           }
                                       }
                                   });



        item_fb_tw_ins = (RelativeLayout) findViewById(R.id.item1);
        item_more_social = (RelativeLayout) findViewById(R.id.item2);
        item_premium = (RelativeLayout) findViewById(R.id.item3);


        SharedPreferences settings = getSharedPreferences("Rotary_Online", 0);

        String name = settings.getString("Rotary_Name", "");
        TextView headerText = (TextView) findViewById(R.id.headertext);
        headerText.setText(name);

        accountType = (TextView) findViewById(R.id.accounttypetext);




    }

    public void loadAccountStatus()
    {

        if (rotaryIsPremium(this) == true) {

            //PREMIUM IS ACTIVE

            item_fb_tw_ins.setVisibility(View.GONE);
            item_more_social.setVisibility(View.GONE);
            item_premium.setVisibility(View.GONE);
            accountType.setText("Account Type : Premium\nThank you for supporting us by using Premium");

        }
        else if (rotaryIsPro(this) == true) {

            //PRO IS ACTIVE - PREMIUM CAN BE BOUGHT
            item_fb_tw_ins.setVisibility(View.GONE);
            item_more_social.setVisibility(View.VISIBLE);
            item_premium.setVisibility(View.GONE);
            accountType.setText("Account Type : Pro\nThank you for supporting us by using Pro");

        }
        else
        {
            //NOTHING IS ACTIVE - FREE USER
            item_fb_tw_ins.setVisibility(View.VISIBLE);
            item_more_social.setVisibility(View.GONE);
            item_premium.setVisibility(View.VISIBLE);
            accountType.setText("Account Type : Free");
        }
    }

    public void loginRotary(View v)
    {
        Intent i=new Intent(purchases.this,RotaryHome_FirstSetup.class);
        purchases.this.startActivity(i);

    }
    /////PRO Purchase finished listener
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener_pro
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU_buy_pro)) {
                authenticateANDupdateSERVER("forPRO");
                //buyButton.setEnabled(false);
                //Purchase Completed
                Toast.makeText(purchases.this,"Purchase Completed!",Toast.LENGTH_LONG).show();
            }

        }
    };

    /////Pro to Premium upgrade finished listener
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener_pro_to_pre
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU_upgrade_to_premium)) {
                authenticateANDupdateSERVER("forPREMIUMUPGRADE");
                //buyButton.setEnabled(false);
                //Purchase Completed
                Toast.makeText(purchases.this,"Purchase Completed!",Toast.LENGTH_LONG).show();
            }

        }
    };
    //////Buy Premium finished listener
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener_pre
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU_buy_pro)) {
                authenticateANDupdateSERVER("forPREMIUM");
                //buyButton.setEnabled(false);
                //Purchase Completed
                Toast.makeText(purchases.this,"Purchase Completed!",Toast.LENGTH_LONG).show();
            }

        }
    };
    //NEVER CONSUME SOMETHING
    //public void consumeItem() {
    //    mHelper.queryInventoryAsync(mReceivedInventoryListener);
    //}

    //IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
   //         = new IabHelper.QueryInventoryFinishedListener() {
   //     public void onQueryInventoryFinished(IabResult result,
   //                                          Inventory inventory) {

    //        if (result.isFailure()) {
    //            // Handle failure
    //        } else {
   //             mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
    //                    mConsumeFinishedListener);
    //        }
    //    }
   // };
    //IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
    //        new IabHelper.OnConsumeFinishedListener() {
   //             public void onConsumeFinished(Purchase purchase,
    //                                          IabResult result) {

     //               if (result.isSuccess()) {
                        //clickButton.setEnabled(true);
     //               } else {
                        // handle error
      //              }
      //          }
      //      };
    public void buyClickPRO(View view) {
        if (mHelper != null) mHelper.flagEndAsync();
        mHelper.launchPurchaseFlow(this, ITEM_SKU_buy_pro, 10001,
                mPurchaseFinishedListener_pro, "mypurchasetoken");
    }
    public void buyClickPRO_to_PRE(View view) {
        if (mHelper != null) mHelper.flagEndAsync();

        mHelper.launchPurchaseFlow(this, ITEM_SKU_upgrade_to_premium, 10001,
                mPurchaseFinishedListener_pro_to_pre, "mypurchasetoken");
    }
    public void buyClickPRE(View view) {
        if (mHelper != null) mHelper.flagEndAsync();

        mHelper.launchPurchaseFlow(this, ITEM_SKU3_buy_premium, 10001,
                mPurchaseFinishedListener_pre, "mypurchasetoken");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
    @Override
    public void onResume()
    {
        super.onResume();

            authenticateANDupdateSERVERonResume();


    }
    public void authenticateANDupdateSERVERonResume()
    {

        loadAccountStatus();


    }

    public void authenticateANDupdateSERVER(String forwhat)
    {

        mHelper.queryInventoryAsync(mGotInventoryListener);


        Toast.makeText(this,"Purchase Completed & Account has been upgraded!",Toast.LENGTH_LONG).show();

    }
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
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


                }
                else if (inventory.hasPurchase(ITEM_SKU_buy_pro) == true )
                {

                    mIsPro = true;

                    SharedPreferences.Editor editor = settingsCHK.edit();
                    editor.putBoolean("isPro", mIsPro);
                    editor.commit();

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
                loadAccountStatus();

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
    @Override
    protected void onPause()
    {

        super.onPause();
        overridePendingTransition(0, R.anim.anim_right);
    }


}
