/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jzs.internal.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import com.jzs.internal.R;

import java.text.SimpleDateFormat;
import android.widget.ImageView;

/**
 * Displays the time
 */
public class DateClockView extends RelativeLayout {
    private static final String ANDROID_CLOCK_FONT_FILE = "/system/fonts/AndroidClock.ttf";
    private final static String M12 = "h:mm";
    private final static String M24 = "kk:mm";

    private Calendar mCalendar;
    private String mFormat;
    private TextView mTimeView;
    private AmPm mAmPm;
    private ContentObserver mFormatChangeObserver;
    private int mAttached = 0; // for debugging - tells us whether attach/detach is unbalanced
    private TextView mDateView;

    /* called by system on minute ticks */
    private final Handler mHandler = new Handler();
    private BroadcastReceiver mIntentReceiver;

    // jz add
    private boolean mIsImageStyle = false;
    private final static int QS_HOUR_FIRST_INDEX = 0;
    private final static int QS_HOUR_LAST_INDEX = 1;
    private final static int QS_MINUTE_FIRST_INDEX = 2;
    private final static int QS_MINUTE_LAST_INDEX = 3;
    private ImageView [] mImgGroup = new ImageView[4];
    
    private String mFontFile;
    // jz add end
    private static class TimeChangedReceiver extends BroadcastReceiver {
        private WeakReference<DateClockView> mClock;
        private Context mContext;

