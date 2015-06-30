package com.jzs.internal.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jzs.internal.R;

public class JzsCircleShortCutViewGroup extends ViewGroup {
    public final static boolean ENABLE_DEBUG = false;
    public final static boolean DEBUG_MEASURE = ENABLE_DEBUG&true;
    public final static boolean DEBUG_LAYOUT = ENABLE_DEBUG&true;
    public final static boolean DEBUG_MOTION = ENABLE_DEBUG&false;
    public final static boolean DEBUG_KEY = ENABLE_DEBUG&false;
    
    public static interface ShortCutViewGroupCallback{
        boolean onChildClicked(View v, String action);
    }

    private final int mRadiusPadding;
    private ShortCutViewGroupCallback mCallback;

    public JzsCircleShortCutViewGroup(Context context) {
        this(context, null);
    }

    public JzsCircleShortCutViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JzsCircleShortCutViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
//        mMode = MODE_SMALL_LINK;
//        mAffixPosition = getViewAffixPosition(context);
//        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        
        Resources r = context.getResources();
        TypedArray a = context.obtainStyledAttributes(
                        attrs, R.styleable.JzsCircleShortCutViewGroup, defStyle, 0);
        
        mRadiusPadding = a.getDimensionPixelSize(R.styleable.JzsCircleShortCutViewGroup_radiusPadding, 40);
        //mModeBackground[MODE_SMALL_LINK] = a.getDrawable(R.styleable.JzsCircleShortCutContainer_smallBackground);
        //mModeBackground[MODE_LARGE] = a.getDrawable(R.styleable.JzsCircleShortCutContainer_largeBackground);
        a.recycle();
        
        if(ENABLE_DEBUG){
            android.util.Log.d("QsLog", "JzsCircleShortCutViewGroup(0)=="
                    +"==mRadiusPadding:"+mRadiusPadding);
        }
    }
    
    public void setShortCutViewGroupCallback(ShortCutViewGroupCallback callback){
        mCallback = callback;
    }
    
    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        if(ENABLE_DEBUG){
            android.util.Log.i("QsLog", "ViewGroup::onAttachedToWindow(0)==width:"+super.getWidth()
                    +"==heightSize:"+super.getHeight());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        
        if(ENABLE_DEBUG){
            android.util.Log.i("QsLog", "ViewGroup::onDetachedFromWindow(0)==width:"+super.getWidth()
                    +"==heightSize:"+super.getHeight());
        }
    }
    
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        if(JzsCircleShortCutViewGroup.ENABLE_DEBUG){
            android.util.Log.i("QsLog", "ViewGroup::onFinishInflate(0)==width:"+super.getWidth()
                    +"==heightSize:"+super.getHeight());
        }
        
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);
        
        if (widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            Drawable dr = super.getBackground();
            if(dr == null)
                throw new IllegalStateException("JzsCircleShortCutContainer only can run at EXACTLY mode!");
            
            widthSize = dr.getIntrinsicWidth();
            heightSize = dr.getIntrinsicHeight();

            if(JzsCircleShortCutViewGroup.DEBUG_MEASURE){
                android.util.Log.i("QsLog", "ViewGroup::onMeasure(0)==widthSize:"+widthSize
                        +"==heightSize:"+heightSize
                        +"==width and height mode error!");
            }
            setMeasuredDimension(widthSize, heightSize);
        }
        
        int width = widthSize - getPaddingLeft() - getPaddingRight();
        int height = heightSize - getPaddingTop() - getPaddingBottom();
        
        if(JzsCircleShortCutViewGroup.DEBUG_MEASURE){
            android.util.Log.i("QsLog", "ViewGroup::onMeasure(1)==widthSize:"+widthSize
                    +"==heightSize:"+heightSize
                    +"==width:"+width
                    +"==height:"+height);
        }
        
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {          
            final View childView = getChildAt(i);
            final ViewGroup.LayoutParams lp =
                (ViewGroup.LayoutParams) childView.getLayoutParams();
            
            int nChildMeasureWidth = 0;
            if(lp.width > 0)
                nChildMeasureWidth = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            else if(lp.width == ViewGroup.LayoutParams.MATCH_PARENT)
                nChildMeasureWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            else
                nChildMeasureWidth = MeasureSpec.makeMeasureSpec(childView.getMeasuredWidth(), MeasureSpec.UNSPECIFIED);
            
            int nChildMeasureHeight = 0;
            if(lp.height > 0)
                nChildMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            else if(lp.height == ViewGroup.LayoutParams.MATCH_PARENT)
                nChildMeasureHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            else
                nChildMeasureHeight = MeasureSpec.makeMeasureSpec(childView.getMeasuredHeight(), MeasureSpec.UNSPECIFIED);
//            if(JzsCircleShortCutViewGroup.DEBUG_MEASURE){
//                android.util.Log.d("QsLog", "onMeasure==i:"+i
//                        +"=width:"+childView.getMeasuredWidth()
//                        +"==height:"+childView.getMeasuredHeight()
//                        +"=lpw:"+lp.width+"=lph:"+lp.height);
//            }

            childView.measure(nChildMeasureWidth, nChildMeasureHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

        
        final int width = r - l;
        final int height = b - t;
        final int centerX = width / 2;
        final int centerY = height / 2;
        final int radius = Math.min(centerX, centerY) - mRadiusPadding;
        if(JzsCircleShortCutViewGroup.DEBUG_LAYOUT){
            android.util.Log.i("QsLog", "ViewGroup::onLayout()==changed:"+changed
                    +"==l:"+l
                    +"==t:"+t
                    +"==r:"+r
                    +"==b:"+b
                    +"==width:"+width
                    +"==height:"+height
                    +"==centerX:"+centerX
                    +"==centerY:"+centerY
                    +"==radius:"+radius);
        }
        
//        if(!changed){
//            return;
//        }
        
        final int count = super.getChildCount();
        for(int i=0; i<count; i++){
            final View view = getChildAt(i);
            final int childWidth = view.getMeasuredWidth();
            final int childHeight = view.getMeasuredHeight();
            if(childWidth == 0 || childHeight == 0)
                break;
            
            final int degress = ((JzsShortcutViewInterface)view).getPositionDegress();
            final double deg = Math.PI * degress / 180.0;
            int x = centerX + (int)(Math.sin(deg) * radius);
            int y = centerY - (int)(Math.cos(deg) * radius);
            
            int left = x - childWidth/2;
            int top = y - childHeight/2;
            
            if(JzsCircleShortCutViewGroup.DEBUG_LAYOUT){
                android.util.Log.i("QsLog", "ViewGroup::onLayout(2)==i:"+i
                        +"==degress:"+degress
                        +"==deg:"+deg
                        +"==x:"+x
                        +"==y:"+y
                        +"==left:"+left
                        +"==top:"+top
                        +"==w:"+childWidth
                        +"==h:"+childHeight
                        +"==sin:"+String.format("%.3g", Math.sin(deg))
                        +"==sinh:"+String.format("%.3g", Math.sinh(deg))
                        +"==cos:"+String.format("%.3g", Math.cos(deg))
                        );
            }

            view.layout(left, top, left + childWidth, top + childHeight);
        }
        
    }
    
}
