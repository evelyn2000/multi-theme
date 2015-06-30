LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := \
										jzs.systemstyle.library 
										
LOCAL_JAVA_LIBRARIES := zlingyin.framework telephony-common

ifeq ($(strip $(QS_THEME_MANAGER_APP_WITHICON)),yes)
LOCAL_MANIFEST_FILE := deskicon/AndroidManifest.xml
endif

LOCAL_PACKAGE_NAME := QsThemeManager
LOCAL_CERTIFICATE := platform

# LOCAL_PROGUARD_ENABLED := full
# LOCAL_PROGUARD_FLAG_FILES := proguard.flags

LOCAL_AAPT_FLAGS += -c mdpi
LOCAL_AAPT_FLAGS += -c hdpi
LOCAL_AAPT_FLAGS += -c xhdpi
LOCAL_AAPT_FLAGS += -c xxhdpi

include $(BUILD_PACKAGE)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
