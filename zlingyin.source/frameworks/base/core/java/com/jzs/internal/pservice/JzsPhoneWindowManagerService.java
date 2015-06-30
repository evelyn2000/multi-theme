package com.jzs.internal.pservice;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;

import com.jzs.common.os.Build;
import com.jzs.common.provider.DeviceSettings;
import com.jzs.utils.ConfigOption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JzsPhoneWindowManagerService {

    private Context mJzsContext;
    private Handler mJzsHandler;
    
    private static final int MSG_ENABLE_RGB_LED_LIGHTS = 1000;

    
    private class JzsPolicyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ENABLE_RGB_LED_LIGHTS:
                    setRgbLedsEnableToDriver(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }
    
    public JzsPhoneWindowManagerService(Context context){
        mJzsContext = context;
        mJzsHandler = new JzsPolicyHandler();
    }
    
    public void systemReady() {
//        mJzsHandler.post(new Runnable() {
//            public void run() {
//                final ContentResolver resolver = mJzsContext.getContentResolver();
//                if(ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS){
//                    DeviceSettings.registerRgbLedLightsContentObserver(resolver, mRgbLedsChangeObserver);
//                    if(DeviceSettings.isRgbLedLightsEnable(resolver)){
//                        Message msg = mJzsHandler.obtainMessage(MSG_ENABLE_RGB_LED_LIGHTS, 1, 0);
//                        mJzsHandler.sendMessage(msg);
//                    } else {
//                        Message msg = mJzsHandler.obtainMessage(MSG_ENABLE_RGB_LED_LIGHTS, 0, 0);
//                        mJzsHandler.sendMessage(msg);
//                    }
//                }
//            }
//        });
    }
    
    private final ContentObserver mRgbLedsChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            
            mJzsHandler.removeMessages(MSG_ENABLE_RGB_LED_LIGHTS);
            final int enable = DeviceSettings.isRgbLedLightsEnable(mJzsContext.getContentResolver()) ? 1 : 0;
            android.util.Log.i("QsLog", "led settings changed:"+enable);
            
            Message msg = mJzsHandler.obtainMessage(MSG_ENABLE_RGB_LED_LIGHTS, enable, 0);
            mJzsHandler.sendMessageDelayed(msg, 1000);
        }
    };
    
    private void setRgbLedsEnableToDriver(int enable){
        File file = new File("/sys/module/leds_drv/parameters/led_enable_setting");
        if(file != null && file.exists()){
            if(file.canWrite()){
                android.util.Log.i("QsLog", "led file change:"+enable);
                try {
                    FileOutputStream fileout = new FileOutputStream(file);
                    if(fileout != null){
                        String buff = String.valueOf(enable);
                        fileout.write(buff.getBytes());
                        fileout.flush();
                        fileout.close();
                    }
                } catch (IOException e) {
                }
            } else {
                android.util.Log.i("QsLog", "led file can't be write able");
            }
        } else {
            android.util.Log.e("QsLog", "led file not exist");
            //mJzsContext.getContentResolver().unregisterContentObserver(mRgbLedsChangeObserver);
        }
    }
}
