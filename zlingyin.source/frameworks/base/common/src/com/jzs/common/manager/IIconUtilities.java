package com.jzs.common.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.List;

public interface IIconUtilities {
    
    public static enum ScaleMode { NORMAL, FIT_TO_MASK };
    int getIconSize();
    void setIconSize(int size);
    int getIconTextureSize();
    void setIconTextureSize(int size);
    
    void setIconFrameBackgroundArray(int arrayRes);
    
    void setMargeMaskPaint(Paint paint);
    void setDisabledPaint(Paint paint);
    void setSelectedMaskPaint(Paint blurPaint, Paint pressPaint, Paint focusedPaint);
    
    void setScaleMode(ScaleMode mode);
    ScaleMode getScaleMode();
    
    void setMaskResource(int res);
    void setMaskDrawable(Drawable dr);
    void setMaskBitmap(Bitmap bmp);
    Bitmap getMaskBitmap();
    Drawable getIconFrameBackground();
    Drawable getIconFrameBackground(int index);
    void getIconFrameBackgroundList(List<Drawable> list);
    
	Bitmap scaleBitmap(Bitmap bm);
	Bitmap scaleBitmap(Bitmap bm, int w, int h);
	
	Bitmap margeBitmapToMask(Bitmap icon, Bitmap mask);
	Bitmap margeBitmapToMask(Drawable icon, Bitmap mask);
	
	Bitmap createIconBitmapWithMask(Bitmap icon, Bitmap mask, Drawable background);
	Bitmap createIconBitmapWithMask(Bitmap icon, Bitmap mask, Bitmap background, Rect padding);
	Bitmap createIconBitmapWithMask(Drawable icon, Bitmap mask, Drawable background);
	Bitmap createIconBitmapWithMask(Drawable icon, Bitmap mask, Bitmap background, Rect padding);
	
	Bitmap createIconBitmapWithMask(Drawable icon);
	Bitmap createIconBitmapWithMask(Drawable icon, Drawable background);
	Bitmap createIconBitmapWithMask(Bitmap icon);
	Bitmap createIconBitmapWithMask(Bitmap icon, Drawable background);
	
	Bitmap createIconBitmapWithBackground(Drawable icon);
	Bitmap createIconBitmapWithBackground(Bitmap icon);
	
	Bitmap createIconBitmapWithMaskAndBackground(Bitmap icon);
	Bitmap createIconBitmapWithMaskAndBackground(Drawable icon);
	
	Bitmap createIconBitmap(Bitmap icon);
	Bitmap createIconBitmap(Bitmap icon, Drawable background);
	Bitmap createIconBitmap(Bitmap icon, Bitmap background, Rect padding);
	
	Bitmap createIconBitmap(Drawable icon);
	Bitmap createIconBitmap(Drawable icon, Drawable background);
	Bitmap createIconBitmap(Drawable icon, Bitmap background, Rect padding);
	
	void drawSelectedAllAppsBitmap(Canvas dest, int destWidth, int destHeight,
            boolean pressed, Bitmap src);
	
	Bitmap resampleIconBitmap(Bitmap bitmap);
	Bitmap drawDisabledBitmap(Bitmap bitmap);
	
	void ReleaseInstance();
}
