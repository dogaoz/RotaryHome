package com.dogaozkaraca.rotaryhome;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;

/**
 * Created by doga on 21/07/15.
 */


public class RevealView_DragNdropView extends View {
    //drag&Drop
    public static String dragNdropItemPackageName;
    public static boolean isIndragNdrop = false;
    public static float dragNdropItem_X;
    public static float dragNdropItem_Y;
    public static int draggingApp_start_x;
    public static int draggingApp_start_y;
    public static int draggingApp_end_x;
    public static int draggingApp_end_y;
    float itemLocation_X;
    float itemLocation_Y;
    public static Bitmap dragNdropItemBitmap;

    Paint paint;
    public RevealView_DragNdropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);






    }

    public RevealView_DragNdropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

    }


    @Override
    public void onDraw(Canvas canvas)
    {
        if(isIndragNdrop)
        {
            try {
                canvas.drawBitmap(dragNdropItemBitmap, (int) dragNdropItem_X - RotaryHome.rotaryView.convertDpToPixel(30), (int) dragNdropItem_Y- RotaryHome.rotaryView.convertDpToPixel(30), paint);
            }
            catch(Exception e)
            {

            }


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        dragNdropItem_X =e.getRawX(); // - RotaryHome.rotaryView.convertDpToPixel(53);
        dragNdropItem_Y =e.getRawY(); //+ RotaryHome.rotaryView.convertDpToPixel(25);

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                //Touches onto apps
                Log.d("FolderManager", "ACTION_UP");

                if (isIndragNdrop)
                {
                    //holdingpage=false;
                    // isIndragNdrop = false;
                    boolean isAddedIntoFolder = false;
                    //Move App Into already existing button
                    itemLocation_X = e.getX();//+ RotaryHome.rotaryView.convertDpToPixel(24);

                    itemLocation_Y = e.getY() ;//+ RotaryHome.rotaryView.convertDpToPixel(24);

                    for (int x = 0; x < FolderManager.getFolderCount(); x++) {
                        Log.w("RotaryRenderer","############START###########");
                        Log.w("RotaryRenderer","Page Item Left = " + RotaryHome.rotaryView.page_circ_start_x[x + 1]);
                        Log.w("RotaryRenderer","Page Item Right = " + RotaryHome.rotaryView.page_circ_end_x[x + 1]);
                        Log.w("RotaryRenderer","Page Item Top = " + RotaryHome.rotaryView.page_circ_start_y[x + 1]);
                        Log.w("RotaryRenderer","Page Item Bottom = " + RotaryHome.rotaryView.page_circ_end_y[x + 1]);
                        Log.w("RotaryRenderer","############################");
                        Log.w("RotaryRenderer","============================");
                        Log.w("RotaryRenderer","itemRawX = " + dragNdropItem_X);
                        Log.w("RotaryRenderer","itemRawY = " + dragNdropItem_Y);
                        Log.w("RotaryRenderer","itemX = " + e.getX());
                        Log.w("RotaryRenderer","itemY = " + e.getY());
                        Log.w("RotaryRenderer","itemLocationX = " + itemLocation_X);
                        Log.w("RotaryRenderer","itemLocationY = " + itemLocation_Y);

                        Log.w("RotaryRenderer","=============END============");
                        if (itemLocation_X > RotaryHome.rotaryView.page_circ_start_x[x + 1] && itemLocation_X < RotaryHome.rotaryView.page_circ_end_x[x + 1] && itemLocation_Y > RotaryHome.rotaryView.page_circ_start_y[x + 1] && itemLocation_Y < RotaryHome.rotaryView.page_circ_end_y[x + 1]*2) {



                            //MoveItemToFolder
                            if (FolderManager.getItemCountInFolder(x) >= 8)
                            {
                                //Error Dialog, max item count
                                Log.e("FolderManager", "Item cannot be added, Folder has more than 8 items");
                                FolderManager.moreThan8itemsError();
                                break;
                            }
                            else
                            {
                                //Add Item into folder
                                FolderManager.addNewItemIntoFolder(x, dragNdropItemPackageName);
                                this.setBackgroundColor(Color.parseColor("#80000000"));

                                //Navigate To Folder
                                isIndragNdrop = false;
                                RotaryHome.rotaryView.holdingpage = false;
                                RotaryHome.rotaryView.center_circle= false;
                                invalidate();
                                RotaryHome.hideAllApps(false,x);
                                isAddedIntoFolder = true;
                                break;


                            }



                            //Set Page and invalidate
                            //  page = x;


                        }




                    }

                    if (e.getX() > RotaryHome.rotaryView.all_apps_start_x &&
                            e.getX() < RotaryHome.rotaryView.all_apps_end_x &&
                            e.getY() > RotaryHome.rotaryView.all_apps_start_y &&
                            e.getY() < RotaryHome.rotaryView.all_apps_end_y) {

                        //createNewFolder
                        int result = FolderManager.newFolderWithOneItem(dragNdropItemPackageName);
                        if (result!=-99)
                        {
                            isIndragNdrop = false;
                            RotaryHome.rotaryView.holdingpage = false;
                            RotaryHome.rotaryView.center_circle= false;
                            invalidate();
                            this.setBackgroundColor(Color.parseColor("#80000000"));
                            RotaryHome.hideAllApps(false, result);
                            isAddedIntoFolder = true;


                        }
                        break;

                    }
                    else if (!isAddedIntoFolder)
                    {
                        //Drag&Drop Cancelled
                        isIndragNdrop = false;
                        RotaryHome.rotaryView.holdingpage = false;
                        RotaryHome.rotaryView.center_circle= false;
                        this.setBackgroundColor(Color.parseColor("#80000000"));
                        RotaryHome.rotaryView.page = RotaryHome.rotaryView.LastPage;
                        RotaryHome.rotaryView.invalidate();
                        invalidate();
                        RotaryHome.showAllApps(false,0,0);
                        break;

                    }



                }

                break;

            case MotionEvent.ACTION_MOVE:

                if (isIndragNdrop)
                {
                    if (!RotaryHome.rotaryView.holdingpage ||  !RotaryHome.rotaryView.center_circle)
                    {

                        RotaryHome.rotaryView.setVisibility(View.VISIBLE);
                        RotaryHome.rotaryView.holdingpage = true;
                        RotaryHome.rotaryView.center_circle= true;
                        RotaryHome.revealView.setBackgroundColor(Color.parseColor("#00000000"));
                        RotaryHome.rotaryAllApps.setVisibility(View.GONE);
                        RotaryHome.rotaryView.invalidate();

                    }
                    //holdingpage=true;

                    invalidate();
                    itemLocation_X = e.getX();//+ RotaryHome.rotaryView.convertDpToPixel(24);

                    itemLocation_Y = e.getY() ;//+ RotaryHome.rotaryView.convertDpToPixel(24);
                    //Bright page when item is on folder
                    for (int x = -1; x < FolderManager.getFolderCount(); x++) {
                        if (x != -1 && itemLocation_X > RotaryHome.rotaryView.page_circ_start_x[x + 1] && itemLocation_X < RotaryHome.rotaryView.page_circ_end_x[x + 1] && itemLocation_Y > RotaryHome.rotaryView.page_circ_start_y[x + 1] && itemLocation_Y < RotaryHome.rotaryView.page_circ_end_y[x + 1]*2) {


                            //Set Page and invalidate
                            RotaryHome.rotaryView.page = x;
                            RotaryHome.rotaryView.invalidate();

                        }
                        else if (e.getX() > RotaryHome.rotaryView.all_apps_start_x &&
                                e.getX() < RotaryHome.rotaryView.all_apps_end_x &&
                                e.getY() > RotaryHome.rotaryView.all_apps_start_y &&
                                e.getY() < RotaryHome.rotaryView.all_apps_end_y) {

                            RotaryHome.rotaryView.page = -1;
                            RotaryHome.rotaryView.invalidate();
                            break;

                        }

                    }



                }
                break;
            case MotionEvent.ACTION_DOWN:

                // startAngle = Math.atan2(e.getY() - circ_start_y, e.getX() - circ_start_x);
                //  startAngle =  Math.toDegrees(startAngle);
                //  RotaryView.x = (int)startAngle;
                // Log.w("RotaryView","startAngle : " + startAngle);
                break;

        }



        return true;
    }

}
