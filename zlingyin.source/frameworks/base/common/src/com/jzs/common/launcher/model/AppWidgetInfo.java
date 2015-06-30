package com.jzs.common.launcher.model;

import android.appwidget.AppWidgetHostView;
import android.content.ComponentName;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class AppWidgetInfo extends ItemInfo {
	/**
     * Identifier for this widget when talking with
     * {@link android.appwidget.AppWidgetManager} for updates.
     */
	public int appWidgetId = NO_ID;

    public ComponentName providerName;

    // TODO: Are these necessary here?
    public int minWidth = -1;
    public int minHeight = -1;
    
    /**
     * View that holds this widget after it's been created.  This view isn't created
     * until Launcher knows it's needed.
     */
    public AppWidgetHostView hostView = null;
    
    
    public AppWidgetInfo(int appWidgetId, ComponentName providerName) {
        itemType = LauncherSettingsCommon.Favorites.ITEM_TYPE_APPWIDGET;
        this.appWidgetId = appWidgetId;
        this.providerName = providerName;

        // Since the widget isn't instantiated yet, we don't know these values. Set them to -1
        // to indicate that they should be calculated based on the layout and minWidth/minHeight
        spanX = -1;
        spanY = -1;
    }

    @Override
    public void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put(LauncherSettingsCommon.Favorites.APPWIDGET_ID, appWidgetId);
    }
    
    @Override
    public String toString() {
        return "AppWidget(id=" + Integer.toString(appWidgetId) + " screen=" + screen + " cellX=" + cellX + " cellY=" + cellY
                + " spanX=" + spanX + " spanY=" + spanY + " providerName = " + providerName + ")";
    }

    @Override
    public void unbind() {
        super.unbind();
        hostView = null;
    }
    
    public AppWidgetInfo(Parcel in){
    	super(in);
    	appWidgetId = in.readInt();
    	minWidth = in.readInt();
    	minHeight = in.readInt();
    	if(in.readInt() > 0)
    		providerName = ComponentName.CREATOR.createFromParcel(in);
    	else
    		providerName = null;
    	
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	super.writeToParcel(dest, flags);
		dest.writeInt(appWidgetId);
		dest.writeInt(minWidth);
		dest.writeInt(minHeight);

		if(providerName != null){
			dest.writeInt(1);
			providerName.writeToParcel(dest, flags);
		} else {
			dest.writeInt(0);
		}
	}
    
    public int describeContents() {
        return 0;
    }
    
	public static final Parcelable.Creator<AppWidgetInfo> CREATOR = new Parcelable.Creator<AppWidgetInfo>() {
		public AppWidgetInfo createFromParcel(Parcel source) {
			return new AppWidgetInfo(source);
		}

		public AppWidgetInfo[] newArray(int size) {
			return new AppWidgetInfo[size];
		}
	};
}
