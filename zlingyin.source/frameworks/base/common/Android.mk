
LOCAL_PATH := $(call my-dir)

# zlingyin common library.



include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := zlingyin.common

LOCAL_MODULE_CLASS := JAVA_LIBRARIES

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_SRC_FILES += $(call all-Iaidl-files-under, src)
LOCAL_SRC_FILES += $(call all-java-files-under, $(JZS_FRAMEWORKS_BASE_JAVA_SRC_DIRS))

# Always use the latest prebuilt Android library.
ifeq ($(call lt,$(PLATFORM_SDK_VERSION),19),T)
LOCAL_SDK_VERSION := 16
else
LOCAL_SDK_VERSION := 17
endif
#current



# LOCAL_PROGUARD_ENABLED := full
# LOCAL_PROGUARD_FLAG_FILES := proguard.flags

include $(BUILD_JAVA_LIBRARY)


include $(call all-makefiles-under,$(LOCAL_PATH))

