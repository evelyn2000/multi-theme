package com.jzs.android.systemstyle.services;

import java.util.List;

import com.jzs.common.content.QsIntent;
import com.jzs.common.plugin.IPlugin;
import com.jzs.common.plugin.IPluginManager;
import com.jzs.common.theme.ThemeManager;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.view.WindowManager;
import com.jzs.android.systemstyle.R;

public class EnablePluginService extends IntentService {

	public EnablePluginService(){
		super("EnableDuiPluginService");
	}
	
	protected void onHandleIntent(Intent intent)
	{
		if(intent == null) return;
		
		final String pluginkey = intent.getStringExtra(QsIntent.EXTTRA_DUI_PLUGIN_KEY);
		if(TextUtils.isEmpty(pluginkey)){
			android.util.Log.e("Jzs.Plugin", "extra attr:'"+QsIntent.EXTTRA_DUI_PLUGIN_KEY+"' can't be empty.");
			return;
		}
		
		IPluginManager pluginMgr = (IPluginManager)getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
		if(pluginMgr != null){
			IPlugin plugin = pluginMgr.getPlugin(pluginkey);
			if(plugin != null){
				
//				ProgressDialog dialog = new ProgressDialog(this);
//		        dialog.setTitle(getString(R.string.app_name));
//		        dialog.setMessage(getString(R.string.str_waiting));
//		        dialog.setIndeterminate(true);
		        //dialog.setCancelable(cancelable);
		        //dialog.setOnCancelListener(cancelListener);
		        
		        
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle(getString(R.string.cancel_download_title)) 
//		        		.setMessage(getString(R.string.cancel_download_content)));
//				dialog = builder.create();
//				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//use alert.  
//				dialog.show();
				
				if(IPluginManager.PLUGIN_FOR_SYSTEMTHEMESTYLE.equals(plugin.getPluginFor())){
					resetLauncher(plugin);
				}
				
				pluginMgr.setDefaultPlugin(plugin);

				//dialog.dismiss();
			}
		}
	}
	
	private void resetLauncher(IPlugin plugin){
		PackageManager pm = getPackageManager();
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	List<ResolveInfo> list = pm.queryIntentActivities(
    			intent, PackageManager.MATCH_DEFAULT_ONLY 
			    			| PackageManager.GET_RESOLVED_FILTER
			    			| PackageManager.GET_DISABLED_COMPONENTS);
    	
    	final String defpkg;
    	
    	if(plugin.getPluginForThemeMainStyle() == ThemeManager.STYLE_LAOREN){
    		defpkg = "com.android.qs.qsvideochatlauncher";
    	} else {
    		defpkg = "com.jzs.dr.mtplauncher";
    	}
    	
    	for(ResolveInfo info : list){
//    		android.util.Log.i("Jzs.Theme", info.toString());
//    		android.util.Log.v("Jzs.Theme", info.activityInfo.toString());
    		
    		ComponentName set = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
    		
    		int status = pm.getComponentEnabledSetting(set);
    		
//    		android.util.Log.i("Jzs.Theme", set.toString()
//    				+", enable:"+status);
    		
    		if("com.android.settings".equals(info.activityInfo.packageName)
    			|| "com.android.provision".equals(info.activityInfo.packageName)){
    			continue;
    		}
    		
    		if(true){
	    		if(defpkg.equals(info.activityInfo.packageName)){// .activityInfo.packageName)
	    			if(status >= PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
		    			pm.setComponentEnabledSetting(set, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 
		    					PackageManager.DONT_KILL_APP);
	    			}
	    		} else if(status < PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
	    			pm.setComponentEnabledSetting(set, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
	    					PackageManager.DONT_KILL_APP);
	    		}
    		}
    	}
	}
}
