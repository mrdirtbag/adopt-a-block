package com.mcgoldricksolutions.hackathon2014.adoptablock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

/**
 * Toggle button showing the start or stop image.
 * @author dirtbag
 *
 */
public class StartStopButton extends ImageButton {
	
	protected boolean started = false;
	
	public StartStopButton(Context context) {
		super(context, null);
	}
	
	public StartStopButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void clicked() {
		Log.d("Btn", "clicked: " + started);
		started = !started;
		Log.d("Btn", "status: " + started);
		if(started){
			this.setBackgroundResource(R.drawable.stop_button);
		} else {
			this.setBackgroundResource(R.drawable.start_button);
		}
	}
}
