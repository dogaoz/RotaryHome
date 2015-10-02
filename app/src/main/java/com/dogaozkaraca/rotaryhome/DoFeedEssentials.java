package com.dogaozkaraca.rotaryhome;

import android.app.Activity;

import at.markushi.ui.CircleButton;


public class DoFeedEssentials {
	public static void launchEditMode(Activity customScreen,
			Activity mLauncher) {

		CircleButton circleButton = (CircleButton) customScreen.findViewById(R.id.circleButton);
		circleButton.setImageResource(R.drawable.ic_action_accept);
		
	}
	
	
	public static void exitEditMode(Activity customScreen,
                                    Activity mLauncher) {

		CircleButton circleButton = (CircleButton) customScreen.findViewById(R.id.circleButton);
		circleButton.setImageResource(R.drawable.docenterbtn);
		
	}
}
