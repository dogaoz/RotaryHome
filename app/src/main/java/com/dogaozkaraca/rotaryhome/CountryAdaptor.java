package com.dogaozkaraca.rotaryhome;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CountryAdaptor extends BaseAdapter {
    private Context mContext;
    private List<CountryItem> mListAppInfo;
    public CountryAdaptor(Context ct, List<CountryItem> feedItemList) {
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
        ImageButton fakeCheckBox;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CountryItem article = mListAppInfo.get(position);
  
        if(convertView == null) {
        	
         LayoutInflater inflater = LayoutInflater.from(mContext);
            
       	
       
        	 ViewHolder holder  = new ViewHolder();
       	 convertView = inflater.inflate(R.layout.do_country_item, null);
         holder.ItemTitle = (TextView)convertView.findViewById(R.id.textView1);
         holder.btn = (ImageButton) convertView.findViewById(R.id.button1);
         holder.fakeCheckBox = (ImageButton)  convertView.findViewById(R.id.ImageButton01);
         convertView.setTag(holder);
        
        


        }
        final ViewHolder holder = (ViewHolder)convertView.getTag();

		if (article.getDrawable() !=null)
       holder.btn.setImageDrawable(article.getDrawable());

        holder.btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooser(article.getCountryCoded(), article.getCountry());
            }
        });

        holder.ItemTitle.setText(article.getCountry());
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                openChooser(article.getCountryCoded(),article.getCountry());
        }
        });
        
        
        return convertView;
    }

    public class source_Item
    {
        String title;
        String URL;
        public source_Item(String title,String URL)
        {
            this.title = title;
            this.URL = URL;

        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle()
        {
            return this.title;
        }

        public void setURL(String URL)
        {
            this.URL = URL;
        }

        public String getURL()
        {
            return this.URL;
        }
    }
public void openChooser(final String country, final String articleCountry)
{
    final Dialog dialog2 = new Dialog(mContext);
    dialog2.setCancelable(true);

    final View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.choose_final_item, null);

    final ListView lv = (ListView) view.findViewById(R.id.rss_choose_detailview);
    final TextView tv = (TextView) view.findViewById(R.id.tw);
    final ArrayList lst = new ArrayList<RSS_source_item>();

    new AsyncTask<String,String, String[]>(){

        LoadingDialog dialog = new LoadingDialog(mContext);
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait...");
            this.dialog.setCancelable(false);
            try {

                this.dialog.show();
            }
            catch(Exception e){}
        }
        @Override
        protected String[] doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {
                Log.w("RO_country","try started");
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://rotary.dogaozkaraca.com/Ro_getFeed.php?country="+country));
                Log.w("RO_country", "response getting");
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));

                String aa = in.readLine();
                Log.w("RO_country","text got");

                if (aa == null) {
                    Log.w("RO_country","null will be returned");

                    return null;
                }
                String[] parts = aa.split("SPACE");

                Log.w("RO_country","space splitted");


                return parts;
            } catch (Exception er) {
                er.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(final String[] result) {


            if(result != null)
            {

                for (int x2=0; x2<result.length;x2++)
                {
                    String[]feedProps = result[x2].split("DORO");


                    lst.add(new RSS_source_item(feedProps[0],feedProps[1]));


                        Log.w("RO_country",feedProps[0]+" and " + feedProps[1]);

                }


            }
            else
            {
                Log.w("RO_country","null returned");

            }
            lv.setAdapter(new RSS_source_adaptor(mContext, lst));
            this.dialog.hide();
            tv.setText(articleCountry);
            tv.setBackgroundColor(ColorScheme.getActionBarColor(mContext));
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(view);

            dialog2.show();
        }
    }.execute();
}

}
