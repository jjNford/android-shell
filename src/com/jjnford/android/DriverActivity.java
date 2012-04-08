package com.jjnford.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jjnford.android.shell.R;
import com.jjnford.android.util.Shell;
import com.jjnford.android.util.Shell.ShellException;

public class DriverActivity extends Activity implements OnClickListener {
	
	public static final String LOG_TAG = "shell-example";
	
	private Button mCommandButton;
	private Button mNetworkButton;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);    	
    }

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		bindButtons();
		super.onResume();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View button) {
		String title = null;
		String[] list = null;
		
		if(button.getId() == mCommandButton.getId()) {
			title = "Commands";
			list = getBinCommands();
		} else if(button.getId() == mNetworkButton.getId()) {
			title = "Network Adapters";
			list = getNetworkAdapters();
		}
		
		// Display dialog with data.
		if(list == null || list.length == 0) {
			displayErrorDialog("Requires that device be rooted.");
		} else {
			displayListDialog(title, list);
		}
	}
	
	/**
	 * Binds events to activities buttons.
	 */
	private void bindButtons() {
		bindCommandButton();
		bindNetworkButton();
	}
	
	/**
	 * Binds events to the command button.
	 */
	private void bindCommandButton() {
		mCommandButton = (Button) findViewById(R.id.bin_commands);
		mCommandButton.setOnClickListener(this);
	}
	
	/**
	 * Bind events to the network button.
	 */
	private void bindNetworkButton() {
		mNetworkButton = (Button) findViewById(R.id.network_adapters);
		mNetworkButton.setOnClickListener(this);
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