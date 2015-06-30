package com.jzs.common;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.jzs.common.manager.IAppsManager;
import com.jzs.common.manager.IComplexManager;
import com.jzs.common.manager.IPackageManager;
import com.jzs.common.plugin.IPluginManager;

import android.content.Context;
import android.util.Log;

public final class JzsClassFactory {
	private final static String TAG = "Jzs.ServicesFactory";
	private final static boolean DEBUG = false;
	public final static String SERVER_MANAGER = "JzsServerManagerService.Qs";
	
	private static Map<String, String> servicesInterfaceMap = new HashMap<String, String>();
    static {

    	servicesInterfaceMap.put(SERVER_MANAGER,
                "com.jzs.internal.server.JzsServerManagerService");
        
    }
    
    private static Map<Class, String> helperInterfaceMap = new HashMap<Class, String>();
    static {
    	helperInterfaceMap.put(IPackageManager.class,
                "com.jzs.internal.server.PackageManagerService");
    	
    	helperInterfaceMap.put(IPluginManager.class,
                "com.jzs.internal.manager.PluginManager");
    	
    	helperInterfaceMap.put(IContextHelper.class,
                "com.jzs.internal.manager.ContextHelper");
    	
    	helperInterfaceMap.put(IAppsManager.class,
                "com.jzs.internal.manager.ApplicationManager");
    	
    	helperInterfaceMap.put(IComplexManager.class,
                "com.jzs.internal.manager.ComplexManager");
    }
    
    public static <T> T  createInstance(Class<?> key, Object... args) {
    	Object obj = null;
    	
    	if(helperInterfaceMap.containsKey(key)){
    		obj = createObject(helperInterfaceMap.get(key), args);
    	}
    	// Cannot return null object.
        if (obj == null) {
            Log.d(TAG, "null object during finding :  " + key);
            throw new RuntimeException();
        }
        return (T) obj;
	}
	
	public static Object createServiceInstance(String key, Context context) {
		if(servicesInterfaceMap.containsKey(key)){
			return createServiceObject(servicesInterfaceMap.get(key), context);
		}
		return null;
	}
	
	public static Object createServiceObject(String className, Context context) {
		if (className == null) {
            Log.e(TAG, "JzsServicesFactory::createObject() Interface full class name is null");
            return null;
        }
		
		try {
			
            Class<?> clz = Class.forName(className);
            Constructor localConstructor = clz.getConstructor(new Class[] { Context.class });
            return localConstructor.newInstance(new Object[] { context });

        } catch (Exception e) {
            Log.w(TAG, "Exception: " + e.getClass().getName());
        }

        return null;
	}
	
	public static Object createObject(String className, Object[] args) {

        if (className == null) {
            Log.e(TAG, "Interface full class name is null");
            return null;
        }
        
        if(DEBUG){
            Log.d(TAG, "createObject: className:" + className+"==args:"+args.length);
        }
        
        try {
            Class<?> clz = Class.forName(className);

            if (args.length == 0) {
                // Default constructor.
                return clz.getConstructor().newInstance();
            }

            // More than one parameters. Look for compatible constructor to the
            // input arguments.
            Constructor<?> ctorList[] = clz.getConstructors();
            for (int i = 0; i < ctorList.length; i++) {
                boolean matched = true;
                Constructor<?> ct = ctorList[i];
                Class<?> paramTypes[] = ct.getParameterTypes();
                if (paramTypes.length != args.length) {
                    continue;
                }

                for (int j = 0; j < paramTypes.length; j++) {
                    Class paramType = paramTypes[j];
                    Class actualType = args[j].getClass();

                    if(DEBUG){
	                    Log.d(TAG, "createObject: paramType=" + paramType
	                            + ", actualType=" + actualType);
                    }

                    if (!paramType.isAssignableFrom(actualType)
                            && !(paramType.isPrimitive() && primitiveMap.get(
                                    paramType).equals(actualType))) {
                        Log.d(TAG, "Parameter not matched, skip");
                        matched = false;
                        break;
                    }
                    
                    if(DEBUG){
                    	Log.d(TAG, "Parameter matched");
                    }
                }

                // All parameter matched. Create the instance from the
                // constructor.
                if (matched) {
                	if(DEBUG){
                		Log.d(TAG, "Constructor matched");
                	}
                    return ct.newInstance(args);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Exception: load class "+className+" error:", e);
        }

        return null;
    }
	
	public static Object createObject(Class clz, Object[] args) {
		if (clz == null) {
            Log.e(TAG, "Interface full class name is null");
            return null;
        }
		
		try {
			
			if(args.length == 0)
				return clz.newInstance();
			
			Constructor localConstructor = checkParameterMatch(clz, args);
			if(localConstructor != null){
				return localConstructor.newInstance(args);
			}

          } catch (Exception e) {
              android.util.Log.e(TAG, "NoSuchMethodException " + e.toString());
          } 
		
		return null;
	}
	
	public static Constructor checkParameterMatch(Class clz, Object[] args){
		
		Constructor<?> ctorList[] = clz.getConstructors();
        for (int i = 0; i < ctorList.length; i++) {
            boolean matched = true;
            Constructor<?> ct = ctorList[i];
            Class<?> paramTypes[] = ct.getParameterTypes();
            if (paramTypes.length != args.length) {
                continue;
            }

            for (int j = 0; j < paramTypes.length; j++) {
                Class paramType = paramTypes[j];
                Class actualType = args[j].getClass();

                if(DEBUG){
                    Log.d(TAG, "createObject: paramType=" + paramType
                            + ", actualType=" + actualType);
                }

                if (!paramType.isAssignableFrom(actualType)
                        && !(paramType.isPrimitive() && primitiveMap.get(
                                paramType).equals(actualType))) {
                	if(DEBUG){
                		Log.d(TAG, "Parameter not matched, skip");
                	}
                    matched = false;
                    break;
                }
                
                if(DEBUG){
                	Log.d(TAG, "Parameter matched");
                }
            }

            if (matched) {
            	if(DEBUG){
            		Log.d(TAG, "Constructor matched");
            	}
                return ct;
            }
        }
		return null;
	}
	
	private static Map<Class, Class> primitiveMap = new HashMap<Class, Class>();
    static {
        primitiveMap.put(boolean.class, Boolean.class);
        primitiveMap.put(byte.class, Byte.class);
        primitiveMap.put(char.class, Character.class);
        primitiveMap.put(short.class, Short.class);
        primitiveMap.put(int.class, Integer.class);
        primitiveMap.put(long.class, Long.class);
        primitiveMap.put(float.class, Float.class);
        primitiveMap.put(double.class, Double.class);
    }
}
