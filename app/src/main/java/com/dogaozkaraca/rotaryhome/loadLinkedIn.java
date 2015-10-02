package com.dogaozkaraca.rotaryhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;


public class loadLinkedIn {
	static int loadLinkedIn(Activity mLauncher,List<DoFeed_Item> FeedItemList)
	{
//		try
//		{
			
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mLauncher);
		final String APIKEY = "77lu0ipw291g8n";
		final String APISECRET = "JhrTZGdC8Bdy4eXM";
		String token = pref.getString("LINKEDIN_OAUTH_TOKEN", "");
		String secret = pref.getString("LINKEDIN_OAUTH_TOKEN_SECRET", "");
		String verifier = pref.getString("LINKEDIN_OAUTH_TOKEN_VERIFIER", "");

		Log.d("LinkedInDO","onDoFeed : "+ token +"###Secret:"+ secret);
		

		
		
		
//		final LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(APIKEY, APISECRET);
//		final LinkedInApiClient client = factory.createLinkedInApiClient(token, secret);
//
//	
//		Network network = client.getNetworkUpdates(EnumSet.of(NetworkUpdateType.SHARED_ITEM));
//		System.out.println("Total updates fetched:" + network.getUpdates().getTotal());
//		for (int i=0;i < network.getUpdates().getUpdateList().size();i++) {
//			Update update = network.getUpdates().getUpdateList().get(i);
//			Log.d("LinkedInDO","-------------------------------");
//			Log.d("LinkedInDO",update.getUpdateKey());
//
//			 if (update.getUpdateContent() != null) {
//				 
//			 
//			Log.d("LinkedInDO",update.getUpdateContent().getPerson().getFirstName()+ " ");
//			Log.d("LinkedInDO",update.getUpdateContent().getPerson().getLastName()+ " ");
//			Log.d("LinkedInDO",update.getUpdateContent().getPerson().getHeadline()+ " "); //User works at ..
//			Log.d("LinkedInDO",update.getUpdateContent().getPerson().getPath()+ " "); //returns null..
//			Log.d("LinkedInDO",update.getUpdateContent().getPerson().getPictureUrl()+ " "); //profileImageUrl...
//			
//			// Log.d("LinkedInDO","getCurrentStatus() :"+update.getUpdateContent().getPerson().getCurrentStatus()+ " ");
//			Log.d("LinkedInDO","########");
//
//			Log.d("LinkedInDO","DateTimeStamp:"+update.getUpdateContent().getPerson().getCurrentStatusTimestamp()+ " ");
//			
//			String postTitle = update.getUpdateContent().getPerson().getFirstName()+ " " + update.getUpdateContent().getPerson().getLastName() + " : ";
//			String profilePictureURL = update.getUpdateContent().getPerson().getPictureUrl();
//			Date date = new Date(1408395867*1000);
//			Log.d("LinkedInDO","DateOfPost:"+date+ " ");
//
//			FeedItemList.add(new DoFeed_Item("linkedin",date ,postTitle, profilePictureURL, "URL", "", null, "-", "-", null));
//
//			 }
//		        if (update.getUpdateComments() != null) {
//		        	Log.d("LinkedInDO","Total comments fetched:" + update.getUpdateComments().getTotal());
//		                for (UpdateComment comment : update.getUpdateComments().getUpdateCommentList()) {
//		                	Log.d("LinkedInDO",comment.getPerson().getFirstName() + " " + comment.getPerson().getLastName() + "->" + comment.getComment());                         
//		                }
//		        }
//		}
//
//		}
//		catch(Exception e)
//		{
//			return 1;
//		}
//		

		return 0;
	}
	
	
//	void userProperties(LinkedInApiClient client)
//	{
//		 Set<ProfileField> propertiesToFetch = EnumSet.of(ProfileField.ID,
//		            ProfileField.FIRST_NAME, ProfileField.LAST_NAME,
//		            ProfileField.HEADLINE, ProfileField.PICTURE_URL,
//		            ProfileField.DATE_OF_BIRTH, ProfileField.PHONE_NUMBERS,
//		            ProfileField.MAIN_ADDRESS, ProfileField.INDUSTRY,
//		            ProfileField.EDUCATIONS, ProfileField.LANGUAGES_LANGUAGE_NAME,
//		            ProfileField.SKILLS_SKILL_NAME,
//		            ProfileField.CERTIFICATIONS_NAME, ProfileField.POSITIONS_TITLE,
//		            ProfileField.POSITIONS_COMPANY_NAME,
//		            ProfileField.POSITIONS_COMPANY);
//		 
//		    Person profile = client.getProfileForCurrentUser(propertiesToFetch);
//
//		    Log.w("LinkedInDO","Name:" + profile.getFirstName() + " " + profile.getLastName());
//		    Log.w("LinkedInDO","Headline:" + profile.getHeadline());
//		    Log.w("LinkedInDO","Summary:" + profile.getSummary());
//		    Log.w("LinkedInDO","Industry:" + profile.getIndustry());
//		    Log.w("LinkedInDO","Picture:" + profile.getPictureUrl());
//	}
}
