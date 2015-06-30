package com.jzs.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.jzs.internal.R;

public class JzsScImageView extends ImageView implements JzsShortcutViewInterface{
    private long mDownTime;
    private final int mKeyCode;
    private final boolean mSupportsLongpress;
    private final String mBtnAction;
    private final int mPosDegress;
    private final int mTouchSlop;
    
    
    public JzsScImageView(Context context) {
        this(context, null);
    }
    
    public JzsScImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public JzsScImageView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JzsScImageView,
                defStyle, 0);

        mKeyCode = a.getInt(R.styleable.JzsScImageView_android_keycode, 0);
        mSupportsLongpress = a.getBoolean(R.styleable.JzsScImageView_keyRepeat, true);
        mBtnAction = a.getString(R.styleable.JzsScImageView_btnAction);
//        if(action != null){
//            if(action.startsWith("tag:")){
//                
//            }
//        }
//        if("settings".equals(getTag())){
//            mPosDegress = 0;
//        } else if("customapps".equals(getTag())){
//            mPosDegress = 72;
//        } else if("back".equals(getTag())){
//            mPosDegress = 144;
//        } else if("home".equals(getTag())){
//            mPosDegress = 216;
//        } else if("screenshot".equals(getTag())){
//            mPosDegress = 288;
//        } else {
            mPosDegress = a.getInt(R.styleable.JzsScImageView_positionDegress, 0);
        //}
        a.recycle();
        
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.v("QsLog", "JzsScImageView()==mKeyCode:"+mKeyCode
                    +"==mSupportsLongpress:"+mSupportsLongpress
                    +"==mPosDegress:"+mPosDegress
                    +"==mBtnAction:"+mBtnAction
                    +"==tag:"+getTag());
        }
        
        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    
    public final String getAction(){
        return mBtnAction;
    }
    
    public final int getPositionDegress(){
        return mPosDegress;
    }
    
    public final void reloadTextResource(){
    }
    
    private final Runnable mCheckLongPress = new Runnable() {
        public void run() {
            if (isPressed()) {
                // Slog.d("KeyButtonView", "longpressed: " + this);
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "ImageView::mCheckLongPress()==mKeyCode:"+mKeyCode
                            );
                }
                if (mKeyCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, KeyEvent.FLAG_LONG_PRESS);
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
                } else {
                    // Just an old-fashioned ImageView
                    performLongClick();
                }
            }
        }
    };
    
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int x, y;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Slog.d("KeyButtonView", "press");
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "ImageView::onTouchEvent(ACTION_DOWN)==mKeyCode:"+mKeyCode
                            +"==isPressed:"+isPressed());
                }
                mDownTime = SystemClock.uptimeMillis();
                setPressed(true);
                if (mKeyCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, 0, mDownTime);
                } else {
                    // Provide the same haptic feedback that the system offers for virtual keys.
                    //performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                    postDelayed(mCheckLongPress, ViewConfiguration.getLongPressTimeout());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int)ev.getX();
                y = (int)ev.getY();
                setPressed(x >= -mTouchSlop
                        && x < getWidth() + mTouchSlop
                        && y >= -mTouchSlop
                        && y < getHeight() + mTouchSlop);
                break;
            case MotionEvent.ACTION_CANCEL:
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "ImageView::onTouchEvent(ACTION_CANCEL)==mKeyCode:"+mKeyCode
                            +"==isPressed:"+isPressed());
                }
                setPressed(false);
                if (mKeyCode != 0) {
                    sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
            case MotionEvent.ACTION_UP:
                final boolean doIt = isPressed();
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "ImageView::onTouchEvent(ACTION_UP)==mKeyCode:"+mKeyCode
                            +"==doIt:"+doIt);
                }
                setPressed(false);
                if (mKeyCode != 0) {
                    if (doIt) {
                        sendEvent(KeyEvent.ACTION_UP, 0);
                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
                        // [ALPS00439010] We should NOT play sound here because PhoneWindow will play sound when it shows menu
                        if (mKeyCode != KeyEvent.KEYCODE_MENU) {
                            playSoundEffect(SoundEffectConstants.CLICK);
                        }
                    } else {
                        sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                    }
                } else {
                    // no key code, just a regular ImageView
                    if (doIt) {
                        performClick();
                    }
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
        }

        return true;
    }

    private void sendEvent(int action, int flags) {
        sendEvent(action, flags, SystemClock.uptimeMillis());
    }

    private void sendEvent(int action, int flags, long when) {
        final int repeatCount = (flags & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent ev = new KeyEvent(mDownTime, when, action, mKeyCode, repeatCount,
                0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                flags | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        InputManager.getInstance().injectInputEvent(ev,
                InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }
}
