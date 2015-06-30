package com.jzs.internal.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jzs.common.content.QsIntent;
import com.jzs.internal.R;

import java.util.Locale;

public class JzsCircleShortCutContainer extends FrameLayout implements JzsCircleShortCutViewGroup.ShortCutViewGroupCallback {
    
    public static interface ShortCutActionCallback{
        boolean doShortCutAction(final View container, final String action, final Runnable changeModeToSmall);
    }
    
    private final static String JZS_CIRCLESHORTCUT_KEY_POSITION = "jzs_circle_sc_key_position";
    
//    public final static int POSITION_LEFT = 0;
//    public final static int POSITION_RIGHT = 1;
//    public final static int POSITION_TOP = 2;
//    public final static int POSITION_BOTTOM = 3;
    
    private final static int AUTO_BACK_TO_SMALL_MODE_TIMEOUT = 5000;
    
    //public final static int POSITION_HOR_RIGHT = 0x20;
    
    //private final static int MODE_SMALL_LINK = 0;
    //private final static int MODE_LARGE = 1;
    //private static int mMode = MODE_SMALL_LINK;
    private static int mAffixPosition = 0;
    private boolean mIsAttached;
    private final WindowManager mWindowManager;
    private static ShortCutActionCallback mShortCutActionCallback = null;
    
//    private int mFirstDownX;
//    private int mFirstDownY;
    private int mLastDownX;
    private int mLastDownY;
    private final int mTouchSlop;
    private boolean mIsTouching;
    
    private View mModeSmallBtn;
    private ViewGroup mModeLargeContainer;
    private static int sCurrentScreenWidth = -1;
    private static int sCurrentScreenHeight = -1;
    
    private int mCurrentOrientation;
    private String mCurrentLocale;
    //private boolean mIsForceLayout = false; 

    
    public JzsCircleShortCutContainer(Context context) {
        this(context, null);
    }

    public JzsCircleShortCutContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JzsCircleShortCutContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mAffixPosition = getViewAffixPosition(context);
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JzsCircleShortCutContainer,
//                defStyle, 0);
//
//        mFloatGravity = a.getInt(R.styleable.JzsCircleShortCutContainer_android_gravity, Gravity.CENTER);
//        mFloatPaddingTop = a.getDimensionPixelSize(R.styleable.JzsCircleShortCutContainer_JzsUserPaddingTop, 0);
//        mFloatPaddingBottom = a.getDimensionPixelSize(R.styleable.JzsCircleShortCutContainer_JzsUserPaddingBottom, 0);
//        a.recycle();
        
        
//        mStatusBarHeight = r.getDimensionPixelSize(
//                com.android.internal.R.dimen.status_bar_height);
        
