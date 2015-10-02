package com.dogaozkaraca.rotaryhome;

import android.animation.Animator;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.params.LensShadingMap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
/**
 * Created by doga on 30/04/15.
 */
public class RotaryView extends View {

    private Paint paint;
    int radius =0;
    public static int x;

    float fontsize;
    static ArrayList<PInfo> abc;
    boolean launchable = false;
    //Pages

    public static int page=0;
    static int itemCountPerPage = 8;


    boolean holdingpage = false;
    boolean center_circle = false;
    double startAngle;
    //Apps

    int app_img_start_x[] = new int[10];
    int app_img_start_y[] = new int[10];
    int app_img_end_x[] = new int[10];
    int app_img_end_y[] = new int[10];

    int app_delete_start_x[] = new int[10];
    int app_delete_start_y[] = new int[10];
    int app_delete_end_x[] = new int[10];
    int app_delete_end_y[] = new int[10];

    int LastPage;
    //All Apps

    int all_apps_start_x;
    int all_apps_start_y;
    int all_apps_end_x;
    int all_apps_end_y;


    boolean FadeAnimationReverse =false;
    int savedAlpha=255;


    //App Operations
    int chosenAppPlace = 0;
    int app_ops_start_x[] = new int[10];
    int app_ops_start_y[] = new int[10];
    int app_ops_end_x[] = new int[10];
    int app_ops_end_y[] = new int[10];
    int pageAppOptions = 9000;


    ////Center Circle
    int circ_start_x = 0;
    int circ_start_y = 0;
    int circ_end_x = 0;
    int circ_end_y = 0;

    //Pages
    int page_circ_start_x[] = new int[1000];
    int page_circ_start_y[] = new int[1000];

    int page_circ_end_x[] = new int[100];
    int page_circ_end_y[] = new int[100];
    
    //Touches 
    int app_circle_center_x = 0;
    int app_circle_center_y = 0;

    public static int cx_forCircleReveal = -5999;
    public static int cy_forCircleReveal = -5999;
    int radius_forUNDOCircleReveal;

    public RotaryView(Context context) {
        super(context);

        PInfo.ctx = context;


    }
    public RotaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        PInfo.ctx = context;

