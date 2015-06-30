package com.jzs.android.systemstyle.model;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.jzs.android.systemstyle.ThemeStyleApplication;
import com.jzs.android.systemstyle.utils.ThemeStyleBaseInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleLocalInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleOnlineInfo;
import com.jzs.android.systemstyle.utils.ThemesSettings;
import com.jzs.android.systemstyle.utils.Util;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;


public class LauncherModel extends BroadcastReceiver {
	
	private final static String TAG = "LauncherModel";
	
	private LoaderTask mLoaderTask;
	private final Object mLock = new Object();
    private DeferredHandler mHandler = new DeferredHandler();
    private static final HandlerThread sWorkerThread = new HandlerThread("jzs-style-loader");
	static {
		sWorkerThread.start();
	}
	private static final Handler sWorker = new Handler(sWorkerThread.getLooper());
	
	private final ThemeStyleApplication mApp;
	private WeakReference<Callbacks> mCallbacks;
	private static final ArrayList<ThemeStyleBaseInfo> sAllItems = new ArrayList<ThemeStyleBaseInfo>();
	private boolean mIsAlreadyLoaded;
	
	public interface Callbacks {
		public boolean isLocalPageVisible();
		public void bindAllItems(ArrayList<ThemeStyleBaseInfo> list, int type);
        public void bindItemAdded(ArrayList<ThemeStyleBaseInfo> list);
        public void bindItemUpdated(ArrayList<ThemeStyleBaseInfo> list);
        public void bindItemRemoved(ArrayList<Integer> list);
	}
	
	public LauncherModel(ThemeStyleApplication app){
		mApp = app;
		Util.Log.i(TAG, "LauncherModel()");
	}
	
	public static int addItemToDatabase(Context context, ThemeStyleBaseInfo item) {
		return addItemToDatabase(context, item, true);
	}
	
	public static int addItemToDatabase(Context context, ThemeStyleBaseInfo item, boolean notify) {
        
        final ContentValues values = new ContentValues();
        final ContentResolver cr = context.getContentResolver();

        item.onAddToDatabase(values);
        Uri result = cr.insert(notify ? ThemesSettings.CONTENT_URI : ThemesSettings.CONTENT_URI_NO_NOTIFICATION, values);

        if (result != null) {
            item.mId = Integer.parseInt(result.getPathSegments().get(1));
            return item.mId;
        }
        
        return ThemeStyleBaseInfo.NO_ID;
    }
	
    /**
     * Move an item in the DB to a new <container, screen, cellX, cellY>
     */
	public static void updateItemInDatabase(Context context, ThemeStyleBaseInfo item, boolean notify) {
        
        final Uri uri = ThemesSettings.getContentUri(item.mId, notify);
        final ContentValues values = new ContentValues();
        final ContentResolver cr = context.getContentResolver();

        item.onAddToDatabase(values);
        
        sWorker.post(new Runnable() {
            public void run() {
                cr.update(uri, values, null, null);
            }
        });
    }
	
	public static void updateItemInDatabase(Context context, int id, final ContentValues values, boolean notify) {
        
        final Uri uri = ThemesSettings.getContentUri(id, notify);
        //final ContentValues values = new ContentValues();
        final ContentResolver cr = context.getContentResolver();

        //item.onAddToDatabase(values);
        
        sWorker.post(new Runnable() {
            public void run() {
                cr.update(uri, values, null, null);
            }
        });
    }
    
	public static void deleteItemFromDatabase(Context context, int id, boolean notify) {
        final ContentResolver cr = context.getContentResolver();
        final Uri uriToDelete = ThemesSettings.getContentUri(id, notify);
        sWorker.post(new Runnable() {
            public void run() {
                cr.delete(uriToDelete, null, null);
            }
        });
    }
	
	@Override
    public void onReceive(Context context, Intent intent) {
		Util.Log.d(TAG, "onReceive intent=" + intent);

        final String action = intent.getAction();
	}
	
	public void initialize(Callbacks callbacks) {
        synchronized (mLock) {
            mCallbacks = new WeakReference<Callbacks>(callbacks);
        }
    }
	
	public void startLoader(Context context, boolean isForce) {
        synchronized (mLock) {
        	Util.Log.i(TAG, "startLoader isLaunching=" + isForce + ",mCallbacks = " + mCallbacks);

            // Don't bother to start the thread if we know it's not going to do anything
            if (mCallbacks != null && mCallbacks.get() != null) {
                // If there is already one running, tell it to stop.
                // also, don't downgrade isLaunching if we're already running
            	isForce = isForce || stopLoaderLocked();
                mLoaderTask = new LoaderTask(context, isForce);
                
               	Util.Log.i(TAG, "(LauncherModel)startLoader mLoaderTask = " + mLoaderTask);

                sWorkerThread.setPriority(Thread.NORM_PRIORITY);
                sWorker.post(mLoaderTask);
            }
        }
    }

