package com.mcgoldricksolutions.hackathon2014.adoptablock;

import android.content.Context;

public abstract class Command {

	protected Context mContext;
	protected String mCommandName = "";
	protected String mStatusMessage;
	
	public Command(Context context, String name) {
		this.mContext = context;
		if(name != null) {
			this.mCommandName = name;
		}
		this.mStatusMessage = null;
	}
	
	/**
	 * Command Name returned.
	 * 
	 * @return
	 */
	public String getName() {
		return mCommandName;
	}
	
	abstract public void execute();

}
