package com.jzs.android.systemstyle.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Window;
//import android.util.Log;

import android.os.storage.StorageVolume;
import android.os.SystemProperties;

public final class Util {
	public final static boolean DEBUG = true;
	public final static boolean DEBUG_LOADERS = (DEBUG && true);
	public final static boolean DEBUG_SAVESTYLE = (DEBUG && true);
	public static final String TAG = "test";
	
	public final static boolean SUPPORT_ONLINE_STYLE = false;
	
    private static final int BUFF_SIZE = 1024;
    private static final String PROC_VERSION_PATH = "/proc/version";
    private static final String NETTYPE_WIFI = "WIFI";
    private static final int FILE_READER_SIZES = 256;
    private static final int PROC_VERSION_GROUP_LENGTH = 4;
    static final long M_SIZE = 1024 * 1024;
    static final long K_SIZE = 1024;
    private static String sAvailablePath;

    public static enum UPDATE_TYPES {
        OTA_UPDATE_FULL, OTA_UPDATE_PART,
    }

    public static final UPDATE_TYPES UPDATE_OPTION = UPDATE_TYPES.OTA_UPDATE_FULL;

    public enum SDCARD_STATUS {
        STATE_OK, STATE_LOST, STATE_UNMOUNT, STATE_INSUFFICIENT
    }

    public enum NETWORK_STATUS {
        STATE_WIFI, STATE_GPRS, STATE_NONE_NETWORK
    }



    public static final int MAX_PERCENT = 100;
    public static final long REFRESHTIME = 3 * 3600 * 1000;
    public static final double DECOMPRESS_RATIO = 1.5;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String ERROR_VERSION = "Unavailable";
    private static final String EXTERNAL_USB_STORAGE = "usbotg";

    private static String sExternalSdCardPath = null;

    private Util() {
    }

    public static class OTAresult {
        static final int ERROR_RUN = -1;
        static final int CHECK_OK = 0;
        static final int OTA_FILE_UNZIP_OK = 30;
        static final int ERROR_INVALID_ARGS = 1;
        static final int ERROR_OTA_FILE = 2;
        static final int ERROR_FILE_OPEN = 3;
        static final int ERROR_FILE_WRITE = 4;
        static final int ERROR_OUT_OF_MEMORY = 5;
        static final int ERROR_PARTITION_SETTING = 6;
        static final int ERROR_ONLY_FULL_CHANGE_SIZE = 7;
        static final int ERROR_ACCESS_SD = 8;
        static final int ERROR_ACCESS_USERDATA = 9;
        static final int ERROR_SD_FREE_SPACE = 10;
        static final int ERROR_SD_WRITE_PROTECTED = 11;
        static final int ERROR_USERDATA_FREE_SPACE = 12;
        static final int ERROR_MATCH_DEVICE = 13;
        static final int ERROR_DIFFERENTIAL_VERSION = 14;
        static final int ERROR_BUILD_PROP = 15;
    }

    public static class SDresult {
        public static final int UNPACK_OK = -1;
        public static final int VERIFY_OK = 0;
        public static final int ERROR_OUT_OF_MEMORY = 1;
        public static final int ERROR_INVALID_ZIP = 2;
        public static final int ERROR_CHECKSUM_ERR = 3;
        public static final int ERROR_VERSION_ERR = 4;
        public static final int ERROR_NO_SDCARD = 5;
    }

