package com.jzs.android.systemstyle.activities;

import java.util.ArrayList;
import java.util.List;

import com.jzs.android.systemstyle.ThemeStyleApplication;
import com.jzs.android.systemstyle.controls.BookmarkAdapter;
import com.jzs.android.systemstyle.controls.BookmarkItem;
import com.jzs.android.systemstyle.controls.BookmarkView;
import com.jzs.android.systemstyle.controls.CustomSimpleStyleAdapter;
import com.jzs.android.systemstyle.controls.PageViewContainer;
import com.jzs.android.systemstyle.controls.PageViewIndicatorCallback;
import com.jzs.android.systemstyle.controls.PageViewIndicatorCallbackImp;
import com.jzs.android.systemstyle.model.LauncherModel;
import com.jzs.android.systemstyle.utils.ThemeStyleBaseInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleLocalInfo;
import com.jzs.android.systemstyle.utils.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TabWidget;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.jzs.android.systemstyle.R;
import com.jzs.common.theme.ThemeManager;

public class ListStyleActivity extends Activity implements LauncherModel.Callbacks {
	
	private final static String TAG = "ListStyleActivity";
	private PageViewContainer mPageViewContainer;
	private TabWidget mTabWidget;
	private CustomSimpleStyleAdapter mLocalStyleAdapter;// = new ArrayList<SimpleStyleItem>();
	private CustomSimpleStyleAdapter mOnlineStyleLists ;//= new ArrayList<SimpleStyleItem>();
	
	public LauncherModel mModel;
	
	private View mDetailContainer;
	
	private LoadDetailTask mLoadTask;
	
