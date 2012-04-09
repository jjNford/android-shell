package com.jjnford.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jjnford.android.shell.R;
import com.jjnford.android.util.Shell;
import com.jjnford.android.util.Shell.ShellException;

public class DriverActivity extends Activity {
	
	public static final String LOG_TAG = "shell-example";
		
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);    	
    }
	
	/**
	 * Events for Bin Commands button binding.
	 * 
	 * @param view The Bin Command Button View.
	 */
	public void onBinCommandsButtonClick(View view) {
		String title = "Commands";
		String[] list = getBinCommands();
		
		if(list == null || list.length == 0) {
			displayErrorDialog("No Commands found.");
		} else {
			displayListDialog(title, list);
		}
	}
	
	/**
	 * Evens for Network Adapters button binding.
	 * 
	 * @param view The Network Adapters Button View.
	 */
	public void onNetworkAdaptersButtonClick(View view) {
		String title = "Network Adapters";
		String[] list = getNetworkAdapters();
		
		if(list == null || list.length == 0) {
			displayErrorDialog("Requires that device be rooted.");
		} else {
			displayListDialog(title, list);
		}
	}
		
	/**
	 * @return List of all commands in /system/bin.
	 */
	private String[] getBinCommands() {
		String[] commands = null;
		try {
			commands = Shell.exec("ls").split("\\s+");
		} catch (ShellException e) {
			Log.e(DriverActivity.LOG_TAG, e.getMessage());
		} 
		return commands;		
	}
	
	/**
	 * @return List of all the devices known network adapters.  Device must be rooted.
	 */
	private String[] getNetworkAdapters() {
		String output = null;
		String[] netcfg = null;
		ArrayList<String> adapters = null;

		try {
			output = Shell.sudo("netcfg");
			if(output != null) {
				netcfg = output.split("\\s+");
				adapters = new ArrayList<String>();
				
				// Parse out adapter names.
				for(int i = 0; i < netcfg.length; i+=5) {
					adapters.add(netcfg[i]);
				}
			}			
		} catch (ShellException e) {
			Log.e(DriverActivity.LOG_TAG, e.getMessage());
		}
		
		// Return null if there is no output returned.
		if(adapters != null) {
			return adapters.toArray(new String[adapters.size()]);
		} else {
			return null;
		}
	}
	
	/**
	 * Creates and displays a dialog showing the given list.
	 * 
	 * @param title Title for the dialog box.
	 * @param list List items to be displayed in the dialog.
	 */
	private void displayListDialog(String title, String[] list) {
		Dialog dialog = new Dialog(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(list, null);
		dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Creates an error dialog.
	 * 
	 * @param msg Message to be displayed in the error dialog.
	 */
	private void displayErrorDialog(String msg) {
		Dialog dialog = new Dialog(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Error");
		builder.setPositiveButton("Continue", null);
		builder.setNegativeButton(null, null);
		builder.setMessage(msg);
		dialog = builder.create();
		dialog.show();
	}
}