package com.dogaozkaraca.rotaryhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.frakbot.imageviewex.Converters;
import net.frakbot.imageviewex.ImageViewEx;

import me.relex.circleindicator.CircleIndicator;


public class RotaryTeacher extends ActionBarActivity {

    private static final int MAX_VIEWS = 5;
    Button Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_teacher);
        getSupportActionBar().hide();

        Btn = (Button) findViewById(R.id.button6);
        // DEFAULT
        ViewPager defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        defaultViewpager.setAdapter(new WalkthroughPagerAdapter());
        defaultViewpager.setOnPageChangeListener(new WalkthroughPageChangeListener());
        defaultIndicator.setViewPager(defaultViewpager);


    }


    class WalkthroughPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return MAX_VIEWS;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            Log.e("walkthrough", "instantiateItem(" + position + ");");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View container2 = inflater.inflate(R.layout.teacher_fragment, null);
            ImageViewEx loading = (ImageViewEx) container2.findViewById(R.id.image);


            switch(position) {
                case 0:
                    //loading.setSource(Converters.assetToByteArray(inflater.getContext().getAssets(), "loading.gif"));
                    break;

                case 1:
                    //loading.setSource(Converters.assetToByteArray(inflater.getContext().getAssets(), "loading.gif"));
                    break;

                case 2:
                   // loading.setSource(Converters.assetToByteArray(inflater.getContext().getAssets(), "loading.gif"));
                    break;

                case 3:
                   // loading.setSource(Converters.assetToByteArray(inflater.getContext().getAssets(), "loading.gif"));
                    break;

                case 4:
                   // loading.setSource(Converters.assetToByteArray(inflater.getContext().getAssets(), "loading.gif"));
                    break;
            }

            ((ViewPager) container).addView(container2, 0);
            return container2;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }
    }


    class WalkthroughPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // Here is where you should show change the view of page indicator
            switch(position) {

                case MAX_VIEWS - 1:
                    Btn.setVisibility(View.VISIBLE);
                    Btn.setText("Sign In");


                    break;

                default:

            }

        }

    }

    public void signin(View v)
    {
        SharedPreferences settings = getSharedPreferences("Rotary_Online", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Rotary_tutorial", "yes");
        editor.commit();
        Intent i=new Intent(this,RotaryHome_FirstSetup.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        signin(null);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit tutorial?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}
