package com.jzs.android.systemstyle.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jzs.android.systemstyle.https.DownloadInfo;
import com.jzs.android.systemstyle.https.HttpManager;
import com.jzs.android.systemstyle.model.LauncherModel;
import com.jzs.android.systemstyle.utils.ThemeStyleBaseInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleLocalInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleOnlineInfo;
import com.jzs.android.systemstyle.utils.ThemesSettings;
import com.jzs.android.systemstyle.utils.Util;
import com.jzs.common.theme.ThemeManager;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;



public class InitStyleInfoService extends Service {
	private static final String TAG = "LoadStyleInfoService";
	private ServiceBinder mBinder = new ServiceBinder();
	//private HttpManager mHttpManager;
	private static SessionStateControlThread sHandleThread = null;
	private static SessionStateControlThread sHandleOnlineThread = null;
	private static SessionStateControlThread sHandleDownloadImgThread = null;
	private DownloadInfo mDownloadInfo;
	
	private static final int DOWNLOAD_IMAGE_THREAD_TYPE = 100;
	//private static final int DOWNLOAD_IMAGE_THREAD_TYPE = 100;
	
	public static final String EXT_START_KEY = "start_ext_key";
	
	public class ServiceBinder extends Binder {
		InitStyleInfoService getService() {
            return InitStyleInfoService.this;
        }
    }

	@Override
    public void onCreate() {
        super.onCreate();
        Util.Log.i(TAG, "[onCreate], ");
        
        mDownloadInfo = DownloadInfo.getInstance(this.getApplicationContext());
        
        if(sHandleThread == null){
        	sHandleThread = new SessionStateControlThread(ThemeStyleBaseInfo.THEME_PATH_LOCAL);
        }
        
        if(sHandleOnlineThread == null){
        	sHandleOnlineThread = new SessionStateControlThread(ThemeStyleBaseInfo.THEME_PATH_ONLINE);
        }
        
        if(sHandleDownloadImgThread == null){
        	sHandleDownloadImgThread = new SessionStateControlThread(DOWNLOAD_IMAGE_THREAD_TYPE);
        }
    }

