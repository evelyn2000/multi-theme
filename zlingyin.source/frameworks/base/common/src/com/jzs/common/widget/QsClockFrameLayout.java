package com.jzs.common.widget;

import com.jzs.common.content.QsIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RemoteViews.RemoteView;

@RemoteView
public class QsClockFrameLayout extends FrameLayout {
    
    private final Handler mHandler = new Handler();
    private boolean mAttached;

    public QsClockFrameLayout(Context context) {
        this(context, null);
    }

    public QsClockFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QsClockFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.sendBroadcast(new Intent(QsIntent.ACTION_JZS_TIME_CHANGED));
        }
    };
}
