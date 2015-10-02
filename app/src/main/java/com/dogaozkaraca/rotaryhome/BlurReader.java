package com.dogaozkaraca.rotaryhome;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by doga on 26/06/15.
 */
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;
import twitter4j.TwitterException;

@SuppressLint("ValidFragment")
public class BlurReader extends DialogFragment {

    String postType;

    String articleTitle;
    String url;
    String mainPictureURL;
    String userPictureAreaURL;
    String postIdForSocialNetworks;
    String openinMainAppURLforSocialNetworks;
    boolean isPostLiked_Hearted_Faved;
    boolean isPostReTweeted;
    String articleSenderOrSource;
    String faves_likes_hearts;
    String retweets;
    boolean isFromSaved;
    DoFeed_Item article;
    public BlurReader(String mpostType,String marticleSenderOrSource,String marticleTitle,String murl,
                      String mmainPictureURL,String muserPictureAreaURL,
                      String mpostIdForSocialNetworks,String mopenInMainAppUrlForSocialNetworks,boolean misPostLiked_Hearted_Faved,
                      boolean misPostReTweeted,String fav_like_heartCount,String retweetCount,boolean isFromSaved,DoFeed_Item article)
    {
        super();
        this.postType =mpostType;
        this.articleSenderOrSource = marticleSenderOrSource;
        this.articleTitle = marticleTitle;
        this.url = murl;
        this.mainPictureURL = mmainPictureURL;
        this.userPictureAreaURL = muserPictureAreaURL;
        this.postIdForSocialNetworks = mpostIdForSocialNetworks;
        this.openinMainAppURLforSocialNetworks  =mopenInMainAppUrlForSocialNetworks;
        this.isPostLiked_Hearted_Faved = misPostLiked_Hearted_Faved;
        this.isPostReTweeted = misPostReTweeted;
        this.faves_likes_hearts = fav_like_heartCount;
        this.retweets = retweetCount;
        this.isFromSaved = isFromSaved;
        this.article = article;


    }
    public BlurReader(){
        super();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(130);
        dialog.getWindow().setBackgroundDrawable(d);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    @Override
    public void onResume() {

        super.onResume();

        Window window = getDialog().getWindow();
        Display display = DoWorkspace.mLauncher.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_Panel);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.blur_reader, null);
        getDialog().setCanceledOnTouchOutside(true);

        final RelativeLayout rssButtons = (RelativeLayout) v.findViewById(R.id.sep);
        rssButtons.setVisibility(View.GONE);
        final Button saveButton = (Button) v.findViewById(R.id.savebutton);

