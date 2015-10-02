package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by doga on 05/06/15.
 */
public final class AdMobActivity
        extends Activity {

    public static AdMobActivity AdMobMemoryLeakWorkAroundActivity;

    public AdMobActivity() {
        super();
        if (AdMobMemoryLeakWorkAroundActivity != null)
            throw new IllegalStateException("This activity should be created only once during the entire application life");
        AdMobMemoryLeakWorkAroundActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }

    public static final void startAdMobActivity(Activity activity) {
        Intent i = new Intent();
        i.setComponent(new ComponentName(activity.getApplicationContext(), AdMobActivity.class));
        activity.startActivity(i);
    }
}