    public void stopLoader() {
        synchronized (mLock) {
            if (mLoaderTask != null) {
            	Util.Log.i(TAG, "(LauncherModel)stopLoader mLoaderTask = " + mLoaderTask);
                mLoaderTask.stopLocked();
            }
        }
    }
    
    public void forceReload() {
        stopLoaderAndSetLoadedFlags();
        if (Util.DEBUG_LOADERS) {
        	Util.Log.i(TAG, "forceReload mLoaderTask =" + mLoaderTask +",this = " + this);
        }        
        // Do this here because if the launcher activity is running it will be restarted.
        // If it's not running startLoaderFromBackground will merely tell it that it needs
        // to reload.
        startLoaderFromBackground();
    }
    
    public void stopLoaderAndSetLoadedFlags() {
        if (Util.DEBUG_LOADERS) {
        	Util.Log.d(TAG, "stopLoaderAndSetLoadedFlags: mLoaderTask =" + mLoaderTask
                    + ",this = " + this);
        }
        synchronized (mLock) {
            // Stop any existing loaders first, so they don't set mAllAppsLoaded
            // or mWorkspaceLoaded to true later.
            stopLoaderLocked();
            mIsAlreadyLoaded = false;
        }
    }

    /**
     * When the launcher is in the background, it's possible for it to miss paired
     * configuration changes.  So whenever we trigger the loader from the background
     * tell the launcher that it needs to re-run the loader when it comes back instead
     * of doing it now.
     */
    public void startLoaderFromBackground() {
        boolean runLoader = false;
        if (mCallbacks != null) {
            Callbacks callbacks = mCallbacks.get();
            if (callbacks != null) {
                // Only actually run the loader if they're not paused.
//                if (!callbacks.setLoadOnResume()) {
//                    runLoader = true;
//                }
            }
        }
        if (runLoader) {
            startLoader(mApp, false);
        }
    }
    
 // If there is already a loader task running, tell it to stop.
    // returns true if isLaunching() was true on the old task
    private boolean stopLoaderLocked() {
        boolean isLaunching = false;
        LoaderTask oldTask = mLoaderTask;        
        if (oldTask != null) {
            if (oldTask.isLaunching()) {
                isLaunching = true;
            }
            oldTask.stopLocked();
        }
        if (Util.DEBUG_LOADERS) {
        	Util.Log.i(TAG, "stopLoaderLocked mLoaderTask =" + mLoaderTask + ",isLaunching = "
                    + isLaunching + ",this = " + this);
        }
        return isLaunching;
    }
	
	private class LoaderTask implements Runnable {
		private Context mContext;
        private Thread mWaitThread;
        private boolean mIsForce;
        private boolean mStopped;
        private boolean mLoadAndBindStepFinished;

        LoaderTask(Context context, boolean isForce) {
            mContext = context;
            mIsForce = isForce;
        }

        boolean isLaunching() {
            return mIsForce;
        }
        
        private void waitForOtherThread() {
			if (mWaitThread != null) {
				boolean done = false;
				while (!done) {
					try {
						mWaitThread.join();
						done = true;
					} catch (InterruptedException ex) {
						// Ignore
					}
				}
				mWaitThread = null;
			}
		}
        
