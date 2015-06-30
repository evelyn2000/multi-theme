
ifeq ($(ENABLE_JZS_POLICY),yes)
GLOBAL_JZS_SYSTEM_JAR_LIST := :\/system\/framework\/zlingyin.common.jar:\/system\/framework\/zlingyin.framework.jar:\/system\/framework\/zlingyin.policy.jar
else
GLOBAL_JZS_SYSTEM_JAR_LIST := :\/system\/framework\/zlingyin.common.jar:\/system\/framework\/zlingyin.framework.jar
endif

ifdef JZS_PATH_SOURCES

GLOBAL_JZS_ADDON_INIT_RC := $(JZS_PATH_SOURCES)/build/core/jzs_init.rc

include $(JZS_PATH_SOURCES)/frameworks/pathmap.mk
include $(JZS_PATH_SOURCES)/frameworks/jpathmap.mk

endif


# PREBUILT_PACKAGE:= $(BUILD_SYSTEM_JZS_EXTENSION)/prebuilt-package.mk
