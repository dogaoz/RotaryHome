package com.dogaozkaraca.rotaryhome;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class AllAppsAdaptor extends BaseAdapter {
    private Context mContext;
    private ArrayList<PInfo> mListAppInfo;
    public AllAppsAdaptor(Context ct, ArrayList<PInfo> feedItemList) {
        mContext = ct;
        mListAppInfo = feedItemList;
        

    }
 
    @Override
    public int getCount() {
        return mListAppInfo.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mListAppInfo.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public class ViewHolder{
        TextView ItemTitle;
        ImageButton appIcon;
        ImageButton fakeCheckBox;

        RelativeLayout rl;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PInfo article = mListAppInfo.get(position);
  
        if(convertView == null) {
        	
         LayoutInflater inflater = LayoutInflater.from(mContext);
            
       	
       
        	 ViewHolder holder  = new ViewHolder();
       	 convertView = inflater.inflate(R.layout.all_apps_item, null);
         holder.ItemTitle = (TextView)convertView.findViewById(R.id.textView1);
         holder.appIcon = (ImageButton) convertView.findViewById(R.id.button1);
         holder.rl = (RelativeLayout) convertView.findViewById(R.id.all_apps_item_rl);

         holder.fakeCheckBox = (ImageButton) convertView.findViewById(R.id.imageButton01);
         convertView.setTag(holder);


        


        }
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        holder.appIcon.setImageDrawable(article.icon);
        holder.ItemTitle.setText(article.appname);

        ///Check whether item ticked for folder creation
        if (RotaryHome.newFolderCreationStarted) {
            if (FolderManager.isItemExistInCurrentFolderCreation(article.packageName)) {
                holder.fakeCheckBox.setVisibility(View.VISIBLE);
                holder.fakeCheckBox.setImageResource(R.drawable.fake_checkbox_bg_true);
            }
            else
            {
                holder.fakeCheckBox.setVisibility(View.GONE);

            }

        }
        else
        {holder.fakeCheckBox.setVisibility(View.GONE);}
        /////////////////////////////////////////////////


        holder.rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RotaryHome.isUninstallMode) {
                    FolderManager.uninstallApp(article.packageName);
                }
                else if (RotaryHome.newFolderCreationStarted) {
                    if (FolderManager.isItemExistInCurrentFolderCreation(article.packageName)) {
                        holder.fakeCheckBox.setVisibility(View.GONE);
                        FolderManager.removeItemFromFolderCreation(article.packageName);
                    } else {
                        holder.fakeCheckBox.setVisibility(View.VISIBLE);
                        holder.fakeCheckBox.setImageResource(R.drawable.fake_checkbox_bg_true);
                        FolderManager.addItemIntoFolderCreation(article.packageName);
                    }

                } else {
                    article.launch();
                }
            }
        });

        holder.ItemTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RotaryHome.isUninstallMode) {
                    FolderManager.uninstallApp(article.packageName);
                }
                else if (RotaryHome.newFolderCreationStarted) {
                    if (FolderManager.isItemExistInCurrentFolderCreation(article.packageName)) {
                        holder.fakeCheckBox.setVisibility(View.GONE);
                        FolderManager.removeItemFromFolderCreation(article.packageName);
                    } else {
                        holder.fakeCheckBox.setVisibility(View.VISIBLE);
                        holder.fakeCheckBox.setImageResource(R.drawable.fake_checkbox_bg_true);
                        FolderManager.addItemIntoFolderCreation(article.packageName);
                    }

                } else {
                    article.launch();
                }
            }
        });

        holder.appIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RotaryHome.isUninstallMode) {
                    FolderManager.uninstallApp(article.packageName);
                }
                else if (RotaryHome.newFolderCreationStarted) {
                    if (FolderManager.isItemExistInCurrentFolderCreation(article.packageName)) {
                        holder.fakeCheckBox.setVisibility(View.GONE);
                        FolderManager.removeItemFromFolderCreation(article.packageName);
                    } else {
                        holder.fakeCheckBox.setVisibility(View.VISIBLE);
                        holder.fakeCheckBox.setImageResource(R.drawable.fake_checkbox_bg_true);
                        FolderManager.addItemIntoFolderCreation(article.packageName);
                    }

                } else {
                    article.launch();
                }
            }
        });


        holder.ItemTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!RotaryHome.isUninstallMode) {
                    RotaryHome.revealView.dragNdropItemPackageName = article.packageName;
                    RotaryHome.revealView.dragNdropItemBitmap = ((BitmapDrawable) article.icon).getBitmap();
                    RotaryHome.revealView.isIndragNdrop = true;
                    RotaryHome.revealView.invalidate();
                }

                return false;
            }
        });

        holder.appIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!RotaryHome.isUninstallMode) {
                    RotaryHome.revealView.dragNdropItemPackageName = article.packageName;
                    RotaryHome.revealView.dragNdropItemBitmap = ((BitmapDrawable) article.icon).getBitmap();
                    RotaryHome.revealView.isIndragNdrop = true;
                    RotaryHome.revealView.invalidate();
                }
                return true;
            }
        });

        holder.rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!RotaryHome.isUninstallMode) {
                    RotaryHome.revealView.dragNdropItemPackageName = article.packageName;
                    RotaryHome.revealView.dragNdropItemBitmap = ((BitmapDrawable) article.icon).getBitmap();
                    RotaryHome.revealView.isIndragNdrop = true;
                    RotaryHome.revealView.invalidate();
                }
                return true;
            }
        });

        return convertView;
    }




}
