package com.jzs.common.launcher.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class ItemInfo implements Parcelable {

	public static final int NO_ID = -1;
	
	/**
     * The id in the settings database for this item
     */
	public long id = NO_ID;
    
    /**
     * One of {@link LauncherSettings.Favorites#ITEM_TYPE_APPLICATION},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_SHORTCUT},
     * {@link LauncherSettings.Favorites#ITEM_TYPE_FOLDER}, or
     * {@link LauncherSettings.Favorites#ITEM_TYPE_APPWIDGET}.
     */
	public int itemType;
    
    /**
     * The id of the container that holds this item. For the desktop, this will be 
     * {@link LauncherSettings.Favorites#CONTAINER_DESKTOP}. For the all applications folder it
     * will be {@link #NO_ID} (since it is not stored in the settings DB). For user folders
     * it will be the id of the folder.
     */
	public long container = NO_ID;
    
    /**
     * Iindicates the screen in which the shortcut appears.
     */
	public int screen = -1;
    
    /**
     * Indicates the X position of the associated cell.
     */
	public int cellX = -1;

    /**
     * Indicates the Y position of the associated cell.
     */
	public int cellY = -1;

    /**
     * Indicates the X cell span.
     */
	public int spanX = 1;

    /**
     * Indicates the Y cell span.
     */
	public int spanY = 1;

    /**
     * Indicates the minimum X cell span.
     */
	public int minSpanX = 1;

    /**
     * Indicates the minimum Y cell span.
     */
	public int minSpanY = 1;

    /**
     * Indicates that this item needs to be updated in the db
     */
	public boolean requiresDbUpdate = false;

    /**
     * Title of the item
     */
	public CharSequence title;

    /**
     * The position of the item in a drag-and-drop operation.
     */
	public int[] dropPos = null;

    /**
     * M: The unread num of the item.
     */
	public int unreadNum = 0;

    public ItemInfo() {
    }    
    public ItemInfo(ItemInfo info) {
        id = info.id;
        cellX = info.cellX;
        cellY = info.cellY;
        spanX = info.spanX;
        spanY = info.spanY;
        screen = info.screen;
        itemType = info.itemType;
        container = info.container;
        // tempdebug:
        //LauncherModel.checkItemInfo(this);
        unreadNum = info.unreadNum;
    }

    /** Returns the package name that the intent will resolve to, or an empty string if
     *  none exists. */
    public static String getPackageName(Intent intent) {
        if (intent != null) {
            String packageName = intent.getPackage();
            if (packageName == null && intent.getComponent() != null) {
                packageName = intent.getComponent().getPackageName();
            }
            if (packageName != null) {
                return packageName;
            }
        }
        return "";
    }
    
    public static String getClassName(Intent intent) {
        if (intent != null) {
            if(intent.getComponent() != null) {
                return intent.getComponent().getClassName();
            }
        }
        return "";
    }

    /**
     * Write the fields of this item to the DB
     * 
     * @param values
     */
    public void onAddToDatabase(ContentValues values) { 
        values.put(LauncherSettingsCommon.BaseLauncherColumns.ITEM_TYPE, itemType);
        values.put(LauncherSettingsCommon.Favorites.CONTAINER, container);
        values.put(LauncherSettingsCommon.Favorites.SCREEN, screen);
        values.put(LauncherSettingsCommon.Favorites.CELLX, cellX);
        values.put(LauncherSettingsCommon.Favorites.CELLY, cellY);
        values.put(LauncherSettingsCommon.Favorites.SPANX, spanX);
        values.put(LauncherSettingsCommon.Favorites.SPANY, spanY);
    }

    public void updateValuesWithCoordinates(ContentValues values, int cellX, int cellY) {
        values.put(LauncherSettingsCommon.Favorites.CELLX, cellX);
        values.put(LauncherSettingsCommon.Favorites.CELLY, cellY);
    }

    public static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            android.util.Log.w("Favorite", "Could not write icon");
            return null;
        }
    }

    public static void writeBitmap(ContentValues values, Bitmap bitmap) {
        if (bitmap != null) {
            byte[] data = flattenBitmap(bitmap);
            values.put(LauncherSettingsCommon.Favorites.ICON, data);
        }
    }

    /**
     * It is very important that sub-classes implement this if they contain any references
     * to the activity (anything in the view hierarchy etc.). If not, leaks can result since
     * ItemInfo objects persist across rotation and can hence leak by holding stale references
     * to the old view hierarchy / activity.
     */
    public void unbind() {
    	
    }
    
    public ItemInfo(Parcel in){
    	id = in.readLong();
    	itemType = in.readInt();
    	screen = in.readInt();
    	cellX = in.readInt();
    	cellY = in.readInt();
    	spanX = in.readInt();
    	spanY = in.readInt();
    	minSpanX = in.readInt();
    	minSpanY = in.readInt();
    	unreadNum = in.readInt();
    	title = in.readString();
    }
    
    public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(itemType);
		dest.writeInt(screen);
		dest.writeInt(cellX);
		dest.writeInt(cellY);
		dest.writeInt(spanX);
		dest.writeInt(spanY);
		dest.writeInt(minSpanX);
		dest.writeInt(minSpanY);
		dest.writeInt(unreadNum);
		if(!TextUtils.isEmpty(title))
			dest.writeString(title.toString());
		else
			dest.writeString("");
	}
    
    public int describeContents() {
        return 0;
    }
    
	public static final Parcelable.Creator<ItemInfo> CREATOR = new Parcelable.Creator<ItemInfo>() {
		public ItemInfo createFromParcel(Parcel source) {
			return new ItemInfo(source);
		}

		public ItemInfo[] newArray(int size) {
			return new ItemInfo[size];
		}
	};

    @Override
    public String toString() {
        return "Item(id=" + this.id + " type=" + this.itemType + " container=" + this.container
            + " screen=" + screen + " cellX=" + cellX + " cellY=" + cellY + " spanX=" + spanX
                + " spanY=" + spanY + " dropPos=" + dropPos
                 + ")";
    }
}
