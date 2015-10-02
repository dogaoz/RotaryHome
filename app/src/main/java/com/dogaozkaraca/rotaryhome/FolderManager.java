package com.dogaozkaraca.rotaryhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by doga on 20/07/15.
 */
public class FolderManager {

    ///new folder creation

    public static void cancelFolderCreation()
    {
        RotaryHome.packageNameListForNewFolder.clear();
        RotaryHome.newFolderCreationStarted = false;
        RotaryHome.rotaryAllApps.invalidateViews();
        RotaryHome.rotarystatusTV.setText("All Apps");

    }
    public static void beginFolderCreation()
    {
        RotaryHome.newFolderCreationStarted = true;
        RotaryHome.packageNameListForNewFolder = new ArrayList();
    }
    public static void addItemIntoFolderCreation(String packagename)
    {
        RotaryHome.packageNameListForNewFolder.add(packagename);
        RotaryHome.rotarystatusTV.setText("Choose Apps ("+(RotaryHome.packageNameListForNewFolder.size())+")");

    }
    public static boolean isItemExistInCurrentFolderCreation(String packagename)
    {
        for(int x=0;x<RotaryHome.packageNameListForNewFolder.size();x++)
        {
            if (RotaryHome.packageNameListForNewFolder.get(x).equals(packagename)) {
                return true;
            }

        }
        return false;

    }
    public static void removeItemFromFolderCreation(String packagename)
    {
        for(int x=0;x<RotaryHome.packageNameListForNewFolder.size();x++)
        {
            if (RotaryHome.packageNameListForNewFolder.get(x).equals(packagename)) {

                RotaryHome.packageNameListForNewFolder.remove(x);
                RotaryHome.rotarystatusTV.setText("Choose Apps (" + (RotaryHome.packageNameListForNewFolder.size()) + ")");
                Log.d("FolderManager", "remove Item From folder creation");

            }

        }


    }
    public static boolean finalizeFolderCreation()
    {
        try
        {

            //CreateFolder
            int newFolderID = getFolderCount();
            ArrayList loadFoldersList = new ArrayList();

            for(int ar=0;ar<loadFolderArray().length;ar++)
            {
                loadFoldersList.add(loadFolderArray()[ar]);
            }
            loadFoldersList.add("folderNameTest");

            saveFolderArray((String[]) loadFoldersList.toArray(new String[loadFoldersList.size()]));

            //SaveItemsIntoIt
            String[] mItemArray = new String[RotaryHome.packageNameListForNewFolder.size()];
            mItemArray = (String[]) RotaryHome.packageNameListForNewFolder.toArray(mItemArray);
            saveFolderItemsArray(mItemArray,newFolderID);

           // List<String> list = ..;
           // String[] array = list.toArray(new String[list.size()]);

            ////
            RotaryHome.packageNameListForNewFolder.clear();
            RotaryHome.newFolderCreationStarted = false;
            RotaryHome.rotaryAllApps.invalidateViews();
            RotaryHome.rotaryAllApps.invalidateViews();
            RotaryHome.hideAllApps(true,-99);
            RotaryHome.rotarystatusTV.setText(RotaryHome.rotaryStatusText);
            RotaryHome.rotaryView.loadFoldersInRotary();
            RotaryHome.rotaryView.page = newFolderID;
            RotaryHome.rotaryView.invalidate();
            Log.d("FolderManager", "New Folder Created");

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("FolderManager", "New Folder Create Action failed");

            return false;
        }

        //returns true if folder created
    }

    public static int newFolderWithOneItem(String packagename)
    {
        try
        {

            //CreateFolder
            int newFolderID = getFolderCount();
            ArrayList loadFoldersList = new ArrayList();

            for(int ar=0;ar<loadFolderArray().length;ar++)
            {
                loadFoldersList.add(loadFolderArray()[ar]);
            }
            loadFoldersList.add("newFolder");

            saveFolderArray((String[]) loadFoldersList.toArray(new String[loadFoldersList.size()]));

            //SaveItemsIntoIt
            String[] mItemArray = new String[1];
            mItemArray[0] = packagename;
            saveFolderItemsArray(mItemArray,newFolderID);

            // List<String> list = ..;
            // String[] array = list.toArray(new String[list.size()]);
            RotaryHome.rotaryView.loadFoldersInRotary();

            Log.d("FolderManager", "New Folder Created With One Item");

            return newFolderID;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("FolderManager", "New Folder Create With One Item Action failed");

            return -99;
        }

        //returns true if folder created
    }

