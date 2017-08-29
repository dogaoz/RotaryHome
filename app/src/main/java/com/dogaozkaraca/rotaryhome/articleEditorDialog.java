package com.dogaozkaraca.rotaryhome;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import net.frakbot.imageviewex.Converters;
import net.frakbot.imageviewex.ImageViewEx;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by doga on 10/05/15.
 */
public class articleEditorDialog extends ProgressDialog implements DialogInterface.OnDismissListener {
    String articleTitle;
    String URL;
    int position;
    public articleEditorDialog(Context context,String articleTitle,String URL,int position)
    {
        super(context);
        this.articleTitle = articleTitle;
        this.URL = URL;
        this.position = position;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_editor_dialog);

        final EditText title = (EditText) findViewById(R.id.editText);
        title.setText(articleTitle);


        EditText url = (EditText) findViewById(R.id.editText2);
        url.setText(URL);

        Button saveChanges = (Button) findViewById(R.id.button5);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTitle(position,articleTitle,title.getText().toString());
                DoWorkspace.reloadOfflineView();
                dismiss();
            }
        });

        Button deleteArticle = (Button) findViewById(R.id.button6);

        deleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearThatOne(position,articleTitle,URL);
                DoWorkspace.reloadOfflineView();
                dismiss();
            }
        });



    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    private void changeTitle(int position,String oldtitle,String newTitle)
    {
        /////////////////////TITLE////////////////////////////
        //Load
        String[] titleList = loadArray("title", getContext());


        List<String> titles = new LinkedList<String>(Arrays.asList(titleList));
        //Do the things

        int findWhereItis = 970324;
        for(int x=0;x<titles.size();x++)
        {
            if(titles.get(x).equals(oldtitle))
            {
                findWhereItis = x;
            }

        }

        Log.w("myfeed", "changed title : " + titles.get(findWhereItis));
        titles.set(findWhereItis,newTitle);

        //Convertback
        String[] simpleArray = new String[titles.size()];
        titles.toArray(simpleArray);


        //Save
        saveArray(simpleArray, "title", getContext());
    }

    private void clearThatOne(int position,String title,String url)
    {
        /////////////////////TITLE////////////////////////////
        //Load
        String[] titleList = loadArray("title", getContext());


        List<String> titles = new LinkedList<String>(Arrays.asList(titleList));
        //Do the things

        int findWhereItis = 970324;
        for(int x=0;x<titles.size();x++)
        {
            if(titles.get(x).equals(title))
            {
                findWhereItis = x;
            }

        }

        Log.w("myfeed", "removed title : " + titles.get(findWhereItis));
        titles.remove(findWhereItis);

        //Convertback
        String[] simpleArray = new String[titles.size()];
        titles.toArray(simpleArray);


        //Save
        saveArray(simpleArray, "title", getContext());



        ////////URL

        //Load
        String[] urlList = loadArray("url", getContext());


        List<String> urls =new LinkedList<String>(Arrays.asList(urlList));
        //Do the things
        int findWhereItis2 = 970324;
        for(int x=0;x<urls.size();x++)
        {
            if(urls.get(x).equals(url))
            {
                findWhereItis2 = x;
            }

        }
        Log.w("myfeed","removed url : " + urls.get(findWhereItis2));
        urls.remove(findWhereItis2);
        //Convertback
        String[] simpleArray2 = new String[urls.size()];
        urls.toArray(simpleArray2);
        //Save
        saveArray(simpleArray2, "url", getContext());

        /////////////////////////

        ////////PostImage

        //Load
        String[] PostImageList = loadArray("postimage", getContext());


        List<String> PostImageurls =new LinkedList<String>(Arrays.asList(PostImageList));
        //Do the things
        int findWhereItis3 = findWhereItis;
        Log.w("myfeed","removed postimage : " + PostImageurls.get(findWhereItis3));
        PostImageurls.remove(findWhereItis3);
        //Convertback
        String[] simpleArray3 = new String[PostImageurls.size()];
        PostImageurls.toArray(simpleArray3);
        //Save
        saveArray(simpleArray3, "postimage", getContext());

        /////////////////////////

        ////////Content

        //Load
        //    String[] ContentList = loadArray("content", getContext());


        //   List<String> Content =new LinkedList<String>(Arrays.asList(ContentList));
        //Do the things
        //  int findWhereItis4 = 970324;
        //  for(int x=0;x<Content.size();x++)
        //  {
        //      if(Content.get(x).equals(article.getsomething))
        //      {
        //          findWhereItis4 = x;
        //     }

        //  }
        // Log.w("myfeed","removed postimage : " + Content.get(findWhereItis4));
        // Content.remove(findWhereItis4);
        //Convertback
        // String[] simpleArray4 = new String[Content.size()];
        // Content.toArray(simpleArray4);
        //Save
        // saveArray(simpleArray4, "content", getContext());

        /////////////////////////


    }

    public static boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("Rotary_RSS_saved", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("Rotary_RSS_saved", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }


}
