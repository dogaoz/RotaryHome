package com.dogaozkaraca.rotaryhome;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
/**
 * Created by doga.ozkaraca on 4/4/2015.
 */
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;
public class openLink {



    public static BlurReader TwitterLink(Context mContext,String link,String statusId,String imageUrl,String sender,String details,String favcount,String retweetcount,boolean isFromSaved,String userImage,DoFeed_Item article,boolean isFaved,boolean isRetweeted)
    {

            BlurReader dFragment = new BlurReader("twitter",sender,details,link,imageUrl,userImage,statusId,null,isFaved,isRetweeted,favcount,retweetcount,isFromSaved,article);




        return dFragment;
    }

    public static BlurReader RSS_Item(Context mContext, String postURL,String postImageUrl,String postTitle,String shortcontent,String content,String articleSenderOrSource,boolean isFromSaved) {


        BlurReader dFragment = new BlurReader("rss",articleSenderOrSource,postTitle,postURL,postImageUrl,null,null,null,false,false,null,null,isFromSaved,null);


        return dFragment;



    }

    public static BlurReader FacebookLink(Context mContext, String postURL,
                                    String fb_UserId,String imageurl,String postimageurl,String likecount,String title,String sender,boolean isFromSaved) {

        BlurReader dFragment = new BlurReader("facebook",sender,title,postURL,imageurl,postimageurl,fb_UserId,null,false,false,likecount,null,isFromSaved,null);

        return dFragment;

    }

    public static BlurReader InstagramLink(final Context mContext, final String postURL,
                                     String fb_UserId,String imageURL,String userImage,String heartcount,String title,String sender,boolean isFromSaved) {

        BlurReader dFragment = new BlurReader("instagram",sender,title,postURL,imageURL,userImage,null,null,false,false,heartcount,null,isFromSaved,null);



        return dFragment;

    }

    public static BlurReader TumblrLink(Context mContext, String postURL,
                                  String fb_UserId,String sender,String title,String mainImage,boolean isFromSaved) {



        BlurReader dFragment = new BlurReader("tumblr",sender,title,postURL,mainImage,null,null,null,false,false,null,null,isFromSaved,null);


        return dFragment;
    }

    public static BlurReader LinkedInLink(Context mContext, String postURL,
                                    String fb_UserId,boolean isFromSaved) {

       // Intent i = new Intent(mContext.getApplicationContext(), NewsReader.class);
       // i.putExtra("urlofnews",postURL);
       // mContext.startActivity(i);

        BlurReader dFragment = new BlurReader("linkedin","","",postURL,null,null,null,null,false,false,null,null,isFromSaved,null);


        return dFragment;
    }

    public static BlurReader FoursquareLink(Context mContext, String postURL,
                                          String fb_UserId,String sender,String title,String mainPicture,boolean isFromSaved) {


        BlurReader dFragment = new BlurReader("foursquare",sender,title,postURL,mainPicture,null,null,null,false,false,null,null,isFromSaved,null);


        return dFragment;
    }

    public static BlurReader FlickrLink(Context mContext, String postURL,
                                            String fb_UserId,String sender,String title,String mainPicture,boolean isFromSaved) {


        BlurReader dFragment = new BlurReader("flickr",sender,title,postURL,mainPicture,null,null,null,false,false,null,null,isFromSaved,null);


        return dFragment;
    }





}