        Configuration config = context.getResources().getConfiguration();
        mCurrentOrientation = config.orientation;
        mCurrentLocale = config.locale.toString();
        
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            Resources r = context.getResources();
            //final Configuration config = r.getConfiguration();
            android.util.Log.i("QsLog", "JzsCircleShortCutContainer(0)==screenWidthDp:"+config.screenWidthDp
                    +"==screenHeightDp:"+config.screenHeightDp
                    +"==densityDpi:"+config.densityDpi
                    +"==orientation:"+config.orientation
                    +"==defaultDpi:"+DisplayMetrics.DENSITY_DEFAULT
                    );
        }
    }

    
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        
        mModeSmallBtn = findViewWithTag("mode_smalllnk_btn");
        mModeSmallBtn.setClickable(false);
        
        mModeLargeContainer = (ViewGroup)findViewWithTag("mode_large_container");
        if(mModeLargeContainer != null){
            mModeLargeContainer.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    changeMode();
                }
            });
            
            //((JzsCircleShortCutViewGroup)mModeLargeContainer).setShortCutViewGroupCallback(this);
            final int count = mModeLargeContainer.getChildCount();
            for(int i=0; i<count; i++){
                final View view = mModeLargeContainer.getChildAt(i);
                if(!TextUtils.isEmpty(((JzsShortcutViewInterface)view).getAction())){
                    view.setOnClickListener(new View.OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            onChildClicked(v, ((JzsShortcutViewInterface)v).getAction());
                            
    //                        if(mCallback != null){
    //                            mCallback.onChildClicked(v, ((JzsScImageView)view).getAction());
    //                        }
                        }
                    });
                }
            }
        }
        
        super.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(!isSmallMode())
                    changeMode();
            }
        });
    }
    
    public boolean isAttached(){
        return mIsAttached;
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        if(isSmallMode()){
            final WindowManager.LayoutParams lp = (WindowManager.LayoutParams)super.getLayoutParams();

            if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
                android.util.Log.i("QsLog", "onAttachedToWindow(0)=="
                        +"==isSmallMode:"+isSmallMode()
                        +"==w:"+lp.width
                        +"==h:"+lp.height
                        +"==x:"+lp.x
                        +"==y:"+lp.y
                        +"==mesw:"+super.getMeasuredWidth()
                        +"==mesh:"+super.getMeasuredHeight());
            }
            
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindowManager.updateViewLayout(this, lp);
        } else {
            if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
                android.util.Log.i("QsLog", "onAttachedToWindow(0)=="
                        +"==isSmallMode:"+isSmallMode());
            }
        }
        
        mIsAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        mIsAttached = false;
        mShortCutActionCallback = null;
        removeCallbacks(mAutoChangeModeToSmall);
        
        super.onDetachedFromWindow();
        
        setViewAffixPosition(getContext(), mAffixPosition);
        
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.i("QsLog", "onDetachedFromWindow(0)==width:"+super.getWidth()
                    +"==heightSize:"+super.getHeight()
                    +"==mAffixPosition:"+Integer.toHexString(mAffixPosition));
        }
    }
    
    //@Override
    public boolean onChildClicked(View v, String action){
        
        removeCallbacks(mAutoChangeModeToSmall);
        
        if(mShortCutActionCallback != null 
            && mShortCutActionCallback.doShortCutAction(this, action, mAutoChangeModeToSmall)){
            return true;
        }
        
        if(action.equals("tag:customapps")){
            super.post(new Runnable(){
                public void run() {
                    
                    mAutoChangeModeToSmall.run();
                    Intent intent = new Intent(QsIntent.ACTION_JZS_APP_CUSTOMAPPS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);

                }
            });
            return true;
        } else if(action.equals("tag:settings")){
            super.post(new Runnable(){
                public void run() {
                    
                    mAutoChangeModeToSmall.run();
                    Intent intent = new Intent(QsIntent.ACTION_JZS_APP_SETTINGS);
                    intent.setPackage("com.android.settings");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);

                }
            });
            return true;
        } else if(!action.startsWith("tag:")) {
            try{
                Intent intent = Intent.parseUri(action, 0);
                if(intent != null){
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    return true;
                }
            } catch (java.net.URISyntaxException e){ }
        } else if(action.equals("tag:lockscreen")){
            super.post(mAutoChangeModeToSmall);
            return true;
        }

        postDelayed(mAutoChangeModeToSmall, AUTO_BACK_TO_SMALL_MODE_TIMEOUT);
        return false;
    }
    
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.i("QsLog", "onConfigurationChanged(0)==screenWidthDp:"+newConfig.screenWidthDp
                    +"==screenHeightDp:"+newConfig.screenHeightDp
                    +"==densityDpi:"+newConfig.densityDpi
                    +"==orientation:"+newConfig.orientation);
        }
        if(mCurrentOrientation != newConfig.orientation){
            mCurrentOrientation = newConfig.orientation;
            sCurrentScreenWidth = -1;
            sCurrentScreenHeight = -1;
        }
        
        final String str = newConfig.locale.toString();
        if(!mCurrentLocale.equals(str)){
            mCurrentLocale = str;
            if(mModeLargeContainer != null){
                final int count = mModeLargeContainer.getChildCount();
                for(int i=0; i<count; i++){
                    final View view = mModeLargeContainer.getChildAt(i);
                    if(view != null){
                        ((JzsShortcutViewInterface)view).reloadTextResource();
                    }
                }
//                mModeLargeContainer.forceLayout();
//                if(mModeLargeContainer.getVisibility() != View.VISIBLE)
//                    mIsForceLayout = true;
            }
        }
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if(JzsCircleShortCutViewGroup.DEBUG_KEY){
            android.util.Log.i("QsLog", "dispatchKeyEvent(0)==event:"+event.getAction()
                    +"==KeyCode:"+event.getKeyCode());
        }
        
