/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 * 
 * MediaTek Inc. (C) 2010. All rights reserved.
 * 
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.jzs.android.systemstyle.controls;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import android.view.View;

import com.jzs.android.systemstyle.R;

/**
 * M: new added class for new common control BookmarkView.
 * 
 *  A view maintain a list of image, title and detail informations, the image
 *  will be contained in a CoverFlow. When user move or fling the CoverFlow, the 
 *  title and detail information will also be updated.
 */
public class BookmarkView extends FrameLayout implements PageViewIndicatorLister {
    private static final String TAG = "BookmarkView";
    private static final boolean DBG = false;

    private static final float DEFAULT_REFLECTION = 0.25f;
    private static final float DEFAULT_MAX_ZOOM = 400.0f;

    private float mMaxZoom = DEFAULT_MAX_ZOOM;
    private float mImageReflection = DEFAULT_REFLECTION;
    
    private int mInfoColor = Color.WHITE;
    private int mTitleColor = Color.WHITE;
    
    /**
     * Horizontal spacing between items.
     */
    private int mSpaceBetweenItems;
    private int mSpaceBetweenIndicators;

    private int mImageDispWidth;
    private int mImageDispHeight;
    
    private int mImageDispSpaceHor;
    private int mImageDispSpaceVer;

    private int mItemCount;
    private int mOldItemCount;
    private int mCurrentSelectedPosition = -1;
    private LayoutInflater mInflater;
    private TextView mTitleView;
    private TextView mInfoView;
    private BounceCoverFlow mCoverflow;
    private PageViewIndicator mPageViewIndicator;
    
    private BookmarkAdapter mBookmarkAdapter;
    /* Bookmark data set observer to watch and handle adapter data change. */
    private BookmarkDataSetObserver mBookmarkDataSetObserver;
    private List<ImageView> mRecycledIndicators = new ArrayList<ImageView>();

    public BookmarkView(Context context) {
        this(context, null);
    }

