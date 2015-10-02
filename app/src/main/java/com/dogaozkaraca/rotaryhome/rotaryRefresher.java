package com.dogaozkaraca.rotaryhome;

/**
 * Created by doga on 10/07/15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class rotaryRefresher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        context.getSharedPreferences("rotary", 0).edit().putBoolean("iNeedRefresh", true).commit();

    }
}
