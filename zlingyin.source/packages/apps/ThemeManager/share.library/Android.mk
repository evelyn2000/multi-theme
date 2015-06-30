LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

# LOCAL_JAVA_RESOURCE_DIRS := $(call all-res-files-under, res)
# LOCAL_ASSET_FILES := $(call find-subdir-assets, res)

LOCAL_MODULE := jzs.systemstyle.library

# LOCAL_MODULE_CLASS := JAVA_LIBRARIES
# LOCAL_CERTIFICATE := platform

include $(BUILD_STATIC_JAVA_LIBRARY)