	private static final float COVER_MULTIPLE_IMAGE = 0.25f;
    private static final float COVER_EXTEND_IMAGE = 0.5f;
    private static final float IMAGE_REFLECTION = 0.25f;
    private static final float MAX_ZOOM_OUT = 350;
    private BookmarkAdapter mBookmarkAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_liststyle);
		
		mDetailContainer = super.findViewById(R.id.detailContainer);
		if(mDetailContainer != null){
			View view = mDetailContainer.findViewWithTag("apply_item");
	        if(view != null){
	        	view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ThemeStyleLocalInfo item = (ThemeStyleLocalInfo)mDetailContainer.getTag();
						if(item != null){
							//mSaveChangedTask.execute(item.jzsId, item.jzcId);
							if(mIsSaveBinding){
								//android.util.Log.d("QsLog","send=jzsId:"+ item.jzsId + "===jzcId:"+ item.jzcId);
								
								Message message = Message.obtain(null, Util.MessageWhat.MSG_PLUGIN_SAVESTYLE, item.mMainId, item.mExtId);  
								message.replyTo = mSaveMessenger;  
								try {  
									mSaveService.send(message);  
								} catch (RemoteException e) {  
								    e.printStackTrace();  
								}  

							} else {
								bindService(new Intent(Util.Action.ACTION_PLUGIN_SAVESTYLESERVICE), mSaveConnection, BIND_AUTO_CREATE);
							}
						}
					}
				});
	        }
	        
	        initBookMark();
		}
		
		mPageViewContainer = (PageViewContainer)super.findViewById(R.id.pageviewcontainer);
		mTabWidget = (TabWidget)super.findViewById(R.id.tabbarwidget);
		
		mModel = ((ThemeStyleApplication)super.getApplicationContext()).getModel();
		mModel.initialize(this);
		
		mModel.startLoader(this, true);
		
		mPageViewContainer.setPageViewIndicatorCallback(new PageViewIndicatorCallbackImp(){
			
			public void onChangeToScreen(int whichScreen){
				mTabWidget.setCurrentTab(whichScreen);
			}
		});
		
		GridView list = (GridView)mPageViewContainer.findViewWithTag("gridviewlocal");
		if(list != null){
			//View emptyview = ;
			list.setEmptyView(mPageViewContainer.findViewWithTag("localemptyview"));
			
			mLocalStyleAdapter = new CustomSimpleStyleAdapter(this, ThemeStyleBaseInfo.THEME_PATH_LOCAL);
			list.setAdapter(mLocalStyleAdapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					ThemeStyleLocalInfo item = (ThemeStyleLocalInfo)parent.getItemAtPosition(position);
					if(item != null){
						mDetailContainer.setTag(item);
						if(mLoadTask != null)
							mLoadTask.cancel(true);
						android.util.Log.w("QsLog","ListStyleActivity::onItemClick=jzsId:"+ item.mMainId + "===jzcId:"+ item.mExtId);
						mLoadTask = new LoadDetailTask();
						mLoadTask.execute(item.mMainId, item.mExtId);
					}
				}
			});
		}
		
		list = (GridView)mPageViewContainer.findViewWithTag("gridviewonline");
		if(list != null){
			list.setEmptyView(mPageViewContainer.findViewWithTag("onlineemptyview"));
			mOnlineStyleLists = new CustomSimpleStyleAdapter(this, ThemeStyleBaseInfo.THEME_PATH_ONLINE);
			list.setAdapter(mOnlineStyleLists);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					
				}
			});
		}
		
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
        bookmark.setImageDispSize(mImgWidth, mImgHeight);
        bookmark.setCoverFlowSpacing(
                -(int) (COVER_MULTIPLE_IMAGE * mImgWidth + COVER_EXTEND_IMAGE));
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
		//mLoadTask.cancel(true);
        super.onDestroy();
        mModel.stopLoader();
        if(mLoadTask != null)
        	mLoadTask.cancel(true);
        
        //mSaveChangedTask.cancel(false);
        if(mIsSaveBinding)
        	super.unbindService(mSaveConnection);
        
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
		if(mPageViewContainer != null && mPageViewContainer.getCurrentPageScreen() == ThemeStyleBaseInfo.THEME_PATH_ONLINE)
			return false;
		return true;
	}
	
	public void bindAllItems(ArrayList<ThemeStyleBaseInfo> list, int type){
		
		if(type == ThemeStyleBaseInfo.THEME_PATH_LOCAL){
			mLocalStyleAdapter.clear();
    		if(list.size() <= 0){
    			View view = mPageViewContainer.findViewWithTag("localemptyview");
    			if(view != null){
    				View progress = view.findViewWithTag("proggressbar");
        			progress.setVisibility(View.GONE);
        			View txt = view.findViewWithTag("promptext");
        			txt.setVisibility(View.VISIBLE);
    			}
    		} else {
    			mLocalStyleAdapter.add(list);
    		}
    		Util.Log.i("Activity", "====size:"+list.size());
    		mLocalStyleAdapter.notifyDataSetChanged();
    		mPageViewContainer.invalidate();
    		
    	} else if(type == ThemeStyleBaseInfo.THEME_PATH_ONLINE){
    		if(list.size() <= 0){
    			View view = mPageViewContainer.findViewWithTag("onlineemptyview");
    			if(view != null){
    				View progress = view.findViewWithTag("proggressbar");
        			progress.setVisibility(View.GONE);
        			View txt = view.findViewWithTag("promptext");
        			txt.setVisibility(View.VISIBLE);
    			}
    		}
    	}
	}
	
    public void bindItemAdded(ArrayList<ThemeStyleBaseInfo> list){
    	
    }
    
    public void bindItemUpdated(ArrayList<ThemeStyleBaseInfo> list){
    	
    }
    
    public void bindItemRemoved(ArrayList<Integer> list){
    	
    }
    
    private Messenger mSaveService;  
    private boolean mIsSaveBinding = false;  
    private ServiceConnection mSaveConnection = new ServiceConnection() {  
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {  
        	mSaveService = new Messenger(service);  
        	mIsSaveBinding = true;
        	
        	ThemeStyleLocalInfo item = (ThemeStyleLocalInfo)mDetailContainer.getTag();
			if(item != null){
	        	//android.util.Log.d("QsLog","send(111)=jzsId:"+ item.jzsId + "===jzcId:"+ item.jzcId);
				
				Message message = Message.obtain(null, Util.MessageWhat.MSG_PLUGIN_SAVESTYLE
						, item.mMainId, item.mExtId);  
				message.replyTo = mSaveMessenger;  
				try {  
					mSaveService.send(message);  
				} catch (RemoteException e) {  
				    e.printStackTrace();  
				}  
			}
        }  
      
        @Override
        public void onServiceDisconnected(ComponentName name) {  
        	mSaveService = null;  
        	mIsSaveBinding = false;  
        } 
    };  
    
    private Messenger mSaveMessenger = new Messenger(new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == Util.MessageWhat.MSG_PLUGIN_SAVESTYLE_BEGIN) {  
                 Util.Log.i(TAG, "Int form process B is "+msg.arg1);
                 showWaitingDialog();
                 //android.util.Log.i("QsLog","recv(111)===MSG_PLUGIN_SAVESTYLE_BEGIN===");
                 return;
            } else if(msg.what == Util.MessageWhat.MSG_PLUGIN_SAVESTYLE_END){
            	finishWaitingDialog();
            	//android.util.Log.i("QsLog","recv(111)===MSG_PLUGIN_SAVESTYLE_END===");
            	return;
            }

            super.handleMessage(msg);
        }  
    });  
        
	
    private final int [] test_images_dr = new int []{
			R.drawable.dr_01,
			R.drawable.dr_02,
			R.drawable.dr_03,
			R.drawable.dr_04,
			R.drawable.dr_05,
	};
    
    private final int [] test_images_ip = new int []{
			R.drawable.ip_01,
			R.drawable.ip_02,
			R.drawable.ip_03,
			R.drawable.ip_04,
			R.drawable.ip_05,
	};
    
    private final int [] test_images_ss = new int []{
			R.drawable.ss_01,
			R.drawable.ss_02,
			R.drawable.ss_03,
			R.drawable.ss_04,
			R.drawable.ss_05,
			R.drawable.ss_06,
			R.drawable.ss_07,
			R.drawable.ss_08,
			R.drawable.ss_09,
			R.drawable.ss_10,
	};
    
    private final int [] test_images_htc = new int []{
			R.drawable.htc_01,
			R.drawable.htc_02,
			R.drawable.htc_03,
			R.drawable.htc_04,
			R.drawable.htc_05,
			R.drawable.htc_06,
			R.drawable.htc_07,
			R.drawable.htc_08,
	};
    
    private class LoadDetailTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int nStyle = params[0];
            
            int [] test_images = test_images_dr;
            if(nStyle == ThemeManager.STYLE_IPHONE){
            	test_images = test_images_ip;
			} else if(nStyle == ThemeManager.STYLE_SAMSUNG){
				test_images = test_images_ss;
			} else if(nStyle == ThemeManager.STYLE_HTC){
				test_images = test_images_htc;
			}
            
            Bitmap bmp = null;
            String title = null;
            boolean publicFlag = false;
            int size = test_images.length;
            for(int i=0; i<size && !isCancelled(); i++){
            	title = "Preview image " + String.valueOf(i + 1);
            	String info = String.valueOf(i + 1) + "/" + String.valueOf(size);
            	Bitmap bm = Util.drawable2Bitmap(getResources().getDrawable(test_images[i]));
                BookmarkItem item = new BookmarkItem(bm, title, info);
                mBookmarkAdapter.add(item, false);
                
                if (!publicFlag && bmp != null/* || title != null*/) {
                	publicFlag = true;
                    publishProgress(i);
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
}
