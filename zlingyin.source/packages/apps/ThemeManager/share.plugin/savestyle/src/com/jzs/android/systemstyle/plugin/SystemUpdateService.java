package com.jzs.android.systemstyle.plugin;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import java.util.List;

import com.jzs.android.systemstyle.https.DownloadInfo;
import com.jzs.android.systemstyle.https.HttpManager;
import com.jzs.android.systemstyle.https.NotifyManager;
import com.jzs.android.systemstyle.https.PackageInfoReader;
import com.jzs.android.systemstyle.https.UpdatePackageInfo;
import com.jzs.android.systemstyle.utils.Util;


public class SystemUpdateService extends Service {

    private static final String TAG = "SystemUpdate/Service";

    static final int MSG_NETWORKERROR = 0;
    static final int MSG_NEWVERSIONDETECTED = 1;
    static final int MSG_RELOAD_ZIP_FILE = 2;
    static final int MSG_NONEWVERSIONDETECTED = 3;
    static final int MSG_DLPKGCOMPLETE = 4;
    static final int MSG_DLPKGUPGRADE = 5;
    static final int MSG_NOTSUPPORT = 6;
    static final int MSG_NOVERSIONINFO = 7;
    static final int MSG_DELTADELETED = 8;
    static final int MSG_SDCARDCRASHORUNMOUNT = 9;
    static final int MSG_SDCARDUNKNOWNERROR = 10;
    static final int MSG_SDCARDINSUFFICENT = 11;
    static final int MSG_SDCARDPACKAGESDETECTED = 12;
    static final int MSG_UNKNOWERROR = 13;
    static final int MSG_OTA_PACKAGEERROR = 14;
    static final int MSG_OTA_NEEDFULLPACKAGE = 15;
    static final int MSG_OTA_USERDATAERROR = 16;
    static final int MSG_OTA_USERDATAINSUFFICENT = 17;
    static final int MSG_OTA_CLOSECLIENTUI = 18;
    static final int MSG_OTA_SDCARDINFUFFICENT = 19;
    static final int MSG_OTA_SDCARDERROR = 20;
    static final int MSG_UNZIP_ERROR = 21;
    static final int MSG_CKSUM_ERROR = 22;
    static final int MSG_UNZIP_LODING = 23;
    static final int MSG_LARGEPKG = 24;

    static final int MSG_SDCARDMOUNTED = 25;
    static final int MSG_SDCARDPACKAGEINVALIDATE = 26;
    static final int MSG_DL_STARTED = 27;
    static final int MSG_FILE_NOT_EXIST = 28;
    static final int MSG_NOTIFY_POWEROFF = 29;



    private static SessionStateControlThread sQueryNewVersionThread;
    private static SessionStateControlThread sDlPkgProgressThread;

    private ServiceBinder mBinder = new ServiceBinder();

    public HttpManager mHttpManager;

    public class ServiceBinder extends Binder {
        SystemUpdateService getService() {
            return SystemUpdateService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
            mHttpManager = HttpManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        Log.i(TAG, "[onStartCommand], action = " + action);
        if (Util.Action.ACTION_AUTO_QUERY_NEWVERSION.equals(action)) {
            Log.i(TAG, "[onStartCommand], new thread to query");
            queryPackages();
        } else if (Util.Action.ACTION_AUTO_DOWNLOAD.equals(action)) {
/*            if (mHttpManager != null) {
                mHttpManager.notifyDlStarted();
            }*/
            startDlPkg();
        } else if (Util.Action.ACTION_MEDIA_MOUNTED.equals(action)) {

            DownloadInfo downloadInfo = DownloadInfo.getInstance(this.getApplicationContext());
            if (downloadInfo.getActivityID() >= 0) {
                Log.v(TAG,
                        "[onStartCommand] mounted, in the downloading/installing process, ignore");
            } else if (isQuerying()) {
                Log.v(TAG, "[onStartCommand] mounted, in the querying process");
            } else {
                Log.v(TAG, "[onStartCommand] mounted, setIfNeedRefresh true");
                downloadInfo.setIfNeedRefresh(true);

                Log.v(TAG, "[onStartCommand] mounted, send info to activities");
                Intent i = new Intent(Util.Action.ACTION_MEDIA_MOUNT_UPDATEUI);
                sendBroadcast(i);
            }
        } else {
            // ACTION_MEDIA_UNMOUNTED/ACTION_MEDIA_BAD_REMOVAL/ACTION_MEDIA_NOFS
            StorageVolume sv = (StorageVolume) intent
                    .getParcelableExtra(StorageVolume.EXTRA_STORAGE_VOLUME);
            String storagePath = sv.getPath();
            Log.v(TAG, storagePath + " crashed");

            DownloadInfo downloadInfo = DownloadInfo.getInstance(this.getApplicationContext());
            int order = downloadInfo.getActivityID();
            if (order >= 0) {
                PackageInfoReader reader = new PackageInfoReader(this,
                        Util.PathName.PKG_INFO_IN_DATA);
                UpdatePackageInfo info = reader.getInfo(order);
                String path = (info == null) ? Util.getAvailablePath(this) : info.path;
                Log.v(TAG,
                        "[onStartCommand] removed, Current downloading/installing Package path is "
                                + path);

                if (path != null && path.contains(storagePath)) {
                    Log.v(TAG,
                            "[onStartCommand] removed, downloading/installing SD card crash, requery packages");

                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle(R.string.error_sdcard)
                            .setMessage(R.string.sdcard_crash_or_unmount_when_install)
                            .setPositiveButton(android.R.string.ok, null).create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.show();

                    Log.v(TAG, "[onStartCommand] removed,reset infos");

                    downloadInfo.resetDownloadInfo();

                    Log.v(TAG, "[onStartCommand] removed, notify activities to finish self");
                    mHttpManager.clearNotification(NotifyManager.NOTIFY_DOWNLOADING);
                    mHttpManager.clearNotification(NotifyManager.NOTIFY_DL_COMPLETED);
                    mHttpManager.clearNotification(NotifyManager.NOTIFY_NEW_VERSION);
                    Intent i = new Intent(Util.Action.ACTION_MEDIA_UNMOUNT_UPDATEUI);
                    i.putExtra("storagePath", storagePath);
                    sendBroadcast(i);
                } else {
                    Log.v(TAG,
                            "[onStartCommand] removed,Not downloading/installing SD card crash, ingnore.");
                }
            } else {
                Log.v(TAG,
                        "[onStartCommand] removed,Not downloading/installing process, requery packages");

                if (isQuerying()) {
                    Toast.makeText(this, R.string.sdcard_unmount, Toast.LENGTH_LONG).show();
                } else {
                    Log.v(TAG, "[onStartCommand] removed,Package scan done, setIfNeedRefresh true");
                    downloadInfo.setIfNeedRefresh(true);

                    Log.v(TAG, "[onStartCommand] removed, notify activities to finish self");
                    Intent i = new Intent(Util.Action.ACTION_MEDIA_UNMOUNT_UPDATEUI);
                    i.putExtra("storagePath", storagePath);
                    sendBroadcast(i);
                }

            }

        }
        stopSelf();
        return START_STICKY;
    }

