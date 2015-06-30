# all framework module add their path via JZS_FRAMEWORK_BASE += $(my-dir)

JZS_ALL_FRAMEWORK_BASE        := 
JZS_PATHMAP_MK                := $(wildcard $(shell find zlingyin.source/frameworks/*/ | grep "/pathmap.mk$$"))
$(foreach mk,$(JZS_PATHMAP_MK),$(eval include $(mk))$(eval JZS_ALL_FRAMEWORK_BASE += $(JZS_FRAMEWORK_BASE)))
JZS_ALL_FRAMEWORK_BASE        := $(addprefix zlingyin.source/frameworks/,$(JZS_ALL_FRAMEWORK_BASE))
JZS_ALL_FRAMEWORK_BASE        := $(addsuffix /java,$(wildcard $(JZS_ALL_FRAMEWORK_BASE)))
FRAMEWORKS_BASE_SUBDIRS       += $(addprefix ../../, $(JZS_ALL_FRAMEWORK_BASE))
FRAMEWORKS_BASE_JAVA_SRC_DIRS += $(JZS_ALL_FRAMEWORK_BASE)