    public static void showFolderName(int folderId)
    {

        if (folderId == -1)
        {
            RotaryHome.folderNameText.setText("All Apps");
            Log.d("FolderManager", "Folder Name Show action begin!");
        }
        else{
            try {
                String folderName = loadFolderArray()[folderId];
                RotaryHome.folderNameText.setText(folderName);
                Log.d("FolderManager", "Folder Name Show action begin!");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("FolderManager", "Folder Name Show action failed!");


            }
        }

    }
    public static void editFolderName(int folderId,String newString)
    {
        try {

            ArrayList ar = getFolders();
            ar.set(folderId, newString);
            saveFolders(ar);
            Log.d("FolderManager", "Folder Name Edited!");

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.e("FolderManager", "Folder Name Edit Failed!");

        }

    }

    public static void addNewItemIntoFolder(int folderID,String packageName)
    {
        //Load Item List of Folder
        ArrayList appsInFolder = new ArrayList();
        for(int ar=0;ar<loadFolderItemsArray(folderID).length;ar++)
        {
            appsInFolder.add(loadFolderItemsArray(folderID)[ar]);
        }
        //Add New Item
        appsInFolder.add(packageName);

        //Save Item List of Folder
        String[] mItemArray = new String[appsInFolder.size()];
        mItemArray = (String[]) appsInFolder.toArray(mItemArray);
        saveFolderItemsArray(mItemArray, folderID);
        Log.d("FolderManager", "Item Added!");
        RotaryHome.rotaryView.loadFoldersInRotary();
        RotaryHome.rotaryView.invalidate();

    }


    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    /////////FOLDER GET ACTIONS
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    public static ArrayList getFolders()
    {
        Log.d("FolderManager", "Get Folders Started");

        ArrayList loadFolderList = new ArrayList();

        for(int ar=0;ar<loadFolderArray().length;ar++)
        {

            loadFolderList.add(loadFolderArray()[ar]);
            Log.w("FolderManager", "Got Folder :" + loadFolderArray()[ar]);

        }
        Log.d("FolderManager", "Get Folders Completed");

        return loadFolderList;
    }
    public static void saveFolders(ArrayList ar)
    {

        saveFolderArray((String[]) ar.toArray(new String[ar.size()]));
        Log.d("FolderManager", "Folders Saved!");

    }
    public static int getFolderCount()
    {
        SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolders", 0);
        int size = prefs.getInt("myFolder" + "_size", 0);

        for(int ar=0;ar<loadFolderArray().length;ar++)
        {
            Log.w("FolderManager", "Got Folder :" + loadFolderArray()[ar]);
        }
        Log.d("FolderManager", "Get Folder Count!");

        return size;
    }
    public static int getItemCountInFolder(int folderId)
    {
        Log.d("FolderManager", "Get Item Count In Folder!");

        return loadFolderItemsArray(folderId).length;
    }

    public static ArrayList getItemsInFolder(int folderId)
    {
        if (folderId <= getFolderCount())
        {
            ArrayList loadFolderItemList = new ArrayList();

            for(int ar=0;ar<loadFolderItemsArray(folderId).length;ar++)
            {
                loadFolderItemList.add(loadFolderItemsArray(folderId)[ar]);
            }
            return loadFolderItemList;

        }

        return null;
    }