    public BookmarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookmarkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                	R.styleable.BookmarkView);
        final Resources resources = context.getResources();
        mImageDispWidth = a.getDimensionPixelSize(R.styleable.BookmarkView_imageDispWidth,
                resources.getDimensionPixelSize(R.dimen.bookmark_bitmap_width));
        mImageDispHeight = a.getDimensionPixelSize(R.styleable.BookmarkView_imageDispHeight,
                resources.getDimensionPixelSize(R.dimen.bookmark_bitmap_height));

        mSpaceBetweenIndicators = a.getDimensionPixelSize(R.styleable.BookmarkView_dotGap, 
                resources.getDimensionPixelSize(R.dimen.bookmark_dot_gap));
        mSpaceBetweenItems = a.getDimensionPixelSize(R.styleable.BookmarkView_spaceBetweenItems, 
                resources.getDimensionPixelSize(R.dimen.bookmark_spacing));
        
        mImageDispSpaceHor = a.getDimensionPixelSize(R.styleable.BookmarkView_spaceDispHorSize, 
        		resources.getDimensionPixelSize(R.dimen.bookmark_disp_spacing_hor));
        
        mImageDispSpaceVer = a.getDimensionPixelSize(R.styleable.BookmarkView_spaceDispVerSize, 
        		resources.getDimensionPixelSize(R.dimen.bookmark_disp_spacing_ver));

        mMaxZoom = a.getFloat(R.styleable.BookmarkView_maxZoomOut, DEFAULT_MAX_ZOOM);
        mImageReflection = a.getFloat(
                R.styleable.BookmarkView_imageReflection, DEFAULT_REFLECTION);

        mTitleColor = a.getColor(R.styleable.BookmarkView_titleColor,
                Color.WHITE);
        mInfoColor = a.getColor(R.styleable.BookmarkView_infoColor,
                Color.WHITE);

        a.recycle();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.bookmarkview_layout, this, true);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        mTitleView = (TextView) findViewById(R.id.bookmarkTitle);
        mInfoView = (TextView) findViewById(R.id.bookmarkInfo);

        mCoverflow = (BounceCoverFlow) findViewById(R.id.bookmarkCoverflow);
        mCoverflow.setGravity(Gravity.CENTER_VERTICAL);
        
        mPageViewIndicator = (PageViewIndicator)super.findViewWithTag("paged_view_indicator");
        if(mPageViewIndicator != null){
        	mPageViewIndicator.initial(getContext(), this);
        }

        mCoverflow.setSpacing(mSpaceBetweenItems);
        mCoverflow.setMaxZoomOut(mMaxZoom);
        mTitleView.setTextColor(mTitleColor);
        mTitleView.setTextColor(mInfoColor);
        
        //mCoverflow.setOnItemSelectedListener(new )

        //mCoverflow.setOnSelectionChangeListener(mSelectionChangeListener);
        
        mCoverflow.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
        	@Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view,
                    int position, long id) {
                //mSelectedPos = position;
        		
        		if(mPageViewIndicatorCallback != null)
                	mPageViewIndicatorCallback.onChangeToScreen(position);
        		
        		refreshInfo(false);
        		//refreshIndicators();
        		
        		if(mSelectionChangeListener != null)
        			mSelectionChangeListener.onSelectionChanged(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Indicate nothing selected when we click the "set" button and
            	
            	if(mPageViewIndicatorCallback != null)
                	mPageViewIndicatorCallback.onChangeToScreen(-1);
            	
            	refreshInfo(false);
        		//refreshIndicators();
        		
            	if(mSelectionChangeListener != null)
        			mSelectionChangeListener.onSelectionChanged(-1);
            }
        });
        
        
    }

    /**
     * Set adapter to bookmark view, set the image display size and reflection
     * which used in bookmark adapter.
     * 
     * @param adapter
     */
    public void setBookmarkAdapter(final BookmarkAdapter adapter) {
        if (DBG) {
            Log.d(TAG, "setBookmarkAdapter adapter = " + adapter + ",mBookmarkDataSetObserver = "
                    + mBookmarkDataSetObserver);
        }
        if (null != mBookmarkAdapter) {
            mBookmarkAdapter.unregisterDataSetObserver(mBookmarkDataSetObserver);
        }

        mCoverflow.setAdapter(adapter);
        mBookmarkAdapter = adapter;
        if (null != mBookmarkAdapter) {
            mBookmarkDataSetObserver = new BookmarkDataSetObserver();
            mBookmarkAdapter.registerDataSetObserver(mBookmarkDataSetObserver);
            mBookmarkAdapter.setImageDispSize(mImageDispWidth, mImageDispHeight);
            mBookmarkAdapter.setImageReflection(mImageReflection);

            mOldItemCount = mItemCount;
            mItemCount = mBookmarkAdapter.getCount();
            if(mPageViewIndicatorCallback != null)
            	mPageViewIndicatorCallback.onPageCountChanged(mItemCount);
        }
    }
    
    private PageViewIndicatorCallback mPageViewIndicatorCallback; 
	public void setPageViewIndicatorCallback(PageViewIndicatorCallback callback){
		mPageViewIndicatorCallback = callback;
	}
	
	public int getCurrentPageScreen(){
		return getCurrentPosition();
	}
	
	public int getPageViewCount(){
		return getItemCount();
	}
	
    public void setSelection(int position){
    	if(mCoverflow != null)
    		mCoverflow.setSelection(position);
    }
    
    public void setEmptyView(View emptyView){
    	if(mCoverflow != null)
    		mCoverflow.setEmptyView(emptyView);
    }
    
    public void setMaxZoomOut(float maxZoom){
    	if(mCoverflow != null)
    		mCoverflow.setMaxZoomOut(maxZoom);
    }

    /**
     * Sets the spacing between items in a cover flow.
     * 
     * @param spacing The spacing in pixels between items in the BounceGallery.
     */
    public void setCoverFlowSpacing(final int spacing) {
        mCoverflow.setSpacing(spacing);
    }

    /**
     * Set the max zoom out to transform cover flow images.
     * 
     * @param maxZoomout The max zoom out.
     */
    public void setCoverFlowMaxZoomOut(final float maxZoomout) {
        mCoverflow.setMaxZoomOut(maxZoomout);
    }

    /**
     * Set the displayed size of the image in cover flow.
     * 
     * @param dispWidth the width of the view to display image in cover flow.
     * @param dispHeight the height of the view to display image in cover flow.
     */
    public void setImageDispSize(final int dispWidth, final int dispHeight) {
        mImageDispWidth = dispWidth;
        mImageDispHeight = dispHeight;
        if (null != mBookmarkAdapter) {
            mBookmarkAdapter.setImageDispSize(dispWidth, dispHeight);
        }
    }

    /**
     * Set the reflection ratio of the image displayed in cover flow.
     * 
     * @param reflection
     */
    public void setImageReflection(final float reflection) {
        if (null != mBookmarkAdapter) {
            mBookmarkAdapter.setImageReflection(reflection);
        }
    }

    /**
     * Set overscroll and overfling distance.
     * 
     * @param distance
     */
    public void setGalleryOverScrollDistance(final int distance) {
        //mCoverflow.setOverScrollDistance(distance);
    }

    /**
     * Get the cover flow view of this bookmark.
     * 
     * @return
     */
//    public BounceCoverFlow getCoverFlow() {
//        return mCoverflow;
//    }

    /**
     * Get the title view of the bookmark.
     * 
     * @return the title view.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Get the information view of the bookmark.
     * 
     * @return the information view.
     */
    public TextView getInfoView() {
        return mInfoView;
    }

    /**
     * Get layout of indicators.
     * @return
     */
