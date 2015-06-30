package com.jzs.android.systemstyle.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jzs.android.systemstyle.R;

public class PageView extends ViewGroup {
	public final static int DIRECTION_VERTICAL = 0;
	public final static int DIRECTION_HORIZONTAL = 1;
	
	private final int mDirection;
	private final int mCellCount;
	private final int mRowCount;
	private final int mChildViewLayoutRes;
	
	public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PageView, defStyle, 0);
        
        mCellCount = Math.max(1, a.getInt(R.styleable.PageView_CellCount, 2));
        mRowCount = Math.max(1, a.getInt(R.styleable.PageView_RowCount, 2));
        mDirection = a.getInt(R.styleable.PageView_direction, DIRECTION_HORIZONTAL);
        
        mChildViewLayoutRes = a.getResourceId(R.styleable.PageView_ChildViewLayout, 0);
        
        a.recycle();
    }
    
    public int getRowCount(){
    	return mRowCount;
    }
    
    public int getCellCount(){
    	return mCellCount;
    }
    
    public int getMaxChildSize(){
    	return getRowCount() * getCellCount();
    }
    
    public boolean hasEmptyCell(){
    	return (super.getChildCount() < getMaxChildSize());
    }
    
    public View addChildItem(){
    	return addChildItem(LayoutInflater.from(getContext()), -1);
    }
    
    public View addChildItem(LayoutInflater inflater){
    	return addChildItem(inflater, -1);
    }
    
    public View addChildItem(LayoutInflater inflater, int index){
    	if(mChildViewLayoutRes > 0 && inflater != null && hasEmptyCell()){
    		View child = inflater.inflate(mChildViewLayoutRes, this, false);
    		super.addView(child, index);
    		return child;
    	}
    	return null;
    }
    
    public View addChildItem(View view, int index){
    	if(view != null && hasEmptyCell()){
    		super.addView(view, index);
    		return view;
    	}
    	
    	return null;
    }
    
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	final int count = getChildCount();
    	final int left = l + super.getPaddingLeft();
    	final int right = r - super.getPaddingRight();
    	final int top = t + super.getPaddingTop();
    	final int bottom = b - super.getPaddingBottom();
    	int x, y;
        for (int i = 0; i < count; i++) {
        	final View childView = getChildAt(i);
        	if (childView.getVisibility() != View.GONE) {
        		final int childwidth = childView.getMeasuredWidth();
        		final int childheight = childView.getMeasuredHeight();
        		if(mDirection == DIRECTION_HORIZONTAL){
        			x = (i%mCellCount) * childwidth;
        			y = (i/mRowCount) * childheight;
        		} else {
        			x = (i/mCellCount) * childwidth;
        			y = (i%mRowCount) * childheight;
        		}
        		x += left;
        		y += top;
        		childView.layout(x, y, x + childwidth, y + childheight);
        	}
        }
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");
        }
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        }
        final int validwidth = width - super.getPaddingLeft() - super.getPaddingRight();
        final int validheight = height - super.getPaddingTop() - super.getPaddingBottom();
        
        final int childwidth = validwidth / mCellCount;
        final int childheight = validheight / mRowCount;
        
        final int nChildMeasureWidth = MeasureSpec.makeMeasureSpec(childwidth, MeasureSpec.EXACTLY);
    	final int nChildMeasureHeight = MeasureSpec.makeMeasureSpec(validheight, MeasureSpec.EXACTLY);
    	
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
        	final View childView = getChildAt(i);
        	childView.measure(nChildMeasureWidth, nChildMeasureHeight);
        }
    }

    
}
