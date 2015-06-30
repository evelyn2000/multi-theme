package com.jzs.android.systemstyle.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzs.android.systemstyle.R;
import com.jzs.android.systemstyle.utils.ThemeStyleBaseInfo;


public class CustomSimpleStyleAdapter extends BaseAdapter {

	private final int mStyleType;
	private final List<ThemeStyleBaseInfo> mListItems;// = new ArrayList<SimpleStyleItem>();
	private LayoutInflater mInflater;
	
    public CustomSimpleStyleAdapter(Context context, int type/*,  List<SimpleStyleItem> listinfo*/) {
    	mStyleType = type;
    	mListItems = new ArrayList<ThemeStyleBaseInfo>();
    	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
//    public void add(String title, Drawable icon, int key){
//    	add(new ThemeStyleInfo(title, icon, key));
//    }
    
    public void add(List<ThemeStyleBaseInfo> list){
    	mListItems.addAll(list);
    }
    
    public void add(ThemeStyleBaseInfo item){
    	mListItems.add(item);
    }
    
    public void clear(){
    	mListItems.clear();
    }
    
    public final int getCount() {
        return mListItems.size();
    }

    public final ThemeStyleBaseInfo getItem(int position) {
        return mListItems.get(position);
    }

    public final long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.simple_style_item, null, false);
            holder = new ViewHolder();
            //holder.container = (FrameLayout) convertView.findViewById(R.id.containerView);
            holder.icon = (PreviewImageView) convertView.findViewWithTag("stylepreview");
            holder.title = (TextView) convertView.findViewWithTag("styletitle");
            convertView.setTag(holder);
        }
        
        ThemeStyleBaseInfo iteminfo = mListItems.get(position);
        holder.icon.setImageBitmap(iteminfo.mIcon);
//        holder.icon.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
        holder.title.setText(iteminfo.mTitle);
        
        return convertView;
    }
    
    static class ViewHolder {
        PreviewImageView icon;
        TextView title;
    }
}
