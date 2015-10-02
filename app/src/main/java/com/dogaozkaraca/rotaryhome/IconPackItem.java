package com.dogaozkaraca.rotaryhome;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class IconPackItem {

	private String title;
	private Dialog dialog;
	public IconPackItem(String string, Dialog dialog2) {
		// TODO Auto-generated constructor stub
		this.title = string;
		this.dialog = dialog2;

	}

	public String getPackageName() {


		return title;


	}

	public Dialog getDialog() {


		return dialog;


	}







}
