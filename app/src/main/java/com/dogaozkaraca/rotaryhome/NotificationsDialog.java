package com.dogaozkaraca.rotaryhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fivehundredpx.android.blur.BlurringView;

import net.frakbot.imageviewex.Converters;
import net.frakbot.imageviewex.ImageViewEx;

/**
 * Created by doga on 10/05/15.
 */
public class NotificationsDialog extends ProgressDialog {
    public NotificationsDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_dialog);


    }


}
