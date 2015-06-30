package com.jzs.android.systemstyle.activities;

import java.util.ArrayList;

import com.jzs.android.systemstyle.controls.BookmarkAdapter;
import com.jzs.android.systemstyle.controls.BookmarkItem;
import com.jzs.android.systemstyle.controls.BookmarkView;
import com.jzs.android.systemstyle.controls.BounceCoverFlow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.Window;
//import com.example.testbouncebookmark.R;

import com.jzs.android.systemstyle.R;

public class MainActivity extends Activity {

	private static final float COVER_MULTIPLE_IMAGE = 0.25f;
    private static final float COVER_EXTEND_IMAGE = 0.5f;
    private static final float IMAGE_REFLECTION = 0.25f;
    private static final float MAX_ZOOM_OUT = 350;
    
	private BookmarkView mBookmark;
    private ArrayList<BookmarkItem> mBookmarkItems;
    private BookmarkAdapter mAdapter;
    
    private int mSelectedPos;
    private int mImgWidth;
    private int mImgHeight;
    private int mThumbIndex;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mBookmarkItems = new ArrayList<BookmarkItem>();

        mBookmark = (BookmarkView) findViewById(R.id.bookmark);
        mImgWidth = getResources().getDimensionPixelSize(R.dimen.thumb_disp_width);
        mImgHeight = getResources().getDimensionPixelSize(R.dimen.thumb_disp_height);
        
        mAdapter = new BookmarkAdapter(this, mBookmarkItems);
        mBookmark.setBookmarkAdapter(mAdapter);
        mBookmark.getTitleView().setTextSize(
                getResources().getDimensionPixelSize(R.dimen.title_size));
        mBookmark.setImageDispSize(mImgWidth, mImgHeight);
        mBookmark.setCoverFlowSpacing(
                -(int) (COVER_MULTIPLE_IMAGE * mImgWidth + COVER_EXTEND_IMAGE));
        mBookmark.setImageReflection(IMAGE_REFLECTION);
        
        //BounceCoverFlow gallery = mBookmark.getCoverFlow();
        mBookmark.setSelection(mSelectedPos);
        mBookmark.setEmptyView(null);
        mBookmark.setMaxZoomOut(MAX_ZOOM_OUT);
        mBookmark.setOnSelectionChangeListener(new BookmarkView.OnSelectionChangeListener() {
			
			@Override
			public void onSelectionChanged(int selected) {
				// TODO Auto-generated method stub
				mSelectedPos = selected;
			}
		});
        
        View view = super.findViewById(R.id.select_item);
        if(view != null){
        	view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent itent = new Intent(Intent.ACTION_MAIN);
					itent.setClass(MainActivity.this, ListStyleActivity.class);
					startActivity(itent);
				}
			});
        }
        mTask.execute();
	}
	
	@Override
    protected void onDestroy() {
        mTask.cancel(true);
        
        Bitmap bmp = null;
        for (BookmarkItem item : mBookmarkItems) {
            bmp = item.getContentBitmap();
            if (bmp != null) {
                bmp.recycle();
            }
        }
        super.onDestroy();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private final int [] test_images = new int []{
			R.drawable.test_img_01,
			R.drawable.test_img_02,
			R.drawable.test_img_03,
			R.drawable.test_img_04,
			R.drawable.test_img_05,
			com.jzs.android.systemstyle.R.drawable.test_img_06,
	};
	
	// loading thumb of video
    private final AsyncTask<Void, Integer, Void> mTask = new AsyncTask<Void, Integer, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
            int index = 0;
            Bitmap bmp = null;
            String title = null;
            
            int size = test_images.length;
            for(int i=0; i<size; i++){
            	title = "test image " + String.valueOf(i + 1);
            	String info = String.valueOf(i + 1) + "/" + String.valueOf(size);
            	Bitmap bm = drawable2Bitmap(getResources().getDrawable(test_images[i]));
                BookmarkItem item = new BookmarkItem(bm, title, info);
                mBookmarkItems.add(item);
                
                if (bmp != null || title != null) {
                    publishProgress(i);
                }
            }
            
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update current retrieved video thumbnai index
            mThumbIndex = values[0];
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void result) {
        	if(!isCancelled()){
        		mAdapter.notifyDataSetChanged();
        		mBookmark.setSelection(0);
        	}
            super.onPostExecute(result);
        }

    };
    
    private Bitmap drawable2Bitmap(Drawable drawable){
    	return drawable2Bitmap(drawable, 0, 0, 0);
    }
    
    private Bitmap drawable2Bitmap(Drawable drawable, int width, int height, int degree){  
    	if(width <= 0)
    		width = drawable.getIntrinsicWidth();
    	
    	if(height <= 0)
    		height = drawable.getIntrinsicHeight();
    	
    	//android.util.Log.e("QsLog", "drawable2Bitmap====width:"+width+"==height:"+height);
        Bitmap bitmap = Bitmap  
                .createBitmap(  
                		width,  
                		height,  
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                : Bitmap.Config.RGB_565);  
        
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);  
        drawable.draw(canvas);  
        
    	if(degree != 0){
    		
    		Matrix matrix = new Matrix(); 
    		matrix.setRotate(degree);

    		Bitmap bitmapRotate = Bitmap.createBitmap(bitmap, 0, 0, 
    				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    		
    		bitmap.recycle();
    		
    		return bitmapRotate;
    	}
        
        return bitmap;  
    }
}