    public static class Action {
        public static final String ACTION_BOOT_COMPLETED = Intent.ACTION_BOOT_COMPLETED;
        public static final String ACTION_AUTO_QUERY_NEWVERSION = "com.jzs.systemupdate.action.AUTO_QUERY_NEWVERSION";
        public static final String ACTION_AUTO_DOWNLOAD = "com.jzs.systemupdate.action.ACTION_AUTO_DOWNLOAD";
        public static final String ACTION_UPDATE_REMIND = "com.jzs.systemupdate.action.UPDATE_REMIND";
        public static final String ACTION_UPDATE_REPORT = "com.jzs.systemupdate.action.UPDATE_REPORT";
        public static final String ACTION_REPORT_ACTIVITY = "com.jzs.systemupdate.action.UpdateReport";
        public static final String ACTION_REFRESH_TIME_OUT = "com.jzs.systemupdate.action.REFRESH_TIME_OUT";
        public static final String ACTION_AUTO_DL_TIME_OUT = "com.jzs.systemupdate.action.AUTO_DL_TIME_OUT";
        public static final String ACTION_UPDATE_TYPE_OPTION = "com.jzs.systemupdate.action.UPDATE_TYPE_OPTION";
        public static final String ACTION_MEDIA_MOUNT_UPDATEUI = "com.jzs.systemupdate.action.ACTION_MEDIA_MOUNTED";
        public static final String ACTION_MEDIA_UNMOUNT_UPDATEUI = "com.jzs.systemupdate.action.ACTION_MEDIA_UNMOUNTED";
        public static final String ACTION_MEDIA_UNMOUNT = Intent.ACTION_MEDIA_UNMOUNTED;
        public static final String ACTION_MEDIA_BAD_REMOVAL = Intent.ACTION_MEDIA_BAD_REMOVAL;
        public static final String ACTION_MEDIA_NOFS = Intent.ACTION_MEDIA_NOFS;
        public static final String ACTION_MEDIA_MOUNTED = Intent.ACTION_MEDIA_MOUNTED;
        public static final String ACTION_OTA_MANAGER = "com.jzs.systemupdate.action.OtaPkgClient";
        public static final String ACTION_INSTALL_REMINDER = "com.jzs.systemupdate.action.InstallReminder";
        public static final String ACTION_SYSTEM_UPDATE_ENTRY = "com.jzs.intent.action.System_Update_Entry";
        public static final String ACTION_SHUTDOWN = Intent.ACTION_SHUTDOWN;
        
        public static final String ACTION_PLUGIN_SAVESTYLESERVICE = "com.jzs.android.intent.action.SaveStyleServices";
        
        public static final String ACTION_INITTYLESERVICE = "com.jzs.android.intent.action.InitStyleInfoService";
    }
    
    public static class MessageWhat{
    	public static final int MSG_PLUGIN_SAVESTYLE = 100;
    	public static final int MSG_PLUGIN_SAVESTYLE_RESPONSE = 101;
    	public static final int MSG_PLUGIN_SAVESTYLE_BEGIN = 102;
    	public static final int MSG_PLUGIN_SAVESTYLE_END = 102;
    }
    
    public static class MessageArg{
    	public static final int MSG_ARG_SUCCESS = 0;
    	public static final int MSG_ARG_FAIL = -100;
    	
    }

    public static class PathName {
        public static final String DATA_PATH = "data/data/com.jzs.android.systemstyle/";
        public static final String INTERNAL_ADDRESS_FILE = DATA_PATH + "system_update/address.xml";
        public static final String PKG_INFO_IN_DATA = DATA_PATH + "pkgInfos.xml";
        public static final String UPDATE_TYPE_IN_DATA = DATA_PATH + "type.txt";
        public static final String OTA_PKG_FOLDER = "/googleota";
        public static final String TEMP_DIR = "/temp";
        public static final String PACKAGE_NAME = "/update.zip";
        public static final String SD_INSTALL_PACKAGE = "install.zip";
        public static final String TEMP_PKG_NAME = "/package.zip";
        public static final String MD5_FILE_NAME = "/md5sum";
        public static final String ENTRY_TYPE = "type.txt";
        public static final String ENTRY_SCATTER = "scatter.txt";
        public static final String ENTRY_CONFIGURE = "configure.xml";
        
        public static final String ROOT_DATA_DIRECTORY_NAME = ".lingyin";
        public static final String THUMB_DETAIL_IMG_DIRNAME = "thumbdetail";
    }

