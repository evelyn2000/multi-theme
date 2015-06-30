/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 * 
 * MediaTek Inc. (C) 2010. All rights reserved.
 * 
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.jzs.android.systemstyle.https;

import com.jzs.android.systemstyle.utils.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NotifyManager {
	public static final int NOTIFY_NEW_VERSION = 1;
	public static final int NOTIFY_DOWNLOADING = 2;
	public static final int NOTIFY_DL_COMPLETED = 3;
    static final String TAG = "SystemUpdate/NotifyManager";

    private Notification.Builder mNotification;
    private int mNotificationType;
    private Context mNotificationContext;
    private NotificationManager mNotificationManager;

    /**
     * Constructor function.
     * @param context    environment context
     */
    public NotifyManager(Context context) {
        mNotificationContext = context;
        mNotification = null;
        mNotificationManager = (NotificationManager) mNotificationContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNewVersionNotification(int title, int icon) {
    	
        Log.i(TAG, "showNewVersionNotification");
        mNotificationType = NOTIFY_NEW_VERSION;
        CharSequence contentTitle = mNotificationContext.getText(title);
        configAndShowNotification(icon,
                contentTitle, null, Util.Action.ACTION_SYSTEM_UPDATE_ENTRY, false, true);
    }

    public void showDownloadCompletedNotification(int title, int content, int icon) {
        Log.i(TAG, "showDownloadCompletedNotification");
        mNotificationType = NOTIFY_DL_COMPLETED;
        CharSequence contentTitle = mNotificationContext.getText(title);
        String contentText = mNotificationContext.getString(content);
        configAndShowNotification(icon,
                contentTitle, contentText, Util.Action.ACTION_OTA_MANAGER, true, false);
    }

    public void showDownloadingNotificaton(String version, int currentProgress, boolean isOngoing) {
        Log.i(TAG, "showDownloadingNotificaton with ongoing " + isOngoing);
        mNotificationType = NOTIFY_DOWNLOADING;
//        String contentTitle = mNotificationContext
//                .getString(R.string.app_name);
//        setNotificationProgress(R.drawable.stat_download_downloading,
//                contentTitle, currentProgress, isOngoing);
    }

    private void setNotificationProgress(int iconDrawableId,
            String contentTitle, int currentProgress, boolean isOngoing) {
        if (mNotification == null) {
            mNotification = new Notification.Builder(mNotificationContext);
            if (mNotification == null) {
                return;
            }
            mNotification
                    .setAutoCancel(true)
                    .setOngoing(isOngoing)
                    .setContentTitle(contentTitle)
                    .setSmallIcon(iconDrawableId)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(
                            getPendingIntenActivity(Util.Action.ACTION_OTA_MANAGER));
            Log.i(TAG, "set motification with " +  isOngoing);

        }
        String percent = "" + currentProgress + "%";
        mNotification.setProgress(Util.MAX_PERCENT, currentProgress, false).setContentInfo(
                percent);

        mNotificationManager.notify(mNotificationType,
                mNotification.getNotification());
    }

    private PendingIntent getPendingIntenActivity(String intentFilter) {

        Intent notificationIntent = new Intent(intentFilter);
        PendingIntent contentIntent = PendingIntent.getActivity(
                mNotificationContext, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }

    private void configAndShowNotification(int iconDrawableId,
            CharSequence contentTitle, String contentText, String intentFilter, boolean isOngoing, boolean autoCancle) {
        mNotification = new Notification.Builder(mNotificationContext);

        if (mNotification == null) {
            return;
        }
        mNotification.setAutoCancel(autoCancle).setContentTitle(contentTitle)
                .setContentText(contentText).setSmallIcon(iconDrawableId)
                .setWhen(System.currentTimeMillis())
                .setOngoing(isOngoing)
                .setContentIntent(getPendingIntenActivity(intentFilter));

        mNotificationManager.notify(mNotificationType,
                mNotification.getNotification());
        mNotification = null;
    }

    public void clearNotification(int notificationId) {
    	
        Log.i(TAG, "clearNotification " + notificationId);
        mNotificationManager.cancel(notificationId);
        mNotification = null;
        return;
    }
}
