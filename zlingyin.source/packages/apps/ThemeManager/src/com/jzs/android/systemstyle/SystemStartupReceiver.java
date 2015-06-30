package com.jzs.android.systemstyle;

import com.jzs.android.systemstyle.utils.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class SystemStartupReceiver extends BroadcastReceiver {
	private final static String TAG = "SystemStartupReceiver";
	@Override
    public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Util.Log.d(TAG, "onReceive()==action:"+action);
		
		if(ConnectivityManager.CONNECTIVITY_ACTION.equalsIgnoreCase(action)) {
			
			context.startService(new Intent(Util.Action.ACTION_INITTYLESERVICE));
			
		} else if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
			
			context.startService(new Intent(Util.Action.ACTION_INITTYLESERVICE));
			
		} else if(Intent.ACTION_SHUTDOWN.equals(action)){
			
			context.stopService(new Intent(Util.Action.ACTION_INITTYLESERVICE));
		}
	}
	
	
}