        private void waitForIdle() {
            // Wait until the either we're stopped or the other threads are done.
            // This way we don't start loading all apps until the workspace has settled
            // down.
            synchronized (LoaderTask.this) {
                //final long workspaceWaitTime = DEBUG_LOADERS ? SystemClock.uptimeMillis() : 0;

                mHandler.postIdle(new Runnable() {
                    public void run() {
                        synchronized (LoaderTask.this) {
                            mLoadAndBindStepFinished = true;
//                                if (DEBUG_LOADERS) {
//                                	Log.d(TAG, "done with previous binding step");
//                                }
                            LoaderTask.this.notify();
                        }
                    }
                });

                while (!mStopped && !mLoadAndBindStepFinished) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        // Ignore
                    }
                }
            }
        }
        
		public void run() {
			
			if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask(0)=======mIsAlreadyLoaded:"+mIsAlreadyLoaded);
			
			waitForOtherThread();
			if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask(1)=======mIsAlreadyLoaded:"+mIsAlreadyLoaded);
			
			final Callbacks cbk = mCallbacks.get();
            final boolean bindLocalFirst = cbk != null ? (cbk.isLocalPageVisible()) : true;
            
            ArrayList<ThemeStyleBaseInfo> sLocalItems = new ArrayList<ThemeStyleBaseInfo>();
            ArrayList<ThemeStyleBaseInfo> sOnlineItems = new ArrayList<ThemeStyleBaseInfo>();
            
			keep_running: {
	            // Elevate priority when Home launches for the first time to avoid
	            // starving at boot time. Staring at a blank home is not cool.
	            synchronized (mLock) {
	                if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "Setting thread priority to " + (mIsForce ? "DEFAULT" : "BACKGROUND"));
	                android.os.Process.setThreadPriority(mIsForce
	                        ? android.os.Process.THREAD_PRIORITY_DEFAULT : android.os.Process.THREAD_PRIORITY_BACKGROUND);
	            }
	            //first load workspace and all apps
	            if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask()=First: step1: loading data, bindLocalFirst = " + bindLocalFirst);
	            onlyLoadAll();
	            
	            if (mStopped) {
	                break keep_running;
	            }
	            
	            if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask()=First: step2: filter data, mIsAlreadyLoaded="+mIsAlreadyLoaded+"==mStopped:"+mStopped);
	            for(ThemeStyleBaseInfo item : sAllItems){
	            	if(item.isLocalStyle())
	            		sLocalItems.add(item);
	            	else
	            		sOnlineItems.add(item);
	            }
	
	            if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask()=Second step:3 bind , bindLocalFirst = " + bindLocalFirst);
	            if(bindLocalFirst)
	            	onlyBind(sLocalItems, ThemeStyleBaseInfo.THEME_PATH_LOCAL);
	            else
	            	onlyBind(sOnlineItems, ThemeStyleBaseInfo.THEME_PATH_ONLINE);
	            
	            // Whew! Hard work done.  Slow us down, and wait until the UI thread has
	            // settled down.
	            synchronized (mLock) {
	                if (mIsForce) {
	                    if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "Setting thread priority to BACKGROUND");
	                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
	                }
	            }
	            if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask()=Second step:4 bind , bindLocalFirst = " + bindLocalFirst+"==mLoadAndBindStepFinished:"+mLoadAndBindStepFinished);
	            
	            waitForIdle();
	            
	            if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask()=Second step:5 bind , bindLocalFirst = " + bindLocalFirst+"==mLoadAndBindStepFinished:"+mLoadAndBindStepFinished);
	            if(!bindLocalFirst)
	            	onlyBind(sLocalItems, ThemeStyleBaseInfo.THEME_PATH_LOCAL);
	            else
	            	onlyBind(sOnlineItems, ThemeStyleBaseInfo.THEME_PATH_ONLINE);

	            // Restore the default thread priority after we are done loading items
	            synchronized (mLock) {
	                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
	            }
	        }
	
	
	        // Update the saved icons if necessary
	        if (Util.DEBUG_LOADERS) Util.Log.d(TAG, "LoaderTask()==end===mLoadAndBindStepFinished:"+mLoadAndBindStepFinished);
	
	        // Clear out this reference, otherwise we end up holding it until all of the
	        // callback runnables are done.
	        mContext = null;
	
	        synchronized (mLock) {
	            // If we are still the last one to be scheduled, remove ourselves.
	            if (mLoaderTask == this) {
	                mLoaderTask = null;
	            }
	        }
		}
		
		private void loadAndBind() {
            // Load the workspace
            if (Util.DEBUG_LOADERS) {
            	Util.Log.d(TAG, "loadAndBindWorkspace mWorkspaceLoaded=" + mIsAlreadyLoaded);
            }

            if (!mIsAlreadyLoaded) {
            	onlyLoadAll();
                synchronized (LoaderTask.this) {
                    if (mStopped) {
                        return;
                    }
                    mIsAlreadyLoaded = true;
                }
            }

            onlyBind(sAllItems, ThemeStyleBaseInfo.THEME_PATH_UNKNOWN);
        }
		
		private void onlyLoadAll(){
			if (Util.DEBUG_LOADERS) {
				Util.Log.d(TAG, "(onlyLoadWorkspace mWorkspaceLoaded = " + mIsAlreadyLoaded + ", this = " + this);
        	}
        	if (!mIsAlreadyLoaded) {
        		LoadAllData();
                synchronized (LoaderTask.this) {
                    if (mStopped) {
                        return;
                    }
                    mIsAlreadyLoaded = true;
                }
            }
		}
		
		private void onlyBind(final ArrayList<ThemeStyleBaseInfo> list, final int type){
			final Callbacks oldCallbacks = mCallbacks.get();
            if (oldCallbacks == null) {
                // This launcher has exited and nobody bothered to tell us.  Just bail.
            	Util.Log.w(TAG, "LoaderTask running with no launcher (type:"+type+")");
                return;
            }
            
            if (Util.DEBUG_LOADERS) {
            	Util.Log.d(TAG, "onlyBind oldCallbacks =" + oldCallbacks + ",type = " + type);
            }
            
            mHandler.post(new Runnable() {
                public void run() {
                    final long t = SystemClock.uptimeMillis();
                    final Callbacks callbacks = tryGetCallbacks(oldCallbacks);
                    if (callbacks != null) {
                        callbacks.bindAllItems(list, type);
                    }
                    if (Util.DEBUG_LOADERS) {
                    	Util.Log.d(TAG, "bound all " + list.size() + " apps from cache in "
                                + (SystemClock.uptimeMillis()-t) + "ms, this = " + this);
                    }
                }
            });
		}
		
		private void LoadAllData(){
			final Context context = mContext;
            final ContentResolver contentResolver = context.getContentResolver();
            sAllItems.clear();
            
            final Cursor c = contentResolver.query(ThemesSettings.CONTENT_URI,
                    null, null, null, null);
            
            try {
                final int idIndex = c.getColumnIndexOrThrow(ThemesSettings._ID);
                final int titleIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_TITLE);
                final int iconIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_PREVIEW_ICON);
                final int serverIdIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_SERVER_ID);
                final int pathtypeIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_PATH_TYPE);
                final int versionIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_VERSION);
                final int jzsIdIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_JZS_ID);
                final int jzcIdIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_JZC_ID);
                
                final int iconUrlIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_PREVIEW_ICON_URL);
                final int lastUpdateDateIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_LAST_UPDATE_DATE);
                //final int dlCompleteIdIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_DOWNLOAD_COMPLETED);
                final int extOptionIndex = c.getColumnIndexOrThrow(ThemesSettings.THEME_EXT_OPTIONS);

                while (!mStopped && c.moveToNext()) {
                	try {
                		
                		int id = c.getInt(idIndex);
                		int itemPathType = c.getInt(pathtypeIndex);
                        String title = c.getString(titleIndex);
                        Bitmap icon = getIconFromCursor(c, iconIndex, context);

                        if(itemPathType == ThemeStyleBaseInfo.THEME_PATH_LOCAL){
                        	int sid = c.getInt(jzsIdIndex);
                    		int cid = c.getInt(jzcIdIndex);
                        	sAllItems.add(new ThemeStyleLocalInfo(id, sid, cid, title, icon));
                        } else {
                        	String version = c.getString(versionIndex);
                        	String serid = c.getString(serverIdIndex);
                        	int opt = c.getInt(extOptionIndex);
                        	int date = c.getInt(lastUpdateDateIndex);
                        	String imgUrl = c.getString(iconUrlIndex);
                        	sAllItems.add(new ThemeStyleOnlineInfo(serid, version, opt
                        			, date, title, icon, id, imgUrl));
                        }
                        
                	} catch (Exception e) {
                		Util.Log.w(TAG, "Desktop items loading interrupted:"+e.getMessage());
                    }
                }
                
            } finally {
                c.close();
            }
		}
		
		public void stopLocked() {
            synchronized (LoaderTask.this) {
                mStopped = true;
                this.notify();
            }
            if (Util.DEBUG_LOADERS) {
            	Util.Log.i(TAG, "stopLocked completed, this = " + LoaderTask.this 
                        + ",mLoaderTask = " + mLoaderTask);
            }
        }

        /**
         * Gets the callbacks object.  If we've been stopped, or if the launcher object
         * has somehow been garbage collected, return null instead.  Pass in the Callbacks
         * object that was around when the deferred message was scheduled, and if there's
         * a new Callbacks object around then also return null.  This will save us from
         * calling onto it with data that will be ignored.
         */
        Callbacks tryGetCallbacks(Callbacks oldCallbacks) {
            synchronized (mLock) {
                if (mStopped) {
                    return null;
                }

                if (mCallbacks == null) {
                    return null;
                }

                final Callbacks callbacks = mCallbacks.get();
                if (callbacks != oldCallbacks) {
                    return null;
                }
                if (callbacks == null) {
                	Util.Log.w(TAG, "no mCallbacks");
                    return null;
                }

                return callbacks;
            }
        }
	}
	
	Bitmap getIconFromCursor(Cursor c, int iconIndex, Context context) {

        byte[] data = c.getBlob(iconIndex);
        try {
        	if(data != null && data.length > 10)
        		return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            
        }
        
        return null;
    }
}
