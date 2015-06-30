
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

PRODUCT_PACKAGES += \
				com.google.android.maps.jar \
				google_generic_update.txt \
				ConfigUpdater \
				GmsCore \
				GoogleLoginService \
				GooglePartnerSetup \
				GoogleServicesFramework \
				GoogleOneTimeInitializer \
				GoogleCalendarSyncAdapter \
				GoogleContactsSyncAdapter \
				GoogleFeedback \
				GoogleBackupTransport 

# com.google.android.media.effects.jar 
#				NetworkLocation 
				
ifeq ($(QS_GMS_PLUGINS_APP),yes)
PRODUCT_PACKAGES += com.google.android.gsf.login 
endif

ifeq ($(QS_ENABLE_GOOGLE_FACELOCK),yes)
PRODUCT_PACKAGES += FaceLock

ifeq ($(QS_GMS_PLUGINS_APP),yes)
PRODUCT_PACKAGES += com.android.facelock 
endif
endif

ifneq ($(QS_ENABLE_GOOGLE_APPS),)
PRODUCT_PACKAGES += $(QS_ENABLE_GOOGLE_APPS)
endif

# Disable building webviewchromium from source
PRODUCT_PREBUILT_WEBVIEWCHROMIUM := no

# PRODUCT_PACKAGE_OVERLAYS += device/sample/overlays/location

# Overrides
#ro.setupwizard.mode=OPTIONAL 
#ifeq ($(call lt,$(PLATFORM_SDK_VERSION),19),T)
PRODUCT_PROPERTY_OVERRIDES += \
		 ro.setupwizard.mode=OPTIONAL \
     ro.com.google.gmsversion=4.4_r1 \
		 ro.com.google.clientidbase=android-google
		 
#endif

endif