    boolean isQuerying() {
        if (sQueryNewVersionThread != null && sQueryNewVersionThread.isAlive()) {
            Log.i(TAG, "onQueryNewVersion back from interrupt, mQueryNewVersionThread="
                    + sQueryNewVersionThread);
            return true;
        }
        return false;
    }

    boolean isDownloading() {
        if (sDlPkgProgressThread != null && sDlPkgProgressThread.isAlive()) {
            Log.i(TAG, "sDlPkgProgressThread back from interrupt, sDlPkgProgressThread="
                    + sDlPkgProgressThread);
            return true;
        }
        
        return false;
    }
    
    public void queryPackages() {
        if (isQuerying()) {
            return;
        }
        sQueryNewVersionThread = null;
        sQueryNewVersionThread = new SessionStateControlThread(DownloadInfo.STATE_QUERYNEWVERSION);

        sQueryNewVersionThread.start();
    }

    // if query not finished yet, would not get any info
    public List<UpdatePackageInfo> loadPackages() {
        PackageInfoReader geter = new PackageInfoReader(this, Util.PathName.PKG_INFO_IN_DATA);
        return geter.getInfoList();
    }

    void cancelDlPkg() {

        stopSelf();
    }


    void startDlPkg() {
    	
    	if (isDownloading()) {
        	mHttpManager.setDownloadState();
        	return;
    	}


        sDlPkgProgressThread = new SessionStateControlThread(DownloadInfo.STATE_DOWNLOADING);

        sDlPkgProgressThread.start();
    }

    boolean checkIsDownloading() {
    	if (sDlPkgProgressThread != null && sDlPkgProgressThread.isAlive()) {
             return true;
        } else {
        	return false;
        }
    }
    void resetDescriptionInfo() {
        if (mHttpManager != null) {
            //mHttpManager.resetDescriptionInfo();
        }
    }

    void setHandler(Handler handler) {
        if (mHttpManager != null) {
            mHttpManager.setMessageHandler(handler);
        }
    }

    void resetHandler(Handler handler) {
        if (mHttpManager != null) {
            mHttpManager.resetMessageHandler(handler);
        }
    }

    class SessionStateControlThread extends Thread {

        /**
         * Constructor function.
         * 
         * @param statusType
         *            current state during the upgrade process
         * @see DownloadInfo
         */
        public SessionStateControlThread(int statusType) {
            status = statusType;
        }

        /**
         * Main executing function of this thread.
         */
        public void run() {

            Log.i(TAG, "SessionStateControlThread, status = " + status);

            switch (status) {
            case DownloadInfo.STATE_QUERYNEWVERSION:
                // Util.deleteFile(Util.PathName.PKG_INFO_IN_DATA);

                DownloadInfo.getInstance(getApplicationContext()).resetDownloadInfo();

                if (mHttpManager != null) {
                    //mHttpManager.queryNewVersion();
                }


                break;
            case DownloadInfo.STATE_DOWNLOADING:
                if (mHttpManager != null) {
                    mHttpManager.onDownloadImage();
                }
                break;
            case DownloadInfo.STATE_CANCELDOWNLOAD:
                cancelDlPkg();
                break;

            case DownloadInfo.STATE_PACKAGEERROR:
                resetDescriptionInfo();
                break;
            default:
                break;
            }

        }

        private int status;
    }
}
