package com.jzs.android.systemstyle.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.jzs.android.systemstyle.R;

public class PageViewContainer extends ViewGroup implements PageViewIndicatorLister {
	
	private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    private int mCurScreen;

    private int mDefaultScreen = 0;

    private static final int TOUCH_STATE_REST = 0;

    private static final int TOUCH_STATE_SCROLLING = 1;

    private static final int SNAP_VELOCITY = 150;

    private int mTouchState = TOUCH_STATE_REST;

    private int mTouchSlop;

    private float mLastMotionX;

    private float mLastMotionY;

    private int mMaximumVelocity;

    private static final int INVALID_POINTER = -1;

    private int mActivePointerId = INVALID_POINTER;

    private boolean isScrollMode = true;

    private WorkspaceOvershootInterpolator mScrollInterpolator;

    private static final float BASELINE_FLING_VELOCITY = 2500.f;

    private static final float FLING_VELOCITY_INFLUENCE = 0.06f;
    
    private int mScrollCacheWidth = 150;    
    private boolean mIsEnableLoopStyle;
    
    private final int mChildViewLayoutRes;
	
    private PageViewIndicatorCallback mPageViewIndicatorCallback; 
	public void setPageViewIndicatorCallback(PageViewIndicatorCallback callback){
		mPageViewIndicatorCallback = callback;
	}
	
	public int getCurrentPageScreen(){
		return mCurScreen;
	}
	
	public int getPageViewCount(){
		return super.getChildCount();
	}

