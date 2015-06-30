package com.jzs.android.systemstyle.https;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.widget.Toast;



import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.jzs.android.systemstyle.utils.ThemeStyleDetailInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleOnlineInfo;
import com.jzs.android.systemstyle.utils.Util;
import com.jzs.android.systemstyle.utils.Util.DeviceInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class HttpManager {
    private static final String TAG = "HttpManager";
    private static final String FINGER_PRINT_NAME = "ro.build.fingerprint";
    private static HttpManager sHttpManager;
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat(Util.DATE_FORMAT);

    private static final String NETTYPE_WIFI = "WIFI";

    private static final String COMMAND_FILE = "/cache/recovery/command";
    private static final String COMMAND_PART2 = "COMMANDPART2";
    private static final String OTA_PATH_IN_RECOVERY = "/sdcard/googleota/update.zip";
    private static final String SYS_OPER_INTENT = "com.mediatek.intent.systemupdate.SysOperService";
    private static final String WRITE_COMMAND_INTENT = "com.mediatek.intent.systemupdate.WriteCommandService";

    private CookieStore mCookies = null;
    // to-do: handle Intent.ACTION_MEDIA_EJECT
    // private static boolean mEjectFlag = false;

    private static final long AUTO_DL_TIME = 3 * 60 * 60 * 1000;
    
    public static final int MSG_HTTP_RESPONSE_RESULT = 8059;

    public static final int HTTP_RESPONSE_SUCCESS = 0;

    public static final int HTTP_RESPONSE_AUTHEN_ERROR = -1002;
    public static final int HTTP_RESPONSE_ILLEGAL_ACCESS = -1004;
    public static final int HTTP_RESPONSE_TOKEN_REQUIRE = -1005;
    public static final int HTTP_RESPONSE_TOKEN_INVALID = -1006;
    public static final int HTTP_RESPONSE_SN_LOST = -1008;
    public static final int HTTP_RESPONSE_VERSION_REQUIRE = -1009;
    public static final int HTTP_RESPONSE_NO_NEW_VERSION = -1010;
    public static final int HTTP_RESPONSE_DATABASE_ERROR = -1103;
    public static final int HTTP_RESPONSE_PARAM_ERROR = -1104;
    public static final int HTTP_RESPONSE_VERSION_ILLEGAL = -1105;
    public static final int HTTP_RESPONSE_VERSION_DELETE = -1106;

    public static final int HTTP_RESPONSE_NETWORK_ERROR = -1201;
    public static final int HTTP_RESPONSE_REQUEST_TOO_LONG = -1202;
    public static final int HTTP_RESPONSE_DELTA_DELETE = -1900;
    public static final int HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT = -1901;
    public static final int HTTP_DETECTED_SDCARD_ERROR = -1902;
    public static final int HTTP_DETECTED_SDCARD_INSUFFICENT = -1903;
    public static final int HTTP_FILE_NOT_EXIST = -1904;
    public static final int HTTP_UNKNOWN_ERROR = -2000;
    public static final int HTTP_RESPONSE_UNZIP_ERROR = -2002;
    public static final int HTTP_RESPONSE_UNZIP_CKSUM = -2003;
    
    public static final int HTTP_RESPONSE_EMPTY_RESULT = -2100;
    public static final int HTTP_RESPONSE_NOSUPPORT_DEVICES = -2101;

    private static final int PORT_NUMBER = 443;
    private static final int TIME_OUT = 30000;
    private static final int FAULT_TOLERANT_BUFFER = 1024;
    private static final int BUFFER_SIZE = 1024;

    private static final int NETWORK_TOAST = 0;
    private boolean mIsDownloading = false;

    private Handler mHandler;
    //private Handler mNetworkHandler;
    private Context mContext = null;
    private DownloadInfo mDownloadInfo = null;
    private int mErrorCode = HTTP_RESPONSE_SUCCESS;
    //private NotifyManager mNotification;
    private HttpParams mHttpParam;
	private ClientConnectionManager mHttpConnMgr;
	
	//private DeviceVersionInfo mDeviceVersionInfo = null;
	private int mMyDevicesId = 0;
	private final static boolean ENABLE_COOKIES_AUTH = false;

	public static HttpManager getInstance(Context context) {
        Util.Log.i(TAG, "sHttpManager = " + sHttpManager);
        if (sHttpManager == null) {
            sHttpManager = new HttpManager(context);
        }
        return sHttpManager;
    }

	private HttpManager(Context context) {
        mContext = context;
        mDownloadInfo = DownloadInfo.getInstance(mContext.getApplicationContext());
        //mNotification = new NotifyManager(mContext);
        
        mMyDevicesId = mDownloadInfo.getMyDevicesId();
        if(mMyDevicesId == DownloadInfo.UNKNOWN_DEVICES_ID){
        	registerMyDevice();
        }
        //	mDeviceVersionInfo = DeviceVersionInfo.getInstance(context);
        
        initHttpParam();
        initHttpClientMgr();
    }

    public void setMessageHandler(Handler handler) {

        mHandler = handler;

        Util.Log.i(TAG, "setMessageHandler, mHandler = " + mHandler);
    }

    public void resetMessageHandler(Handler handler) {
    	Util.Log.i(TAG, "resetMessageHandler");
        if (mHandler == handler) {
            mHandler = null;
            Util.Log.i(TAG, "resetMessageHandler");
        }
    }
    
    public int registerMyDevice(){
    	if (mMyDevicesId > DownloadInfo.UNKNOWN_DEVICES_ID) {
			return mMyDevicesId;
		}
    	
    	String url = ServerAddrReader.getInstance().getLoginAddress();
    	try{
    		ArrayList<BasicNameValuePair> bnvpa = new ArrayList<BasicNameValuePair>();
    		DeviceVersionInfo info = DeviceVersionInfo.getInstance(mContext);
    		if (info != null) {
    			info.appendBasicNameValuePair(bnvpa);
 			} else {
 				mErrorCode = HTTP_RESPONSE_PARAM_ERROR;
 				return HTTP_RESPONSE_PARAM_ERROR;
 			}
    		
    		HttpResponse response = doPost(url, null, bnvpa);
			if (response == null) {
				Util.Log.i(TAG, "getSupportVersionThumbList: response = null");
				mErrorCode = HTTP_UNKNOWN_ERROR;
				return HTTP_UNKNOWN_ERROR;
			}
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				Util.Log.i(TAG,
						"registerMyDevice, ReasonPhrase = "
								+ status.getReasonPhrase());
				mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
				return HTTP_RESPONSE_AUTHEN_ERROR;
			}

			String content = getChunkedContent(response.getEntity());
			Util.Log.i(TAG, "registerMyDevice, response content = " + content);
			try {

				HttpResponseBaseContent res = new HttpResponseBaseContent(content);
				if (res.isResponseSuccess()) {

					Util.Log.i(TAG, "registerMyDevice, response mDeviceId = " + res.mDeviceId);
					// if(res.mDeviceId > DownloadInfo.UNKNOWN_DEVICES_ID)
					mMyDevicesId = res.mDeviceId;
					mDownloadInfo.setMyDevicesId(mMyDevicesId);
					return mMyDevicesId;

				} else {
					mErrorCode = res.mStatus;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
			}

			//return mErrorCode;
			
    	} catch (IOException e) {
			e.printStackTrace();
			mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
			//return HTTP_RESPONSE_AUTHEN_ERROR;
		}
    	
    	return mErrorCode;
    }
    
    public int getSupportVersionThumbList(ArrayList<ThemeStyleOnlineInfo> list){
    	return getSupportVersionThumbList(list, 0, null);
    }
    public int getSupportVersionThumbList(ArrayList<ThemeStyleOnlineInfo> list, int maxRetCount, String startServId){
//		if (!Util.isNetWorkAvailable(mContext, mDownloadInfo.getIfWifiDLOnly() ? "wifi" : null)) {
//		    return HTTP_RESPONSE_NETWORK_ERROR;
//		}
		Util.Log.i(TAG, "getSupportVersionThumbList");
		String url = ServerAddrReader.getInstance().getVersionThumbListAddress();
		
		try {
			ArrayList<BasicNameValuePair> bnvpa = new ArrayList<BasicNameValuePair>();
			if (mMyDevicesId > DownloadInfo.UNKNOWN_DEVICES_ID) {
				bnvpa.add(new BasicNameValuePair("deviceid", String
						.valueOf(mMyDevicesId)));
			} else {
				mErrorCode = HTTP_RESPONSE_PARAM_ERROR;
				return HTTP_RESPONSE_PARAM_ERROR;
			}
			
			if(!TextUtils.isEmpty(startServId)){
				bnvpa.add(new BasicNameValuePair("startid", startServId));
			}
			
			if(maxRetCount > 0){
				bnvpa.add(new BasicNameValuePair("count", String.valueOf(maxRetCount)));
			}

			HttpResponse response = doPost(url, null, bnvpa);
			if (response == null) {
				Util.Log.i(TAG, "getSupportVersionThumbList: response = null");
				mErrorCode = HTTP_UNKNOWN_ERROR;
				return HTTP_UNKNOWN_ERROR;
			}
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				Util.Log.i(TAG,
						"getSupportVersionThumbList, ReasonPhrase = "
								+ status.getReasonPhrase());
				mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
				return HTTP_RESPONSE_AUTHEN_ERROR;
			}

			String content = getChunkedContent(response.getEntity());
			Util.Log.i(TAG, "getSupportVersionThumbList, response content = " + content);
			try {

				HttpResponseStyleContent res = new HttpResponseStyleContent();
				if (res.parseResult(new JSONObject(content))) {

					return res.mTotalCount;

				} else {
					mErrorCode = res.mStatus;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
			}

			return mErrorCode;

		} catch (IOException e) {
			e.printStackTrace();
			mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
			return HTTP_RESPONSE_AUTHEN_ERROR;
		}
    }
    
    public int getStyleDetailInfo(String serId, ArrayList<ThemeStyleDetailInfo> list){
    	Util.Log.i(TAG, "getStyleDetailInfo");
		String url = ServerAddrReader.getInstance().getDetailInfoAddress();
		try {
			ArrayList<BasicNameValuePair> bnvpa = new ArrayList<BasicNameValuePair>();
			bnvpa.add(new BasicNameValuePair("id", serId));
			
			HttpResponse response = doPost(url, null, bnvpa);
			if (response == null) {
				Util.Log.i(TAG, "getSupportVersionInfo: response = null");
				mErrorCode = HTTP_UNKNOWN_ERROR;
				return HTTP_UNKNOWN_ERROR;
			}
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				Util.Log.i(TAG, "getSupportVersionInfo, ReasonPhrase = "
								+ status.getReasonPhrase());
				mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
				return HTTP_RESPONSE_AUTHEN_ERROR;
			}

			String content = getChunkedContent(response.getEntity());
			Util.Log.i(TAG, "getStyleDetailInfo, response content = " + content);
			try {
				HttpResponseStyleDetailContent res = new HttpResponseStyleDetailContent();				
				if (res.parseResult(new JSONObject(content), list)) {
					return res.mTotalCount;
				} else {
					mErrorCode = res.mStatus;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
			}
			return mErrorCode;
		} catch (IOException e) {
			e.printStackTrace();
			mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
			return HTTP_RESPONSE_AUTHEN_ERROR;
		}
    }

    private void initHttpParam() {
        mHttpParam = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(mHttpParam, TIME_OUT);
        HttpConnectionParams.setSoTimeout(mHttpParam, TIME_OUT);

    }
    
    private void initHttpClientMgr() {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));


		mHttpConnMgr = new ThreadSafeClientConnManager(mHttpParam,
				schemeRegistry);
    }


    private HttpResponse doPost(String url, Map<String, String> headers,
            ArrayList<BasicNameValuePair> bnvpa) {

    	Util.Log.i(TAG, "doPost, url = " + url + ", mCookies = " + mCookies);
        HttpContext localcontext = new BasicHttpContext();
        if (ENABLE_COOKIES_AUTH && mCookies != null) {
            localcontext.setAttribute(ClientContext.COOKIE_STORE, mCookies);
        }
        HttpResponse response = null;
        try {
            HttpHost host = null;
            HttpPost httpPost = null;

            if (url.contains("https")) {
                Uri uri = Uri.parse(url);
                host = new HttpHost(uri.getHost(), PORT_NUMBER, uri.getScheme());
                httpPost = new HttpPost(uri.getPath());
            } else {
                httpPost = new HttpPost(url);
            }

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (bnvpa != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(bnvpa));
            }
            DefaultHttpClient httpClient = new DefaultHttpClient(mHttpConnMgr, mHttpParam);

            try {
                if (url.contains("https")) {
                	Util.Log.i(TAG, "doPost, https");
                    response = httpClient.execute(host, httpPost);
                } else {
                	Util.Log.i(TAG, "doPost, http");
                	Util.Log.i(TAG, "mHttpClient =" + httpClient + "httpPost = " + httpPost
                            + "localcontext = " + localcontext);
                    response = httpClient.execute(httpPost, localcontext);
                }
                if (ENABLE_COOKIES_AUTH && mCookies == null) {
                    mCookies = httpClient.getCookieStore();
                    Util.Log.i(TAG, "mCookies size = " + mCookies.getCookies().size());
                }
                return response;
            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
                mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
            }

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
        }
        return response;
    }

    private String getChunkedContent(HttpEntity entity) throws IOException {

    	Util.Log.i(TAG, "getChunkedContent, isChunked = "
                + Boolean.valueOf(entity.isChunked()).toString());
        InputStream in = entity.getContent();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rCount = 0;
        byte[] buff = new byte[BUFFER_SIZE];

        try {
            while ((rCount = in.read(buff, 0, BUFFER_SIZE)) > 0) {
                swapStream.write(buff, 0, rCount);
            }

            return new String(swapStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
//            onShutdownConn();
//            if (mDownloadInfo.getDLSessionStatus() == DownloadInfo.STATE_DOWNLOADING) {
//                mDownloadInfo.setDLSessionStatus(DownloadInfo.STATE_PAUSEDOWNLOAD);
//                Log.e(TAG, "getChunkedContent, exception to set pause state");
//            }
            mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
            return null;
        }
    }

    

    /*
     * void setEjectFlag(Boolean flag) { synchronized (this) { mEjectFlag = flag; }
     * 
     * 
     * }
     */
    public int writeFile(HttpResponse response, long currSize) {
    	Util.Log.i(TAG, "writeFile");
        
        if (mDownloadInfo.getDLSessionStatus() != DownloadInfo.STATE_QUERYNEWVERSION) {
//            mNotification.clearNotification(NotifyManager.NOTIFY_DOWNLOADING);
//            mNotification
//            .showDownloadingNotificaton(mDownloadInfo.getVerNum(), (int) (((double) Util
//                    .getFileSize(Util.getPackageFileName(mContext)) / (double) mDownloadInfo
//                    .getUpdateImageSize()) * 100), true);
        }

        Util.cancelAlarm(mContext, Util.Action.ACTION_AUTO_DL_TIME_OUT);
        mDownloadInfo.setOtaAutoDlStatus(false);
        mDownloadInfo.setIfPauseWithinTime(false);  

        try {
            // response.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
            // 10000);
            InputStream in = response.getEntity().getContent();
            File ifolder = new File(Util.getPackagePathName(mContext));
            if (!ifolder.exists()) {
                ifolder.mkdirs();
            }
            RandomAccessFile out = null;
            
            String pkgFile = Util.getPackageFileName(mContext);
            try {
                out = new RandomAccessFile(pkgFile, "rws");
                out.seek(currSize);
            } catch (IOException e) {
                e.printStackTrace();
                onShutdownConn();
                mErrorCode = HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT;
                return mErrorCode;
            }
            byte[] buff = new byte[4096];
            int rc = 0;
            int i = 0;
            int j = 0;
            boolean rightnow = false;
            boolean finish = false;
            File fPkg = new File(pkgFile);
            
            if(fPkg == null) {
                out.close();
                mErrorCode = HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT;
                return mErrorCode;
            }
            while ((rc = in.read(buff, 0, 4096)) > 0) {
                // to-do: handle Intent.ACTION_MEDIA_EJECT
                /*
                 * synchronized (this) { if (mEjectFlag) { try { out.close(); } catch (IOException
                 * e) { e.printStackTrace(); } onShutdownConn(); return mErrorCode =
                 * HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT; } }
                 */

                try {
                	if (fPkg.exists()) {
                        out.write(buff, 0, rc);
                	} else {
                		Util.Log.e(TAG, "file not exist during downloading ");
                    	setPauseState();
                        out.close();
                        onShutdownConn();
                        mErrorCode = HTTP_FILE_NOT_EXIST;
                        if (mHandler != null) {

                            //mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_FILE_NOT_EXIST));
                        }
                        return mErrorCode;
                	}

                } catch (IOException e) {
                    e.printStackTrace();
                    out.close();
                    onShutdownConn();
                    mErrorCode = HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT;
                    return mErrorCode;
                }
                i++;
                int status = mDownloadInfo.getDLSessionStatus();
                if (status == DownloadInfo.STATE_PAUSEDOWNLOAD
                        || status == DownloadInfo.STATE_QUERYNEWVERSION) {
                	Util.Log.i(TAG, "writeFile, DownloadInfo = " + status);
                    mCookies = null;
                    finish = false;
                    out.close();
                    onShutdownConn();
                    return 0;

                }
                if (mHandler == null) {
                    if (rightnow) {
                        i = 200;
                        rightnow = false;
                    }
                    if (i == 200) {
                        onDownloadProcessUpdate();
                        i = 0;
                    }
                } else {
                    if (!rightnow) {
                        i = 18;
                        rightnow = true;
                    }
                    if (i == 20) {
                        i = 0;
                        onDownloadProcessUpdate();
                    }
                }
                j++;
                if (j == 20) {
                    onTransferRatio();
                    j = 0;
                }
                finish = true;
            }
            Util.Log.i(TAG, "writeFile, finish, rc = " + rc + "bytes" + ". finish = " + finish);
            if (finish) {
                onTransferRatio();
                onDownloadProcessUpdate();
            }

            long curSize = Util.getFileSize(Util.getPackageFileName(mContext));
            Util.Log.i(TAG,
                    "curSize = " + curSize + " mNewVersionInfo.mSize = "
                            + mDownloadInfo.getUpdateImageSize());

            out.close();
            
            if (curSize >= mDownloadInfo.getUpdateImageSize()) {

                onShutdownConn();
                return 0;
            }

        } catch (SocketTimeoutException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        
    	showNoNetworkToast();
    	

        if (mDownloadInfo.getDLSessionStatus() == DownloadInfo.STATE_DOWNLOADING) {
        	setPauseState();
        	Util.Log.e(TAG, "writeFile, exception to set pause state");
        	mDownloadInfo.setOtaAutoDlStatus(true);
        	mDownloadInfo.setIfPauseWithinTime(true);
            Util.setAlarm(mContext, AlarmManager.RTC, Calendar.getInstance()
                    .getTimeInMillis() + AUTO_DL_TIME, Util.Action.ACTION_AUTO_DL_TIME_OUT);


        }
        
//        if (mHandler != null) {
//
//            mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_NETWORKERROR));
//        }

        onShutdownConn();

        mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;
        return mErrorCode;
    }

    private void onTransferRatio() {

        long totalSize = mDownloadInfo.getUpdateImageSize();
        long currSize = Util.getFileSize(Util.getPackageFileName(mContext));
        if (currSize < 0) {
            currSize = 0;
        }
        if (totalSize == 0) {
            totalSize = -1;
        }
        if (totalSize < 0) {
            return;
        }
        int ratio = (int) (((double) currSize / (double) totalSize) * 100);
        if (ratio > Util.MAX_PERCENT) {
            ratio = Util.MAX_PERCENT;
            currSize = totalSize;
        }

        mDownloadInfo.setDownLoadPercent(ratio);
    }

    public void onDownloadProcessUpdate() {

//        mNotification
//                .showDownloadingNotificaton(mDownloadInfo.getVerNum(), (int) (((double) Util
//                        .getFileSize(Util.getPackageFileName(mContext)) / (double) mDownloadInfo
//                        .getUpdateImageSize()) * 100), true);
//
//        if (mHandler != null) {
//
//            mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_DLPKGUPGRADE));
//
//        }
    }

    private class HttpResponseContent {
        int mRand = -1;
        String mSessionId = null;
        String mAndroidNum = null;
        String mVersionName = null;
        long mFileSize = -1;
        String mReleaseNote = null;
        int mPkgId = -1;
        boolean mIsFullPkg = false;
        String mFingerprint = null;
    }

    private void sendErrorMessage() {
    	Util.Log.i(TAG, "sendErrorMessage, mErrorCode = " + mErrorCode);   	
//    	if ((mErrorCode == HTTP_RESPONSE_NETWORK_ERROR)
//    			||(mErrorCode == HTTP_UNKNOWN_ERROR)
//    			||(mErrorCode == HTTP_RESPONSE_AUTHEN_ERROR)) {
//    		showNoNetworkToast();
//    		
//    	}
        if (mHandler != null) {
        	mHandler.sendMessage(mHandler.obtainMessage(MSG_HTTP_RESPONSE_RESULT, mErrorCode, 0));
        	
//            switch (mErrorCode) {
//
//            case HTTP_RESPONSE_NO_NEW_VERSION:
//
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(SystemUpdateService.MSG_NONEWVERSIONDETECTED));
//                break;
//
//            case HTTP_RESPONSE_VERSION_ILLEGAL:
//            case HTTP_RESPONSE_VERSION_DELETE:
//
//                mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_NOTSUPPORT));
//                break;
//            case HTTP_RESPONSE_VERSION_REQUIRE:
//
//                mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_NOVERSIONINFO));
//                break;
//                
//            case HTTP_RESPONSE_REQUEST_TOO_LONG:
//            	
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(SystemUpdateService.MSG_LARGEPKG));
//                
//                break;
//                
//            case HTTP_RESPONSE_DELTA_DELETE:
//                mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_DELTADELETED));
//                break;
//
//            case HTTP_DETECTED_SDCARD_ERROR:
//
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(SystemUpdateService.MSG_SDCARDUNKNOWNERROR));
//                break;
//
//            case HTTP_DETECTED_SDCARD_INSUFFICENT:
//
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(SystemUpdateService.MSG_SDCARDINSUFFICENT));
//                break;
//
//            case HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT:
//
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(SystemUpdateService.MSG_SDCARDCRASHORUNMOUNT));
//                break;
//
//            case HTTP_RESPONSE_NETWORK_ERROR:
//            case HTTP_UNKNOWN_ERROR:
//            case HTTP_RESPONSE_AUTHEN_ERROR:
//
//                mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_NETWORKERROR));
//                break;
//
//            case HTTP_RESPONSE_UNZIP_ERROR:
//            case HTTP_RESPONSE_UNZIP_CKSUM:
//                mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_UNZIP_ERROR));
//                break;
//
//            default:
//
//                mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_UNKNOWERROR));
//
//            }
        }
    }

    public void clearNotification(int type) {
        //mNotification.clearNotification(type);
    }
    

    
    public void onDownloadImage() {
    	Util.Log.i(TAG, "onDownloadImage");

//        mNotification.clearNotification(NotifyManager.NOTIFY_DOWNLOADING);
//        mNotification
//        .showDownloadingNotificaton(mDownloadInfo.getVerNum(), (int) (((double) Util
//                .getFileSize(Util.getPackageFileName(mContext)) / (double) mDownloadInfo
//                .getUpdateImageSize()) * 100), true);

        if (mIsDownloading) {
            return;
        }
        mIsDownloading = true;
        notifyDlStarted();

        
        boolean isunzip = mDownloadInfo.getDLSessionUnzipState();
        boolean isren = mDownloadInfo.getDLSessionRenameState();
        if (isren && isunzip) {
        	
            setNotDownload();
            UpgradePkgManager.deleteCrashPkgFile(Util.getPackagePathName(mContext));
            onDownloadPackageUnzipAndCheck();

            return;
        }
        mDownloadInfo.setDLSessionStatus(DownloadInfo.STATE_DOWNLOADING);        
        String strNetWorkType = mDownloadInfo.getIfWifiDLOnly() ? NETTYPE_WIFI : "";

        if (!Util.isNetWorkAvailable(mContext, strNetWorkType)) {
            mErrorCode = HTTP_RESPONSE_NETWORK_ERROR;

            sendErrorMessage();
            setPauseState();
            setNotDownload();

            return;
        }

        // to-do: handle Intent.ACTION_MEDIA_EJECT
        // setEjectFlag(false);

        String url = ServerAddrReader.getInstance().getDownloadDeltaAddress();

        int packageid = mDownloadInfo.getDLSessionDeltaId();
        BasicNameValuePair deltaId = new BasicNameValuePair(
                mDownloadInfo.getFullPkgFlag() ? "versionId" : "deltaId", String.valueOf(packageid));
        Util.Log.i(TAG, "onDownloadImage pkgid = " + packageid);

        ArrayList<BasicNameValuePair> bnvpa = new ArrayList<BasicNameValuePair>();
        bnvpa.add(deltaId);
        long currentSize = Util.getFileSize(Util.getPackageFileName(mContext));
        currentSize -= FAULT_TOLERANT_BUFFER;
        if (currentSize < 0) {
            currentSize = 0;
        }
        BasicNameValuePair sizePar = new BasicNameValuePair("HTTP_RANGE",
                String.valueOf(currentSize));
        bnvpa.add(sizePar);

        if (mDownloadInfo.getFullPkgFlag()) {
            BasicNameValuePair version = new BasicNameValuePair("version",
                    mDownloadInfo.getVerNum());
            bnvpa.add(version);
            url = ServerAddrReader.getInstance().getDownloadFullAddress();
            if (url == null) {
                //url = mContext.getResources().getString(R.string.address_download_full);
            }
            Util.Log.v(TAG, "download full url = " + url);
        }
        HttpResponse response = doPost(url, null, bnvpa);
        
        if (mDownloadInfo.getDLSessionStatus() != DownloadInfo.STATE_DOWNLOADING) {
        	Util.Log.i(TAG, "onDownloadImage: status not right");
            setNotDownload();
        	return;
        }
        if (response == null) {
        	Util.Log.i(TAG, "onDownloadImage: response = null");
            mErrorCode = HTTP_UNKNOWN_ERROR;

            sendErrorMessage();
            setPauseState();
            setNotDownload();

            return;
        }
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != HttpStatus.SC_OK
                && status.getStatusCode() != HttpStatus.SC_PARTIAL_CONTENT) {
        	Util.Log.i(TAG, "onDownloadImage, ReasonPhrase = " + status.getReasonPhrase()
                    + ", status.getStatusCode() = " + status.getStatusCode());
            mCookies = null;

            
            if (status.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                //resetDescriptionInfo();
                mErrorCode = HTTP_RESPONSE_DELTA_DELETE;
            } else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TOO_LONG) {
            	mErrorCode = HTTP_RESPONSE_REQUEST_TOO_LONG;
            } else {
                mErrorCode = HTTP_RESPONSE_AUTHEN_ERROR;
            }
            sendErrorMessage();
            setPauseState();
            setNotDownload();

            return;
        }
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        int ret = writeFile(response, currentSize);
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        Util.Log.i(TAG, "onDownloadImage, download result = " + ret);

        if (ret == 0) {
            int downloadStatus = mDownloadInfo.getDLSessionStatus();

            if (downloadStatus == DownloadInfo.STATE_PAUSEDOWNLOAD
                    || downloadStatus == DownloadInfo.STATE_QUERYNEWVERSION) {
                setNotDownload();
                return;
            }
        }
        if (ret == HTTP_DETECTED_SDCARD_CRASH_OR_UNMOUNT) {
            //resetDescriptionInfo();
            sendErrorMessage();
            setNotDownload();
            return;
        }
        if (ret == HTTP_RESPONSE_NETWORK_ERROR) {

            setNotDownload();
            
            checkIfAutoDl();
            
            return;
        }
        
        if (ret == HTTP_FILE_NOT_EXIST) {
            setNotDownload();
            return;
        }
        onDownloadPackageUnzipAndCheck();
        mIsDownloading = false;
    }
    
    private void checkIfAutoDl() {
    	Util.Log.i(TAG, "checkIfAutoDl again ");    	
    	Util.NETWORK_STATUS networkStatus = Util.getNetworkType(mContext);
    	
    	Util.Log.i(TAG, "networkStatus = " + networkStatus);
        if (networkStatus == Util.NETWORK_STATUS.STATE_NONE_NETWORK) {
            return;
        }
        

        int status = mDownloadInfo.getDLSessionStatus();
        Util.Log.i(TAG, "status = " + status);
        
        if ((status == DownloadInfo.STATE_PAUSEDOWNLOAD)
        		&& (mDownloadInfo.getOtaAutoDlStatus())){

        	if ((networkStatus == Util.NETWORK_STATUS.STATE_WIFI)
        			&& mDownloadInfo.getIfPauseWithinTime()) {
        		onDownloadImage();
                return;

        	} else {
        		Util.Log.i(TAG, "showDlReminderNotification");
//        		mNotification.clearNotification(NotifyManager.NOTIFY_DOWNLOADING);
//                mNotification
//                .showDownloadingNotificaton(mDownloadInfo.getVerNum(), (int) (((double) Util
//                        .getFileSize(Util.getPackageFileName(mContext)) / (double) mDownloadInfo
//                        .getUpdateImageSize()) * 100), false);

                return;

        		
        	}
        }
        
        return;
    }


    private void onShutdownConn() {
    	Util.Log.i(TAG, "onShutdownConn");
/*        if (mHttpClient != null) {
            mHttpClient.getConnectionManager().shutdown();
        }
        mHttpClient = null;*/
        mCookies = null;
    }

    private void onDownloadPackageUnzipAndCheck() {

        onPackageUnzipping();
        // onDownloadPause();
        mDownloadInfo.setDLSessionUnzipState(true);
        if (!mDownloadInfo.getDLSessionRenameState()) {

            if (!UpgradePkgManager.renameOtaPkg(mContext)) {
                mErrorCode = HTTP_RESPONSE_UNZIP_ERROR;
                sendErrorMessage();
                return;
            }
            mDownloadInfo.setDLSessionRenameState(true);
        }

        long unzipSize = UpgradePkgManager.getSpaceForUnzipOtaPkg(mContext);

        Util.SDCARD_STATUS sdstat = Util.checkSdcardState(mContext, unzipSize);
        switch (sdstat) {
        case STATE_INSUFFICIENT:
            mErrorCode = HTTP_DETECTED_SDCARD_INSUFFICENT;
            sendErrorMessage();
            return;
        case STATE_LOST:
        case STATE_UNMOUNT:
            mErrorCode = HTTP_DETECTED_SDCARD_ERROR;
            sendErrorMessage();
            return;
        default:
            break;
        }

        int result = UpgradePkgManager.unzipUpgradePkg(
                UpgradePkgManager.getTempOtaPackage(mContext), null);
        if (result == UpgradePkgManager.UNZIP_SUCCESS) {
            onDownloadComplete();
            mDownloadInfo.setDLSessionUnzipState(false);
            mDownloadInfo.setDLSessionRenameState(false);
            UpgradePkgManager.deleteUnusedOtaFile(Util.getPackagePathName(mContext));
            return;
        } else {
            mErrorCode = (result == UpgradePkgManager.CKSUM_ERROR) ? HTTP_RESPONSE_UNZIP_CKSUM
                    : HTTP_RESPONSE_UNZIP_ERROR;
            sendErrorMessage();
            return;
        }
    }

    public void onPackageUnzipping() {

        mDownloadInfo.setDLSessionStatus(mDownloadInfo.STATE_PACKAGEUNZIPPING);
        if (mHandler != null) {
            //mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_UNZIP_LODING));

        }
    }

    public void onDownloadComplete() {
    	Util.Log.i(TAG, "onDownloadComplete");

        mDownloadInfo.setDLSessionStatus(DownloadInfo.STATE_DLPKGCOMPLETE);

//        mNotification.clearNotification(NotifyManager.NOTIFY_DOWNLOADING);
//        mNotification.showDownloadCompletedNotification();

        /*if(!Util.checkIfTopActivity(mContext, OtaPkgManagerActivity.CLASS_NAME)) {

            Intent reminder = new Intent(mContext,ForegroundDialogService.class);
            reminder.putExtra(ForegroundDialogService.DLG_ID, ForegroundDialogService.DIALOG_INSTALL_REMINDER);

            mContext.startService(reminder);
        }*/

        
        if (mHandler != null) {

          //  mHandler.sendMessage(mHandler.obtainMessage(SystemUpdateService.MSG_DLPKGCOMPLETE));
        } 

        // mNewVersionInfo.version = null;
        mDownloadInfo.setDLSessionDeltaId(-1);
//        mDownloadInfo.setVersionNote(null);
        mDownloadInfo.setUpdateImageSize(-1);

        mDownloadInfo.setDownLoadPercent(Util.MAX_PERCENT);
    }

    public void setPauseState() {
		if (mDownloadInfo.getDLSessionStatus() == DownloadInfo.STATE_DOWNLOADING) {
	        mDownloadInfo.setDLSessionStatus(DownloadInfo.STATE_PAUSEDOWNLOAD);		
		}

    }
    public void setDownloadState() {
        mDownloadInfo.setDLSessionStatus(DownloadInfo.STATE_DOWNLOADING);
    	
    }
    
    public void setNotDownload() {
        mIsDownloading = false;
//		if (mNotification != null) {
//			
//			int dlStatus = mDownloadInfo.getDLSessionStatus();
//			
//			if ((dlStatus == DownloadInfo.STATE_DOWNLOADING)
//					||(dlStatus == DownloadInfo.STATE_PAUSEDOWNLOAD)) {
//				mNotification.clearNotification(NotifyManager.NOTIFY_DOWNLOADING);
//				mNotification
//						.showDownloadingNotificaton(
//								mDownloadInfo.getVerNum(),
//								(int) (((double) Util.getFileSize(Util
//										.getPackageFileName(mContext)) / (double) mDownloadInfo
//										.getUpdateImageSize()) * 100), false);
//			}
//
//		}
    }


    /**
     * ******************system operator service start******************
     **/

    /**
     * These constant flag must be the same as that defined in SysOperService.java, please follow
     * them.
     **/
    private static final int MSG_NONE = 0;
    private static final int MSG_DELETE_COMMANDFILE = 1;
    private static final String CMD_FILE_KEY = "COMMANDFILE";

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    private Messenger mService = null;
    private boolean mIsBound = false;
    private int mNeedServiceDo = MSG_NONE;

    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	Util.Log.i(TAG, "handleMessage, msg.what=" + msg.what);
            switch (msg.what) {

            case MSG_DELETE_COMMANDFILE:
            	Util.Log.i(TAG, "MSG_DELETE_COMMANDFILE: arg1=" + msg.arg1);
                break;
            default:
                super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
        	Util.Log.i(TAG, "onServiceConnected");
            mService = new Messenger(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void notifySysoper(int actionType) {
        try {
            switch (actionType) {

            case MSG_DELETE_COMMANDFILE:
                Message msg = Message.obtain(null, MSG_DELETE_COMMANDFILE);
                msg.replyTo = mMessenger;
                Bundle data = new Bundle();
                if (data == null) {
                    return;
                }
                data.putString(CMD_FILE_KEY, COMMAND_FILE);
                msg.setData(data);
                if (mService != null) {
                    mService.send(msg);
                }
                break;
            default:
                break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    private boolean doBindService(Context context) {

        if (context != null) {
            mIsBound = context.bindService(new Intent(SYS_OPER_INTENT), mConnection,
                    Context.BIND_AUTO_CREATE);
        }
        Util.Log.i(TAG, "dobindService, isbound=" + mIsBound);
        return mIsBound;
    }

    private void doUnbindService(Context context) {
    	Util.Log.i(TAG, "doUnbindService");
        if (mIsBound) {
            mService = null;
            if (context != null) {
                context.unbindService(mConnection);
            }
            mIsBound = false;
        }
    }
    /**
     * ******************system operator service stop******************
     **/

    private void showNoNetworkToast() {
    	
//    	if (mNetworkHandler != null) {
//            mNetworkHandler.sendEmptyMessage(NETWORK_TOAST);		
//    	}

    }

    public void notifyDlStarted() {
    	if(mHandler != null) {
//            mHandler.sendMessage(mHandler
//                    .obtainMessage(SystemUpdateService.MSG_DL_STARTED));

    	}
    }
}