        if (isFromSaved)
        {
            saveButton.setVisibility(View.GONE);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveArticle();
            }
        });

        final Button shareButton = (Button) v.findViewById(R.id.sharebutton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArticle();
            }
        });

        final RelativeLayout titleBg = (RelativeLayout) v.findViewById(R.id.titleBg);

        ImageView postTypeIV = (ImageView) v.findViewById(R.id.feedItemType);
        postTypeIV.setVisibility(View.VISIBLE);

        final ImageButton retweetbtn = (ImageButton) v.findViewById(R.id.retweets_reshares);
        final TextView retweetbtn_text = (TextView) v.findViewById(R.id.retweets_resharestext);

        final ImageButton favbtn = (ImageButton) v.findViewById(R.id.likes_favs);
        final TextView favbtn_text = (TextView) v.findViewById(R.id.likes_favstext);

        final WebView rssReader = (WebView) v.findViewById(R.id.webView2);
        rssReader.setVisibility(View.GONE);
        rssReader.setWebChromeClient(new WebChromeClient());
        rssReader.setWebViewClient(new WebViewClient());
        TextView articleSenderorSourceTV = (TextView) v.findViewById(R.id.articleHeaderText);
        TextView articleDetailsTextTV = (TextView) v.findViewById(R.id.articleText);

        articleSenderorSourceTV.setText(Html.fromHtml(articleSenderOrSource));
        articleDetailsTextTV.setText(articleTitle);

        final TextView openinapptext = (TextView) v.findViewById(R.id.openInAppText);

        final ImageView postImage = (ImageView) v.findViewById(R.id.articlePicture);


        ImageView postUserImage = (ImageView) v.findViewById(R.id.postImageIncluded);

        if (mainPictureURL != null && !postType.equals("flickr"))
        {
            try {
                Picasso.with(getActivity()).load(mainPictureURL).into(postImage);
            }
            catch(Exception e){}


        }

        if(mainPictureURL != null && userPictureAreaURL != null && !postType.equals("flickr"))
        {
            try {
            Picasso.with(getActivity()).load(userPictureAreaURL).into(postUserImage);
        }
        catch(Exception e){}

        }

        if (postType.equals("rss"))
            {
                Picasso.with(getActivity()).load(R.drawable.rss_feeditem).into(postTypeIV);
                postImage.setVisibility(View.INVISIBLE);
                retweetbtn.setVisibility(View.GONE);
                retweetbtn_text.setVisibility(View.GONE);
                favbtn.setVisibility(View.GONE);
                favbtn_text.setVisibility(View.GONE);
                rssReader.setVisibility(View.VISIBLE);
                rssReader.loadUrl(url);
                openinapptext.setVisibility(View.GONE);
                rssButtons.setVisibility(View.VISIBLE);
                titleBg.setVisibility(View.GONE);

            }
        else if (postType.equals("twitter"))
        {
            Picasso.with(getActivity()).load(R.drawable.twitter_feeditem).into(postTypeIV);

            favbtn_text.setText(faves_likes_hearts);
            retweetbtn_text.setText(retweets);

            if (mainPictureURL == null)
            {
                Picasso.with(getActivity()).load(userPictureAreaURL).into(postImage);

            }

            if(isPostLiked_Hearted_Faved)
            {
                favbtn.setImageResource(R.drawable.ic_action_fave_on);


            }

            if(isPostReTweeted)
            {
                retweetbtn.setImageResource(R.drawable.ic_action_rt_on);
            }

            // notifyDataSetChanged(); put this aside
            // We have to access item, get article from openLink<--NewsAdapter
            favbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPostLiked_Hearted_Faved)
                    {
                        favbtn.setImageResource(R.drawable.ic_action_fave_off);
                        article.setIfLikedFavedHearted(false);

                        if (faves_likes_hearts != null && Integer.parseInt(faves_likes_hearts) != 0)
                        {

                            favbtn_text.setText((Integer.parseInt(faves_likes_hearts) -1 )+"");
                            article.setFavOrLikeOrInstaHeartCount((Integer.parseInt(faves_likes_hearts) - 1) + "");

                        }
                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    loadTwitter.twitter.destroyFavorite(Long.parseLong(postIdForSocialNetworks));
                                } catch (TwitterException e) {
                                    Log.e("RotaryTwitter","undo favorite failed");
                                }
                                return null;
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                    else
                    {
                        favbtn.setImageResource(R.drawable.ic_action_fave_on);
                        article.setIfLikedFavedHearted(true);

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    loadTwitter.twitter.createFavorite(Long.parseLong(postIdForSocialNetworks));
                                } catch (TwitterException e) {
                                    Log.e("RotaryTwitter","favorite failed");
                                }
                                return null;
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                        if (faves_likes_hearts != null)
                        {

                            favbtn_text.setText((Integer.parseInt(faves_likes_hearts)+1)+"");
                            article.setFavOrLikeOrInstaHeartCount((Integer.parseInt(faves_likes_hearts) + 1) + "");
                        }

                    }
                }
            });

            retweetbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isPostReTweeted)
                    {
                        retweetbtn.setImageResource(R.drawable.ic_action_rt_off);

                        article.setIfRetweeted(false);

                        if (article.getRetweetReshares() != null && Integer.parseInt(article.getRetweetReshares()) != 0) {

                            retweetbtn_text.setText((Integer.parseInt(retweets)-1)+"");
                            article.setRetweetReshares((Integer.parseInt(retweets) - 1) + "");
                        }

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {

                                try {
                                    List<twitter4j.Status> retweets = loadTwitter.twitter.getUserTimeline();
                                    for (twitter4j.Status retweet : retweets) {
                                        Log.e("RotaryTwitter","retweet user" + retweet.getUser());

                                        Log.e("RotaryTwitter", "retweet text" + retweet.getText());
                                        if(retweet != null && (retweet.getRetweetedStatus().getId() == Long.parseLong(postIdForSocialNetworks)))
                                            loadTwitter.twitter.destroyStatus(retweet.getId());
                                    }
                                } catch (TwitterException e) {
                                    Log.e("RotaryTwitter","undo retweet failed");
                                }
                                catch (Exception e2)
                                {
                                    Log.e("RotaryTwitter","undo retweet failed FATAL");

                                }
                                return null;
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



                    }
                    else
                    {
                        retweetbtn.setImageResource(R.drawable.ic_action_rt_on);

                        article.setIfRetweeted(true);

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    loadTwitter.twitter.retweetStatus(Long.parseLong(postIdForSocialNetworks));
                                } catch (TwitterException e) {
                                    Log.e("RotaryTwitter","retweet failed");
                                }
                                return null;
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        if (article.getRetweetReshares() != null)
                        {

                            retweetbtn_text.setText((Integer.parseInt(article.getRetweetReshares())+1)+"");
                            article.setRetweetReshares((Integer.parseInt(article.getRetweetReshares()) + 1) + "");
                        }

                    }


                }
            });

            Boolean isTwitterInstalled = isAppInstalled("com.twitter.android",getActivity());

            if (isTwitterInstalled == true)
            {
            openinapptext.setText("See details in Twitter App!");
            openinapptext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        String url = "twitter://status?status_id="+postIdForSocialNetworks;
                        Intent tweet_in_native_app = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getActivity().startActivity(tweet_in_native_app);

                }
            });
            }
            else
            {
                openinapptext.setVisibility(View.INVISIBLE);
            }

        }
        else if (postType.equals("instagram"))
        {
            Picasso.with(getActivity()).load(R.drawable.instagram_feeditem).into(postTypeIV);
            favbtn.setImageResource(R.drawable.insta_likebutton_false);
            favbtn_text.setText(faves_likes_hearts);

            retweetbtn.setVisibility(View.GONE);
            retweetbtn_text.setVisibility(View.GONE);
            openinapptext.setText("See details in Instagram App!");
            openinapptext.setVisibility(View.GONE);
            //postImage.getLayoutParams().height = DoWorkspace.mLauncher.getWindowManager().getDefaultDisplay().getWidth();

            Boolean isInstagramInstalled = isAppInstalled("com.instagram.android",getActivity());
            if (isInstagramInstalled == true) {
                openinapptext.setVisibility(View.VISIBLE);
                openinapptext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] separated = url.split("/");
                        String url = "http://instagr.am/p/"+separated[4];




                        try {
                            Intent insta = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            insta.setPackage("com.instagram.android");
                            startActivity(insta);
                        } catch (ActivityNotFoundException e) {
                           // Intent post_in_native_app = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                           // getActivity().startActivity(post_in_native_app);
                        }

                    }
                });
            }

        }
        else if (postType.equals("facebook"))
        {
            Picasso.with(getActivity()).load(R.drawable.fb_feeditem).into(postTypeIV);
            favbtn.setImageResource(R.drawable.fb_likebutton);
            favbtn_text.setText(faves_likes_hearts);

            retweetbtn.setVisibility(View.GONE);
            retweetbtn_text.setVisibility(View.GONE);
            openinapptext.setText("See details in Facebook App!");
            Boolean isFacebookInstalled = isAppInstalled("com.facebook.katana",getActivity());

            if (userPictureAreaURL != null && !userPictureAreaURL.equals("")) {
                try {
                    Picasso.with(getActivity()).load(userPictureAreaURL).into(postImage);
                } catch (Exception e) {
                }
                try {
                    Picasso.with(getActivity()).load("https://graph.facebook.com/"+postIdForSocialNetworks+"/picture?type=large").into(postUserImage);
                }
                catch(Exception e){}

            }
            else {
                try {
                    Picasso.with(getActivity()).load("https://graph.facebook.com/"+postIdForSocialNetworks+"/picture?type=large").into(postImage);
                }
                catch(Exception e){}
            }


            if (isFacebookInstalled == true)
            {
                openinapptext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                    int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

                    if (versionCode >= 3002850) {
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));;
                    } else {
                        // open the Facebook app using the old method (fb://profile/id or fb://pro
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                    }
                });
            }
            else
            {
                openinapptext.setVisibility(View.INVISIBLE);


            }

        }
        else if (postType.equals("linkedin"))
        {
            Picasso.with(getActivity()).load(R.drawable.linkedin_feeditem).into(postTypeIV);
            retweetbtn.setVisibility(View.GONE);
            retweetbtn_text.setVisibility(View.GONE);
            favbtn.setVisibility(View.GONE);
            favbtn_text.setVisibility(View.GONE);
            openinapptext.setText("See details in LinkedIn App!");
            openinapptext.setVisibility(View.INVISIBLE);


        }
        else if (postType.equals("flickr"))
        {
            Picasso.with(getActivity()).load(R.drawable.flickr_feeditem).into(postTypeIV);
            retweetbtn.setVisibility(View.GONE);
            retweetbtn_text.setVisibility(View.GONE);
            favbtn.setVisibility(View.GONE);
            favbtn_text.setVisibility(View.GONE);
            openinapptext.setText("More Details on Flickr!");
            openinapptext.setVisibility(View.VISIBLE);

            try {
                Picasso.with(getActivity()).load(mainPictureURL).into(postImage);
            }
            catch(Exception e){}

            openinapptext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postImage.setVisibility(View.INVISIBLE);
                    retweetbtn.setVisibility(View.GONE);
                    retweetbtn_text.setVisibility(View.GONE);
                    favbtn.setVisibility(View.GONE);
                    favbtn_text.setVisibility(View.GONE);
                    rssReader.setVisibility(View.VISIBLE);
                    rssReader.loadUrl(url);
                    openinapptext.setVisibility(View.GONE);
                    rssButtons.setVisibility(View.VISIBLE);
                    titleBg.setVisibility(View.GONE);
                }

            });

        }
        else if (postType.equals("foursquare"))
        {            Picasso.with(getActivity()).load(R.drawable.foursquare_feeditem).into(postTypeIV);
            retweetbtn.setVisibility(View.GONE);
            retweetbtn_text.setVisibility(View.GONE);
            favbtn.setVisibility(View.GONE);
            favbtn_text.setVisibility(View.GONE);
            openinapptext.setText("See details in Foursquare App!");
            openinapptext.setVisibility(View.INVISIBLE);


        }
        else if (postType.equals("tumblr"))
        {
            Picasso.with(getActivity()).load(R.drawable.tumblr_feeditem).into(postTypeIV);
            postImage.setVisibility(View.INVISIBLE);
            retweetbtn.setVisibility(View.GONE);
            retweetbtn_text.setVisibility(View.GONE);
            favbtn.setVisibility(View.GONE);
            favbtn_text.setVisibility(View.GONE);
            rssReader.setVisibility(View.VISIBLE);
            rssReader.loadUrl(url);
            openinapptext.setVisibility(View.GONE);
            rssButtons.setVisibility(View.VISIBLE);
            titleBg.setVisibility(View.GONE);

            openinapptext.setText("See details in Tumblr App!");
            openinapptext.setVisibility(View.INVISIBLE);


        }



        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (this != null) {
            this.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(isFromSaved)
        {
            OfflineNewsAdapter.dismissTheDialogAnim();

        }
        else
        {
            NewsAdapter.dismissTheDialogAnim();

        }

        DoWorkspace.newsAdapterPublic.notifyDataSetChanged();

    }

    public void shareArticle()
    {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, articleTitle + ", " + url);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share..."));



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

    public void saveArticle()
    {
        Log.w("myfeed", "--ARTICLE SAVE--");

        /////////////////////TITLE////////////////////////////
        //Load
        String[] titleList = loadArray("title", getActivity());


        List<String> titles =new LinkedList<String>(Arrays.asList(titleList));
        //Do the things
        titles.add(articleTitle);

        Log.w("myfeed", "added title : " + articleTitle);
        //Convertback
        String[] simpleArray = new String[titles.size()];
        titles.toArray(simpleArray);
        //Save
        saveArray(simpleArray, "title",getActivity());

        /////////////////////Article////////////////////////////
        //Load
        String[] ArticleList = loadArray("article", getActivity());


        List<String> Articles =new LinkedList<String>(Arrays.asList(ArticleList));
        //Do the things
        Articles.add(articleTitle);

        Log.w("myfeed", "added article text: " + articleTitle);
        //Convertback
        String[] simpleArray3 = new String[Articles.size()];
        Articles.toArray(simpleArray3);
        //Save
        saveArray(simpleArray3, "article", getActivity());

        ////////POST IMAGE

        //Load
        String[] imageurlList = loadArray("postimage",getActivity());


        List<String> imageurls = new LinkedList<String>(Arrays.asList(imageurlList));
        //Do the things

        Log.w("myfeed", "added postimage : " + mainPictureURL);
        imageurls.add(mainPictureURL);

        //Convertback
        String[] simpleArray4 = new String[imageurls.size()];
        imageurls.toArray(simpleArray4);
        //Save
        saveArray(simpleArray4, "postimage", getActivity());

        ////////POST URL

        //Load
        String[] urlList = loadArray("url",getActivity());


        List<String> urls = new LinkedList<String>(Arrays.asList(urlList));
        //Do the things

        Log.w("myfeed", "added url : " + url);
        urls.add(url);

        //Convertback
        String[] simpleArray2 = new String[urls.size()];
        urls.toArray(simpleArray2);
        //Save
        saveArray(simpleArray2, "url", getActivity());

        /////////////////////////

        DoWorkspace.reloadOfflineView();

        Toast.makeText(getActivity(), "Article succesfully saved!", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }
        // set the animations to use on showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_fade);

        // alternative way of doing it
        //getDialog().getWindow().getAttributes().
        //    windowAnimations = R.style.dialog_animation_fade;

        // ... other stuff you want to do in your onStart() method
    }

    private static boolean isAppInstalled(String packageName,Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


}
