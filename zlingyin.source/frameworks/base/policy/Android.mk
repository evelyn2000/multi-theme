LOCAL_PATH:= $(call my-dir)

ifeq ($(ENABLE_JZS_POLICY), yes)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := zlingyin.policy.static
LOCAL_JAVA_LIBRARIES := mediatek-common mms-common android.policy zlingyin.framework

LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := zlingyin.policy
# LOCAL_NO_LINGYIN_DEX_PREOPT := true
# LOCAL_PRIVILEGED_MODULE := true
# LOCAL_PROGUARD_ENABLED := full
# LOCAL_PROGUARD_FLAG_FILES := proguard.flags

include $(BUILD_JAVA_LIBRARY)

# additionally, build unit tests in a separate .apk
include $(call all-makefiles-under,$(LOCAL_PATH))

endif