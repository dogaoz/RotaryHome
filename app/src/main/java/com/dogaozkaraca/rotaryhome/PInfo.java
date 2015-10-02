package com.dogaozkaraca.rotaryhome;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by doga on 05/05/15.
 */
public class PInfo {
    public String appname = "";
    public String packageName = "";
    public String activityName ="";
    public Drawable icon;
    public int position = 0;
    public int appImportance = 0;
    public ComponentName name;
    public static Context ctx;

    public void launch()
    {


        Intent i=new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(name);

        ctx.startActivity(i);

    }

    public static void uninstallApp(String pckName)
    {
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",pckName,null));
        ctx.startActivity(intent);
    }
    public static void raiseTheApp(String packageplusactivityname)
    {
        SharedPreferences prefs = ctx.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE);
        int get = prefs.getInt(packageplusactivityname, 0);
        prefs.edit().putInt(packageplusactivityname, get + 1).apply();

    }
    public static void dropTheApp(String packageplusactivityname)
    {
        SharedPreferences prefs = ctx.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE);

        int get = prefs.getInt(packageplusactivityname, 0);

        if(get <= -1)
        {
            prefs.edit().putInt(packageplusactivityname, 0).apply();
        }
        if(get != 0) {
            prefs.edit().putInt(packageplusactivityname, get - 1).apply();
        }


    }

    public static boolean iconPackApplied = false;
    public static ArrayList<PInfo> getInstalledApps() {
        SharedPreferences prefs = ctx.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE);

        iconPackApplied = prefs.getBoolean("isIconPackApplied",false);
        String appliedIconPackPackageName = "";

        if (iconPackApplied) {
            appliedIconPackPackageName = prefs.getString("appliedIconPackPackageName", "default");
        }
        Resources ress =null;
        int i5 = -999;
        XmlResourceParser xpp = null;
        if (iconPackApplied == true && !appliedIconPackPackageName.equals("default")) {
            try {
                ress = ctx.getPackageManager().getResourcesForApplication(appliedIconPackPackageName);
                i5 = ress.getIdentifier("appfilter", "xml", appliedIconPackPackageName);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                //Log.e("RotaryComp","error,IconPackDisabled");
                iconPackApplied=false;
            }
        }

        final ArrayList<PInfo> res = new ArrayList<PInfo>();

        PackageManager pm=ctx.getPackageManager();
        Intent main=new Intent(Intent.ACTION_MAIN, null);

        main.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> launchables=pm.queryIntentActivities(main, 0);

        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(pm));


        for (int i = 0; i < launchables.size(); i++) {

            ResolveInfo launchable=launchables.get(i);
            ActivityInfo activity=launchable.activityInfo;
            PInfo newInfo = new PInfo();
            if (activity.applicationInfo.loadLabel(ctx.getPackageManager()).toString().length() > 15) {
                String[] separated = activity.applicationInfo.loadLabel(ctx.getPackageManager()).toString().split(" ");

                newInfo.appname = activity.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
            } else {
                newInfo.appname = activity.loadLabel(ctx.getPackageManager()).toString();
            }
            newInfo.packageName = activity.packageName;
            newInfo.position = i;
            newInfo.activityName = activity.name;
            if (iconPackApplied == true && !appliedIconPackPackageName.equals("default") && !appliedIconPackPackageName.equals("") && ress != null)
            {
                String componentName = ctx.getPackageManager().getLaunchIntentForPackage(activity.packageName).getComponent().toString();

                    xpp = ress.getXml(i5);



                try {
                    try {

                    while (xpp.getEventType()!= XmlPullParser.END_DOCUMENT) {

                            if (xpp.getEventType() == XmlPullParser.START_TAG)
                            {
                                if (xpp.getAttributeCount()!=0 && xpp.getAttributeValue(0).equals(componentName))
                                {
                                    //Log.w("RotaryComp","icon set");
                                    newInfo.icon = resize(ress.getDrawable(ress.getIdentifier(xpp.getAttributeValue(1), "drawable", appliedIconPackPackageName)));
                                    break;
                                }

                            }



                        xpp.next();
                    }
                    }
                    catch (Exception e)
                    {  // Log.w("RotaryComp","exception");

                        e.printStackTrace();}

                    if(newInfo.icon==null) {
                        //Log.w("RotaryComp","icon error,default loaded");

                        newInfo.icon = resize(activity.loadIcon(ctx.getPackageManager()));
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    newInfo.icon = resize(activity.loadIcon(ctx.getPackageManager()));

                }


            }
            else {
                newInfo.icon = resize(activity.loadIcon(ctx.getPackageManager()));
            }

            newInfo.name =new ComponentName(activity.applicationInfo.packageName,activity.name);
            newInfo.appImportance = prefs.getInt(activity.packageName + activity.name, 0);
            // Log.w("wtfisthis",activity.packageName + activity.name);
            res.add(newInfo);
        }

        Comparator ourComparator=new Comparator<PInfo>() {
            @Override
            public int compare(PInfo lhs, PInfo rhs) {
                return rhs.appImportance - lhs.appImportance;
            }

        };
            Collections.sort(res, ourComparator);


        return res;
    }

    private static Drawable resize(Drawable image) {
        if(image.getIntrinsicHeight() !=(int)RotaryView.convertDpToPixel(48) && image.getIntrinsicHeight() !=(int)RotaryView.convertDpToPixel(48)) {

            if(image instanceof BitmapDrawable) {
                // getApplicationIcon gives a Drawable which is then cast as a BitmapDrawable
                 //icon = Bitmap.createScaledBitmap(((BitmapDrawable) apkIcon).getBitmap(), 32, 32, true);
                Bitmap b = ((BitmapDrawable) image).getBitmap();
                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, (int) RotaryView.convertDpToPixel(48), (int) RotaryView.convertDpToPixel(48), false);
                return new BitmapDrawable(ctx.getResources(), bitmapResized);
                 }
               else {
                   Bitmap icon = Bitmap.createBitmap(image.getIntrinsicWidth(), image.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                   Canvas canvas = new Canvas(icon);
                   image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                   image.draw(canvas);
                Bitmap bitmapResized = Bitmap.createScaledBitmap(icon, (int) RotaryView.convertDpToPixel(48), (int) RotaryView.convertDpToPixel(48), false);

                return new BitmapDrawable(ctx.getResources(), bitmapResized);

            }

        }
        else
        {
            if(image instanceof BitmapDrawable) {
                return image;
            }
            else
            {
                Bitmap icon = Bitmap.createBitmap(image.getIntrinsicWidth(), image.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(icon);
                image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                image.draw(canvas);
                return new BitmapDrawable(ctx.getResources(), icon);

            }
        }
    }

}