        public TimeChangedReceiver(DateClockView clock) {
            mClock = new WeakReference<DateClockView>(clock);
            mContext = clock.getContext();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Post a runnable to avoid blocking the broadcast.
            final boolean timezoneChanged =
                    intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED);
            final DateClockView clock = mClock.get();
            if (clock != null) {
                clock.mHandler.post(new Runnable() {
                    public void run() {
                        if (timezoneChanged) {
                            clock.mCalendar = Calendar.getInstance();
                        }
                        clock.updateTime();
                    }
                });
            } else {
                try {
                    mContext.unregisterReceiver(this);
                } catch (RuntimeException e) {
                    // Shouldn't happen
                }
            }
        }
    };

    static class AmPm {
        private TextView mAmPmTextView;
        private String mAmString, mPmString;

        private ImageView mAmPmImageView;

        AmPm(View parent, Typeface tf){
            this(parent, tf, false);
        }

        AmPm(View parent, Typeface tf, boolean isImgStyle) {
            if(isImgStyle)
                mAmPmImageView = (ImageView)parent.findViewWithTag("am_pm");
            
            if(mAmPmImageView == null){
                mAmPmTextView = (TextView) parent.findViewById(R.id.am_pm);
                if(mAmPmTextView == null)
                	mAmPmTextView = (TextView) parent.findViewWithTag("am_pm");
            }
            
            if (mAmPmTextView != null && tf != null) {
                mAmPmTextView.setTypeface(tf);
            }

            String[] ampm = new DateFormatSymbols().getAmPmStrings();
            mAmString = ampm[0];
            mPmString = ampm[1];
        }

        void setShowAmPm(boolean show) {
            if (mAmPmTextView != null) {
                mAmPmTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            }

            if(mAmPmImageView != null){
                mAmPmImageView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }

        boolean isShowAmPm(){
            if (mAmPmTextView != null) {
                return (mAmPmTextView.getVisibility() == View.VISIBLE ? true : false);
            }

            if(mAmPmImageView != null){
                return (mAmPmImageView.getVisibility() == View.VISIBLE ? true : false);
            }
            
            return false;
        }

        void setIsMorning(boolean isMorning) {
            if (mAmPmTextView != null) {
                mAmPmTextView.setText(isMorning ? mAmString : mPmString);
            }
			if(mAmPmImageView != null){
                mAmPmImageView.setImageLevel(isMorning ? 0 : 1);//.setImageResource(isMorning ? R.drawable.zzzz_ss_keyguard_lockscreen_am : R.drawable.zzzz_ss_keyguard_lockscreen_pm);
            }
        }
    }

    private static class FormatChangeObserver extends ContentObserver {
        private WeakReference<DateClockView> mClock;
        private Context mContext;
        public FormatChangeObserver(DateClockView clock) {
            super(new Handler());
            mClock = new WeakReference<DateClockView>(clock);
            mContext = clock.getContext();
        }
        @Override
        public void onChange(boolean selfChange) {
            DateClockView digitalClock = mClock.get();
            if (digitalClock != null) {
                digitalClock.setDateFormat();
                digitalClock.updateTime();
            } else {
                try {
                    mContext.getContentResolver().unregisterContentObserver(this);
                } catch (RuntimeException e) {
                    // Shouldn't happen
                }
            }
        }
    }

    public DateClockView(Context context) {
        this(context, null);
    }

    public DateClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if("img".equals(super.getTag())){
            mIsImageStyle = true;
        } else {
			mIsImageStyle = false;
		}
        
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                com.jzs.internal.R.styleable.ClockView);
//        mFontFile = a.getNonResourceString(com.jzs.internal.R.styleable.ClockView_ClockFontFile);
//        a.recycle();
//        
//        //android.util.Log.i("QsLog", "==clockview==11=mFontFile:"+mFontFile+"=");
//        if(TextUtils.isEmpty(mFontFile)){
//        	mFontFile = ANDROID_CLOCK_FONT_FILE;
//        } else {
//        	File file = new File("/system/fonts", mFontFile);
//        	if(!file.exists())
//        		mFontFile = ANDROID_CLOCK_FONT_FILE;
//        	else
//        		mFontFile = file.getPath();
//        }
       //android.util.Log.i("QsLog", "==clockview==22=mFontFile:"+mFontFile+"=");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(mIsImageStyle){
            mImgGroup[QS_HOUR_FIRST_INDEX] = (ImageView)this.findViewWithTag("hourfirst");
            mImgGroup[QS_HOUR_LAST_INDEX] = (ImageView)this.findViewWithTag("hourlast");
            mImgGroup[QS_MINUTE_FIRST_INDEX] = (ImageView)this.findViewWithTag("minutefirst");
            mImgGroup[QS_MINUTE_LAST_INDEX] = (ImageView)this.findViewWithTag("minutelast");

            mAmPm = new AmPm(this, null, true);
        } else {
	        mTimeView = (TextView) findViewById(R.id.clock_text);
	        if(mTimeView == null)
	        	mTimeView = (TextView) findViewWithTag("clock_text");
			if(mTimeView != null && !TextUtils.isEmpty(mFontFile))
		        mTimeView.setTypeface(Typeface.createFromFile(mFontFile));//ANDROID_CLOCK_FONT_FILE
	        mAmPm = new AmPm(this, null);
		}
        
        mDateView = (TextView) findViewWithTag("ctrl_date_textview");
        
        mCalendar = Calendar.getInstance();
        setDateFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mAttached++;

        /* monitor time ticks, time changed, timezone */
        if (mIntentReceiver == null) {
            mIntentReceiver = new TimeChangedReceiver(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiverAsUser(mIntentReceiver, UserHandle.OWNER, filter, null, null );
        }

        /* monitor 12/24-hour display preference */
        if (mFormatChangeObserver == null) {
            mFormatChangeObserver = new FormatChangeObserver(this);
            getContext().getContentResolver().registerContentObserver(
                    Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        }

        updateTime();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mAttached--;

        if (mIntentReceiver != null) {
            getContext().unregisterReceiver(mIntentReceiver);
        }
        if (mFormatChangeObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(
                    mFormatChangeObserver);
        }

        mFormatChangeObserver = null;
        mIntentReceiver = null;
    }

    void updateTime(Calendar c) {
        mCalendar = c;
        updateTime();
    }

    public void updateTime() {
        mCalendar.setTimeInMillis(System.currentTimeMillis());

		if(mIsImageStyle){
            java.util.Date dt = mCalendar.getTime();
            
            if(mImgGroup[QS_HOUR_FIRST_INDEX] != null){
                final int index = (mAmPm.isShowAmPm() ? (dt.getHours() > 12 ? (dt.getHours()-12) : dt.getHours()) : dt.getHours())/10;
                //android.util.Log.i("QsLog", "updateTime()===Hours:"+dt.getHours()+"==am:"+mAmPm.isShowAmPm());
                if(index <= 0 && mAmPm.isShowAmPm()){
                    mImgGroup[QS_HOUR_FIRST_INDEX].setVisibility(View.GONE);
                } else {
                    mImgGroup[QS_HOUR_FIRST_INDEX].setImageLevel(index);//setImageResource(mTimeImageResource[hourefirst]);
                    mImgGroup[QS_HOUR_FIRST_INDEX].setVisibility(View.VISIBLE);
                }
            }
            
            if(mImgGroup[QS_HOUR_LAST_INDEX] != null){
                final int index = mAmPm.isShowAmPm() ? ((dt.getHours() > 12 ? (dt.getHours()-12) : dt.getHours())%10) : (dt.getHours()%10);
                
                mImgGroup[QS_HOUR_LAST_INDEX].setImageLevel(index);//.setImageResource(mTimeImageResource[hourelast]);
            }
            
            if(mImgGroup[QS_MINUTE_FIRST_INDEX] != null){
                final int index = dt.getMinutes()/10;
                mImgGroup[QS_MINUTE_FIRST_INDEX].setImageLevel(index);//.setImageResource(mTimeImageResource[index]);
            }
            
            if(mImgGroup[QS_MINUTE_LAST_INDEX] != null){
                final int index = dt.getMinutes()%10;
                mImgGroup[QS_MINUTE_LAST_INDEX].setImageLevel(index);//.setImageResource(mTimeImageResource[index]);
            }
        }
        CharSequence newTime = DateFormat.format(mFormat, mCalendar);
		if(mTimeView != null)
	        mTimeView.setText(newTime);
        mAmPm.setIsMorning(mCalendar.get(Calendar.AM_PM) == 0);
        
        if(mDateView != null){
            mDateView.setText(DateFormat.getDateFormat(getContext()).format(mCalendar.getTime()));
        }
    }

    private void setDateFormat() {
        mFormat = android.text.format.DateFormat.is24HourFormat(getContext()) ? M24 : M12;
        mAmPm.setShowAmPm(mFormat.equals(M12));
    }
}