    @Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Util.Log.d(TAG, "Service onStart");
	}

	@Override
    public void onDestroy() {
    	Util.Log.d(TAG, "Service onDestroy");
        super.onDestroy();
        if(sHandleThread != null){
        	sHandleThread.setExitThread();
        }
        
        if(sHandleOnlineThread != null){
        	sHandleOnlineThread.setExitThread();
        }
        
        if(sHandleDownloadImgThread != null){
        	sHandleDownloadImgThread.setExitThread();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }
    
    @Override 
    public int onStartCommand(Intent intent, int flags, int startId) { 
    	String action = intent.getAction();
    	
    	int type = intent.getIntExtra(EXT_START_KEY, -1); 
    	Util.Log.i(TAG, "onStartCommand==action:"+action+"==type:"+type); 
    	
    	if(type >= 0){
    		if(type == ThemeStyleBaseInfo.THEME_PATH_LOCAL){
    			if(sHandleThread != null && !sHandleThread.isAlive()){
    	        	sHandleThread.start();
    	        }
    		} else if(type == ThemeStyleBaseInfo.THEME_PATH_ONLINE){
    			if(sHandleOnlineThread != null && !sHandleOnlineThread.isAlive()){
    				sHandleOnlineThread.start();
    	        }
    		} else if(type == DOWNLOAD_IMAGE_THREAD_TYPE){
    			if(sHandleDownloadImgThread != null && !sHandleDownloadImgThread.isAlive()){
    				sHandleDownloadImgThread.start();
    	        }
    		}
    	} else {
    		if(sHandleThread != null && !sHandleThread.isAlive()
    				&& sHandleOnlineThread != null && !sHandleOnlineThread.isAlive()
    				&& sHandleDownloadImgThread != null && !sHandleDownloadImgThread.isAlive()){
    			
    			sHandleOnlineThread.start();
	        	sHandleThread.start();
	        }
    	}
        return START_STICKY;
    }
    
    private void checkAndStopService(){
    	Util.Log.d(TAG, "checkAndStopService()==local:"+checkLocalThreadFinished()
    			+"==online:"+checkOnlineThreadFinished()
    			+"==download:"+checkDownloadThreadFinished());
    	if(checkLocalThreadFinished()
			&& checkOnlineThreadFinished()
			&& checkDownloadThreadFinished()){
			
    		stopSelf();
        }
    }
    
    private boolean checkLocalThreadFinished(){
    	return (sHandleThread == null || !sHandleThread.isAlive() || sHandleThread.isFinishedTask());
    }
    
    private boolean checkOnlineThreadFinished(){
    	return (sHandleOnlineThread == null || !sHandleOnlineThread.isAlive() || sHandleOnlineThread.isFinishedTask());
    }
    
    private boolean checkDownloadThreadFinished(){
    	return (sHandleDownloadImgThread == null || !sHandleDownloadImgThread.isAlive() || sHandleDownloadImgThread.isFinishedTask());
    }
    
//    private void initialized(SessionStateControlThread thread){
//    	//final DownloadInfo downloadInfo = DownloadInfo.getInstance(this.getApplicationContext());
//    	
//    	//InitLocalStyle(downloadInfo, thread);
//    	//InitOnlineStyle(downloadInfo, thread);
//    	
//    	stopSelf();
//    }
    
    private void InitOnlineStyle(SessionStateControlThread thread){
    	if(thread.isExitThread() || !Util.isNetWorkAvailable(this) 
    		|| mDownloadInfo.getInitializeOnlineStyleStatus() == DownloadInfo.INIT_ONLINE_STYLE_STATUS_NOTSUPPORT){
    		return;
    	}
    	
    	mDownloadInfo.setInitializeOnlineStyleStatus(DownloadInfo.INIT_ONLINE_STYLE_STATUS_CHECKING);
    	
    	HttpManager httpManager = HttpManager.getInstance(this.getApplicationContext());
    	
    	int deviceid = httpManager.registerMyDevice();
    	if(!DownloadInfo.isValidMyDevicesId(deviceid)){
    		if(deviceid == HttpManager.HTTP_RESPONSE_NOSUPPORT_DEVICES)
    			mDownloadInfo.setInitializeOnlineStyleStatus(DownloadInfo.INIT_ONLINE_STYLE_STATUS_NOTSUPPORT);
    		else
    			mDownloadInfo.setInitializeOnlineStyleStatus(deviceid);
    		return;
    	}
    	
    	if(thread.isExitThread() || !Util.isNetWorkAvailable(this, mDownloadInfo.getIfWifiDLOnly() ? "wifi" : null)){
    		if(thread.isExitThread())
    			mDownloadInfo.setInitializeOnlineStyleStatus(DownloadInfo.INIT_ONLINE_STYLE_STATUS_NOTSTART);
    		else
    			mDownloadInfo.setInitializeOnlineStyleStatus(DownloadInfo.INIT_ONLINE_STYLE_STATUS_NETWORK_LIMIT);
    		return;
    	}
    	
    	mDownloadInfo.setInitializeOnlineStyleStatus(DownloadInfo.INIT_ONLINE_STYLE_STATUS_DOWNLOADING);
    	final int MAX_SIZE = 3;
    	String startId = "";
    	//int count = 1;
    	
    	HashMap<String, TempSimpleStyleInfo> mSavedStyleMap = new HashMap<String, TempSimpleStyleInfo>();
    	final ContentResolver contentResolver = getContentResolver();
        
        final Cursor c = contentResolver.query(ThemesSettings.CONTENT_URI,
                new String[]{ThemesSettings.THEME_ID, 
        		ThemesSettings.THEME_SERVER_ID, 
        		ThemesSettings.THEME_VERSION, 
        		ThemesSettings.THEME_PREVIEW_ICON_URL},
                ThemesSettings.THEME_PATH_TYPE+"="+ThemeStyleBaseInfo.THEME_PATH_ONLINE, 
                null, null);
        
		try {
			while (!thread.isExitThread() && c.moveToNext()) {
				try {

					int id = c.getInt(0);
					String serid = c.getString(1);
					String ver = c.getString(2);
					String img = c.getString(3);

					mSavedStyleMap.put(serid, new TempSimpleStyleInfo(id, ver, img));

				} catch (Exception e) {
					Util.Log.w(TAG, "Desktop items loading interrupted:" + e.getMessage());
				}
			}

		} finally {
			c.close();
		}
        
    	final ArrayList<ThemeStyleOnlineInfo> list = new ArrayList<ThemeStyleOnlineInfo>(MAX_SIZE);
    	while(httpManager.getSupportVersionThumbList(list, MAX_SIZE, startId) > 0){
    		for(ThemeStyleOnlineInfo item : list){
    			
    			startId = item.mServerKey;
    			
    			TempSimpleStyleInfo old = mSavedStyleMap.get(item.mServerKey);
    			if(old != null){
    				item.mId = old.id;
    				
    				final ContentValues values = new ContentValues();
    				item.onAddToDatabase(values);
    				
    				old.imgurl = item.mIconUrl;
    				if(old.version == null || !old.version.equalsIgnoreCase(item.mVersion)){
    					values.put(ThemesSettings.THEME_PREVIEW_ICON, "");
    				} else {
    					old = null;
    				}
    				
    				ThemesSettings.updateItemInDatabase(this, item.mId, values, false);
    				
    			} else {
    				int id = ThemesSettings.addItemToDatabase(this, item, false);
    				if(id > ThemeStyleBaseInfo.NO_ID)
    					old = new TempSimpleStyleInfo(id, item.mVersion, item.mIconUrl);
    			}
    			
    			if(old != null){
    				sHandleDownloadImgThread.pushDownloadImageItem(old);
    				if(!sHandleDownloadImgThread.isAlive() || sHandleDownloadImgThread.isFinishedTask())
    					sHandleDownloadImgThread.start();
    			}
    		}
    		list.clear();
    	}
    	
    	mDownloadInfo.setInitializeOnlineStyleStatus(DownloadInfo.INIT_ONLINE_STYLE_STATUS_DONE);
    }
    
    private void InitLocalStyle(SessionStateControlThread thread){

    	if(mDownloadInfo.isInitializedLocalStyle()){
    		return;
    	}
    	
    	List<ThemeStyleBaseInfo> all = new ArrayList<ThemeStyleBaseInfo>();
    	all.add(new ThemeStyleLocalInfo(0, 0, 0, "Android", 
	    		Util.drawable2Bitmap(getResources().getDrawable(com.jzs.android.systemstyle.R.drawable.dr_01))
	    		));
    	
		if(com.jzs.utils.ConfigOption.QS_THEME_SAMSUNG){
			all.add(new ThemeStyleLocalInfo(0, ThemeManager.STYLE_SAMSUNG, ThemeManager.SUB_STYLE_I9500, "Samsung", 
					Util.drawable2Bitmap(getResources().getDrawable(com.jzs.android.systemstyle.R.drawable.ss_01))
					));
		}
		
		if(com.jzs.utils.ConfigOption.QS_THEME_IPHONE){
			all.add(new ThemeStyleLocalInfo(0, ThemeManager.STYLE_IPHONE, 0, "Iphone", 
					Util.drawable2Bitmap(getResources().getDrawable(com.jzs.android.systemstyle.R.drawable.ip_01))
					));
		}
		
		if(com.jzs.utils.ConfigOption.QS_THEME_HTC){
			all.add(new ThemeStyleLocalInfo(0, ThemeManager.STYLE_HTC, 0, "Htc", 
					Util.drawable2Bitmap(getResources().getDrawable(com.jzs.android.systemstyle.R.drawable.htc_01))
					));
		}

		for (ThemeStyleBaseInfo item : all) {
		    ContentValues values = new ContentValues();
		    item.onAddToDatabase(values);
		    //db.insert(ThemesSettings.TABLE_NAME, null, values);
		    LauncherModel.addItemToDatabase(this, item, false);
		}
//    	
    	mDownloadInfo.setInitializedLocalStyle();
    }
    
    private void downloadPreviewImages(TempSimpleStyleInfo info, Thread thread){
    	if(info == null || info.id == ThemeStyleBaseInfo.NO_ID)
    		return;
    	
    	final Bitmap bmp = Util.downloadImages(info.imgurl);
    	if(bmp != null){
    		final ContentValues values = new ContentValues();
    		if(ThemeStyleBaseInfo.writeBitmap(values, bmp))
    			ThemesSettings.updateItemInDatabase(this, info.id, values, true);
    	}
    }
    
    class SessionStateControlThread extends Thread {
    	private boolean mIsExitFlag;
    	private boolean misFinishedTask;
    	private int mType;
    	private final ArrayList<TempSimpleStyleInfo> mDownloadArray;
    	public SessionStateControlThread(int type){
    		mType = type;
    		misFinishedTask = false;
    		mDownloadArray = new ArrayList<TempSimpleStyleInfo>();
    	}
    	
    	public int getHandlerType(){
    		return mType;
    	}
    	
    	public void pushDownloadImageItem(TempSimpleStyleInfo info){
    		if(mType != DOWNLOAD_IMAGE_THREAD_TYPE)
    			return;
    		
    		synchronized(mDownloadArray){
    			mDownloadArray.add(info);
    		}
    	}
    	
    	public void run() {
    		synchronized(this){
    			mIsExitFlag = false;
    			misFinishedTask = false;
    			
    			if(mType == ThemeStyleBaseInfo.THEME_PATH_ONLINE){
    				InitOnlineStyle(this);
    			} else if(mType == ThemeStyleBaseInfo.THEME_PATH_LOCAL){
    				InitLocalStyle(this);
    			} else if(mType == DOWNLOAD_IMAGE_THREAD_TYPE){
    				
    				while(!mIsExitFlag && (!mDownloadArray.isEmpty() || !checkOnlineThreadFinished() || !checkLocalThreadFinished())){
    					    				
    					if(mDownloadArray.isEmpty()){
    						try {
    			                Thread.sleep(1000);
    			            } catch (InterruptedException e) {
    			            }
    					} else {
    						synchronized(mDownloadArray){
    							downloadPreviewImages(mDownloadArray.remove(0), this);
    						}
    					}
    				}
    			}

    			misFinishedTask = true;
            	checkAndStopService();
    		}
        }
    	
    	public boolean isFinishedTask(){
    		return misFinishedTask;
    	}
    	
    	public boolean isExitThread(){
    		return mIsExitFlag;
    	}
    	
    	public void setExitThread(){
    		synchronized(this){
    			mIsExitFlag = true;
    		}
    	}
    }
    
    class TempSimpleStyleInfo{
		int id;
		String version;
		String imgurl;
		public TempSimpleStyleInfo(int id, String ver, String img){
			this.id = id;
			this.version = ver;
			this.imgurl = img;
		}
	}
}
