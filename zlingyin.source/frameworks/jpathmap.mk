# all framework module add their path via JZS_FRAMEWORK_BASE += $(my-dir)

JZS_ALL_FRAMEWORK             := 
JZS_MPATHMAP_MK               := $(wildcard $(shell find zlingyin.source/frameworks/*/ | grep "/jpathmap.mk$$"))
$(foreach mk,$(JZS_MPATHMAP_MK),$(eval include $(mk))$(eval JZS_ALL_FRAMEWORK += $(JZS_FRAMEWORK)))
JZS_ALL_FRAMEWORK             := $(addprefix ../../,$(JZS_ALL_FRAMEWORK))
JZS_ALL_FRAMEWORK             := $(addsuffix /java,$(JZS_ALL_FRAMEWORK))
JZS_FRAMEWORKS_BASE_JAVA_SRC_DIRS += $(JZS_ALL_FRAMEWORK)

