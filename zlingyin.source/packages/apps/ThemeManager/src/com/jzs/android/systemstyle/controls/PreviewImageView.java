package com.jzs.android.systemstyle.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.jzs.android.systemstyle.R;

public class PreviewImageView extends ImageView {
	
//	private Bitmap mCheckOnFlagBmp;
//	private Bitmap mCheckOffFlagBmp;
	private final Drawable mCheckDrawable;
	private final int mIconGravity;
	
	public PreviewImageView(Context context) {
        this(context, null);
    }

    public PreviewImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreviewImageView, defStyle, 0);
        mCheckDrawable = a.getDrawable(R.styleable.PreviewImageView_checkedicon);
        mIconGravity = a.getInt(R.styleable.PreviewImageView_icongravity, Gravity.RIGHT|Gravity.TOP);
        a.recycle();
       
    }
    
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        
        Drawable d = mCheckDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (mCheckDrawable == null) {
            return; // couldn't resolve the URI
        }
        
        int saveCount = canvas.getSaveCount();
        canvas.save();
        
        float dx = super.getWidth() - super.getPaddingRight()/2 - mCheckDrawable.getIntrinsicWidth();
        float dy = super.getPaddingTop()/2;
        canvas.translate(dx, dy);
        mCheckDrawable.draw(canvas);
        
        canvas.restoreToCount(saveCount);
    }
}
