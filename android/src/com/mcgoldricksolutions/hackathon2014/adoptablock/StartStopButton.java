package com.mcgoldricksolutions.hackathon2014.adoptablock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Toggle button showing the start or stop image.
 * @author dirtbag
 *
 */
public class StartStopButton extends ImageButton {

	protected static Drawable START_IMAGE;
	protected static Drawable STOP_IMAGE;
	
	protected boolean started = false;
	
	public StartStopButton(Context context) {
		super(context, null);
	}
	
	public StartStopButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		START_IMAGE = context.getResources().getDrawable(R.drawable.start_button);
		STOP_IMAGE = context.getResources().getDrawable(R.drawable.stop_button);
	}

	public void clicked() {
		started = !started;
		
		if(!started){
			this.setBackgroundDrawable(START_IMAGE);
		} else {
			this.setBackgroundDrawable(STOP_IMAGE);
		}
	}
}
