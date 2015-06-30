package com.jzs.common.plugin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * {@hide}
 */
public class PluginService extends Service implements IPluginService {    
    private IPluginServiceCallback mCallback;
    public PluginService(Context context){
        super.attachBaseContext(context);
    }
    
    public final void setPluginCallback(IPluginServiceCallback callback){
        mCallback = callback;
    }
    
    protected final IPluginServiceCallback getPluginCallback(){
        return mCallback;
    }
    
    @Override
    public final IBinder onBind(Intent intent) {
        return null;//new Messenger(mHandler).getBinder();
    }
    
    /* for plugin */
    public boolean applyPlugin(){
        return true;
    }
    
    /* for plugin target */
    public boolean applyAssignPlugin(IPlugin plugin, IPlugin oldplugin){
//        android.util.Log.i("QsLog", "applyAssignPlugin(11)=plugin:"+plugin
//                +"=old:"+oldplugin
//                +"=this:"+this);
        return true;
    }
    
    /* for plugin target */
    public boolean resetPlugin(IPlugin plugin){
//        android.util.Log.i("QsLog", "resetPlugin(11)=plugin:"+plugin
//                +"=this:"+this);
        return true;
    }
//    
//    protected Handler getHandler(){
//        return mHandler;
//    }
//    
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            final boolean result;
//            switch (msg.what) {
//                case IPluginManager.PLUGIN_MSG_APPLY:{                    
//                    result = applyPlugin();
//                } break;
//                case IPluginManager.PLUGIN_MSG_APPLY_ASSIGN_PLUGIN:{
//                    result = applyAssignPlugin((IPlugin)msg.obj);
//                } break;
//                case IPluginManager.PLUGIN_MSG_RESET_PLUGIN:{
//                    result = resetPlugin((IPlugin)msg.obj);
//                } break;
//                default:
//                    result = false;
//                    return;
//            }
//            
//            final Messenger callback = msg.replyTo;
//            if(callback != null){
//                final int what = msg.what;
//                super.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Message reply = Message.obtain(null, what, result ? 1 : 0, 0);
//                        try {
//                            callback.send(reply);
//                        } catch (RemoteException e) { }
//                    }
//                });
//            }
//        }
//    };
}