        init(context, null);






    }

    public RotaryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        PInfo.ctx = context;
        init(context, null);
    }
    private void init(Context context, AttributeSet attrs)
    {

        abc = new ArrayList<PInfo>();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        final SharedPreferences settings5 = context.getSharedPreferences("font_updater", 0);


        if(settings5.getString("fonts","forgottensans").equals("forgottensans") && RotaryHome.rotaryIsPremium(context))
        {
            paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/forgotten.ttf"));

        }
        else if (settings5.getString("fonts","forgottensans").equals("lobster") && RotaryHome.rotaryIsPremium(context))
        {
            paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/lobster.ttf"));


        }
        else if (settings5.getString("fonts","forgottensans").equals("exo2") && RotaryHome.rotaryIsPremium(context))
        {
            paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Exo2.0_Thin.otf"));

        }
        else if (settings5.getString("fonts","forgottensans").equals("android"))
        {

            paint.setTypeface(Typeface.DEFAULT);

        }
        else
        {
            paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/forgotten.ttf"));
        }
        fontsize=14;
        paint.setTextSize(convertDpToPixel(fontsize));
        paint.setStrokeWidth(5);
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RotaryHome.rotaryView.paint.setStyle(Paint.Style.STROKE);
        RotaryHome.rotaryView.paint.setAlpha(130);
        // DRAW APP CIRCLE
        int w = RotaryHome.rotaryView.getWidth();
        int h = RotaryHome.rotaryView.getHeight();

        int pl = RotaryHome.rotaryView.getPaddingLeft()+(int)RotaryHome.rotaryView.convertDpToPixel(35);
        int pr = RotaryHome.rotaryView.getPaddingRight()+(int)RotaryHome.rotaryView.convertDpToPixel(35);
        int pt = RotaryHome.rotaryView.getPaddingTop()+(int)RotaryHome.rotaryView.convertDpToPixel(35);
        int pb = RotaryHome.rotaryView.getPaddingBottom()+(int)RotaryHome.rotaryView.convertDpToPixel(35);

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        RotaryHome.rotaryView.radius = (Math.min(usableWidth, usableHeight) / 2);
        int cx = pl + (usableWidth / 2);
        int cy = pt + (usableHeight / 2);
        paint.setShadowLayer(3, 2, 1, Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, RotaryHome.rotaryView.radius, RotaryHome.rotaryView.paint);

        // DRAW PAGE CIRCLE


        int pl2 = RotaryHome.rotaryView.getPaddingLeft()+(int)RotaryHome.rotaryView.convertDpToPixel(125);
        int pr2 = RotaryHome.rotaryView.getPaddingRight()+(int)RotaryHome.rotaryView.convertDpToPixel(125);
        int pt2 = RotaryHome.rotaryView.getPaddingTop()+(int)RotaryHome.rotaryView.convertDpToPixel(125);
        int pb2 = RotaryHome.rotaryView.getPaddingBottom()+(int)RotaryHome.rotaryView.convertDpToPixel(125);

        int centerOfDeleteFolderButton_X = RotaryHome.deviceScreenWidth - (pr2 + pl2) + (int)RotaryHome.rotaryView.convertDpToPixel(63);
        int centerOfDeleteFolderButton_Y = RotaryHome.deviceScreenHeight - (pt2 + pb2) + (int)RotaryHome.rotaryView.convertDpToPixel(63);

        RotaryHome.removeFolderIconSizeShouldbe = RotaryHome.deviceScreenWidth - (pr2 + pl2) - (int)RotaryHome.rotaryView.convertDpToPixel(50);
        int usableWidth2 = w - (pl2 + pr2);
        int usableHeight2 = h - (pt2 + pb2);

        int radius2 = Math.min(usableWidth2, usableHeight2) / 2;
        int cx2 = pl2 + (usableWidth2 / 2);
        int cy2 = pt2 + (usableHeight2 / 2);

        if (RotaryHome.isFolderEditMode || RotaryHome.revealView.isIndragNdrop) {
            RotaryHome.rotaryView.paint.setStyle(Paint.Style.FILL);
            RotaryHome.rotaryView.paint.setStrokeWidth(2);
            RotaryHome.rotaryView.paint.setAlpha(255);
        }
        else
        {
            RotaryHome.rotaryView.paint.setAlpha(80);
        }

        canvas.drawCircle(cx2, cy2, radius2, RotaryHome.rotaryView.paint);

        RotaryHome.rotaryView.circ_start_x = cx2 - radius2;
        RotaryHome.rotaryView.circ_start_y = cy2 -radius2;
        RotaryHome.rotaryView.circ_end_x = cx2 + radius2;
        RotaryHome.rotaryView. circ_end_y = cy2 + radius2;


        RotaryHome.rotaryView.app_circle_center_x = cx;
        RotaryHome.rotaryView.app_circle_center_y = cy;


        try {


            RotaryHome.rotaryView.drawAppItems(canvas, usableWidth, usableHeight, pl, pt);
        }
        catch(Exception e){}

        RotaryHome.rotaryView.drawPageItems(canvas, usableWidth2, usableHeight2, pl2, pt2);

    }
    public void drawPageItems(Canvas canvas, int usableWidth2, int usableHeight2, int pl2, int pt2) {

        paint.setTextSize(convertDpToPixel(fontsize));


        int p_radius = (Math.min(usableWidth2, usableHeight2) / 2);


        if (holdingpage == false) {
            for (int x = 0; x < folderCount; x++) {
                //plus one for making space all apps
                int angle = 360 / (folderCount+1) * (x + 4);
                double pageX = usableWidth2 / 2 + pl2 + p_radius * cos((360 - angle) * (Math.PI / 180));
                double pageY = usableHeight2 / 2 + pt2 - p_radius * sin((angle) * (Math.PI / 180));
                paint.setAlpha(85);
                paint.setShadowLayer(3, 2, 1, Color.WHITE);

                if (x == page) {
                    if(RotaryHome.isDarkTheme == true) {
                        paint.setAlpha(255);

                    }
                    else {
                        paint.setColor(Color.BLACK);
                        paint.setAlpha(170);
                    }
                }

                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(2);
                canvas.drawCircle((int) pageX, (int) pageY, convertDpToPixel(10), paint);


                     paint.setColor(Color.BLACK);

                if(x == page && RotaryHome.isDarkTheme == false) {
                    paint.setColor(Color.WHITE);
                    paint.setShadowLayer(3, 2, 1, Color.BLACK);
                }

                try {
                    Rect bounds = new Rect();
                    String text2 =FolderManager.getFolderName(x).substring(0, 1);
                    paint.getTextBounds(text2, 0, text2.length(), bounds);
                    canvas.drawText(text2, (int) pageX - bounds.width()/2, (int) pageY + convertDpToPixel(4), paint);
                }
                catch(Exception e)
                {

                }

                paint.setColor(Color.WHITE);
                paint.setAlpha(255);
                paint.setShadowLayer(3, 2, 1, Color.BLACK);





            }
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            //AllAppsButton
            int angle = 360 / (folderCount+1) * (folderCount + 4);
            double pageX = usableWidth2 / 2 + pl2 + p_radius * cos((360 - angle) * (Math.PI / 180));
            double pageY = usableHeight2 / 2 + pt2 - p_radius * sin((angle) * (Math.PI / 180));
            canvas.drawCircle((int) pageX, (int) pageY, convertDpToPixel(10), paint);

            for (int x2 = 0; x2 < 4; x2++) {


                int angle2 = 360 / 4 * (x2 + 4);
                //1
                double appStartX = pageX  + convertDpToPixel(4) * cos((angle2 + RotaryView.x) * (Math.PI / 180));
                double appStartY = pageY  - convertDpToPixel(4) * sin((360 - angle2 - RotaryView.x) * (Math.PI / 180));

                    paint.setColor(Color.BLACK);


                canvas.drawCircle((int) appStartX, (int) appStartY, convertDpToPixel(2), paint);
                paint.setColor(Color.WHITE);


            }
        }





    }

    private void drawAppItems(Canvas canvas, int usableWidth, int usableHeight, int pl,int pt) {


        if (holdingpage == true)
        {

            paint.setAlpha(255);
            paint.setTextSize(convertDpToPixel(20));
            if (center_circle == true) {
                //Plus 1 for All Apps Button
                int angle;
                double holdX = 0;
                double holdY = 0;
                for (int x = 0; x <= folderCount; x++) {
                    paint.setAlpha(120);
                    paint.setShadowLayer(3, 2, 1, Color.WHITE);

                    if (x == page)
                    {
                        if(RotaryHome.isDarkTheme == true) {
                            paint.setAlpha(255);

                        }
                        else {
                            paint.setColor(Color.BLACK);
                            paint.setAlpha(170);
                        }
                    }
                    //plus one for creating space for all apps button
                    angle = 360 / (folderCount+1) * (x + 4);
                    holdX = usableWidth / 2 + convertDpToPixel(32)  + radius * cos((360 - angle) * (Math.PI / 180));
                    holdY = usableHeight / 2 + convertDpToPixel(32) - radius * sin((angle) * (Math.PI / 180));
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(2);
                    canvas.drawCircle((int) holdX, (int) holdY, convertDpToPixel(32), paint);
                    //paint.setColor(Color.BLACK);
                    //canvas.drawText(x + 1 + "", (int) holdX - convertDpToPixel(1), (int) holdY + convertDpToPixel(5), paint);
                    paint.setColor(Color.WHITE);
                    paint.setAlpha(255);
                    paint.setShadowLayer(3, 2, 1, Color.BLACK);

                    page_circ_start_x[x + 1] = (int) holdX - (int) convertDpToPixel(18);
                    page_circ_start_y[x + 1] = (int) holdY - (int) convertDpToPixel(18);
                    page_circ_end_x[x + 1] = (int) holdX + (int) convertDpToPixel(18);
                    page_circ_end_y[x + 1] = (int) holdY + (int) convertDpToPixel(18);





                        for (int x2 = 0; x2 < itemCountPerPage; x2++) {
                            try {

                                int angle2 = 360 / itemCountPerPage * (x2 + 4);
                                //1
                                double appStartX = holdX - (int) convertDpToPixel(7) + convertDpToPixel(22) * cos((angle2 + RotaryView.x) * (Math.PI / 180));
                                double appStartY = holdY - (int) convertDpToPixel(7) - convertDpToPixel(22) * sin((360 - angle2 - RotaryView.x) * (Math.PI / 180));
                                Bitmap b = ((BitmapDrawable) FolderManager.getIcon(x,x2)).getBitmap();
                                Bitmap b2 = Bitmap.createScaledBitmap(b, (int) convertDpToPixel(14), (int) convertDpToPixel(14), false);


                                canvas.drawBitmap(b2, (int) appStartX, (int) appStartY, paint);


                            } catch (Exception e) {
                            }

                        }


                }


                //AllAppsButton
                paint.setAlpha(120);
                paint.setShadowLayer(3, 2, 1, Color.WHITE);

                if (page == -1)
                {
                    if(RotaryHome.isDarkTheme == true) {
                        paint.setAlpha(255);

                    }
                    else {
                        paint.setColor(Color.BLACK);
                        paint.setAlpha(170);
                    }
                }
                    angle = 360 / (folderCount + 1) * (folderCount + 4);
                    holdX = usableWidth / 2 + convertDpToPixel(32) + radius * cos((360 - angle) * (Math.PI / 180));
                    holdY = usableHeight / 2 + convertDpToPixel(32) - radius * sin((angle) * (Math.PI / 180));
                    all_apps_start_x = (int) holdX - (int) convertDpToPixel(18);
                    all_apps_start_y = (int) holdY - (int) convertDpToPixel(18);
                    all_apps_end_x = (int) holdX + (int) convertDpToPixel(18);
                    all_apps_end_y = (int) holdY + (int) convertDpToPixel(18);


                    //DrawBackgroundCircle
                    canvas.drawCircle((int) holdX, (int) holdY, convertDpToPixel(32), paint);
                paint.setAlpha(255);

                    if (!RotaryHome.revealView.isIndragNdrop) {

                    for (int x2 = 0; x2 < 4; x2++) {

                        int angle2 = 360 / 4 * (x2 + 4);
                        //1
                        double appStartX = holdX + convertDpToPixel(18) * cos((angle2 + RotaryView.x) * (Math.PI / 180));
                        double appStartY = holdY - convertDpToPixel(18) * sin((360 - angle2 - RotaryView.x) * (Math.PI / 180));
                        if(page == -1 && !RotaryHome.isDarkTheme) {
                            paint.setColor(Color.WHITE);
                        }
                        else
                        {
                            paint.setColor(Color.BLACK);
                        }
                            canvas.drawCircle((int) appStartX, (int) appStartY, convertDpToPixel(7), paint);
                        paint.setColor(Color.WHITE);



                    }
                }
                else
                {
                 //   if(RotaryHome.isDarkTheme == true) {
                 //       paint.setColor(Color.WHITE);

                 //       paint.setAlpha(255);

                 //   }
                 //   else {
                  //      paint.setColor(Color.BLACK);
                  //      paint.setAlpha(255);
                  //  }
                    Bitmap b2 = Bitmap.createScaledBitmap(((BitmapDrawable) RotaryHome.ctx.getResources().getDrawable(R.drawable.plus)).getBitmap(), (int) convertDpToPixel(36), (int) convertDpToPixel(36), false);


                    canvas.drawBitmap(b2, (int) all_apps_start_x ,(int) all_apps_start_y, paint);
                    //Draw Add Folder Icon Instead of All Apps Button




                }
                    cx_forCircleReveal = (int) holdX;
                    cy_forCircleReveal = (int) holdY;



            }
            else
            {

                paint.setTextSize(convertDpToPixel(fontsize));

                // EDITING APP & FOLDER

                paint.setStyle(Paint.Style.FILL);
                paint.setShadowLayer(3, 2, 1, Color.BLACK);

                if (0 == 1)
                {
                    if(savedAlpha==255)
                    {
                        FadeAnimationReverse = true;
                    }
                    else if (savedAlpha==128)
                    {
                        FadeAnimationReverse = false;

                    }
                    if (FadeAnimationReverse == true) {
                        savedAlpha--;
                        paint.setAlpha(savedAlpha);
                    }
                    else
                    {
                        savedAlpha++;
                        paint.setAlpha(savedAlpha);

                    }

                }
                Rect bounds = new Rect();
                String text ="\u2212";
                paint.getTextBounds(text, 0, text.length(), bounds);
                for (int x=0;x<itemCountPerPage;x++)
                {
                    try {

                        int angle = 360 / itemCountPerPage *(x+4);
                        //1
                        double appStartX = usableWidth / 2 + pl / 4 + radius * cos((angle +RotaryView.x) * (Math.PI / 180));
                        double appStartY = usableHeight / 2 - radius * sin((360-angle - RotaryView.x) * (Math.PI / 180));
                        Bitmap b = ((BitmapDrawable) FolderManager.getIcon(page,x)).getBitmap();
                        canvas.drawBitmap(b, (int) appStartX, (int) appStartY, paint);
                        paint.setAlpha(255);
                        int angle3 = -45;

                        double appStartX2 = usableWidth / 2 + pl / 4 +convertDpToPixel(24) + radius * cos((angle +RotaryView.x) * (Math.PI / 180));
                        double appStartY2 = usableHeight / 2 +convertDpToPixel(24) - radius * sin((360-angle - RotaryView.x) * (Math.PI / 180));                      paint.setStyle(Paint.Style.FILL);
                        paint.setStrokeWidth(2);

                        double appStartX5 = appStartX2 + convertDpToPixel(24) * cos((angle3 +RotaryView.x) * (Math.PI / 180));
                        double appStartY5 = appStartY2 - convertDpToPixel(24) * sin((360-angle3 - RotaryView.x) * (Math.PI / 180));
                        canvas.drawCircle((int) appStartX5, (int) appStartY5, convertDpToPixel(10), paint);

                        //canvas.drawCircle((int) appStartX2, (int) appStartY2, convertDpToPixel(10), paint);
                        //canvas.drawCircle((int) appStartX, (int) appStartY, convertDpToPixel(10), paint);

                        paint.setColor(Color.BLACK);


                        canvas.drawText(text, (int) appStartX5 -(bounds.width()/2), (int) appStartY5 + convertDpToPixel(4), paint);
                        paint.setColor(Color.WHITE);
                        int textSize = (int) paint.measureText(FolderManager.getAppName(page, x));
                        int num = (b.getWidth()-textSize )/2;
                        if (textSize > b.getWidth())
                        {
                            if (x== 0 )
                            {
                                canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                            }
                            else if (x == 4)
                            {
                                canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX + num*2, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                            }
                            else
                            {
                                canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX + num, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                            }

                        }
                        else
                        {

                            canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX + num, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                        }


                        app_img_start_x[x + 1] = (int) appStartX;
                        app_img_start_y[x + 1] = (int) appStartY;
                        app_img_end_x[x + 1] = (int) appStartX + b.getWidth();
                        app_img_end_y[x + 1] = (int) appStartY + b.getHeight();

                        app_delete_start_x[x + 1] = (int) appStartX5 - (int)convertDpToPixel(9) ;
                        app_delete_start_y[x + 1] = (int) appStartY5  - (int)convertDpToPixel(9);
                        app_delete_end_x[x + 1] = (int) appStartX5 + (int)convertDpToPixel(9);
                        app_delete_end_y[x + 1] = (int) appStartY5 + (int)convertDpToPixel(9);


                    }
                    catch(Exception e) {}

                }

            }


        }
        else {

            paint.setTextSize(convertDpToPixel(fontsize));

            // DRAW APP ITEMS

            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(3, 2, 1, Color.BLACK);
            paint.setAlpha(255);


            FolderManager.showFolderName(page);
                for (int x = 0; x < itemCountPerPage; x++) {
                    try {

                        int angle = 360 / itemCountPerPage * (x + 4);
                        //1
                        double appStartX = usableWidth / 2 + pl / 4 + radius * cos((angle + RotaryView.x) * (Math.PI / 180));
                        double appStartY = usableHeight / 2 - radius * sin((360 - angle - RotaryView.x) * (Math.PI / 180));
                        Bitmap b = ((BitmapDrawable) FolderManager.getIcon(page, x)).getBitmap();
                        canvas.drawBitmap(b, (int) appStartX, (int) appStartY, paint);

                        int textSize = (int) paint.measureText(FolderManager.getAppName(page, x));
                        int num = (b.getWidth() - textSize) / 2;
                        if (textSize > b.getWidth()) {
                            if (x == 0) {
                                canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                            } else if (x == 4) {
                                canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX + num * 2, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                            } else {
                                canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX + num, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                            }

                        } else {

                            canvas.drawText(FolderManager.getAppName(page, x), (int) appStartX + num, (int) appStartY + b.getHeight() + convertDpToPixel(15), paint);

                        }


                        app_img_start_x[x + 1] = (int) appStartX;
                        app_img_start_y[x + 1] = (int) appStartY;
                        app_img_end_x[x + 1] = (int) appStartX + b.getWidth();
                        app_img_end_y[x + 1] = (int) appStartY + b.getHeight();

                    } catch (Exception e) {
                    }

                }





        }
    }


    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }



    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.




        if (holdingpage == false) {

            if (e.getX() > circ_start_x && e.getX() < circ_end_x && e.getY() > circ_start_y && e.getY() < circ_end_y) {
                startOnHold(e);
                center_circle = true;
            }


            for (int x=0;x<itemCountPerPage;x++)
            {
                if (e.getX() > app_img_start_x[x+1] && e.getX() < app_img_end_x[x+1] && e.getY() > app_img_start_y[x+1] && e.getY() < app_img_end_y[x+1]) {
                    startOnHold(e);
                    chosenAppPlace = x + (page * itemCountPerPage);
                }
            }




        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
               //Touches onto apps

                if (RotaryHome.revealView.isIndragNdrop)
                {

                }
                else {
                    if (page == -1) {
                        page = LastPage;
                   RotaryHome.showAllApps(false,0,0);

                    }
                    if (holdingpage == false) {


                        for (x = 0; x < itemCountPerPage; x++) {
                            if (e.getX() > app_img_start_x[x + 1] && e.getX() < app_img_end_x[x + 1] && e.getY() > app_img_start_y[x + 1] && e.getY() < app_img_end_y[x + 1]) {
                                try {
                                    FolderManager.launch(page, x);

                                } catch (Exception e2) {
                                }

                                return true;
                            }

                        }

                    } else {

                        if (center_circle == true) {
                            for (int x = 0; x < folderCount; x++) {

                                if (e.getX() > page_circ_start_x[x + 1] && e.getX() < page_circ_end_x[x + 1] && e.getY() > page_circ_start_y[x + 1] && e.getY() < page_circ_end_y[x + 1]) {


                                    page = x;
                                    LastPage = page;
                                    if (RotaryHome.tutorialRotaryAppChooser != null) {
                                        RotaryHome.tutorialRotaryAppChooser.hide();
                                        RotaryHome.tutorialRotaryAppChooser = null;
                                    }


                                } else if (e.getX() > all_apps_start_x &&
                                        e.getX() < all_apps_end_x &&
                                        e.getY() > all_apps_start_y &&
                                        e.getY() < all_apps_end_y) {

                                    page = LastPage;

                                }


                            }
                            center_circle = false;
                            holdingpage = false;
                        } else {
                            for (int x = 0; x < itemCountPerPage; x++) {
                                if (e.getX() > app_delete_start_x[x + 1] && e.getX() < app_delete_end_x[x + 1] && e.getY() > app_delete_start_y[x + 1] && e.getY() < app_delete_end_y[x + 1]) {

                                    FolderManager.removeAppFromFolder(page, x);

                                }
                            }
                            // holdingpage= false;
                        }

                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (RotaryHome.revealView.isIndragNdrop)
                {




                }
                else {
                    if (center_circle == true) {
                        for (int x = 0; x < folderCount; x++) {
                            if (e.getX() > page_circ_start_x[x + 1] && e.getX() < page_circ_end_x[x + 1] && e.getY() > page_circ_start_y[x + 1] && e.getY() < page_circ_end_y[x + 1]) {

                                if (page != x) {
                                    page = x;
                                    FolderManager.showFolderName(page);
                                    LastPage = page;
                                    invalidate();
                                }

                            }


                        }

                        if (e.getX() > all_apps_start_x &&
                                e.getX() < all_apps_end_x &&
                                e.getY() > all_apps_start_y &&
                                e.getY() < all_apps_end_y) {

                            if (page != -1) {
                                page = -1;
                                FolderManager.showFolderName(page);

                                invalidate();
                            }

                        }

                    } else {

                       //  double Angle = Math.atan2(e.getY() - circ_start_y, e.getX() - circ_start_x);
                        //   Log.w("RotaryView","circ_start_x : " + circ_start_x);
                        //   Log.w("RotaryView","circ_start_y : " + circ_start_y);
                        //   Log.w("RotaryView","e.getY() : " + e.getY());
                        //   Log.w("RotaryView","e.getX() : " + e.getX());
                        //   Log.w("RotaryView","the angle is : " + Angle);
                      // Angle =  Math.toDegrees(Angle);
                        //   Log.w("RotaryView","the new angle is : " + Angle);

                       //     RotaryView.x = (int)Angle;

                        //  invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:

               // startAngle = Math.atan2(e.getY() - circ_start_y, e.getX() - circ_start_x);
               // startAngle =  Math.toDegrees(startAngle);
               // RotaryView.x = (int)startAngle;
                //invalidate();
               // Log.w("RotaryView","startAngle : " + startAngle);
                break;

        }




      //  if (gestureDetector.onTouchEvent(e)) {
      //      return true;
      //  }
        return true;
    }

    private void startOnHold(MotionEvent e) {

        if (center_circle == true) {
            if (e.getEventTime() - e.getDownTime() > 50 && e.getEventTime() - e.getDownTime()<900) {

                performHapticFeedback(1);
                holdingpage = true;
                invalidate();


            }
        }
        else
        {

            if (e.getEventTime() - e.getDownTime() > 400 && e.getEventTime() - e.getDownTime()<900) {
                pageAppOptions=9000;
                performHapticFeedback(1);
                RotaryHome.activateFolderEditMode();
                holdingpage = true;
                invalidate();


            }
        }
    }

    public static int folderCount;
    public static void loadFoldersInRotary() {
        folderCount = FolderManager.getFolderCount();


    }



    public static void refreshApps(final Context mContext)
    {
        final boolean[] iNeedRefresh = new boolean[1];
        new AsyncTask<Void,Void,Void>() {
            protected void onPreExecute() {
                iNeedRefresh[0] = mContext.getSharedPreferences("rotary", 0).getBoolean("iNeedRefresh",true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (iNeedRefresh[0]) {
                    abc = PInfo.getInstalledApps();

                    //int countOfApps = abc.size();
                    //if (countOfApps >= 88)
                    //{
                    //    folderCount = 88 / itemCountPerPage;
                    //}
                    //else
                   // {
                    //    folderCount = countOfApps / itemCountPerPage;
                    //}

                    //if (countOfApps % itemCountPerPage != 0) {
                    //    folderCount = folderCount + 1;
                    //}
                    //Don't need now
                    mContext.getSharedPreferences("rotary", 0).edit().putBoolean("iNeedRefresh", false).commit();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void v)
            {
                AllAppsAdaptor aaa =  new AllAppsAdaptor(RotaryHome.ctx,abc);
                RotaryHome.rotaryAllApps.setAdapter(aaa);
                //RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);
                RotaryView.loadFoldersInRotary();

                RotaryHome.rotaryView.invalidate();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void refreshAppsWithLoadingScreen(final Context mContext)
    {
        final boolean[] iNeedRefresh = new boolean[1];
        new AsyncTask<Void,Void,Void>() {

            LoadingDialog dialog = new LoadingDialog(RotaryHome.ctx);

            protected void onPreExecute() {
                iNeedRefresh[0] = mContext.getSharedPreferences("rotary", 0).getBoolean("iNeedRefresh",true);

                this.dialog.setMessage("Please Wait...");
                this.dialog.setCancelable(false);
                try {

                    this.dialog.show();
                } catch (Exception e) {
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (iNeedRefresh[0]) {
                    abc = PInfo.getInstalledApps();

                    //int countOfApps = abc.size();
                    //if (countOfApps >= 88)
                    //{
                    //    folderCount = 88 / itemCountPerPage;
                    //}
                    //else
                    // {
                    //    folderCount = countOfApps / itemCountPerPage;
                    //}

                    //if (countOfApps % itemCountPerPage != 0) {
                    //    folderCount = folderCount + 1;
                    //}
                    //Don't need now
                    mContext.getSharedPreferences("rotary", 0).edit().putBoolean("iNeedRefresh", false).commit();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void v)
            {
                AllAppsAdaptor aaa =  new AllAppsAdaptor(RotaryHome.ctx,abc);
                RotaryHome.rotaryAllApps.setAdapter(aaa);
                //RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);
                RotaryView.loadFoldersInRotary();
                RotaryHome.rotaryView.invalidate();

                if (RotaryHome.isFolderFirstTime)
                {
                    FolderManager.firstTimeForFolders();
                }

                try {

                    dialog.dismiss();
                } catch (Exception e) {
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }




    public void refresh()
    {

        final SharedPreferences settings5 = RotaryHome.ctx.getSharedPreferences("font_updater", 0);

        //RotaryHome.rotarystatusTV.setText("Loading...");
        if(settings5.getString("fonts","forgottensans").equals("forgottensans") && RotaryHome.rotaryIsPremium(RotaryHome.ctx))
        {
            paint.setTypeface(Typeface.createFromAsset(RotaryHome.ctx.getAssets(), "fonts/forgotten.ttf"));

        }
        else if (settings5.getString("fonts","forgottensans").equals("lobster") && RotaryHome.rotaryIsPremium(RotaryHome.ctx))
        {
            paint.setTypeface(Typeface.createFromAsset(RotaryHome.ctx.getAssets(), "fonts/lobster.ttf"));


        }
        else if (settings5.getString("fonts","forgottensans").equals("exo2") && RotaryHome.rotaryIsPremium(RotaryHome.ctx))
        {
            paint.setTypeface(Typeface.createFromAsset(RotaryHome.ctx.getAssets(), "fonts/Exo2.0_Thin.otf"));

        }
        else if (settings5.getString("fonts","forgottensans").equals("android"))
        {
            paint.setTypeface(Typeface.DEFAULT);

        }
        else
        {
            paint.setTypeface(Typeface.createFromAsset(RotaryHome.ctx.getAssets(), "fonts/forgotten.ttf"));
        }
        RotaryHome.isDarkTheme = true;
        RotaryHome.ctx.getSharedPreferences("colors", 0).edit().putBoolean("isDarkTheme", true).commit();
        RotaryHome.loadCorrectButtons();
    }

    public static void loadWeather(final Context ctx,final boolean isErrorFixed)
    {
        final String degreeSymbol = "\u00b0";
        final String[] C_or_F = {"C"};
        final String[] Location = {"Izmir"};
        final int[] currentTemp = {30};
        final int[] tempStatus = new int[1];

        new AsyncTask<String,String,String>()
        {


            @Override
            protected void onPreExecute()
            {
                RotaryHome.rotarystatusTV.setText("Loading...");
            }
            @Override
            protected String doInBackground(String... params) {

                try {



                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    try {


                        SharedPreferences settings = ctx.getSharedPreferences("Location", 0);
                        final String locationcityname =  settings.getString("Location", "Izmir");


                        SharedPreferences settings87 = ctx.getSharedPreferences("DoWEATHER", 0);
                        final String locality =  settings87.getString("DoWEATHER", "metric");
                        if (locality.equals("metric"))
                        {
                            C_or_F[0] = "C";
                        }
                        else
                        {
                            C_or_F[0] = "F";
                        }


                        request.setURI(new URI("http://api.openweathermap.org/data/2.5/weather?q="+locationcityname +"&mode=json&units="+locality+""));
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return "error";
                    }
                    HttpResponse response = client.execute(request);

                    HttpEntity entity = response.getEntity();

                    if(entity != null){
                        try {
                            JSONObject respObject = new JSONObject(EntityUtils.toString(entity));
                            //String active = respObject.getString("active");
                            String name = respObject.getString("name");
                           // Log.d("DOGA","CUR LOCATION : " + name);
                            String temp =null;
                            Location[0] = name;
                            try
                            {

                                temp = respObject.getString("main");
                                JSONObject respObject2 = new JSONObject(temp);
                                int temp2 = respObject2.getInt("temp");
                             //   Log.d("DO","weather : " + name + ", " + temp2);
                                currentTemp[0] = temp2;
                            }
                            catch(Exception tempErr)
                            {
                            //    Log.e("DOGA_TEMP","ERROR");
                                currentTemp[0] = 00;
                            }

                            try
                            {
                             //   Log.d("DO","WEATHER STATUS STARTED");


                                temp = respObject.getString("weather");

                                JSONArray respObject2 = new JSONArray();

                                // contacts JSONArray
                                JSONArray weather = null;
                                weather = respObject.getJSONArray("weather");
                                JSONObject c = weather.getJSONObject(0);

                                tempStatus[0] = c.getInt("id");
                               // Log.d("DO","WEATHER STATUS TASK ID GOT : "+ tempStatus[0]);




                            }
                            catch(Exception eertew)
                            {
                              //  Log.e("DogaOzkaraca","Weather status failed");
                             //   Log.e("DO","WEATHER STATUS TASK ID FAILED : "+ eertew);
                            }
                            Location[0] = name;

                            //  String tab1_text = respObject.getString("tab1_text");
                        }
                        catch(Exception eer3)
                        {
                            Location[0] = "Weather Error";
                            currentTemp[0] = 00;

                          //  Log.e("DOGAWEATHER","WEATHER TASK FAILED :" + eer3);
                        }
                        //....
                    }
                    else{
                        //Do something here...
                    }


                } catch (IOException e) {
                  //  Log.e("DO","ASYNCTASK WEATHER TASK FAILED");
                    return "error";
                }


                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                if (result != null && result.equals("error"))
                {
                    RotaryHome.rotaryStatusText = "Connection Failed!";
                }
                else {
                    if(!RotaryHome.rotaryStatusText.startsWith("Can't") || isErrorFixed!=false) {
                        RotaryHome.rotaryStatusText = Location[0] + ", " + currentTemp[0] + degreeSymbol + C_or_F[0];
                    }
                }
                RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




    }




}
