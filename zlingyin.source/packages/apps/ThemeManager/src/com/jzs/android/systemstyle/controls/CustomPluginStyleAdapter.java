package com.jzs.android.systemstyle.controls;

import java.util.ArrayList;
import java.util.List;

import com.jzs.common.plugin.IPlugin;
import com.jzs.common.plugin.IPluginBase;
import com.jzs.common.plugin.IPluginTarget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jzs.android.systemstyle.R;

public class CustomPluginStyleAdapter extends BaseAdapter {
	private final List<IPluginBase> mListItems;// = new ArrayList<SimpleStyleItem>();
	private LayoutInflater mInflater;
	private PackageManager mPackageManager;
	private int mItemLayoutRes;
	
	public CustomPluginStyleAdapter(Context context){
		this(context, R.layout.simple_style_item);
	}
	
    public CustomPluginStyleAdapter(Context context, int layout) {
    	mItemLayoutRes = layout;
    	mListItems = new ArrayList<IPluginBase>();
    	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	mPackageManager = context.getPackageManager();
    }
    
    public void addTarget(List<IPluginTarget> list){
    	mListItems.addAll(list);
    }
    
    public void addPlugin(List<IPlugin> list){
    	mListItems.addAll(list);
    }
    
    public void add(IPluginTarget item){
    	mListItems.add(item);
    }
    
    public void clear(){
    	mListItems.clear();
    }
    
    public final int getCount() {
        return mListItems.size();
    }

    public final IPluginBase getItem(int position) {
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
            convertView = mInflater.inflate(mItemLayoutRes, null, false);
            holder = new ViewHolder();
            //holder.container = (FrameLayout) convertView.findViewById(R.id.containerView);
            holder.icon = (PreviewImageView) convertView.findViewWithTag("stylepreview");
            holder.title = (TextView) convertView.findViewWithTag("styletitle");
            convertView.setTag(holder);
        }
        
        IPluginBase iteminfo = mListItems.get(position);
        if(iteminfo != null){
	        holder.icon.setImageDrawable(iteminfo.loadPreview(mPackageManager));	
	        holder.title.setText(iteminfo.loadTitle(mPackageManager));
        }
        
        return convertView;
    }
    
    static class ViewHolder {
        PreviewImageView icon;
        TextView title;
    }
}
