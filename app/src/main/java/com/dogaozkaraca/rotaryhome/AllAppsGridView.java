package com.dogaozkaraca.rotaryhome;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by doga on 21/07/15.
 */
public class AllAppsGridView extends GridView {
    public AllAppsGridView(Context context) {
        super(context);
    }
    public AllAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    public AllAppsGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public static String AGL = "RotaryTouch";
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!RotaryHome.revealView.isIndragNdrop) {
            return super.onInterceptTouchEvent(event);
        }
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(AGL, "InterceptAction = DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(AGL, "InterceptAction = MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(AGL, "InterceptAction = CANCEL");

                return false;

        }

            return true;
        //returning true tells your main Activity that you want the custom GridView to handle this TouchEvent; It will then send the TouchEvent to your GridView's onTouchEvent() method for handling.
    }


}
