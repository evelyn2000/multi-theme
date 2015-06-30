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

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.jzs.android.systemstyle.utils.Util;

/**
 * Get OTA Server for internal OTA update
 * 
 * @author mtk80800
 * 
 */
public class ServerAddrReader {
    private static final String TAG = "SystemUpdate/ServerAddrReader";
    private static final String XML_TAG_ADDRESS_LOGIN = "address_login";
    private static final String XML_TAG_ADDRESS_CHECK_VERSION = "address_check_version";
    private static final String XML_TAG_ADDRESS_DOWNLOAD_FULL = "address_download_full";
    private static final String XML_TAG_ADDRESS_DOWNLOAD_DELTA = "address_download_delta";
    private static final String XML_TAG_ADDRESS_GET_DEFAIL_INFO = "address_get_detailinfo";
    private static final String XML_TAG_ADDRESS_GET_THUMB_LIST = "address_get_thumblist";
    //private static final String XML_TAG_ADDRESS_REGISTER_DEVICE = "address_register_device";
    
    private static final String DEFAULT_ADDRESS_LOGIN = "address_login";
    private static final String DEFAULT_ADDRESS_CHECK_VERSION = "http://ota.qishangtel.com/fota/checkversion.php";
    private static final String DEFAULT_ADDRESS_DOWNLOAD_FULL = "http://ota.qishangtel.com/fota/download/download.php";
    private static final String DEFAULT_ADDRESS_GET_DEFAIL_INFO = "http://ota.qishangtel.com/fota/getversioninfo.php";
    private static final String DEFAULT_ADDRESS_GET_THUMB_LIST = "http://ota.qishangtel.com/fota/getversioninfo.php";
    //private static final String DEFAULT_ADDRESS_REGISTER_DEVICE = "http://ota.qishangtel.com/fota/getversioninfo.php";
    
    private XmlParser mAddrParser;
    private static ServerAddrReader mGetAttrFromXml;

    public static ServerAddrReader getInstance() {
        Util.Log.d(TAG, "getInstance");
        if (mGetAttrFromXml == null) {
            mGetAttrFromXml = new ServerAddrReader();
        }
        return mGetAttrFromXml;
    }

    private ServerAddrReader() {
        File configFileInSystem = new File(Util.PathName.INTERNAL_ADDRESS_FILE);
        if (configFileInSystem.exists()) {
        	Util.Log.d(TAG, "address.xml exists in system");
            try {
                mAddrParser = new XmlParser(Util.PathName.INTERNAL_ADDRESS_FILE);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        	Util.Log.w(TAG, "address.xml does not exist in system");
        }
    }

    public String getLoginAddress() {
        String loginAddr = DEFAULT_ADDRESS_LOGIN;
        if (mAddrParser != null) {
        	Util.Log.d(TAG, "Parser LoginAddress from " + XML_TAG_ADDRESS_LOGIN);
            loginAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_LOGIN);
            Util.Log.d(TAG, "LoginAddress = " + loginAddr);
        }
        return loginAddr;
    }
    
    public String getDetailInfoAddress() {
        String retAddr = DEFAULT_ADDRESS_GET_DEFAIL_INFO;
        if (mAddrParser != null) {
        	Util.Log.d(TAG, "Parser getDetailInfoAddress from " + XML_TAG_ADDRESS_GET_DEFAIL_INFO);
            retAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_GET_DEFAIL_INFO);
            Util.Log.d(TAG, "getDetailInfoAddress = " + retAddr);
        }
        return retAddr;
    }
    
//    public String getVersionThumbListAddress() {
//        String retAddr = DEFAULT_ADDRESS_GET_THUMB_LIST;
//        if (mAddrParser != null) {
//        	Util.Log.d(TAG, "Parser getVersionThumbListAddress from " + XML_TAG_ADDRESS_GET_THUMB_LIST);
//            retAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_GET_THUMB_LIST);
//            Util.Log.d(TAG, "LoginAddress = " + retAddr);
//        }
//        return retAddr;
//    }
    
    public String getVersionThumbListAddress() {
        String retAddr = DEFAULT_ADDRESS_GET_THUMB_LIST;
        if (mAddrParser != null) {
        	Util.Log.d(TAG, "Parser getVersionThumbListAddress from " + XML_TAG_ADDRESS_GET_THUMB_LIST);
            retAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_GET_THUMB_LIST);
            Util.Log.d(TAG, "LoginAddress = " + retAddr);
        }
        return retAddr;
    }

    public String getCheckVersionAddress() {
        String loginAddr = DEFAULT_ADDRESS_CHECK_VERSION;
        if (mAddrParser != null) {
        	Util.Log.d(TAG, "Parser CheckVersionAddress from " + XML_TAG_ADDRESS_CHECK_VERSION);
            loginAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_CHECK_VERSION);
            Util.Log.d(TAG, "CheckVersionAddress = " + loginAddr);
        }
        return loginAddr;
    }

    public String getDownloadFullAddress() {
        String loginAddr = DEFAULT_ADDRESS_DOWNLOAD_FULL;
        if (mAddrParser != null) {
        	Util.Log.d(TAG, "Parser DownloadFullAddress from " + XML_TAG_ADDRESS_DOWNLOAD_FULL);
            loginAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_DOWNLOAD_FULL);
            Util.Log.d(TAG, "DownloadFullAddress = " + loginAddr);
        }
        return loginAddr;
    }

    public String getDownloadDeltaAddress() {
        String loginAddr = null;
        if (mAddrParser != null) {
        	Util.Log.d(TAG, "Parser DownloadDeltaAddress from " + XML_TAG_ADDRESS_DOWNLOAD_DELTA);
            loginAddr = mAddrParser.getValByTagName(XML_TAG_ADDRESS_DOWNLOAD_DELTA);
            Util.Log.d(TAG, "DownloadDeltaAddress = " + loginAddr);
        }
        return loginAddr;
    }

}
