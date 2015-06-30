package com.jzs.android.systemstyle.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jzs.android.systemstyle.utils.ThemeStyleBaseInfo;
import com.jzs.android.systemstyle.utils.Util;
import com.jzs.common.plugin.IPluginManager;
import com.jzs.common.theme.ThemeManager;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;


public class SaveStyleServices extends Service {

	public static final int GET_RESULT = 1;  
	
	private SaveTask mSaveTask;
	
    private final Messenger mMessenger = new Messenger(new Handler() {  
        private int remoteInt = 1;
        @Override  
        public void handleMessage(Message msg) {  
        	switch(msg.what){
        	case Util.MessageWhat.MSG_PLUGIN_SAVESTYLE:
        		if(Util.DEBUG_SAVESTYLE)
        			Util.Log.d("SaveStyleServices===recv()=jzsId:"+ msg.arg1 + "===jzcId:"+ msg.arg2);
        		//android.util.Log.e("QsLog",);
        		if(mSaveTask == null)
        			mSaveTask = new SaveTask(msg.replyTo);
        		else
        			return;
        		
        		mSaveTask.execute(msg.arg1, msg.arg2);
        		return;
    		default:
    			break;
        	}
        	
        	super.handleMessage(msg);
        }  
    });  
      
    @Override  
    public IBinder onBind(Intent intent) {  
        return mMessenger.getBinder();  
    }

    final static String QS_BOOT_ANIMATION_KEY = "persist.qs.bootanimation";
    final static String QS_SHUT_ANIMATION_KEY = "persist.qs.shutanimation";
    protected boolean changeStyle(int nStyle, int subkey)
    {
    	//ComponentName cn = ComponentName.unflattenFromString(
        //        mContext.getString(com.android.internal.R.string.config_statusBarComponent));
        final boolean bIsReboot = !(Util.getPropString("ro.kernel.qemu").equals("1"));//android.os.Build.BOARD.equals("generic");
        
    	if(bIsReboot){
    		
	    	PackageManager mPm = getPackageManager();
	    	Intent intget = new Intent(Intent.ACTION_MAIN);
	    	intget.addCategory(Intent.CATEGORY_HOME);
	    	//intget.setComponent(null);
	    	List<ResolveInfo> rList = mPm.queryIntentActivities(
	    			intget, PackageManager.MATCH_DEFAULT_ONLY | PackageManager.GET_RESOLVED_FILTER );
	    	
	    	String strCn = null;
			if(nStyle == ThemeManager.STYLE_IPHONE){
				strCn = "com.android.iphonelauncher/com.android.iphonelauncher.Launcher";
			} else if(nStyle == ThemeManager.STYLE_SAMSUNG){
				//subkey = android.os.Build.QSCUSTOMER_I9500;
				strCn = "com.android.qstwzlauncher/com.android.qstwzlauncher.Launcher";
			}  else if(nStyle == ThemeManager.STYLE_HTC){
				strCn = "com.android.qshtclauncher2/com.android.qshtclauncher2.Launcher";
			} else {
				strCn = "com.android.launcher/com.android.launcher2.Launcher";
			}
			ComponentName cn = ComponentName.unflattenFromString(strCn);
			
			//android.util.Log.e("QsLog","====strCn:"+ strCn);
			ResolveInfo defaultRi = null;
			int N;
	        if ((rList != null) && ((N = rList.size()) > 0)) {
	            // Only display the first matches that are either of equal
	            // priority or have asked to be default options.
	        	int bestMatch = 0;
	        	boolean bContained = false;
	        	ComponentName[] set = new ComponentName[N];
	            for (int i=0; i<N; i++) {
	                ResolveInfo ri = rList.get(i);
	                set[i] = new ComponentName(ri.activityInfo.packageName,
	                		ri.activityInfo.name);
	                if (ri.match > bestMatch) bestMatch = ri.match;
	                
	                if(!bContained){
	                	bContained = set[i].equals(cn);
	                	if(bContained){
	                		defaultRi = ri;
	                	}
	                }
	                
	                //android.util.Log.e("QsLog","==bContained:"+bContained+"==packageName:"+ ri.activityInfo.packageName+"==class:"+ri.activityInfo.name);
	                
	                if(set[i].getClassName().startsWith(set[i].getPackageName()))
	                	mPm.clearPackagePreferredActivities(set[i].getPackageName());
	                else
	                	mPm.clearPackagePreferredActivities(set[i].getClassName());
	            }
	            //android.util.Log.d("QsLog","====bestMatch:"+ Integer.toHexString(bestMatch) + "===N:"+ N+"==Contained:"+bContained);
	            if(bContained)
	            {
		    		IntentFilter filter = new IntentFilter();
		    		filter.addAction(Intent.ACTION_MAIN);
		    		if(defaultRi != null && defaultRi.filter != null){
		    			for(int i=0; i<defaultRi.filter.countCategories(); i++){
		    				filter.addCategory(defaultRi.filter.getCategory(i));
		    			}
		    		} else {
			    		filter.addCategory(Intent.CATEGORY_HOME);
		    		}
		    		filter.addCategory(Intent.CATEGORY_DEFAULT);
		    		
		    		List<ComponentName> prefActList = new ArrayList<ComponentName>();
		    		List<IntentFilter> intentList = new ArrayList<IntentFilter>();
		
		    		mPm.getPreferredActivities(intentList, prefActList, cn.getPackageName());
//		    		android.util.Log.d("QsLog","=packageName:"+ cn.getPackageName() + "===prefActList:"+ prefActList.size()+"==match:0x"+Integer.toHexString(bestMatch));
//		    		for(ComponentName item : prefActList){
//		    			android.util.Log.d("QsLog","========:"+ item);
//		    		}
	                //bestMatch = (bestMatch | 0x80000000);
		    		mPm.addPreferredActivity(filter, bestMatch, set, cn);
	            }
	        }

	        IPluginManager pluginMgr = (IPluginManager)super.getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
	        if(pluginMgr != null)
	        	pluginMgr.changeSystemStyle(nStyle, subkey);
	        //android.util.Log.d("QsLog","=animfile:"+ animfile + "===shutanim:"+ shutanim+"==nStyle:"+nStyle);
    	}

		if(bIsReboot){
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
		
		return bIsReboot;
    }
    
    private class SaveTask extends AsyncTask<Integer, Integer, Boolean> {
    	
    	private final Messenger mReplyMessenger; 
    	public SaveTask(Messenger replyMsg){
    		mReplyMessenger = replyMsg;
    	}
    	
		protected Boolean doInBackground(Integer... types) {
			return changeStyle(types[0], types[1]);
		}
		
		//@Override
        protected void onProgressUpdate(Integer... values) {
        	
		}

		protected void onPreExecute() {
			if (mReplyMessenger != null) {
				try {
					mReplyMessenger.send(Message.obtain(null, Util.MessageWhat.MSG_PLUGIN_SAVESTYLE_BEGIN));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
    		
			mSaveTask = null;
			result = (result && !isCancelled());
			
			if (mReplyMessenger != null) {
				try {
					mReplyMessenger.send(Message.obtain(null, Util.MessageWhat.MSG_PLUGIN_SAVESTYLE_END,
							(result ? Util.MessageArg.MSG_ARG_SUCCESS : Util.MessageArg.MSG_ARG_FAIL), 0));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
        	if(!result){
        		return;
        	}

        	PowerManager pm = (PowerManager) SaveStyleServices.this.getSystemService(Context.POWER_SERVICE);
			pm.reboot("launcher");
		}
	}
    
}
