package com.jzs.android.systemstyle.activities;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;

import com.jzs.android.systemstyle.R;
import com.jzs.android.systemstyle.controls.CustomAppsAdapter;
import com.jzs.android.systemstyle.utils.Util;
import com.jzs.common.content.QsIntent;
import com.jzs.common.plugin.IPluginManager;
import com.jzs.common.plugin.IPluginTarget;

public class ListSupportAppsActivity extends Activity {

	private CustomAppsAdapter mCustomAppsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_apps);
		
		
		ListView listview = (ListView)super.findViewById(android.R.id.list);
		if(listview != null){
			
			mCustomAppsAdapter = new CustomAppsAdapter(this);
			listview.setAdapter(mCustomAppsAdapter);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					IPluginTarget item = (IPluginTarget)parent.getItemAtPosition(position);
					if(item != null){
						showAllPlugins(item);
//						mDetailContainer.setTag(item);
//						if(mLoadTask != null)
//							mLoadTask.cancel(true);
//						android.util.Log.w("QsLog","onItemClick=jzsId:"+ item.mMainId + "===jzcId:"+ item.mExtId);
//						mLoadTask = new LoadDetailTask();
//						mLoadTask.execute(item.mMainId, item.mExtId);
					}
				}
			});
			
			IPluginManager pluginMgr = (IPluginManager)super.getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
			if(pluginMgr != null){
				
				pluginMgr.dumpPlugins();
				
				List<IPluginTarget> list= pluginMgr.getPluginTargetList();
				if(list != null){
					mCustomAppsAdapter.addTarget(list);
					mCustomAppsAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	private void showAllPlugins(IPluginTarget item){
		Intent intent = new Intent(QsIntent.ACTION_JZS_LIST_PLUGINS);
		//intent.setClass(this, ListPluginActivity.class);
		intent.setClass(this, ListThemeActivity.class);
		intent.putExtra(QsIntent.EXTTRA_JZS_LIST_PLUGINS, item.getKey());
		startActivity(intent);
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
}
