
include $(LOCAL_PATH)/common.mk

PRODUCT_COPY_FILES += $(foreach file,$(local_framework_keylayouts),\
    $(file):system/usr/keylayout/$(notdir $(file)))

PRODUCT_COPY_FILES += $(foreach file,$(local_framework_keycharmaps),\
    $(file):system/usr/keychars/$(notdir $(file)))

PRODUCT_COPY_FILES += $(foreach file,$(local_framework_keyconfigs),\
    $(file):system/usr/idc/$(notdir $(file)))
