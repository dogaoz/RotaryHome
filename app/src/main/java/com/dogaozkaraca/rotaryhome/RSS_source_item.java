package com.dogaozkaraca.rotaryhome;


import android.content.Context;
import android.graphics.drawable.Drawable;

public class RSS_source_item {

	private String title;
	private String url;
	public RSS_source_item(String title,String url) {
		// TODO Auto-generated constructor stub
		this.title = title;
		this.url = url;

	}


	public String getTitle()
	{

		return this.title;
	}
	public String getURL()
	{

		return this.url;
	}





}
