package com.jzs.common.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SlideIndexerBar extends TextView  {
	
	public static char[] AlphabetList = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
		'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
		'X', 'Y', 'Z'}; 
	private int mSelectIndex = -1;
	private SectionIndexer mSectionIndexter = null;
	private OnTouchingPositionChangedListener mOnTouchingLetterChangedListener;
	public interface OnTouchingPositionChangedListener {  
        public void onTouchingPositionChanged(int position);
    }
	
	public SlideIndexerBar(Context context) {
		this(context, null);
	}

	public SlideIndexerBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SlideIndexerBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public void setOnTouchingPositionChangedListener(OnTouchingPositionChangedListener listener) {  
		mOnTouchingLetterChangedListener = listener;
    }

	public void setSectionIndexer(SectionIndexer indexer){
		mSectionIndexter = indexer;
	}
	
	public void onScrollBarPostionChanged(ListView listview){
		onScrollBarPostionChanged(listview.getFirstVisiblePosition());
	}
	
	public void onScrollBarPostionChanged(int position){
		if(mSectionIndexter == null)
			return;
		int index = -1;
		//int pos = listview.getFirstVisiblePosition();
		if(position >= 0){
			int section = mSectionIndexter.getSectionForPosition(position);
			
			if(section >= 0){
				Object[] obj = mSectionIndexter.getSections();
				if(obj != null && section < obj.length){
					String sectionstr = String.valueOf(obj[section]);
					
					for (int i = 0; i < AlphabetList.length; i++) {
						if (compareAlphabetCharecter(i, sectionstr) >= 0) {
							index = i;
							break;
						}
					}
				}
				
//				android.util.Log.v("QsLog", "onScrollBarPostionChanged() ==position:"+position
//						+", section:"+section
//						+", sectionstr:"+sectionstr
//						+", index:"+index);
			}
		}
		
		if(index != mSelectIndex){
			mSelectIndex = index;
			postInvalidate();
		}
	}

	private int compareAlphabetCharecter(int index, String str){
		if(index >= 0){
			if(str != null && str.length() > 0){
				char cur = str.charAt(0);
				if((AlphabetList[index] == cur) || ((AlphabetList[index]+32) == cur)){
					return 0;
				}
				
				return AlphabetList[index] - cur;
			}
			
			return 1;
		} 
		if(str == null || str.length() == 0){
			return 0;
		}
		return -1;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			
			final int y = (int)event.getY();
			
			int selected = (int) (y * AlphabetList.length / getHeight());
			if (selected >= AlphabetList.length) {
				selected = AlphabetList.length - 1;
			} else if (selected < 0) {
				selected = 0;
			}
			
			if(mSectionIndexter == null || mSelectIndex == selected)
				return true;

//			android.util.Log.v("QsLog", "onTouchEvent() ==selected:"+selected
//					+", mSelectIndex:"+mSelectIndex
//					+", y:"+y
//					+", height:"+getHeight());
			
			int position = -1;
			Object[] obj = mSectionIndexter.getSections();
			if(obj != null){
				for (int i = 0; i < obj.length; i++) {
					if (compareAlphabetCharecter(selected, String.valueOf(obj[i])) <= 0) {
						position = i;
						break;
					}
				}
//				
//				android.util.Log.i("QsLog", "onTouchEvent(3) ==selected:"+selected
//						+", position:"+position);
				
				position = mSectionIndexter.getPositionForSection(position);
				if (position == -1) {
					return true;
				}
				if(mOnTouchingLetterChangedListener != null)
					mOnTouchingLetterChangedListener.onTouchingPositionChanged(position);
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		int height = getHeight();
        int width = getWidth();
		if(width <= 0 || height <= 0)
			return;
		
		final Drawable dr = super.getBackground();
		if(dr != null){
			dr.setBounds(0, 0, width, height);
			dr.draw(canvas);
		}
		ColorStateList colorlist = super.getTextColors();
		int defaultcolor = colorlist != null ? colorlist.getDefaultColor() : Color.BLACK;
		int forcecolor = (defaultcolor&0x000F0F0F);
		if(colorlist != null)
			forcecolor = colorlist.getColorForState(View.FOCUSED_SELECTED_STATE_SET, forcecolor);
		
		Paint paint = getPaint();
		final Paint.Align txtalign = paint.getTextAlign();
		int singleHeight = height / AlphabetList.length;
		width = width - getPaddingLeft() - getPaddingRight();
		for (int i = 0; i < AlphabetList.length; i++) {
			String charetc = String.valueOf(AlphabetList[i]);
            if (i == mSelectIndex) {  
          		paint.setColor(forcecolor);
                paint.setFakeBoldText(true);
            } else {
            	paint.setColor(defaultcolor);
            	paint.setFakeBoldText(false);
            }
            float xPos = getPaddingLeft() + width / 2 - paint.measureText(charetc) / 2;;
//            if(txtalign == Paint.Align.CENTER)
//            	xPos += width / 2 - paint.measureText(charetc) / 2;
//            else if(txtalign == Paint.Align.RIGHT)
//            	xPos += width - paint.measureText(charetc);
            
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(charetc, xPos, yPos, paint);
        }  
		
	}
}