//        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK
//            && mModeLargeContainer.getVisibility() == View.VISIBLE){
//            
//            changeMode();
//            return true;
//        }
        
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

        if(!changed){
            return;
        }
        
        if(mModeLargeContainer.getVisibility() != View.VISIBLE){
            super.onLayout(changed, l, t, r, b);
            return;
        }
                
        final int width = r - l;
        final int height = b - t;
        if(JzsCircleShortCutViewGroup.DEBUG_LAYOUT){
            android.util.Log.i("QsLog", "Container::onLayout()==w:"+sCurrentScreenWidth
                    +"==h:"+sCurrentScreenHeight
                    +"==nw:"+width
                    +"==nh:"+height
                    +"==top:"+t
                    +"==top1:"+super.getTop());
        }

        final int childWidth = mModeLargeContainer.getMeasuredWidth();
        final int childHeight = mModeLargeContainer.getMeasuredHeight();
        
        int x = (int)(getAffixPercentX(mAffixPosition) * sCurrentScreenWidth / 100 
                        - childWidth/2 + mModeSmallBtn.getMeasuredWidth()/2);
        int y = (int)((getAffixPercentY(mAffixPosition) * sCurrentScreenHeight * sCurrentScreenHeight) / (height * 100) 
                        - childHeight/2 + mModeSmallBtn.getMeasuredHeight()/2);
        if(x < 0)
            x = 0;
        else if((x + childWidth) > width)
            x = width - childWidth;
        
        if(y < 0)
            y = 0;
        else if((y + childHeight) > height)
            y = height - childHeight;
        
        mModeLargeContainer.layout(x, y, x + childWidth, y + childHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "Container::onTouchEvent(ACTION_DOWN)==isSmallMode:"+isSmallMode()
                            +"==isPressed:"+isPressed());
                }
                if(isSmallMode() && getVisibility() == View.VISIBLE){
                    setPressed(true);
                    mIsTouching = true;
                    mLastDownX = (int)event.getRawX();
                    mLastDownY = (int)event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsTouching){
                    final int x = (int)event.getRawX();
                    final int y = (int)event.getRawY();
                    int dx = x - mLastDownX;
                    int dy = y - mLastDownY;

                    if((Math.abs(dx) > mTouchSlop || Math.abs(dy) > mTouchSlop)){
                        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams)super.getLayoutParams();
                        int newX = lp.x + dx;
                        int newY = lp.y + dy;
                        
                        if(newX < 0)
                            newX = 0;
                        //else if(newX > )
                        
                        if(newY < 0)
                            newY = 0;
                            
                        if(newX != lp.x || newY != lp.y){
                            setPressed(false);                            
                            lp.x = newX;
                            lp.y = newY;

                            mWindowManager.updateViewLayout(this, lp);
                            
                            mLastDownX = x;
                            mLastDownY = y;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "Container::onTouchEvent(ACTION_CANCEL)==isSmallMode:"+isSmallMode()
                            +"==isPressed:"+isPressed());
                }
                setPressed(false);
                mIsTouching = false;
                break;
            case MotionEvent.ACTION_UP:
                
                if(JzsCircleShortCutViewGroup.DEBUG_MOTION){
                    android.util.Log.i("QsLog", "Container::onTouchEvent(ACTION_UP)==isSmallMode:"+isSmallMode()
                            +"==isPressed:"+isPressed()
                            +"==mIsTouching:"+mIsTouching);
                }
                if(mIsTouching){
                    final boolean doIt = isPressed();
                    mIsTouching = false;
                    setPressed(false);
                    super.post(new Runnable(){
                        public void run() {
                            updateCurrentPosition();
                            if(doIt){
                                changeMode();
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
        return mIsTouching || super.onTouchEvent(event);
    }
    
    private final Runnable mAutoChangeModeToSmall = new Runnable() {
        public void run() {
            if(mIsAttached && mModeLargeContainer.getVisibility() == View.VISIBLE) {
                changeMode();
            }
        }
    };

    public static JzsCircleShortCutContainer showShortCutContainer(Context context, ShortCutActionCallback callback){
//        if(mMode != MODE_SMALL_LINK)
//            return null;
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.w("QsLog", "showShortCutContainer(0)==");
        }
        final JzsCircleShortCutContainer floatView = (JzsCircleShortCutContainer)LayoutInflater.from(context).inflate(R.layout.circle_shortcut_container, 
                null, false);
        final WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.w("QsLog", "showShortCutContainer(1)==");
        }
        
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                //| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        lp.windowAnimations = 0;
        
        lp.setTitle("JzsCircleShortCut");
        lp.format = PixelFormat.TRANSLUCENT;
        
        if(floatView != null){
            mShortCutActionCallback = callback;
            
            sCurrentScreenWidth = -1;
            
            initViewPosition(wm, lp, floatView);
            wm.addView(floatView, lp);

            if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
                android.util.Log.w("QsLog", "showShortCutContainer(2)==");
            }
        } else {
            mShortCutActionCallback = null;
        }
        
        return floatView;
    }

    private boolean isSmallMode(){
        return (mModeSmallBtn.getVisibility() == View.VISIBLE);
    }
    
    private void updateCurrentPosition(){
        if(!isSmallMode()){
            return;
        }
        
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.i("QsLog", "updateCurrentPosition(0)==w:"+sCurrentScreenWidth
                    +"==h:"+sCurrentScreenHeight
                    );
        }
        
        if(sCurrentScreenWidth < 0 || sCurrentScreenHeight < 0){
            DisplayMetrics dm = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getRealMetrics(dm);  
            sCurrentScreenWidth = dm.widthPixels;
            sCurrentScreenHeight = dm.heightPixels;
        }
        
        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams)super.getLayoutParams();
        int perX = (int)(lp.x * 100  / sCurrentScreenWidth);
        int perY = (int)(lp.y * 100  / sCurrentScreenHeight);
        
        mAffixPosition = formatAffixPosition(perX, perY);
    }
    
    private void changeMode(){
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.i("QsLog", "changeMode(0)==width:"+super.getWidth()
                    +"==heightSize:"+super.getHeight()
                    +"==mode:"+(mModeSmallBtn.getVisibility() == View.VISIBLE ? "small" : "large"));
        }
        removeCallbacks(mAutoChangeModeToSmall);
        
        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams)super.getLayoutParams();
        
        if(mModeSmallBtn.getVisibility() == View.VISIBLE){
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            //lp.gravity = Gravity.LEFT|Gravity.TOP;
            //lp.gravity = mFloatGravity;//Gravity.CENTER;
            lp.x = 0;
            lp.y = 0;

            super.setVisibility(View.INVISIBLE);
            
            mModeLargeContainer.setVisibility(View.VISIBLE);
            mModeSmallBtn.setVisibility(View.GONE);
            mWindowManager.updateViewLayout(this, lp);

            postDelayed(mAutoChangeModeToSmall, AUTO_BACK_TO_SMALL_MODE_TIMEOUT);
            
        } else {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            
            initViewPosition(mWindowManager, lp, this);
            
            super.setVisibility(View.INVISIBLE);
            
            mModeSmallBtn.setVisibility(View.VISIBLE);
            mModeLargeContainer.setVisibility(View.GONE);
            mWindowManager.updateViewLayout(this, lp);

        }
        
        if(mIsAttached) {
            super.post(new Runnable(){
                public void run() {
                    setVisibility(View.VISIBLE);
                }
            });
        }
    }
    
    private static void initViewPosition(WindowManager wm, WindowManager.LayoutParams lp, View view){
        int width = view.getMeasuredWidth();//.getWidth();
        int height = view.getMeasuredHeight();//.getHeight();
        
        if(JzsCircleShortCutViewGroup.DEBUG_MEASURE){
            android.util.Log.i("QsLog", "initViewPosition(0)==width:"+view.getWidth()
                    +"==heightSize:"+view.getHeight()
                    +"==width:"+width
                    +"==height:"+height
                    +"==mAffixPosition:"+Integer.toHexString(mAffixPosition));
        }
        
        if(sCurrentScreenWidth < 0 || sCurrentScreenHeight < 0){
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(dm);
            sCurrentScreenWidth = dm.widthPixels;
            sCurrentScreenHeight = dm.heightPixels;
        }
        
        lp.x = (int)(getAffixPercentX(mAffixPosition) * sCurrentScreenWidth / 100);
        lp.y = (int)(getAffixPercentY(mAffixPosition) * sCurrentScreenHeight / 100);

        lp.gravity = Gravity.LEFT|Gravity.TOP;
        
        if(JzsCircleShortCutViewGroup.DEBUG_MEASURE){
            android.util.Log.i("QsLog", "initViewPosition(2)==x:"+lp.x
                    +"==y:"+lp.y
                    +"==value:"+Integer.toHexString(mAffixPosition)
                    +"==x:"+getAffixPercentX(mAffixPosition)
                    +"==y:"+getAffixPercentY(mAffixPosition));
        }
    }
    
    
    
    private static int getAffixPercentX(int value){
        return (((value>>8)&0xFF)%100);
    }
    private static int getAffixPercentY(int value){
        return ((value&0xFF)%100);
    }
    
    private static int formatAffixPosition(int percentX, int percentY){
        return (((0xFF&percentX) << 8 ) | (percentY&0xFF));
    }
    
    private static int getViewAffixPosition(Context context){
        return android.provider.Settings.System.getInt(context.getContentResolver(), 
                JZS_CIRCLESHORTCUT_KEY_POSITION, formatAffixPosition(0, 55));
    }
    
    /*
     * pos : must be POSITION_TOP POSITION_BOTTOM POSITION_LEFT POSITION_RIGHT
     * perent : 0 - 100;
     */
    public final static void setViewAffixPosition(Context context, int percentX, int percentY){
        setViewAffixPosition(context, formatAffixPosition(percentX, percentY));
    }
    
    private final static void setViewAffixPosition(Context context, int value){
        android.provider.Settings.System.putInt(context.getContentResolver(), 
                JZS_CIRCLESHORTCUT_KEY_POSITION, value);
    }
}
