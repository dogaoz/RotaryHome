package com.dogaozkaraca.rotaryhome;

import android.text.Html;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class loadRSS {
	   public static int loadRSS(String type,String rssURL,List<DoFeed_Item> FeedItemList,String sourceName)
	    {

            String feed_fix = null;

	    	try {



                try {

                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI("http://rotary.dogaozkaraca.com/getSourceProps.php?url="+rssURL));

                    HttpResponse response = client.execute(request);
                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));

                   feed_fix = in.readLine();


                } catch (Exception er) {

                }




                if (feed_fix != null && feed_fix.equals("for_billboard"))
                {






                    JSONObject json = rss_reader.getJSONFromUrl("http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=100&q="+ rssURL);
                    // parsing json object
                    if (json.getInt("responseStatus") == 200) {
                        Log.d("RSS","responseStatus=200");
                        JSONObject responseData = json.getJSONObject("responseData");
                        JSONObject feed =  responseData.getJSONObject("feed");
                        JSONArray entries = feed.getJSONArray("entries");


                        for (int i = 0; i < entries.length(); i++) {

                            JSONObject post = (JSONObject) entries.getJSONObject(i);

                            String title =post.getString("title");
                            String content_snippet = post.getString("contentSnippet");
                            String publishedDate = post.getString("publishedDate");
                            // java.text.ParseException: Unparseable date: "Fri, 22 Aug 2014 14:29:11 -0700" (at offset 0)

                            SimpleDateFormat  format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US);

                            Date date = null;
                            try {
                                date = format.parse(publishedDate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Log.d("DoFeed_DateTime","RSS Date Parsed : " +date + "////with this post : " + title);


                            String url = post.getString("link");
                            String author = post.getString("author");
                            String content = post.getString("content");
                            Log.w("DoFeed_RSS","Content : "+content);

                            String finalimgurl = "no_image_fix";


                            if (finalimgurl != null)
                            {
                                DoFeed_Item df = new DoFeed_Item(type ,date,content,finalimgurl,url,"",null,content_snippet,content,null);
                                df.setSourceName(sourceName);
                                FeedItemList.add(df);
                            }
                            else
                            {
                                DoFeed_Item df = new DoFeed_Item(type ,date,content,"",url,"",null,content_snippet,content,null);
                                df.setSourceName(sourceName);
                                FeedItemList.add(df);

                            }
                            Log.d("DoFeed_Item","Title : "+title +"  Link : "+url+"  Author : "+author + "  PublishedDate : "+ publishedDate);
                        }
                    }
                    else
                    {
                        return 1;
                    }



                }
                //else if (feed_fix != null )
               // {



             //   }
                else if (feed_fix != null && (feed_fix.equals("no_images_t1") || feed_fix.equals("unwanted_wrong_images")))
                {




                    JSONObject json = rss_reader.getJSONFromUrl("http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=100&q="+ rssURL);
                    // parsing json object
                    if (json.getInt("responseStatus") == 200) {
                        Log.d("RSS","responseStatus=200");
                        JSONObject responseData = json.getJSONObject("responseData");
                        JSONObject feed =  responseData.getJSONObject("feed");
                        JSONArray entries = feed.getJSONArray("entries");


                        for (int i = 0; i < entries.length(); i++) {
                            JSONObject post = (JSONObject) entries.getJSONObject(i);

                            String title = post.getString("title");
                            String content_snippet = post.getString("contentSnippet");
                            String publishedDate = post.getString("publishedDate");
                            // java.text.ParseException: Unparseable date: "Fri, 22 Aug 2014 14:29:11 -0700" (at offset 0)

                            SimpleDateFormat  format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US);

                            Date date = null;
                            try {
                                date = format.parse(publishedDate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Log.d("DoFeed_DateTime","RSS Date Parsed : " +date + "////with this post : " + title);


                            String url = post.getString("link");
                            String author = post.getString("author");
                            String content = post.getString("content");
                            Log.w("DoFeed_RSS","Content : "+content);

                            String finalimgurl = "no_image_fix";


                            if (finalimgurl != null)
                            {
                                DoFeed_Item df = new DoFeed_Item(type ,date,title,finalimgurl,url,"",null,content_snippet,content,null);
                                df.setSourceName(sourceName);
                                FeedItemList.add(df);
                            }
                            else
                            {
                                DoFeed_Item df = new DoFeed_Item(type ,date,title,"",url,"",null,content_snippet,content,null);
                                df.setSourceName(sourceName);
                                FeedItemList.add(df);

                            }
                            Log.d("DoFeed_Item","Title : "+title +"  Link : "+url+"  Author : "+author + "  PublishedDate : "+ publishedDate);
                        }
                    }
                    else
                    {
                        return 1;
                    }



                }
                else
                {


                    JSONObject json = rss_reader.getJSONFromUrl("http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=100&q="+ rssURL);
                    // parsing json object
                    if (json.getInt("responseStatus") == 200) {
                        Log.d("RSS","responseStatus=200");
                        JSONObject responseData = json.getJSONObject("responseData");
                        JSONObject feed =  responseData.getJSONObject("feed");
                        JSONArray entries = feed.getJSONArray("entries");


                        for (int i = 0; i < entries.length(); i++) {
                            JSONObject post = (JSONObject) entries.getJSONObject(i);

                            String title = post.getString("title");
                            String content_snippet = post.getString("contentSnippet");
                            String publishedDate = post.getString("publishedDate");
                            // java.text.ParseException: Unparseable date: "Fri, 22 Aug 2014 14:29:11 -0700" (at offset 0)

                            SimpleDateFormat  format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US);

                            Date date = null;
                            try {
                                date = format.parse(publishedDate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Log.d("DoFeed_DateTime","RSS Date Parsed : " +date + "////with this post : " + title);


                            String url = post.getString("link");
                            String author = post.getString("author");
                            String content = post.getString("content");
                            Log.w("DoFeed_RSS","Content : "+content);

                            String finalimgurl = null;
                            try
                            {
                                String[] getimg1 = content.split("<img src=\"");
                                Log.w("DoFeed_RSS","Image URL try : " + getimg1[0] + getimg1[1]);

                                String[] getimg2 = getimg1[1].split("\"");
                                Log.w("DoFeed_RSS","Image URL try : " + getimg2[0] + getimg2[1]);

                                finalimgurl = getimg2[0];
                                Log.d("DoFeed_RSS","Image URL try : " + finalimgurl);

                            }
                            catch(Exception e)
                            {
                                Log.e("DoFeed_RSS","Image URL try 1 error : " + e);

                                try
                                {

                                    JSONArray mediagroups =  post.getJSONArray("mediaGroups");
                                    JSONObject contents =  mediagroups.getJSONObject(0);
                                    JSONArray imageurlb =  contents.getJSONArray("contents");
                                    JSONObject imageurln =  imageurlb.getJSONObject(0);
                                    String imageurl =  imageurln.getString("url");

                                    finalimgurl = imageurl.toString();
                                    Log.d("DoFeed_RSS","Image URL try 2 : " + finalimgurl);


                                }
                                catch (Exception e2)
                                {
                                    Log.e("DoFeed_RSS","Image URL try 2 error : " + e2);

                                }

                            }

                            //String content = post.getString("content");

                            if (finalimgurl != null)
                            {
                                DoFeed_Item df = new DoFeed_Item(type ,date,title,finalimgurl,url,"",null,content_snippet,content,null);
                                df.setSourceName(sourceName);
                                FeedItemList.add(df);
                            }
                            else
                            {
                                DoFeed_Item df = new DoFeed_Item(type ,date,title,"",url,"",null,content_snippet,content,null);
                                df.setSourceName(sourceName);
                                FeedItemList.add(df);

                            }
                            Log.d("DoFeed_Item","Title : "+title +"  Link : "+url+"  Author : "+author + "  PublishedDate : "+ publishedDate);
                        }
                    }
                    else
                    {
                        return 1;
                    }



                }










			} catch (JSONException e) {
			return 1;
			}
	    	

	    	return 0;
	    }
}
