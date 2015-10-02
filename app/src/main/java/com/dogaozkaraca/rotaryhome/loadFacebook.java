package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class loadFacebook {

    static void facebookLoadIntoItemList(final List<DoFeed_Item> FeedItemList,final String type ,Activity mLauncher) {

		    try {

				 //response2 = facebookConnector.getFacebook().request("me/home", parameters,"GET");
				Log.w("DoFeed-Facebook","hi");

				new GraphRequest(
						AccessToken.getCurrentAccessToken(),
						"/me/home",
						null,
						HttpMethod.GET,
						new GraphRequest.Callback() {
							public void onCompleted(GraphResponse response) {
       						     /* handle the result */
								String response2 = null;

								response2 = response.getRawResponse();
								Log.w("DoFeed-Facebook",response2);


								if (response2 !=null)
								{
									JSONObject respObject = null;
									try {
										respObject = new JSONObject(response2);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									JSONArray id = null;
									try {

										id = respObject.getJSONArray("data");
										Log.w("DoFeed-Facebook","idgotdolauncher"+id);
										for (int k = 0; k < 50; k++) {

											JSONObject c = id.getJSONObject(k);
											String likecount;
											Boolean isLiked;
											try
											{

												likecount = c.getJSONObject("comments").getJSONArray("data").getJSONObject(0).getInt("like_count") + "";
												//,3isLiked = c.getJSONObject("data").getBoolean("user_likes");
												Log.w("DOF",""+likecount);
											}
											catch(Exception e)
											{
												likecount = null;
												Log.w("DOF","error :"+ e);
											}
											String post = c.getString("id");

											//  String caa = c.getString("name");

											Log.w("DoFeed-Facebook","post: "+post);

											JSONObject cas = c.getJSONObject("from");
											try
											{
												String NameOfWhoPosted = cas.getString("name");

												String PostTitle = c.getString("message");

												String createdTime = c.getString("created_time");
												Log.w("DoFeed","PostCreatedTime : "+createdTime);

												String postImageUrl = null;


												JSONArray castr = c.getJSONArray("actions");
												JSONObject casd = castr.getJSONObject(0);
												String PostLink = casd.getString("link");


												String PostUserId = cas.getString("id");

												String getPostTag = c.getString("type");
												Log.w("DoFeed-Facebook","posttag: " + getPostTag);
												String photoUrl = null;
												if (PostTitle == null)
												{
													PostTitle = " ";
												}
												if (getPostTag.equals("photo"))
												{

													PostTitle =  "Just shared a Photo ! | " +PostTitle;
													Log.w("DoFeed-Facebook",k + "  : is a photo");
													//photoid
													try
													{
														postImageUrl = c.getString("object_id");
														postImageUrl = "https://graph.facebook.com/"+postImageUrl+"/picture?type=normal";
														Log.w("DoFeed-Facebook",postImageUrl + "returned this as a imageurl");
													}
													catch(Exception e)
													{
														Log.e("DoFeed-Facebook","Error : " + e);
													}

													//  FacebookPostTag[k] = getPostTag;
												}
												else if (getPostTag.equals("video"))
												{

													PostTitle = "Just shared a Video ! | " +PostTitle;

												}
												else if (getPostTag.equals("link"))
												{

													PostTitle = "Just shared a Link ! | " +PostTitle;


												}
												else if (getPostTag.equals("status"))
												{

													PostTitle = "Just updated Status ! | " +PostTitle;


												}

												Log.w("DoFeed","FB Photo URL :" + photoUrl);

												Log.d("DoFeed_DateTime","Date parse starting: "+createdTime);


												SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",Locale.US);

												Date date = null;
												try {
													date = format.parse(createdTime);
												} catch (ParseException e) {
													Log.e("DoDate","FB Date parse error : "+ e);

												}
												Log.d("DoFeed_DateTime", "Facebook Post Date Parsed : " + date + " //with this post : " + PostTitle);






												DoFeed_Item df = new DoFeed_Item(type, date, PostTitle, photoUrl, PostLink, PostUserId, postImageUrl, likecount, null, null);
												df.setSourceName(NameOfWhoPosted);
												FeedItemList.add(df);

											}
											catch (Exception er)
											{
												Log.w("DoFeed-Facebook","Error passing that post");
											}

										}
									} catch (JSONException e) {

										Log.w("DoFeed-Facebook","e: "+e);

									}

								}

							}
						}
				).executeAndWait();

				
		

			} catch (Exception e) {
				Log.e("DoFeed-Facebook","error: "+ e.getStackTrace().toString());
				 
			}

		    
	}
}