    public static class InfoXmlTags {
        public static final String XML_TAG_PRODUCT = "product";
        public static final String XML_TAG_FLAVOR = "flavor";
        public static final String XML_TAG_LANGUAGE = "language";
        public static final String XML_TAG_OEM = "oem";
        public static final String XML_TAG_OPERATOR = "operator";
        public static final String XML_TAG_ANDROID_NUM = "androidnumber";
        public static final String XML_TAG_VERSION_NAME = "versionname";
        public static final String XML_TAG_FINGERPRINT = "fingerprint";
        public static final String XML_TAG_PUBLISH_TIME = "publishtime";
        public static final String XML_TAG_NOTES = "notes";
        public static final String XML_TAG_PATH = "path";
    }
    
    public static File getCacheVersionThumbDetailPath(Context context, String serverid){
    	String ext;
    	if(TextUtils.isEmpty(serverid))
    		ext = String.format("%s", PathName.THUMB_DETAIL_IMG_DIRNAME);
    	else
    		ext = String.format("%s/%s", PathName.THUMB_DETAIL_IMG_DIRNAME, serverid);
    	return new File(getCacheDataRootDirectory(context), ext);
    }
    
    public static File getCacheDataRootDirectory(Context context){
    	File file = new File(Environment.getExternalStorageDirectory(), PathName.ROOT_DATA_DIRECTORY_NAME);
    	if(!file.exists()){
    		if(file.mkdirs()){
    			try{
	    			File nomediafile = new File(file, ".nomedia");
	    			if(!nomediafile.exists())
	    				nomediafile.createNewFile();
    			} catch (IOException e){
    				
    			}
    		}
    	}
    	return file;
    }

    public static boolean isSdcardAvailable(Context context) {

        //if (FeatureOption.MTK_2SDCARD_SWAP || FeatureOption.MTK_SHARED_SDCARD) {

            sExternalSdCardPath = getExternalSDCardPath(context);

            Log.i(TAG, "sExternalSdCardPath = " + sExternalSdCardPath);

            if (sExternalSdCardPath != null) {
                StorageManager storManager = (StorageManager) context
                        .getSystemService(Context.STORAGE_SERVICE);

                if (storManager == null) {
                    return false;
                }

                return Environment.MEDIA_MOUNTED.equals(storManager.getVolumeState(sExternalSdCardPath));

            }

        //}

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    }

    public static String getExternalSDCardPath(Context context) {

        StorageManager storManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);

        if (storManager == null) {
            return null;
        }

        StorageVolume[] volumes = storManager.getVolumeList();

        if (volumes == null) {
            return null;
        }

        for (int i = 0; i < volumes.length; i++) {
            if ((volumes[i] != null) && (volumes[i].isRemovable())) {

                String path = volumes[i].getPath();
                if ((path != null) && (!path.contains(EXTERNAL_USB_STORAGE))) {
                    return path;
                }
            }
        }