    public static void removeAppFromFolder(final int page,final int appId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(RotaryHome.ctx,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to remove "+getAppName(page,appId)+" from this folder?");
        builder .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                    }
                })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();


                        ArrayList loadAppListInFolder = new ArrayList();

                        for (int ar = 0; ar < loadFolderItemsArray(page).length; ar++) {
                            loadAppListInFolder.add(loadFolderItemsArray(page)[ar]);
                        }
                        for (int rm = 0; rm < loadAppListInFolder.size(); rm++) {
                            if (rm == appId) {
                                loadAppListInFolder.remove(rm);
                            }
                        }
                        saveFolderItemsArray((String[]) loadAppListInFolder.toArray(new String[loadAppListInFolder.size()]), page);
                        if (loadAppListInFolder.size() ==0)
                        {
                            removeFolder(page,true);
                        }
                        //RemoveItemsFromRemovedFolder
                        RotaryHome.rotaryView.invalidate();
                        Log.d("FolderManager", "App Removed From Folder!");

                    }
                });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();


    }


    public static int getColorOfFolder(int folderId)
    {

        return Color.WHITE;
    }

    public static String getFolderName(int folderId)
    {

        return loadFolderArray()[folderId];
    }
    public static void removeFolder(final int page,boolean withoutConfirmation) {

        if(withoutConfirmation)
        {
            ArrayList loadFoldersList = new ArrayList();

            for (int ar = 0; ar < loadFolderArray().length; ar++) {
                loadFoldersList.add(loadFolderArray()[ar]);
            }
            for (int rm = 0; rm < loadFoldersList.size(); rm++) {
                if (rm == page) {
                    loadFoldersList.remove(rm);
                }
            }
            saveFolderArray((String[]) loadFoldersList.toArray(new String[loadFoldersList.size()]));

            //RemoveItemsFromRemovedFolder
            prepareRemovingFolderItemsFromArray(page);
            dropRemovedFolderItemsFromArray(page);
            RotaryHome.rotaryView.loadFoldersInRotary();
            RotaryHome.rotaryView.page = 0;
            RotaryHome.deActivateFolderEditMode();
            Log.d("FolderManager", "Folder Removed !");
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(RotaryHome.ctx, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            builder.setTitle("Are you sure?");
            builder.setMessage("Do you want to remove this folder?");
            builder.setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();
                        }
                    })
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();

                            ArrayList loadFoldersList = new ArrayList();

                            for (int ar = 0; ar < loadFolderArray().length; ar++) {
                                loadFoldersList.add(loadFolderArray()[ar]);
                            }
                            for (int rm = 0; rm < loadFoldersList.size(); rm++) {
                                if (rm == page) {
                                    loadFoldersList.remove(rm);
                                }
                            }
                            saveFolderArray((String[]) loadFoldersList.toArray(new String[loadFoldersList.size()]));

                            //RemoveItemsFromRemovedFolder
                            prepareRemovingFolderItemsFromArray(page);
                            dropRemovedFolderItemsFromArray(page);
                            RotaryHome.rotaryView.loadFoldersInRotary();
                            RotaryHome.rotaryView.page = 0;
                            RotaryHome.deActivateFolderEditMode();
                            Log.d("FolderManager", "Folder Removed !");

                        }
                    });
            AlertDialog alertdialog = builder.create();
            alertdialog.show();
        }

    }




    public static boolean saveFolderArray(String[] array) {
        SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolders", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("myFolder" + "_size", array.length);
        for(int i=0;i<array.length;i++) {
            editor.putString("myFolder" + "_" + i, array[i]);
            Log.d("FolderManager","folder name is saved :"+array[i]);
        }
        return editor.commit();
    }

    public static String[] loadFolderArray() {
        SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolders", 0);
        int size = prefs.getInt("myFolder" + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++) {
            array[i] = prefs.getString("myFolder" + "_" + i, null);
            Log.d("FolderManager","folder name is loaded :"+array[i]);

        }
        return array;
    }
    public static boolean saveFolderItemsArray(String[] array, int newFolderID) {
        SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolderItems", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("itemsOfFolderID"+newFolderID +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString("itemsOfFolderID"+newFolderID + "_" + i, array[i]);
        return editor.commit();
    }

    public static boolean prepareRemovingFolderItemsFromArray(int folderID) {
        SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolderItems", 0);
        int size = prefs.getInt("itemsOfFolderID" + folderID + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString("itemsOfFolderID"+folderID + "_" + i, null);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("itemsOfFolderID"+folderID +"_size", 9555);
        for(int i=0;i<array.length;i++)
            editor.putString("itemsOfFolderID"+folderID + "_" + i, "cleared");
        return editor.commit();
    }


    private static void dropRemovedFolderItemsFromArray(int removedItemPosition) {


            SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolderItems", 0);
            int size = prefs.getInt("itemsOfFolderID" + removedItemPosition + "_size", 0);
            if (size==9555)//Double Check if removed
            {

                for (int k=removedItemPosition;k<(getFolderCount()+1);k++)
                {
                    String[] ld = loadFolderItemsArray(k+1);

                    saveFolderItemsArray(ld,k);

                    if(k==getFolderCount())
                    {
                        //Dispose last not necessary page
                        size = prefs.getInt("itemsOfFolderID" + (k+1) + "_size", 0);
                        String array[] = new String[size];
                        for(int i=0;i<size;i++)
                            array[i] = prefs.getString("itemsOfFolderID"+(k+1) + "_" + i, null);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("itemsOfFolderID"+(k+1) +"_size", 0);
                        for(int i=0;i<array.length;i++)
                            editor.putString("itemsOfFolderID"+(k+1) + "_" + i, null);
                       editor.commit();


                    }


                }
            }
    }

    public static String[] loadFolderItemsArray(int folderID) {
        SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("RotaryFolderItems", 0);
        int size = prefs.getInt("itemsOfFolderID" + folderID + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString("itemsOfFolderID"+folderID + "_" + i, null);
        return array;
    }



    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    /////////ITEM GET ACTIONS
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    public static Drawable getIcon(int folderId,int itemId)
    {
        String packageName = (String) FolderManager.getItemsInFolder(folderId).get(itemId);

        final PackageManager pm = RotaryHome.ctx.getApplicationContext().getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            if (RotaryHome.rotaryView.abc != null && RotaryHome.rotaryView.abc.size() >2 )
            {
                for(int app=0;app<RotaryHome.rotaryView.abc.size();app++)
                {
                    if (RotaryHome.rotaryView.abc.get(app).packageName.equals(packageName))
                    {
                        return RotaryHome.rotaryView.abc.get(app).icon;
                    }


                }
            }
            else {
                Drawable currentApp = ai.loadIcon(pm);
                SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE);

                boolean iconPackApplied = prefs.getBoolean("isIconPackApplied", false);
                String appliedIconPackPackageName = "";

                if (iconPackApplied) {
                    appliedIconPackPackageName = prefs.getString("appliedIconPackPackageName", "default");
                }
                Resources ress = null;
                int i5 = -999;
                XmlResourceParser xpp = null;
                if (iconPackApplied == true && !appliedIconPackPackageName.equals("default")) {
                    try {
                        ress = RotaryHome.ctx.getPackageManager().getResourcesForApplication(appliedIconPackPackageName);
                        i5 = ress.getIdentifier("appfilter", "xml", appliedIconPackPackageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.e("RotaryComp","error,IconPackDisabled");
                        iconPackApplied = false;
                    }
                }
                if (iconPackApplied == true && !appliedIconPackPackageName.equals("default") && !appliedIconPackPackageName.equals("") && ress != null) {
                    String componentName = RotaryHome.ctx.getPackageManager().getLaunchIntentForPackage(packageName).getComponent().toString();

                    xpp = ress.getXml(i5);


                    try {
                        try {

                            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {

                                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                                    if (xpp.getAttributeCount() != 0 && xpp.getAttributeValue(0).equals(componentName)) {
                                        //Log.w("RotaryComp","icon set");
                                        return resize(ress.getDrawable(ress.getIdentifier(xpp.getAttributeValue(1), "drawable", appliedIconPackPackageName)));

                                    }

                                }


                                xpp.next();
                            }
                        } catch (Exception e) {  // Log.w("RotaryComp","exception");

                            e.printStackTrace();
                        }


                        //Log.w("RotaryComp","icon error,default loaded");
                        return resize(currentApp);


                    } catch (Exception e) {
                        e.printStackTrace();
                        return resize(currentApp);

                    }


                } else {
                    return resize(currentApp);
                }
            }
        } catch (Exception e) {return null;}

        return null;
    }
    public static String getPackageName(int folderId,int itemId) {
        return (String) FolderManager.getItemsInFolder(folderId).get(itemId);
    }
    public static String getAppName(int folderId,int itemId)
    {
        String packageName = (String) FolderManager.getItemsInFolder(folderId).get(itemId);

        final PackageManager pm = RotaryHome.ctx.getApplicationContext().getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            return pm.getApplicationLabel(ai).toString();

        } catch (Exception e) {return null;}


    }

    public static void launch(int folderId, int itemId)
    {

        String packagename = getPackageName(folderId,itemId);
        Intent i=new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(RotaryHome.ctx.getPackageManager().getLaunchIntentForPackage(packagename).getComponent());

        RotaryHome.ctx.startActivity(i);

    }


    private static Drawable resize(Drawable image) {
        if(image.getIntrinsicHeight() !=(int)RotaryView.convertDpToPixel(48) && image.getIntrinsicHeight() !=(int)RotaryView.convertDpToPixel(48)) {

            if(image instanceof BitmapDrawable) {
                // getApplicationIcon gives a Drawable which is then cast as a BitmapDrawable
                //icon = Bitmap.createScaledBitmap(((BitmapDrawable) apkIcon).getBitmap(), 32, 32, true);
                Bitmap b = ((BitmapDrawable) image).getBitmap();
                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, (int) RotaryView.convertDpToPixel(48), (int) RotaryView.convertDpToPixel(48), false);
                return new BitmapDrawable(RotaryHome.ctx.getResources(), bitmapResized);
            }
            else {
                Bitmap icon = Bitmap.createBitmap(image.getIntrinsicWidth(), image.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(icon);
                image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                image.draw(canvas);
                Bitmap bitmapResized = Bitmap.createScaledBitmap(icon, (int) RotaryView.convertDpToPixel(48), (int) RotaryView.convertDpToPixel(48), false);

                return new BitmapDrawable(RotaryHome.ctx.getResources(), bitmapResized);

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
                return new BitmapDrawable(RotaryHome.ctx.getResources(), icon);

            }
        }
    }



    ///////////////DIALOGS

    public static void moreThan8itemsError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RotaryHome.ctx,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Folder is Full");
        builder.setMessage("This folder has reached maximum number of apps.");
        builder .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }

    public static void uninstallApp(final String packagename)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(RotaryHome.ctx,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Uninstall");
        builder.setMessage("Do you want to uninstall " + getAppNameFromPackageName(packagename) + " ?");
        builder .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();

                        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", packagename, null));
                        RotaryHome.ctx.startActivity(intent);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }

    public static boolean isSystemApp(String packageName)
    {

        final PackageManager pm = RotaryHome.ctx.getApplicationContext().getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            return (ai.flags
                    & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0;

        } catch (Exception e) {return false;}

    }
    private static String getAppNameFromPackageName(String packageName) {

        final PackageManager pm = RotaryHome.ctx.getApplicationContext().getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);

            return pm.getApplicationLabel(ai).toString();

        } catch (Exception e) {return null;}

    }


    /////FOLDER RESTORE FROM OLD APP IMPORTANCE USED APPS


    private static void clearAllImportanceValues() {
        for(int x=0;x<RotaryHome.rotaryView.abc.size();x++)
        {
            if( RotaryHome.rotaryView.abc.get(x).appImportance > 0)
            {
                SharedPreferences prefs = RotaryHome.ctx.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE);
                prefs.edit().putInt(RotaryHome.rotaryView.abc.get(x).packageName+RotaryHome.rotaryView.abc.get(x).activityName, 0).commit();

            }
        }
    }

    public static void firstTimeForFolders()
    {

        ArrayList<PInfo> KlasorRestorasyonu = new ArrayList<PInfo>();;
        for(int x=0;x<RotaryHome.rotaryView.abc.size();x++)
        {
           if( RotaryHome.rotaryView.abc.get(x).appImportance > 0)
           {
                KlasorRestorasyonu.add(RotaryHome.rotaryView.abc.get(x));
           }
        }

        //RestoreIfAppIsUpgraded
        if (KlasorRestorasyonu.size() >0)
        {
            newUpdateDialog();
           restoreFoldersFromAppImportanceVersions(KlasorRestorasyonu);
           clearAllImportanceValues();


        }
        else //Load Default Scheme
        {
            loadDefaultScheme();

        }
        RotaryHome.rotaryView.loadFoldersInRotary();
        RotaryHome.rotaryView.invalidate();
        RotaryHome.ctx.getSharedPreferences("rotaryFolders", 0).edit().putBoolean("isFolderFirstTime", false).commit();
    }

    public static void restoreFoldersFromAppImportanceVersions(ArrayList<PInfo> itemList)
    {
        boolean isNotDividable = false;
        int pageCount=itemList.size()/8;


        if (itemList.size()/8 % 8 !=0)
        {
            isNotDividable = true;
            pageCount++;
        }

        if (itemList.size() <8)
        {
            isNotDividable = true;
            pageCount++;

        }
        //CreateFolderAtMax8items
        for (int itemsA=0;itemsA<pageCount;itemsA++) {
            try {


                int newFolderID = getFolderCount();
                ArrayList loadFoldersList = new ArrayList();

                for (int ar = 0; ar < loadFolderArray().length; ar++) {
                    loadFoldersList.add(loadFolderArray()[ar]);
                }
                loadFoldersList.add("New Awesome Folder");

                saveFolderArray((String[]) loadFoldersList.toArray(new String[loadFoldersList.size()]));

                //SaveItemsIntoIt
                int k =8;
                if (itemsA == pageCount-1 && isNotDividable)
                {
                    k = itemList.size() -8*(itemList.size()/8);
                }
                String[] mItemArray = new String[k];

                for (int a=0;a<k;a++)
                {
                    mItemArray[a] = itemList.get((itemsA*8)+a).packageName;

                }
                saveFolderItemsArray(mItemArray, newFolderID);

                // List<String> list = ..;
                // String[] array = list.toArray(new String[list.size()]);

                Log.d("FolderManager", "New Folder Created With One Item");
            } catch (Exception e) {

            }
        }

    }

    public static void loadDefaultScheme()
    {
        try
        {

            //CreateFolder
            int newFolderID = getFolderCount();
            ArrayList loadFoldersList = new ArrayList();

            for(int ar=0;ar<loadFolderArray().length;ar++)
            {
                loadFoldersList.add(loadFolderArray()[ar]);
            }
            loadFoldersList.add("New Awesome Folder");

            saveFolderArray((String[]) loadFoldersList.toArray(new String[loadFoldersList.size()]));

            //SaveItemsIntoIt
            String[] mItemArray = new String[8];
            for (int ap=0;ap<8;ap++)
            {
                mItemArray[ap] = RotaryHome.rotaryView.abc.get(ap).packageName;
            }
            saveFolderItemsArray(mItemArray,newFolderID);

            // List<String> list = ..;
            // String[] array = list.toArray(new String[list.size()]);

            Log.d("FolderManager", "New Folder Created With One Item");

        }
        catch (Exception e) {
        }

        //returns true if folder created
    }

    public static void newUpdateDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(RotaryHome.ctx,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("RotaryHome Updated!");
        builder.setMessage("Hey, We have just updated your homescreen!\nAnd we added new 'Folder Feature'. So you can create,edit and remove folders on your homescreen now. No more complexity while editing and opening your apps.\n\nP.S: RotaryHome took care of everything for you. We've created folders by using your old preferences,and moved rest of the unused apps into All Apps");
        builder .setCancelable(false)
                .setPositiveButton("Perfect!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();

                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }

}
