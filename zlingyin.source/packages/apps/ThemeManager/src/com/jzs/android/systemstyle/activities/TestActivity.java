package com.jzs.android.systemstyle.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.jzs.android.systemstyle.R;

public class TestActivity extends Activity  {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_apps);
		
		final Intent mainIntent = new Intent("android.intent.action.JZS_TEST_META_ARRAY");
		
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> localList = packageManager.queryIntentActivities(mainIntent, 
															PackageManager.GET_ACTIVITIES 
															| PackageManager.GET_META_DATA
															| PackageManager.GET_RESOLVED_FILTER);

		android.util.Log.i("QsLog", "======size:"+localList.size());
		for (ResolveInfo localResolveInfo : localList){
			final ComponentInfo info = localResolveInfo.activityInfo;
			parseItem(info, packageManager);
		}
	}
	
	private void parseItem(ComponentInfo info, PackageManager pm){
		if(info == null || info.metaData == null){
			android.util.Log.i("QsLog", "parseItem(0)===parameter error==="+info);
			return;
		}
		
		final String pkg = info.packageName;
		android.util.Log.i("QsLog", "parseItem(1)===pkg:"+pkg);
		
		int testint = info.metaData.getInt("com.jzs.plugin.meta.META_TEST_INTEGER_KEY");
		int testint1 = info.metaData.getInt("com.jzs.plugin.meta.META_TEST_INTEGER1_KEY");
		
		int testint2 = loadInteger(pm, pkg, testint);
		
		android.util.Log.i("QsLog", "parseItem(2)===test int resource :"+Integer.toHexString(testint)
				+"===test int value:"+Integer.toHexString(testint1)
				+"===test resource value:"+Integer.toHexString(testint2));
		
		testint = info.metaData.getInt("com.jzs.plugin.meta.META_TEST_STRING1_KEY");
		
		
		if(true){
			int strid = info.metaData.getInt("com.jzs.plugin.meta.META_TEST_ARRAY_STRING_KEY");
			android.util.Log.i("QsLog", "parseItem(3)===test array string id:"+Integer.toHexString(strid)+"==");
			String [] resultStrArray = loadStringArray(pm, pkg, strid);
			if(resultStrArray != null){
				for(int i=0; i<resultStrArray.length; i++){
					android.util.Log.i("QsLog", "parseItem(3)=="+i
							+"==id:"+resultStrArray[i]);
				}
			}
			
			strid = info.metaData.getInt("com.jzs.plugin.meta.META_TEST_ARRAY_STRING1_KEY");
			android.util.Log.i("QsLog", "parseItem(4)===test array string1 id:"+Integer.toHexString(strid)+"==");
			resultStrArray = loadStringArray(pm, pkg, strid);
			if(resultStrArray != null){
				for(int i=0; i<resultStrArray.length; i++){
					android.util.Log.i("QsLog", "parseItem(4)=="+i
							+"==id:"+resultStrArray[i]);
				}
			}
			
			int [] resultArray = loadResourceArray(pm, pkg, strid);
			if(resultArray != null){
				for(int i=0; i<resultArray.length; i++){
					android.util.Log.i("QsLog", "parseItem(4-1)=="+i
							+"==id:"+Integer.toHexString(resultArray[i])
							+"==string:"+loadText(pm, pkg, resultArray[i]));
				}
			}
			
			
			strid = info.metaData.getInt("com.jzs.plugin.meta.META_TEST_ARRAY_IMG_KEY");
			android.util.Log.i("QsLog", "parseItem(5)===test array image id:"+Integer.toHexString(strid)+"==");
			int [] resultImgArray = loadResourceArray(pm, pkg, strid);
			if(resultImgArray != null){
				for(int i=0; i<resultImgArray.length; i++){
					android.util.Log.i("QsLog", "parseItem(4)=="+i
							+"==id:"+Integer.toHexString(resultImgArray[i])
							+"==img:"+(loadDrawable(pm, pkg, resultImgArray[i]) != null ? "success" : "fail"));
				}
			}

		} else {
			
			String [] strArray = info.metaData.getStringArray("com.jzs.plugin.meta.META_TEST_ARRAY_STRING_KEY");
			if(strArray != null){
				for(int i=0; i<strArray.length; i++){
					android.util.Log.i("QsLog", "parseItem(3)=="+i+"=="+strArray[i]);
				}
			} else {
				android.util.Log.i("QsLog", "parseItem(3)===test array string fail==");
			}
			
			String [] strArray1 = info.metaData.getStringArray("com.jzs.plugin.meta.META_TEST_ARRAY_STRING1_KEY");
			if(strArray1 != null){
				for(int i=0; i<strArray1.length; i++){
					android.util.Log.d("QsLog", "parseItem(4)=="+i+"=="+strArray1[i]);
				}
			} else {
				android.util.Log.i("QsLog", "parseItem(4)===test array string1 fail==");
			}
			
			int [] imgarray = info.metaData.getIntArray("com.jzs.plugin.meta.META_TEST_ARRAY_IMG_KEY");
			if(imgarray != null){
				for(int i=0; i<imgarray.length; i++){
					android.util.Log.d("QsLog", "parseItem(5)=="+i+"=="+Integer.toHexString(imgarray[i]));
				}
			} else {
				android.util.Log.i("QsLog", "parseItem(5)===test array image fail==");
			}
		}
	}
	
	public Drawable loadDrawable(PackageManager pm, String pkg, int res){
    	if (pm != null && res != 0) {
            return pm.getDrawable(pkg, res, null);
        }
		return null;
    }
	
    public CharSequence loadText(PackageManager pm, String pkg, int res){
    	if (pm != null && res != 0) {
            return pm.getText(pkg, res, null);
        }
		return null;
    }
    
    public int loadInteger(PackageManager pm, String pkg, int resid){
    	if (pm != null && resid != 0) {
    		try{
    			Resources res = pm.getResourcesForApplication(pkg);
    			return res.getInteger(resid);
    		} catch (PackageManager.NameNotFoundException ex){
    			android.util.Log.e("QsLog", "loadInteger() get resource fail " + ex.toString());
    		} catch (Resources.NotFoundException ex){
    			android.util.Log.e("QsLog", "loadInteger() get value "+Integer.toHexString(resid)+" fail " + ex.toString());
    		}
            //return pm.get(pkg, res, null);
        }
		return 0;
    }
    
    public int[] loadIntegerArray(PackageManager pm, String pkg, int resid){
    	if (pm != null && resid != 0) {
    		try{
    			Resources res = pm.getResourcesForApplication(pkg);
    			return res.getIntArray(resid);
    		} catch (PackageManager.NameNotFoundException ex){
    			android.util.Log.e("QsLog", "loadIntegerArray() get resource fail " + ex.toString());
    		} catch (Resources.NotFoundException ex){
    			android.util.Log.e("QsLog", "loadIntegerArray() get value "+Integer.toHexString(resid)+" fail " + ex.toString());
    		}
            //return pm.get(pkg, res, null);
        }
		return null;
    }
    
    public int[] loadResourceArray(PackageManager pm, String pkg, int resid){
    	if (pm != null && resid != 0) {
    		try{
    			Resources res = pm.getResourcesForApplication(pkg);
    			TypedArray array = res.obtainTypedArray(resid);
    	        int n = array.length();
    	        int ids[] = new int[n];
    	        for (int i = 0; i < n; ++i) {
    	            ids[i] = array.getResourceId(i, 0);
    	        }
    	        array.recycle();
    	        return ids;
    		} catch (PackageManager.NameNotFoundException ex){
    			android.util.Log.e("QsLog", "loadIntegerArray() get resource fail " + ex.toString());
    		} catch (Resources.NotFoundException ex){
    			android.util.Log.e("QsLog", "loadIntegerArray() get value "+Integer.toHexString(resid)+" fail " + ex.toString());
    		}
            //return pm.get(pkg, res, null);
        }
		return null;
    }
    
    public String[] loadStringArray(PackageManager pm, String pkg, int resid){
    	if (pm != null && resid != 0) {
    		try{
    			Resources res = pm.getResourcesForApplication(pkg);
    			return res.getStringArray(resid);
    		} catch (PackageManager.NameNotFoundException ex){
    			android.util.Log.e("QsLog", "loadStringArray() get resource fail " + ex.toString());
    		} catch (Resources.NotFoundException ex){
    			android.util.Log.e("QsLog", "loadStringArray() get value "+Integer.toHexString(resid)+" fail " + ex.toString());
    		}
            //return pm.get(pkg, res, null);
        }
		return null;
    }
    
    
}
