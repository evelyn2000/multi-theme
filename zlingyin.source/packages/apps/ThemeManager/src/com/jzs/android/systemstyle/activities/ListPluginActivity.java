package com.jzs.android.systemstyle.activities;


import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.text.TextUtils;
import android.view.View;

import com.jzs.android.systemstyle.R;
import com.jzs.android.systemstyle.controls.CustomAppsAdapter;
import com.jzs.android.systemstyle.utils.Util;
import com.jzs.common.content.QsIntent;
import com.jzs.common.plugin.IPlugin;
import com.jzs.common.plugin.IPluginManager;

public class ListPluginActivity extends Activity {

	public final static String PLUGIN_TARGET_KEY = "PLUGIN_TARGET_KEY";
	
	private String mTargetKey;
	private CustomAppsAdapter mCustomAppsAdapter;
	
	private SetDefaultTask mSetDefaultTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTargetKey = getIntent().getStringExtra(QsIntent.EXTTRA_JZS_LIST_PLUGINS);
		if(TextUtils.isEmpty(mTargetKey)){
		    finish();
		    return;
		}
		setContentView(R.layout.activity_apps);
		
//		View view = findViewById(R.id.apply_item);
//        if(view != null){
//        	view.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//        }
		
		ListView listview = (ListView)super.findViewById(android.R.id.list);
		if(listview != null){
			
			mCustomAppsAdapter = new CustomAppsAdapter(this);
			listview.setAdapter(mCustomAppsAdapter);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					IPlugin item = (IPlugin)parent.getItemAtPosition(position);
					if(item != null){
						//showAllPlugins(item);
//						mDetailContainer.setTag(item);
						if(mSetDefaultTask != null)
							mSetDefaultTask.cancel(true);
						//android.util.Log.w("QsLog","ListThemeActivity::onItemClick=jzsId:"+ item.mMainId + "===jzcId:"+ item.mExtId);
						mSetDefaultTask = new SetDefaultTask();
						mSetDefaultTask.execute(item);
					}
				}
			});
			
			if(!TextUtils.isEmpty(mTargetKey)){
				IPluginManager pluginMgr = (IPluginManager)super.getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
				if(pluginMgr != null){
					
					//pluginMgr.dumpPlugins();
					
					List<IPlugin> list= pluginMgr.getPluginListByTargetType(mTargetKey);
					if(list != null){
						mCustomAppsAdapter.addPlugin(list);
						mCustomAppsAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}
	
	@Override
    protected void onDestroy() {
		//mLoadTask.cancel(true);
        super.onDestroy();
	}
	
	private Dialog mProgressDialog = null;
    private void showWaitingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = Util.showWaitingDialog(this, R.layout.progressbar);
        }
    }
    
    private void finishWaitingDialog() {
        if (mProgressDialog != null) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                // We catch exception here, because have no impact on user
                android.util.Log.d("QsLog", "Exception when Dialog.dismiss()...");
            } finally {
                mProgressDialog = null;
            }
        }
    }
    
    private boolean setDefaultPlugin(IPlugin plugin){
    	IPluginManager pluginMgr = (IPluginManager)getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
		if(pluginMgr != null){
			pluginMgr.setDefaultPlugin(plugin);
//			List<IPlugin> list= pluginMgr.getPluginListByTargetType(mTargetKey);
//			if(list != null){
//				mCustomAppsAdapter.addPlugin(list);
//				mCustomAppsAdapter.notifyDataSetChanged();
//			}
		}
    	return true;
    }
    
    private class SetDefaultTask extends AsyncTask<IPlugin, Void, Boolean> {

    	protected Boolean doInBackground(IPlugin... types) {
    		//IPlugin plugin = types[0];
			return setDefaultPlugin(types[0]);
		}
		
		//@Override
        protected void onProgressUpdate(Integer... values) {
        	
		}

		protected void onPreExecute() {
			showWaitingDialog();
//			if (mReplyMessenger != null) {
//				try {
//					mReplyMessenger.send(Message.obtain(null, Util.MessageWhat.MSG_PLUGIN_SAVESTYLE_BEGIN));
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//			}
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
    		
			mSetDefaultTask = null;
			result = (result && !isCancelled());
			
			finishWaitingDialog();
			
//			if (mReplyMessenger != null) {
//				try {
//					mReplyMessenger.send(Message.obtain(null, Util.MessageWhat.MSG_PLUGIN_SAVESTYLE_END,
//							(result ? Util.MessageArg.MSG_ARG_SUCCESS : Util.MessageArg.MSG_ARG_FAIL), 0));
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//			}
			
			ListPluginActivity.this.finish();

//        	PowerManager pm = (PowerManager) SaveStyleServices.this.getSystemService(Context.POWER_SERVICE);
//			pm.reboot("launcher");
		}
	}
}
