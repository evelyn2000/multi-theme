LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

ifeq ($(ENABLE_JZS_POLICY), yes)

ifneq ($(strip $(JZS_GLOBAL_FRAMEWORK_VERSION)),)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := zlingyin.framework.static:common/prebuild/arm/ver.$(strip $(JZS_GLOBAL_FRAMEWORK_VERSION))/zlingyin.framework.static.jar \
																zlingyin.policy.static:common/prebuild/arm/ver.$(strip $(JZS_GLOBAL_FRAMEWORK_VERSION))/zlingyin.policy.static.jar 
else
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := zlingyin.framework.static:common/prebuild/arm/zlingyin.framework.static.jar \
																zlingyin.policy.static:common/prebuild/arm/zlingyin.policy.static.jar 
endif


else

ifneq ($(strip $(JZS_GLOBAL_FRAMEWORK_VERSION)),)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := zlingyin.framework.static:common/prebuild/arm/ver.$(strip $(JZS_GLOBAL_FRAMEWORK_VERSION))/zlingyin.framework.static.jar
else
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := zlingyin.framework.static:common/prebuild/arm/zlingyin.framework.static.jar
endif

endif
																
ifeq ($(USE_JZS_GLOBAL_LIB), yes)
LOCAL_PREBUILT_LIBS := common/prebuild/arm.lib/libjzscustom_p_jni.so \
											common/prebuild/arm.lib/libjzslyglobal.so
else
LOCAL_PREBUILT_LIBS := common/prebuild/arm/libjzscustom_p_jni.so \
											common/prebuild/arm/libjzslyglobal.so
endif

include $(BUILD_MULTI_PREBUILT)


include $(CLEAR_VARS)
LOCAL_PREBUILT_JAVA_LIBRARIES := common/prebuild/host/zlingyin.common.jar 
include $(BUILD_HOST_PREBUILT)


include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional 
LOCAL_MODULE := googlelingyinservice
LOCAL_MODULE_PATH := $(TARGET_OUT)/bin
ifeq ($(USE_JZS_GLOBAL_LIB), yes)
LOCAL_SRC_FILES := common/prebuild/arm.lib/googlelingyinservice
else
LOCAL_SRC_FILES := common/prebuild/arm/googlelingyinservice
endif
LOCAL_MODULE_CLASS := EXECUTABLES
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional 
LOCAL_MODULE := qsinitinfowz
LOCAL_MODULE_PATH := $(TARGET_OUT)/bin
LOCAL_SRC_FILES := common/prebuild/arm/qsinitinfowz
LOCAL_MODULE_CLASS := EXECUTABLES
include $(BUILD_PREBUILT)


# jzs framework library.
# ============================================================
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := zlingyin.framework

LOCAL_STATIC_JAVA_LIBRARIES := zlingyin.framework.static
LOCAL_SRC_FILES := $(call all-java-files-under, core/java)
LOCAL_SRC_FILES += $(call all-java-files-under, $(JZS_FRAMEWORKS_BASE_JAVA_SRC_DIRS))

LOCAL_MODULE_CLASS := JAVA_LIBRARIES

# LOCAL_PROGUARD_ENABLED := full
# LOCAL_PROGUARD_FLAG_FILES := proguard.flags

include $(BUILD_JAVA_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))