//    public ViewGroup getIndicatorsLayout() {
//        return mIndicators;
//    }
    
    /**
     * Get item count of bookmark items in adapter.
     * 
     * @return the item count of bookmark items.
     */
    public int getItemCount() {
        return mItemCount;
    }
    
    /**
     * Get current selected position of bookmark.
     * 
     * @return current selected position.
     */
    public int getCurrentPosition() {
        return mCurrentSelectedPosition;
    }

    /**
     * This function used to refresh information, called when the selection
     * changed or user force update, used to refresh the title and info string.
     * 
     * @param force whether force to update.
     */
    public void refreshInfo(boolean force) {
        if (null != mBookmarkAdapter) {
            // Clear title and info text if data invalid.
            if (mBookmarkAdapter.getCount() == 0) {
                Log.d(TAG, "refreshInfo and data invalid.");
                mTitleView.setText("");
                mInfoView.setText("");
                mCurrentSelectedPosition = 0;
                return;
            }

            int selectedPos = mCoverflow.getSelectedItemPosition();
            if (DBG) {
            	Log.d(TAG, "refreshInfo: new selectedPos = " + selectedPos
                        + ", old selected pos = " + mCurrentSelectedPosition + ",force = " + force);
            }
            if (force || mCurrentSelectedPosition != selectedPos) {
                final BookmarkItem item = (BookmarkItem) mBookmarkAdapter.getItem(selectedPos);
                if(item != null){
	                mTitleView.setText(item.mTitle);
	                mInfoView.setText(item.mInfo);
                }
                mCurrentSelectedPosition = selectedPos;
            }
        }
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = mCoverflow.getMeasuredWidth();//MeasureSpec.getSize(widthMeasureSpec);
        final int height = mCoverflow.getMeasuredHeight();//MeasureSpec.getSize(heightMeasureSpec);
        
        if(width <= 0 || height <= 0)
        	return;
        
        int dspw = width - super.getPaddingLeft() - super.getPaddingRight() - mImageDispSpaceHor * 2;
        int dsph = height - super.getPaddingTop() - super.getPaddingBottom() - mImageDispSpaceVer * 2;
        
        if (DBG) {
            Log.d(TAG, "Bookmark::onMeasure()===width:" + width + "==height:"+height
            		+"==new dsp:("+dspw +","+dsph+")"
            		+"==old dsp:("+mImageDispWidth +","+mImageDispHeight+")"
            		+"==space:("+mImageDispSpaceHor +","+mImageDispSpaceVer+")"
            		+"==mCoverflow:("+mCoverflow.getMeasuredWidth() +","+mCoverflow.getMeasuredHeight()+")");
        }
        
        if(dspw != mImageDispWidth || dsph != mImageDispHeight){
        	setImageDispSize(dspw, dsph);
        	setCoverFlowSpacing(-(int) (0.25f * dspw + 0.5f));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (DBG) {
        	Log.d(TAG, "onAttachedToWindow:mBookmarkAdapter = " + mBookmarkAdapter
                    + ",mBookmarkDataSetObserver = " + mBookmarkDataSetObserver);
        }        
        if (null != mBookmarkAdapter && null == mBookmarkDataSetObserver) {
            mBookmarkDataSetObserver = new BookmarkDataSetObserver();
            mBookmarkAdapter.registerDataSetObserver(mBookmarkDataSetObserver);

            // Data may have changed while we were detached, update it.
            mOldItemCount = mItemCount;
            mItemCount = mBookmarkAdapter.getCount();
            if(mPageViewIndicatorCallback != null)
            	mPageViewIndicatorCallback.onPageCountChanged(mItemCount);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (DBG) {
        	Log.d(TAG, "onAttachedToWindow:mBookmarkAdapter = " + mBookmarkAdapter
                    + ",mRecycledIndicators.size = " + mRecycledIndicators.size());
        }
        // Detach all views remain in the recycled list.
        mRecycledIndicators.clear();

        if (null != mBookmarkAdapter) {
            mBookmarkAdapter.unregisterDataSetObserver(mBookmarkDataSetObserver);
            mBookmarkDataSetObserver = null;
        }
    }

    final class BookmarkDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            mBookmarkAdapter.clearBitmapCache();
            mOldItemCount = mItemCount;
            mItemCount = mBookmarkAdapter.getCount();
            if(mPageViewIndicatorCallback != null)
            	mPageViewIndicatorCallback.onPageCountChanged(mItemCount);
            if (DBG) {
            	Log.d(TAG, "Bookmark data changes: mItemCount = " + mItemCount + ",mOldItemCount = "
                        + mOldItemCount + ",mCurrentSelectedPosition = " + mCurrentSelectedPosition);
            }

            // If the current selected position exceed the total count, set it
            // to the last one.
            if (mCurrentSelectedPosition > mItemCount - 1) {
               // mCoverflow.setNextSelectedPositionInt(mItemCount - 1);
            	mCoverflow.setSelection(mItemCount - 1);
            }

            /*
             * Since maybe only bookmark item like title change, we need to
             * force refresh informations even the number of data didn't change.
             */
            refreshInfo(true);
        }

        @Override
        public void onInvalidated() {
            if (DBG) {
            	Log.d(TAG, "Bookmark data invalidate:mItemCount = " + mItemCount
                        + ",mOldItemCount = " + mOldItemCount);
            }
            mBookmarkAdapter.clearBitmapCache();
            refreshInfo(true);
        }
    }
    
    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        mSelectionChangeListener = listener;
    }
    private OnSelectionChangeListener mSelectionChangeListener;
    public static interface OnSelectionChangeListener {
        /**
         * Called when the selection changes.
         */
        void onSelectionChanged(int selected);
    }
}
