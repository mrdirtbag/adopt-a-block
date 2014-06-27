package com.mcgoldricksolutions.hackathon2014.adoptablock;

import android.os.AsyncTask;


/**
 * Execute a Command off the UI Thread
 * 
 * @author dirtbag
 *
 */
public class CommandRunner {
	
	/**
	 * List of commands to be executed.
	 */
	protected Command[] mCommands;
	
	/**
	 * Take an array of commands to be executed on a new thread
	 * 
	 * @param commands
	 */
	public CommandRunner(Command[] commands) {
		
		this.mCommands = commands;
		
	}
	
	/**
	 * Perform what ever commands are queued up, but do it in the background.
	 */
	public void executeInBackground() {

		new LongRunningGetIO().execute(mCommands);	
	}	

	/**
	 * Inner class for running commands in background thread.  These tasks would 
	 * block the UI if allowed to run in the UI thread.
	 * 
	 * @author dirtbag
	 *
	 */
	private class LongRunningGetIO extends AsyncTask <Command /*Commands to Execute*/, Boolean /* Progress */, String /* Result */> {

	    private static final String DEBUG_TAG = "CommandRunner$LongRunningGetIO";		
		
	    /**
	     * To be done in background thread, iterate the array of commands, run each one in order.
	     */
	    @Override
		protected String doInBackground(Command...commands) {

			for(int i=0; i<commands.length; i++) {
				commands[i].execute();
			}
			
			return "";
		}
		
		/**
		 * After the long running process ends, we need to cleanup, by updating the GUI
		 */
		@Override
     	protected void onPostExecute(String results) {
	
           
        }
		

	}
		

}