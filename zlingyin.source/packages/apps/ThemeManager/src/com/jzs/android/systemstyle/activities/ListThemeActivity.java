package com.jzs.android.systemstyle.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TabWidget;

import com.jzs.android.systemstyle.R;
import com.jzs.android.systemstyle.controls.BookmarkAdapter;
import com.jzs.android.systemstyle.controls.BookmarkItem;
import com.jzs.android.systemstyle.controls.BookmarkView;
import com.jzs.android.systemstyle.controls.CustomPluginStyleAdapter;
import com.jzs.android.systemstyle.controls.PageViewContainer;
import com.jzs.android.systemstyle.controls.PageViewIndicatorCallbackImp;
import com.jzs.android.systemstyle.utils.Util;
import com.jzs.common.content.QsIntent;
import com.jzs.common.plugin.IPlugin;
import com.jzs.common.plugin.IPluginBase;
import com.jzs.common.plugin.IPluginDetail;
import com.jzs.common.plugin.IPluginManager;
import com.jzs.common.theme.ThemeManager;


public class ListThemeActivity extends Activity {

	private final static String TAG = "Jzs.ListStyleActivity";
	private PageViewContainer mPageViewContainer;
	private TabWidget mTabWidget;
	
	private View mDetailContainer;
	private View mApplyThemeBtn;
	
	private LoadDetailTask mLoadTask;
	
	private static final float COVER_MULTIPLE_IMAGE = 0.25f;
    private static final float COVER_EXTEND_IMAGE = 0.5f;
    private static final float IMAGE_REFLECTION = 0.25f;
    private static final float MAX_ZOOM_OUT = 350;
    private BookmarkAdapter mBookmarkAdapter;
    
    private CustomPluginStyleAdapter mLocalStyleAdapter;// = new ArrayList<SimpleStyleItem>();
	private CustomPluginStyleAdapter mOnlineStyleAdapter;
    
	private SetDefaultTask mSetDefaultTask;
	private String mPluginKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPluginKey = getIntent().getStringExtra(QsIntent.EXTTRA_JZS_LIST_PLUGINS);
		
		if(Util.SUPPORT_ONLINE_STYLE)
			setContentView(R.layout.activity_liststyle);
		else
			setContentView(R.layout.activity_liststyle_local);
		
		mDetailContainer = super.findViewById(R.id.detailContainer);
		if(mDetailContainer != null){
			mApplyThemeBtn = mDetailContainer.findViewWithTag("apply_item");
			//mApplyThemeBtn = 
	        if(mApplyThemeBtn != null){
	        	mApplyThemeBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						IPlugin item = (IPlugin)mDetailContainer.getTag();
						if(item != null){
//							if(mSetDefaultTask != null)
//								mSetDefaultTask.cancel(true);

							//android.util.Log.w("QsLog","ListThemeActivity::onItemClick=apply item:"+ item.toString());
							mSetDefaultTask = new SetDefaultTask();
							mSetDefaultTask.execute(item);
						}
					}
				});
	        }
	        
	        initBookMark();
		}
		
		mPageViewContainer = (PageViewContainer)super.findViewById(R.id.pageviewcontainer);
		mTabWidget = (TabWidget)super.findViewById(R.id.tabbarwidget);
		
		if(mTabWidget != null){
			mPageViewContainer.setPageViewIndicatorCallback(new PageViewIndicatorCallbackImp(){
				
				public void onChangeToScreen(int whichScreen){
					mTabWidget.setCurrentTab(whichScreen);
				}
			});
		}
		
		GridView list = (GridView)mPageViewContainer.findViewWithTag("gridviewlocal");
		if(list != null){
			//View emptyview = ;
			list.setEmptyView(mPageViewContainer.findViewWithTag("localemptyview"));
			
			mLocalStyleAdapter = new CustomPluginStyleAdapter(this);
			list.setAdapter(mLocalStyleAdapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					IPlugin item = (IPlugin)parent.getItemAtPosition(position);
					if(item != null){
						mDetailContainer.setTag(item);
						if(mLoadTask != null)
							mLoadTask.cancel(true);
						//android.util.Log.d("QsLog","onItemClick===plugin:"+ item.toString());
						mLoadTask = new LoadDetailTask();
						mLoadTask.execute(item);
					}
				}
			});
			
			
			if(!TextUtils.isEmpty(mPluginKey)){
				IPluginManager pluginMgr = (IPluginManager)super.getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
				if(pluginMgr != null){
					
					//pluginMgr.dumpPlugins();
					
					List<IPlugin> pluginlist= pluginMgr.getPluginListByTargetType(mPluginKey);
					if(pluginlist != null){
						mLocalStyleAdapter.addPlugin(pluginlist);
						mLocalStyleAdapter.notifyDataSetChanged();
					}
				}
			}
		}
		
