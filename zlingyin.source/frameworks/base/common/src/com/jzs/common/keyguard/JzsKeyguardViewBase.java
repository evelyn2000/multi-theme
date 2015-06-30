package com.jzs.common.keyguard;

import com.jzs.utils.ConfigOption;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public abstract class JzsKeyguardViewBase extends FrameLayout {
	public static final int VIEW_BACKGROUND_COLOR = 0x70000000;
	
	private static final Drawable mBackgroundDrawable = new Drawable() {
        @Override
        public void draw(Canvas canvas) {
            canvas.drawColor(VIEW_BACKGROUND_COLOR, PorterDuff.Mode.SRC);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    };
    
	public JzsKeyguardViewBase(Context context) {
        this(context, null);
    }

    public JzsKeyguardViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    protected final boolean isSupportBackgroundMergeFilter(){
    	if(ConfigOption.QS_PRJ_NAME.startsWith("A802")){
    		return false;
    	}
    	return false;
    }
    
    public void setCustomBackground(Drawable dr) {
    	if(dr != null){
    		setBackground(dr);
    	} else if(isSupportBackgroundMergeFilter()){
    		setBackground(mBackgroundDrawable);
    	}
    }
    /**
     * Called when the screen turned off.
     */
    public abstract void onScreenTurnedOff();

    /**
     * Called when the screen turned on.
     */
    public abstract void onScreenTurnedOn();

    /**
     * Called when the view needs to be shown.
     */
    public abstract void show();

    /**
     * Verify that the user can get past the keyguard securely.  This is called,
     * for example, when the phone disables the keyguard but then wants to launch
     * something else that requires secure access.
     *
     * The result will be propogated back via {@link KeyguardViewCallback#keyguardDone(boolean)}
     */
    public abstract void verifyUnlock();

    /**
     * Called before this view is being removed.
     */
    public abstract void cleanUp();

    /**
     * Gets the desired user activity timeout in milliseconds, or -1 if the
     * default should be used.
     */
    public abstract long getUserActivityTimeout();

    
    public abstract void setJzsViewMediatorCallback(IJzsViewMediatorCallback viewMediatorCallback);
    
    public void dismiss(){
    	//dismiss(false);
    }

    public void dismiss(boolean authenticated) {
    }
    
    public void showAssistant(){
    	
    }
    public void dispatch(MotionEvent event){
    	
    }
    public void launchCamera(){
    	
    }
    public void ipoShutDownUpdate(){
    	
    }
    
    public boolean handleBackKey(){
    	return false;
    }
    
    public boolean handleMenuKey(){
    	return false;
    }
    
    public void goToWidget(int appWidgetId){
    	
    }
    
    public boolean isCurrentSimPinPukView(){
        return false;
    }

    public void setShowNextViewFlag(boolean showNextView) {
    }
    
    public void initializeSwitchingUserState(boolean switching){
    	
    }

    public void setJzsKeyguardSecurityModel(JzsKeyguardSecurityModel securitymodel, JzsKeyguardUpdateMonitor monitor) {
    	//mJzsKeyguardSecurityModel = securitymodel;
    }

    public boolean isDefaultStyle(){
    	return true;
    }
    
    public boolean isJzsFullScreenStyle(){
		return false;
	}
    
    public boolean isJzsEnableScreenRotation(){
    	return false;
    }
    
    public boolean isJzsEnableSystemWallpaper(){
    	return true;
    }
    // add for silead
    public void showAutoDismissDialog(String message, int timeout) {
    }
}
