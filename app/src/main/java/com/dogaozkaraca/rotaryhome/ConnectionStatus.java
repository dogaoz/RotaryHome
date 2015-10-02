package com.dogaozkaraca.rotaryhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by doga on 10/05/15.
 */
public class ConnectionStatus extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try
        {
            RotaryHome.connectionStatus =RotaryHome.getNetworkClass(context);
            RotaryHome.wifi.setText(RotaryHome.connectionStatus);
        }
        catch(Exception e)
        {

        }
    }


}
