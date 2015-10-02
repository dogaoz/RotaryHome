package com.dogaozkaraca.rotaryhome;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class IconPackAdaptor extends BaseAdapter {
    private Context mContext;
    private List<IconPackItem> mListAppInfo;
    public IconPackAdaptor(Context ct, List<IconPackItem> feedItemList) {
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
        ImageButton btn;
        CircleButton circleButton;
        RelativeLayout rl;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IconPackItem article = mListAppInfo.get(position);
  
        if(convertView == null) {
        	
         LayoutInflater inflater = LayoutInflater.from(mContext);
            
       	
       
        	 ViewHolder holder  = new ViewHolder();
       	 convertView = inflater.inflate(R.layout.icon_pack_item, null);
         holder.ItemTitle = (TextView)convertView.findViewById(R.id.textView1);
         holder.btn = (ImageButton) convertView.findViewById(R.id.button1);
         holder.circleButton = (CircleButton)  convertView.findViewById(R.id.set_iconpack_btn);
         holder.rl = (RelativeLayout) convertView.findViewById(R.id.icon_pack_item_rl);
         convertView.setTag(holder);
        
        


        }
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        if ((article.getPackageName().equals("com.dogaozkaraca.rotaryhome") && mContext.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE).getString("appliedIconPackPackageName","default").equals("default")) || mContext.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE).getString("appliedIconPackPackageName","default").equals(article.getPackageName()))
        {
            holder.rl.setBackgroundColor(Color.parseColor("#D8F8FF"));
        }
        else
        {
            holder.rl.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
        PackageManager pm = mContext.getPackageManager();
        try {
            ApplicationInfo af = pm.getApplicationInfo(article.getPackageName(), 0);

            if (article.getPackageName().equals("com.dogaozkaraca.rotaryhome"))
            {
                holder.ItemTitle.setText("Default");

            }
            else {
                holder.ItemTitle.setText(pm.getApplicationLabel(af));
            }
            holder.btn.setImageDrawable(pm.getApplicationIcon(af));
        }
        catch (Exception e)
        {
            holder.btn.setVisibility(View.GONE);
        }
        holder.circleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (article.getPackageName().equals("com.dogaozkaraca.rotaryhome"))
                {
                    mContext.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("appliedIconPackPackageName","default")
                            .putBoolean("isIconPackApplied",false)
                            .commit();
                    Toast.makeText(mContext, "Icon pack applied!", Toast.LENGTH_LONG).show();
                    article.getDialog().dismiss();
                }
                else {
                    mContext.getSharedPreferences("Rotary_App_Prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("appliedIconPackPackageName", article.getPackageName())
                            .putBoolean("isIconPackApplied",true)
                            .commit();
                    Toast.makeText(mContext, "Icon pack applied!", Toast.LENGTH_LONG).show();
                    article.getDialog().dismiss();
                }
                RotaryHome.rotaryStatus=1;
            }
        });

        holder.circleButton.setColor(ColorScheme.getBackgroundColor(mContext));

        
        return convertView;
    }




}