        return null;

    }

    /**
     * Check if the SD card is available and has enough space.
     * 
     * @param context
     *            environment context
     * @param miniSize
     *            the minimized size needed for checking, unit is byte.
     * 
     * @return the SD card status
     * @see SDCARD_STATUS
     * 
     */

    public static SDCARD_STATUS checkSdcardState(Context context, long miniSize) {

        if (!isSdcardAvailable(context)) {
            return SDCARD_STATUS.STATE_LOST;
        }

        long insufficientSpace = getExtraSpaceNeeded(context, miniSize);

        if (insufficientSpace == -1) {
            Log.e(TAG, "checkSdcardIsAvailable false, card mount error");
            return SDCARD_STATUS.STATE_UNMOUNT;
        } else if (insufficientSpace > 0) {
            return SDCARD_STATUS.STATE_INSUFFICIENT;
        }

        return SDCARD_STATUS.STATE_OK;
    }

    /**
     * Calculate how much space is needed to be freed for the upgrade package.
     * 
     * @param context
     *            environment context
     * @param miniSize
     *            the minimized size needed for checking with unit byte.-1 means error occurs. 0
     *            means the space is enough
     * 
     * @return the space needed to be freed to hold the file of the size of "miniSize"
     * 
     */
    public static long getExtraSpaceNeeded(Context context, long miniSize) {
        Log.i(TAG, "checkSdcardSpaceNeeded miniSize = " + miniSize);

        String availablePath = getAvailablePath(context);
        if (availablePath == null) {
            return -1;
        }
        StatFs statfs = new StatFs(availablePath);
        if (statfs == null) {
            return -1;
        }

        long totalSize = (long) statfs.getBlockSize() * statfs.getAvailableBlocks();
        Log.i(TAG, "checkSdcardSpaceNeeded, totalSize = " + totalSize);
        if (totalSize < miniSize) {
            return miniSize - totalSize;
        }

        return 0;
    }

    public static String getAvailablePath(Context context) {
        if (sAvailablePath == null) {
        	if (sExternalSdCardPath == null) {
                getExternalSDCardPath(context);
            }
            
            if (sExternalSdCardPath != null) {
            	sAvailablePath = sExternalSdCardPath;
            } else {
                File sdcardSystem = Environment.getExternalStorageDirectory();
                sAvailablePath = sdcardSystem.getPath();
            }
        }
        return sAvailablePath;
    }

    public static long getFileSize(String path) {
    	
        if (path == null) {
  	
        	return 0;
        }

        File file = new File(path);

        if (file.isFile() && file.exists()) {
            return file.length();
        }

        return 0;

    }

    public static void deleteFile(String path) {
        Log.e(TAG, "[deleteFile], path is " + path);
        if (path == null) {
            Log.w(TAG, "path is null, return");
            return;
        }

        File file = new File(path);

        if (file.isFile()) {

            if (file.exists()) {
                boolean result = file.delete();
                Log.i(TAG, "deleteFile result is:" + result);
            } else {
                Log.e(TAG, path + "does not exist");
            }
            return;

        }
        if (file.isDirectory()) {

            String[] strFileList = file.list();

            if (strFileList == null) {
                return;
            }

            String folderPath = path.endsWith(File.separator) ? path : path + File.separator;
            for (String strName : strFileList) {

                deleteFile(folderPath + strName);

            }
        }

    }

    public static boolean isNetWorkAvailable(Context context){
    	return isNetWorkAvailable(context, null);
    }
    
    public static boolean isNetWorkAvailable(Context context, String typeName) {

        Log.i(TAG, "isNetWorkAvailable: context = " + context + "typeName = " + typeName);

        boolean ret = false;

        ConnectivityManager connetManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connetManager == null) {
            Log.e(TAG, "isNetWorkAvailable connetManager = null");
            return ret;
        }
        NetworkInfo[] infos = connetManager.getAllNetworkInfo();
        if (infos == null) {
            return ret;
        }
        if ((typeName == null) || (typeName.length() <= 0)) {
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if (infos[i].isConnected() && infos[i].isAvailable()) {
                    ret = true;
                    break;
                }
            }
        } else {
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if (infos[i].getTypeName().equalsIgnoreCase(typeName) && infos[i].isConnected()
                        && infos[i].isAvailable()) {
                    Log.i(TAG, "isNetWorkAvailable name is : " + infos[i].getTypeName());
                    ret = true;
                    break;
                }
            }
        }

        Log.i(TAG, "isNetWorkAvailable result is : " + ret);
        return ret;
    }

    public static NETWORK_STATUS getNetworkType(Context context) {
        NETWORK_STATUS ret = NETWORK_STATUS.STATE_NONE_NETWORK;

        ConnectivityManager connetManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connetManager == null) {
            Log.e(TAG, "isNetWorkAvailable connetManager = null");
            return ret;
        }
        NetworkInfo[] infos = connetManager.getAllNetworkInfo();
        if (infos == null) {
            return ret;
        }
        for (int i = 0; i < infos.length && infos[i] != null; i++) {
            if (infos[i].isConnected() && infos[i].isAvailable()) {

                if (infos[i].getTypeName().equalsIgnoreCase(NETTYPE_WIFI)) {
                    ret = NETWORK_STATUS.STATE_WIFI;
                } else {
                    ret = NETWORK_STATUS.STATE_GPRS;
                }

                break;
            }
        }

        Log.i(TAG, "get network stype is : " + ret);
        return ret;

    }

    public static class DeviceInfo {
        String mImei;
        String mSim;
        String mSnNumber;
        String mOperator;
    }

    public static DeviceInfo getDeviceInfo(Context context) {

        if (context == null) {
            return null;
        }
        TelephonyManager teleMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (teleMgr != null) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.mImei = teleMgr.getDeviceId();
            deviceInfo.mSim = teleMgr.getLine1Number();
            deviceInfo.mSnNumber = teleMgr.getSimSerialNumber();
            deviceInfo.mOperator = teleMgr.getNetworkOperator();

            return deviceInfo;
        } else {
            return null;
        }

    }
    
    public static String getDeviceVersionInfo(Context context) {

        StringBuilder builder = new StringBuilder();
        String oem = SystemProperties.get("ro.product.manufacturer");
        String product = SystemProperties.get("ro.product.device");
        String lang = SystemProperties.get("ro.product.locale.language");
        String buildnumber = SystemProperties.get("ro.build.display.id");
        String oper = SystemProperties.get("ro.operator.optr");
        String flavor = SystemProperties.get("ro.build.flavor");

        if (oem == null) {
            oem = "null";
        }
        if (product == null) {
            product = "null";
        }
        if (lang == null) {
            lang = "null";
        }
        if (buildnumber == null) {
            buildnumber = "null";
        }
        if (oper == null) {
            oper = "null";
        }

        oem = oem.replaceAll("_", "\\$");
        product = product.replaceAll("_", "\\$");

        builder.append(oem).append("_").append(product);
        if (!TextUtils.isEmpty(flavor)) {
            builder.append("[").append(flavor).append("]");
        }
        if (isGmsLoad(context)) {
            builder.append("[gms]");
        }

        lang = lang.replaceAll("_", "\\$");
        buildnumber = buildnumber.replaceAll("_", "\\$");
        oper = oper.replaceAll("_", "\\$");

        builder.append("_").append(lang).append("_").append(buildnumber).append("_").append(oper);

        String versionInfo = builder.toString();
        Log.i(TAG, "getDeviceVersionInfo = " + versionInfo);

        return versionInfo;
    }

    private static boolean isGmsLoad(Context context) {

        String gmsFlagPath = "";//context.getResources().getString(R.string.gms_load_flag_jzslib);

        File file = new File(gmsFlagPath);

        return ((file != null) && file.exists());

    }

    public static boolean checkIfTopActivity(Context context, String classname) {

        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);  
        ComponentName componentInfo = taskInfo.get(0).topActivity; 
                
        if(componentInfo != null) {
        	
            String strClass = componentInfo.getClassName();
            Log.i(TAG, "strClass = " + strClass);           
            
        	return classname.equals(strClass);
        } else {
        	return false;
        }
    }

    public static void setAlarm(Context context, int alarmType, long time, String action) {
        Log.i(TAG, "setAlarm enter, time = " + time + ", current time = "
                + Calendar.getInstance().getTimeInMillis());
        Intent it = new Intent(action);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, it,
                PendingIntent.FLAG_CANCEL_CURRENT);

        if ((alarmMgr != null) && (operation != null)) {
            alarmMgr.cancel(operation);
            alarmMgr.set(alarmType, time, operation);
        }

    }

    public static void cancelAlarm(Context context, String action) {
        Log.i(TAG, "cancelAlarm enter, action = " + action + ", current time = "
                + Calendar.getInstance().getTimeInMillis());
        Intent it = new Intent(action);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, it,
                PendingIntent.FLAG_CANCEL_CURRENT);

        if ((alarmMgr != null) && (operation != null)) {
            alarmMgr.cancel(operation);
        }

    }

    public static String getPackagePathName(Context context) {

        String availablePath = getAvailablePath(context);
        if (availablePath != null) {
            String packagePath = availablePath + PathName.OTA_PKG_FOLDER;
            File dir = new File(packagePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Log.i(TAG, "packagePath = " + packagePath);
            return packagePath;
        } else {
            Log.e(TAG, "packagePath == null");
            return null;
        }
    }

    public static String getTempPath(Context context) {

        String packagePath = getPackagePathName(context);
        if (packagePath != null) {
            String tempPath = packagePath + Util.PathName.TEMP_DIR;
            File dir = new File(tempPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Log.i(TAG, "getTempPath = " + tempPath);
            return tempPath;
        } else {
            return null;
        }
    }

    public static String getPackageFileName(Context context) {
        String packagePath = getPackagePathName(context);
        if (packagePath != null) {
            String path = packagePath + PathName.PACKAGE_NAME;
            Log.i(TAG, "getPackageFileName = " + path);
            return path;
        } else {
            return null;
        }
    }

    public static int unzipFileElement(ZipFile zipFile, String strSrcFile, String strDesFile) {

        Log.i(TAG, "unzipFileElement:" + strSrcFile + ";" + strDesFile);
        ZipEntry zipEntry = zipFile.getEntry(strSrcFile);

        if (zipEntry == null) {
            return OTAresult.ERROR_OTA_FILE;
        }

        try {
            InputStream in = zipFile.getInputStream(zipEntry);

            if (in == null) {
                return OTAresult.ERROR_FILE_OPEN;
            }

            File desFile = new File(strDesFile);
            if (desFile == null) {
                return OTAresult.ERROR_FILE_OPEN;
            }
            if (desFile.exists()) {
                desFile.delete();
            }
            desFile.createNewFile();
            OutputStream out = new FileOutputStream(desFile);
            if (out == null) {
                return OTAresult.ERROR_FILE_OPEN;

            }
            byte[] buffer = new byte[BUFF_SIZE];
            int realLength = 0;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            out.close();
            out = null;
            in.close();
            in = null;
        } catch (IOException e) {
            e.printStackTrace();
            return OTAresult.ERROR_FILE_WRITE;
        }

        return OTAresult.OTA_FILE_UNZIP_OK;
    }

    public static String getFormattedKernelVersion() {
        String procVersionStr;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(PROC_VERSION_PATH),
                    FILE_READER_SIZES);

            try {
                procVersionStr = reader.readLine();
            } finally {
                reader.close();
            }

            if (procVersionStr == null) {
                return ERROR_VERSION;
            }
            // M: For match proc version pattern
            procVersionStr = procVersionStr.replace(" (prerelease)", "").replace(" SMP", "");
            // M: end
            final String procVersionRegex = "\\w+\\s+" + /* ignore: Linux */
            "\\w+\\s+" + /* ignore: version */
            "([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
            "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
            "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
            "([^\\s]+)\\s+" + /* group 3: #26 */
            "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
            "(.+)"; /* group 4: date */

            Pattern p = Pattern.compile(procVersionRegex);
            if (p == null) {
                return ERROR_VERSION;
            }
            Matcher m = p.matcher(procVersionStr);
            if (m == null) {
                return ERROR_VERSION;
            }
            if (!m.matches()) {
                Log.e(TAG, "Regex did not match on /proc/version: " + procVersionStr);
                return ERROR_VERSION;
            } else if (m.groupCount() < PROC_VERSION_GROUP_LENGTH) {
                Log.e(TAG, "Regex match on /proc/version only returned " + m.groupCount()
                        + " groups");
                return ERROR_VERSION;
            } else {
                StringBuilder buildVersion = new StringBuilder(m.group(PROC_VERSION_GROUP_LENGTH));
                return buildVersion.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "FileNotFoundException when getting kernel version for Device Info screen");
            return ERROR_VERSION;
        } catch (IOException e) {
            Log.e(TAG, "IO Exception when getting kernel version for Device Info screen", e);
            return ERROR_VERSION;
        }
    }
    