//		list = (GridView)mPageViewContainer.findViewWithTag("gridviewonline");
//		if(list != null){
//			list.setEmptyView(mPageViewContainer.findViewWithTag("onlineemptyview"));
//			mOnlineStyleLists = new CustomSimpleStyleAdapter(this, ThemeStyleBaseInfo.THEME_PATH_ONLINE);
//			list.setAdapter(mOnlineStyleLists);
//			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//	            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//					
//				}
//			});
//		}
		
		if(mTabWidget != null){
			mTabWidget.setCurrentTab(0);
			mTabWidget.getChildTabViewAt(0).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mTabWidget.setCurrentTab(0);
					mPageViewContainer.setToScreen(0);
				}
			});
			
			mTabWidget.getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mTabWidget.setCurrentTab(1);
					mPageViewContainer.setToScreen(1);
				}
			});
		}
	}
	
	private void initBookMark(){
		
		BookmarkView bookmark = (BookmarkView) mDetailContainer.findViewWithTag("bookmark");
	    
		int mImgWidth = getResources().getDimensionPixelSize(R.dimen.thumb_disp_width);
		int mImgHeight = getResources().getDimensionPixelSize(R.dimen.thumb_disp_height);
        
		//ArrayList<BookmarkItem> mBookmarkItems;
        mBookmarkAdapter = new BookmarkAdapter(this, new ArrayList<BookmarkItem>());
        bookmark.setBookmarkAdapter(mBookmarkAdapter);
        bookmark.getTitleView().setTextSize(
                getResources().getDimensionPixelSize(R.dimen.title_size));
//        bookmark.setImageDispSize(mImgWidth, mImgHeight);
//        bookmark.setCoverFlowSpacing(
//                -(int) (COVER_MULTIPLE_IMAGE * mImgWidth + COVER_EXTEND_IMAGE));
        bookmark.setImageReflection(IMAGE_REFLECTION);

        bookmark.setEmptyView(null);
        bookmark.setMaxZoomOut(MAX_ZOOM_OUT);

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mDetailContainer.getVisibility() == View.VISIBLE){
			if(mLoadTask != null)
				mLoadTask.cancel(true);
			
			if(mSetDefaultTask != null && !mSetDefaultTask.isCancelled() 
				&& mSetDefaultTask.getStatus() == AsyncTask.Status.RUNNING){
				return;
			}
			
			mDetailContainer.setVisibility(View.GONE);
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finishWaitingDialog();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		if(mLoadTask != null && !mLoadTask.isCancelled() &&  mLoadTask.getStatus() == AsyncTask.Status.RUNNING){
//			showWaitingDialog();
//		}
	}

	@Override
    protected void onDestroy() {		
        super.onDestroy();
        
        if(mLoadTask != null)
        	mLoadTask.cancel(true);
        
        android.util.Log.e("QsLog","onDestroy===");
//        if(mSetDefaultTask != null)
//        	mSetDefaultTask.cancel(false);
        
        if(mDetailContainer.getVisibility() == View.VISIBLE){
			mDetailContainer.setVisibility(View.GONE);
		}
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
		
	public boolean isLocalPageVisible(){
		if(mPageViewContainer != null && mPageViewContainer.getCurrentPageScreen() > 0)
			return false;
		return true;
	}

	private class LoadDetailTask extends AsyncTask<IPluginBase, Integer, Void> {
		
        @Override
        protected Void doInBackground(IPluginBase... params) {
        	final IPluginBase plugin = params[0];
            
        	//loadAndParseDetail(plugin, this);
        	if(plugin == null)
        		return null;
        	
        	final PackageManager pkgmanager = getPackageManager();
        	List<IPluginDetail> list = plugin.getDetailList(pkgmanager);

        	if(list != null){
        		
	        	final ApplicationInfo info = plugin.getApplicationInfo(pkgmanager);
	        	
	        	for(IPluginDetail detail : list){
	        		if(isCancelled()){
	        			break;
	        		}
	        		
	        		final Bitmap bm = Util.drawable2Bitmap(detail.loadPreview(pkgmanager, info));
	        		if(bm != null){
	    	    		BookmarkItem item = new BookmarkItem(bm, detail.getTitle(), detail.getDescription());
	    	    		mBookmarkAdapter.add(item, false);
	    	    		
	    	    		if(mBookmarkAdapter.getCount()%4 == 1)
	    	    			publishProgress(mBookmarkAdapter.getCount());
	        		}
	        	}
        	}
            
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
           mBookmarkAdapter.notifyDataSetChanged();
        }

        protected void onPreExecute() {
			 //showWaitingDialog();
        	mBookmarkAdapter.clear();
        	mDetailContainer.setVisibility(View.VISIBLE);
		}
        
        @Override
        protected void onPostExecute(Void result) {
        	super.onPostExecute(result);

        	if(isCancelled()){
        		mDetailContainer.setVisibility(View.GONE);
        		mBookmarkAdapter.clear();
        		return;
        	}
        	
        	mLoadTask = null;
        	mBookmarkAdapter.notifyDataSetChanged();
        }
    }
	
	private boolean setDefaultPlugin(IPlugin plugin){
		
		IPluginManager pluginMgr = (IPluginManager)super.getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
        if(pluginMgr != null){
        	android.util.Log.w("QsLog","ListThemeActivity::setDefaultPlugin=apply plugin:"+ plugin.toString());
       		pluginMgr.setDefaultPlugin(plugin);
        }
        
		return false;
	}
	
	private class SetDefaultTask extends AsyncTask<IPlugin, Void, Boolean> {
//		private final IPlugin mPlugin;
//		private final View mApplyBtn;
//		public SetDefaultTask(IPlugin plugin, View btn){
//			mPlugin = plugin;
//			mApplyBtn = btn;
//		}
		
		@Override
    	protected Boolean doInBackground(IPlugin... types) {
			return setDefaultPlugin(types[0]);
		}

    	@Override
		protected void onPreExecute() {
    		//android.util.Log.e("QsLog","SetDefaultTask::onPreExecute===");
    		if(mApplyThemeBtn != null)
    			mApplyThemeBtn.setEnabled(false);
			showWaitingDialog();
		}
    	
    	@Override
    	protected void onCancelled() {
    		super.onCancelled();
    		
    		//android.util.Log.e("QsLog","SetDefaultTask::onCancelled===isCancelled:"+isCancelled());
        }
    	
    	@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
    		
			mSetDefaultTask = null;
			result = (result && !isCancelled());
			
			//android.util.Log.e("QsLog","SetDefaultTask::onPostExecute=result:"+ result +"==isCancelled:"+isCancelled());
			
			finishWaitingDialog();
			if(mApplyThemeBtn != null)
				mApplyThemeBtn.setEnabled(true);
			
			if(result){
				PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
				pm.reboot("launcher");
			}
		}
	}
	

}
