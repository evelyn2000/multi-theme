
ifneq ($(strip $(QS_SUPPORT_THEME_STYLE_RES)),)
ifneq ($(strip $(QS_SUPPORT_THEME_STYLE_RES)),android)

JZS_LOCAL_STYLE_SUPPORT_LIST_CFG := $(strip $(filter qss% qsc%, $(QS_SUPPORT_THEME_STYLE_RES)))
ifneq ($(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG),)
CUSTOM_LOCALES += $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)
endif

ifneq ($(filter qishang, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss3, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss3
endif
endif

ifneq ($(filter iphone, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss4, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss4
endif
endif

ifneq ($(filter wphone, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss5, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss5
endif
endif

ifneq ($(filter htc, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss6, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss6
endif
endif

ifneq ($(filter xiaomi, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss10, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss10
endif
endif

ifneq ($(filter samsung, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss11, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss11
endif
endif

ifneq ($(filter laoren, $(QS_SUPPORT_THEME_STYLE_RES)),)
ifeq ($(filter qss13, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qss13
endif
endif

ifeq ($(filter qsc%, $(JZS_LOCAL_STYLE_SUPPORT_LIST_CFG)),)
CUSTOM_LOCALES += qsc1
endif

else
CUSTOM_LOCALES += qss1 qsc1
endif
else
CUSTOM_LOCALES += qss1 qsc1
endif

ifneq ($(QS_ENABLE_GOOGLE_BASE_APPS),no)

PRODUCT_PROPERTY_OVERRIDES += \
     ro.com.google.gmsversion=4.2_r2
     
endif