//    public static byte[] downloadImages(final String imageUrl) {
//        // If there is no avatar, we're done
//        if (TextUtils.isEmpty(imageUrl)) {
//            return null;
//        }
//
//        try {
//            Log.i(TAG, "Downloading avatar: " + imageUrl);
//            // Request the avatar image from the server, and create a bitmap
//            // object from the stream we get back.
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            try {
//                final BitmapFactory.Options options = new BitmapFactory.Options();
//                final Bitmap avatar = BitmapFactory.decodeStream(connection.getInputStream(),
//                        null, options);
//
//                // Take the image we received from the server, whatever format it
//                // happens to be in, and convert it to a JPEG image. Note: we're
//                // not resizing the avatar - we assume that the image we get from
//                // the server is a reasonable size...
//                Log.i(TAG, "Converting avatar to JPEG");
//                ByteArrayOutputStream convertStream = new ByteArrayOutputStream(
//                        avatar.getWidth() * avatar.getHeight() * 4);
//                avatar.compress(Bitmap.CompressFormat.JPEG, 95, convertStream);
//                convertStream.flush();
//                convertStream.close();
//                // On pre-Honeycomb systems, it's important to call recycle on bitmaps
//                avatar.recycle();
//                return convertStream.toByteArray();
//            } finally {
//                connection.disconnect();
//            }
//        } catch (java.net.MalformedURLException muex) {
//            // A bad URL - nothing we can really do about it here...
//            Log.e(TAG, "Malformed avatar URL: " + imageUrl);
//        } catch (IOException ioex) {
//            // If we're unable to download the avatar, it's a bummer but not the
//            // end of the world. We'll try to get it next time we sync.
//            Log.e(TAG, "Failed to download user avatar: " + imageUrl);
//        }
//        return null;
//    }
    
    public static Bitmap downloadImages(final String imageUrl) {
        // If there is no avatar, we're done
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }

        try {
            Log.i(TAG, "Downloading avatar: " + imageUrl);
            // Request the avatar image from the server, and create a bitmap
            // object from the stream we get back.
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                final Bitmap bmp = BitmapFactory.decodeStream(connection.getInputStream(),
                        null, options);

                return bmp;
                
            } finally {
                connection.disconnect();
            }
        } catch (java.net.MalformedURLException muex) {
            // A bad URL - nothing we can really do about it here...
            Log.e(TAG, "Malformed avatar URL: " + imageUrl);
        } catch (IOException ioex) {
            // If we're unable to download the avatar, it's a bummer but not the
            // end of the world. We'll try to get it next time we sync.
            Log.e(TAG, "Failed to download user avatar: " + imageUrl);
        }
        return null;
    }

    public static UPDATE_TYPES getUpdateType() {
        UPDATE_TYPES type = UPDATE_OPTION;
        Log.v(TAG, "get update Type  = " + type);
        return type;
    }
    
    public static final String UNKNOWN = "";
    public static String getPropString(String property) {
        return getPropString(property, UNKNOWN);
    }
    
    public static String getPropString(String property, String def) {
        return SystemProperties.get(property, UNKNOWN);
    }

    public static long getPropLong(String property) {
        try {
            return Long.parseLong(SystemProperties.get(property));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public static int getPropInteger(String property) {
    	return getPropInteger(property, 0);
    }
    
    public static int getPropInteger(String property, int def) {
    	return SystemProperties.getInt(property, def);
    }
    
    public static boolean getPropBoolean(String property) {
    	return Boolean.parseBoolean(SystemProperties.get(property));
    }
    
    
    public static Bitmap drawable2Bitmap(Drawable drawable){  
    	if(drawable != null){
	        if(drawable instanceof BitmapDrawable){  
	            return ((BitmapDrawable)drawable).getBitmap();  
	        } else if(drawable instanceof NinePatchDrawable){  
	            Bitmap bitmap = Bitmap  
	                    .createBitmap(  
	                            drawable.getIntrinsicWidth(),  
	                            drawable.getIntrinsicHeight(),  
	                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
	                                    : Bitmap.Config.RGB_565);  
	            Canvas canvas = new Canvas(bitmap);  
	            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
	                    drawable.getIntrinsicHeight());  
	            drawable.draw(canvas);  
	            
	            return bitmap;  
	        }
    	}
        
        return null;
    }
    
	public static Bitmap drawable2Bitmap(Drawable drawable, int width, int height){  
		if(drawable != null){
	        if(drawable instanceof BitmapDrawable){  
	            return ((BitmapDrawable)drawable).getBitmap();  
	        } else if(drawable instanceof NinePatchDrawable){  
	        	
	        	if(width <= 0)
	        		width = drawable.getIntrinsicWidth();
	        	
	        	if(height <= 0)
	        		height = drawable.getIntrinsicHeight();
	        	
	            Bitmap bitmap = Bitmap  
	                    .createBitmap(  
	                    		width,  
	                    		height,  
	                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
	                                    : Bitmap.Config.RGB_565);  
	            Canvas canvas = new Canvas(bitmap);  
	            drawable.setBounds(0, 0, width, height);  
	            drawable.draw(canvas);  
	            
	            return bitmap;  
	        }
		}
        
        return null;
    }
	
	public static Dialog showWaitingDialog(Context context, int layout){
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(layout);
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}
	
    public static final class Log{
    	
    	public static void d(String tag, String txt){
    		if(DEBUG)
    			d(tag + "::" + txt);
    	}
    	
    	public static void d(String txt){
    		if(DEBUG)
    			android.util.Log.d(TAG, txt);
    	}
    	
    	public static void i(String tag, String txt){
    		if(DEBUG)
    			i(tag + "::" + txt);
    	}
    	
    	public static void i(String txt){
    		if(DEBUG)
    			android.util.Log.i(TAG, txt);
    	}
    	
    	public static void w(String tag, String txt){
    		if(DEBUG)
    			w(tag + "::" + txt);
    	}
    	
    	public static void w(String tag, String msg, Throwable tr){
    		if(DEBUG)
    			w(tag + "::" + msg, tr);
    	}
    	
    	public static void w(String txt){
    		if(DEBUG)
    			android.util.Log.w(TAG, txt);
    	}
    	
    	public static void w(String msg, Throwable tr){
    		if(DEBUG)
    			android.util.Log.w(TAG, msg, tr);
    	}
    	
    	public static void e(String tag, String txt){
    		if(DEBUG)
    			e(tag + "::" + txt);
    	}
    	
    	public static void e(String txt){
    		if(DEBUG)
    			android.util.Log.e(TAG, txt);
    	}
    	
    	public static void e(String tag, String msg, Throwable tr){
    		if(DEBUG)
    			e(tag + "::" + msg, tr);
    	}
    	
    	public static void e(String msg, Throwable tr){
    		if(DEBUG)
    			android.util.Log.e(TAG, msg, tr);
    	}
    	
    	public static void v(String tag, String txt){
    		if(DEBUG)
    			v(tag + "::" + txt);
    	}
    	
    	public static void v(String txt){
    		if(DEBUG)
    			android.util.Log.e(TAG, txt);
    	}
    }
    
}