    public PageViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageViewContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScrollInterpolator = new WorkspaceOvershootInterpolator();
        mScroller = new Scroller(context, mScrollInterpolator);
        
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PageViewContainer, defStyle, 0);
        
        mDefaultScreen = a.getInt(R.styleable.PageViewContainer_defaultScreen, 0);
        mChildViewLayoutRes = a.getResourceId(R.styleable.PageViewContainer_ChildViewLayout, 0);
        
        a.recycle();
        
        mCurScreen = mDefaultScreen;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        
        mScrollCacheWidth = (int)(context.getResources().getDisplayMetrics().density * 150);
    }
    
    public int getChildViewLayoutRes(){
    	return mChildViewLayoutRes;
    }
    
    public void setIsScrollMode(boolean isScrollMode) {
        this.isScrollMode = isScrollMode;
    }
    
    public void setEnableLoopStyle(boolean enable){
    	
    }
    
    public boolean addInScreen(View child, int screen){
    	PageView page = (PageView)super.getChildAt(screen);
    	//if(page == null)
    	
    	return false;
    }
    
    public View addPageScreen(){
    	return addPageScreen(LayoutInflater.from(getContext()), -1);
    }
    
    public View addPageScreen(LayoutInflater inflater){
    	return addPageScreen(inflater, -1);
    }
    
    public View addPageScreen(LayoutInflater inflater, int index){
    	if(mChildViewLayoutRes > 0 && inflater != null){
    		View child = inflater.inflate(mChildViewLayoutRes, this, false);
    		super.addView(child, index);
    		return child;
    	}
    	return null;
    }
    
    public View addPageScreen(View view, int index){
    	if(view != null){
    		super.addView(view, index);
    		return view;
    	}
    	
    	return null;
    }
    
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	final int count = getChildCount();
    	int left = l;// + super.getPaddingLeft();
    	int top = t + super.getPaddingTop();
    	int bottom = b - super.getPaddingBottom();
    	int pagewidth = r - l;
        for (int i = 0; i < count; i++) {
        	final View childView = getChildAt(i);
        	if (childView.getVisibility() != View.GONE) {
        		childView.layout(left + super.getPaddingLeft(), top, left + pagewidth - super.getPaddingRight(), bottom);
        		left += pagewidth;
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
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
        	
        	final View childView = getChildAt(i);
        	final ViewGroup.LayoutParams lp =
                (ViewGroup.LayoutParams) childView.getLayoutParams();
        	
        	int nChildMeasureWidth = 0;
        	if(lp.width > 0)
        		nChildMeasureWidth = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
        	else if(lp.width == ViewGroup.LayoutParams.MATCH_PARENT || lp.width == ViewGroup.LayoutParams.FILL_PARENT)
        		nChildMeasureWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        	else
        		nChildMeasureWidth = MeasureSpec.makeMeasureSpec(childView.getMeasuredWidth(), MeasureSpec.UNSPECIFIED);
        	
        	int nChildMeasureHeight = 0;
        	if(lp.height > 0)
        		nChildMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        	else if(lp.height == ViewGroup.LayoutParams.MATCH_PARENT || lp.height == ViewGroup.LayoutParams.FILL_PARENT)
        		nChildMeasureHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        	else
        		nChildMeasureHeight = MeasureSpec.makeMeasureSpec(childView.getMeasuredHeight(), MeasureSpec.UNSPECIFIED);
        	//android.util.Log.d("QsLog", "onMeasure==i:"+i+"=width:"+childView.getMeasuredWidth()+"==height:"+childView.getMeasuredHeight()+"=lpw:"+lp.width+"=lph:"+lp.height);

        	childView.measure(nChildMeasureWidth, nChildMeasureHeight);
        }
        
        //android.util.Log.d("QsLog", "onMeasure==width:"+width+"==heightMeasureSpec:"+heightMeasureSpec);
        //scrollTo(mCurScreen * width, 0);
    }

    
	public void setToScreen(int whichScreen) {
	     whichScreen = Math.max(0, Math.min(whichScreen, getVisibleChildCount() - 1));
	     mCurScreen = whichScreen;
	     scrollTo(whichScreen * getWidth(), 0);
	}

   public void setToDefaultScreen()
   {
       //android.util.Log.d("QsLog", "QsIphoneToolbarContainer==mDefaultScreen:"+mDefaultScreen+"==mTotalVisiablePageCount:"+mTotalVisiablePageCount);
       setToScreen(mDefaultScreen);
   }

   public int getVisibleChildCount(){
	   int count = super.getChildCount();
	   for(int i=count-1; i>=0; i--){
		   View view = super.getChildAt(i);
		   if(view.getVisibility() == View.GONE)
			   count--;
	   }
       return count;
   }

   public void computeScroll() {
       if (mScroller.computeScrollOffset()) {
           scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
           postInvalidate();
       }
   }

   public boolean onTouchEvent(MotionEvent event) {
	   if(getPageViewCount() <= 1)
		   return super.onTouchEvent(event);
	   
       if (mVelocityTracker == null) {
           mVelocityTracker = VelocityTracker.obtain();
       }
       mVelocityTracker.addMovement(event);
       final int action = event.getAction();
       switch (action) {
           case MotionEvent.ACTION_DOWN:
               if (!mScroller.isFinished()) {
                   mScroller.abortAnimation();
               }
               mTouchState = TOUCH_STATE_SCROLLING;
               mLastMotionX = event.getX();
               mActivePointerId = event.getPointerId(0);
               break;
           case MotionEvent.ACTION_MOVE:
               if (mTouchState == TOUCH_STATE_SCROLLING) {
                   final int pointerIndex = event.findPointerIndex(mActivePointerId);
                   final float x = event.getX(pointerIndex);
                   float deltaX = mLastMotionX - x;
                   mLastMotionX = x;
                   if (deltaX < 0) {
                       if (getScrollX() > 0) {
                           scrollBy((int) Math.max(-getScrollX(), deltaX), 0);
                       }
                       else if(-getScrollX() < mScrollCacheWidth)
                       {
                       		scrollBy((int) Math.max(-mScrollCacheWidth-getScrollX(), deltaX), 0);
                       }
                   } else if (deltaX > 0) {
                       final int availableToScroll = getVisibleChildCount() * getWidth()
                               - getScrollX() - getWidth() + mScrollCacheWidth;
                       //android.util.Log.d("QsLog", "onTouchEvent==ACTION_MOVE==availableToScroll:"+availableToScroll+"==deltaX:"+deltaX+"==scx:"+getScrollX());
                       
                       if (availableToScroll > 0) {

                           scrollBy((int) Math.min(availableToScroll, deltaX), 0);
                       }
                   } else {
                       awakenScrollBars();
                   }
               }
               break;
           case MotionEvent.ACTION_UP:
               if (mTouchState == TOUCH_STATE_SCROLLING) {
                   final VelocityTracker velocityTracker = mVelocityTracker;
                   velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                   final int velocityX = (int) velocityTracker.getXVelocity(mActivePointerId);
                   if (isScrollMode) {
                       final int screenWidth = getWidth();
                       int whichScreen = 0;
                       if(getScrollX() < 0)
                       		whichScreen = 0;
                       else 
                       {
						whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;
						if(whichScreen >= getVisibleChildCount())
							whichScreen = getVisibleChildCount() - 1;
                       }
                       
                       final float scrolledPos = (float) getScrollX() / screenWidth;

                       if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                           // Fling hard enough to move left.
                           // Don't fling across more than one screen at a
                           // time.
                           final int bound = scrolledPos < whichScreen ? mCurScreen - 1
                                   : mCurScreen;
                           snapToScreen(Math.min(whichScreen, bound), velocityX, true);
                       } else if (velocityX < -SNAP_VELOCITY && mCurScreen < getVisibleChildCount() - 1) {
                           // Fling hard enough to move right
                           // Don't fling across more than one screen at a
                           // time.
                           final int bound = scrolledPos > whichScreen ? mCurScreen + 1
                                   : mCurScreen;
                           snapToScreen(Math.max(whichScreen, bound), velocityX, true);
                       } else {
                           snapToScreen(whichScreen, 0, true);
                       }
                   } else {
                       int max = (getVisibleChildCount() - 1) * getWidth();
                       if (velocityX > SNAP_VELOCITY) {
                           mScroller.fling(getScrollX(), 0, -velocityX * 2 / 3, 0, 0, max, 0, 0);
                       } else if (velocityX < -SNAP_VELOCITY) {
                           mScroller.fling(getScrollX(), 0, -velocityX * 2 / 3, 0, 0, max, 0, 0);
                       }
                       if (mVelocityTracker != null) {
                           mVelocityTracker.recycle();
                           mVelocityTracker = null;
                       }
                   }
               }
               mTouchState = TOUCH_STATE_REST;
               mActivePointerId = INVALID_POINTER;
               break;
           case MotionEvent.ACTION_CANCEL:
               mTouchState = TOUCH_STATE_REST;
               mActivePointerId = INVALID_POINTER;
               break;
           case MotionEvent.ACTION_POINTER_UP:
               onSecondaryPointerUp(event);
               break;
       }
       return true;
   }

   public boolean onInterceptTouchEvent(MotionEvent ev) {
	   
	   if(getPageViewCount() <= 1)
		   return super.onInterceptTouchEvent(ev);
	   
       final int action = ev.getAction();
       if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
           return true;
       }

       if (mVelocityTracker == null) {
           mVelocityTracker = VelocityTracker.obtain();
       }
       mVelocityTracker.addMovement(ev);

       switch (action) {
           case MotionEvent.ACTION_MOVE: {
               final int pointerIndex = ev.findPointerIndex(mActivePointerId);
               final float x = ev.getX(pointerIndex);
               final int xDiff = (int) Math.abs(x - mLastMotionX);
               final int touchSlop = mTouchSlop;
               boolean xMoved = xDiff > touchSlop;
               if (xMoved) {
                   mTouchState = TOUCH_STATE_SCROLLING;
               }
           }
               break;
           case MotionEvent.ACTION_DOWN: {
               final float x = ev.getX();
               final float y = ev.getY();
               mActivePointerId = ev.getPointerId(0);
               mLastMotionX = x;
               mLastMotionY = y;
               mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
           }
               break;
           case MotionEvent.ACTION_CANCEL:
           case MotionEvent.ACTION_UP:
               mTouchState = TOUCH_STATE_REST;
               mActivePointerId = INVALID_POINTER;
               if (mVelocityTracker != null) {
                   mVelocityTracker.recycle();
                   mVelocityTracker = null;
               }
               break;
           case MotionEvent.ACTION_POINTER_UP:
               onSecondaryPointerUp(ev);
               break;
       }
       return mTouchState != TOUCH_STATE_REST;
   }

   private void onSecondaryPointerUp(MotionEvent ev) {
       final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
       final int pointerId = ev.getPointerId(pointerIndex);
       if (pointerId == mActivePointerId) {
           // This was our active pointer going up. Choose a new
           // active pointer and adjust accordingly.
           // TODO: Make this decision more intelligent.
           final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
           mLastMotionX = ev.getX(newPointerIndex);
           mLastMotionY = ev.getY(newPointerIndex);
           mActivePointerId = ev.getPointerId(newPointerIndex);
           if (mVelocityTracker != null) {
               mVelocityTracker.clear();
           }
       }
   }

   private void snapToScreen(int whichScreen, int velocity, boolean settle) {
       whichScreen = Math.max(0, Math.min(whichScreen, getVisibleChildCount() - 1));

       View focusedChild = getFocusedChild();
       if (focusedChild != null && whichScreen != mCurScreen
               && focusedChild == getChildAt(mCurScreen)) {
           focusedChild.clearFocus();
       }

       final int screenDelta = Math.max(1, Math.abs(whichScreen - mCurScreen));
       final int newX = whichScreen * getWidth();
       final int delta = newX - getScrollX();
       int duration = (screenDelta + 1) * 100;

       if (!mScroller.isFinished()) {
           mScroller.abortAnimation();
       }

       mCurScreen = whichScreen;

       if (settle) {
           mScrollInterpolator.setDistance(screenDelta);
       } else {
           mScrollInterpolator.disableSettle();
       }

       velocity = Math.abs(velocity);
       if (velocity > 0) {
           duration += (duration / (velocity / BASELINE_FLING_VELOCITY))
                   * FLING_VELOCITY_INFLUENCE;
       } else {
           duration += 32;
       }

       awakenScrollBars(duration);
       mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
       invalidate();
       
       if(mPageViewIndicatorCallback != null)
    	   mPageViewIndicatorCallback.onChangeToScreen(whichScreen);
   }
    
    private static class WorkspaceOvershootInterpolator implements Interpolator {
        private static final float DEFAULT_TENSION = 1.3f;

        private float mTension;

        public WorkspaceOvershootInterpolator() {
            mTension = DEFAULT_TENSION;
        }

        public void setDistance(int distance) {
            mTension = distance > 0 ? DEFAULT_TENSION / distance : DEFAULT_TENSION;
        }

        public void disableSettle() {
            mTension = 0.f;
        }

        public float getInterpolation(float t) {
            // _o(t) = t * t * ((tension + 1) * t + tension)
            // o(t) = _o(t - 1) + 1
            t -= 1.0f;
            return t * t * ((mTension + 1) * t + mTension) + 1.0f;
        }
    }
    
}
