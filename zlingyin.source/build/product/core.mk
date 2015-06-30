
# PRODUCT_BRAND := generic
# PRODUCT_DEVICE := generic
# PRODUCT_NAME := core

PRODUCT_PROPERTY_OVERRIDES := 

ifneq ($(strip $(QS_DEFAULT_RINGTONE)),)
PRODUCT_PROPERTY_OVERRIDES += ro.config.ringtone=$(strip $(QS_DEFAULT_RINGTONE))
else
PRODUCT_PROPERTY_OVERRIDES += ro.config.ringtone=Backroad.ogg
endif

ifneq ($(strip $(QS_DEFAULT_NOTIFICATION_SOUND)),)
PRODUCT_PROPERTY_OVERRIDES += ro.config.notification_sound=$(strip $(QS_DEFAULT_NOTIFICATION_SOUND))
else
PRODUCT_PROPERTY_OVERRIDES += ro.config.notification_sound=Proxima.ogg
endif

ifneq ($(strip $(QS_DEFAULT_ALARM_ALERT)),)
PRODUCT_PROPERTY_OVERRIDES += ro.config.alarm_alert=$(strip $(QS_DEFAULT_ALARM_ALERT))
else
PRODUCT_PROPERTY_OVERRIDES += ro.config.alarm_alert=Alarm_Classic.ogg
endif

ifneq ($(strip $(QS_DEFALUT_LANGUAGE_DEFINED)),)
PRODUCT_PROPERTY_OVERRIDES += persist.sys.language=$(strip $(QS_DEFALUT_LANGUAGE_DEFINED))
endif

ifneq ($(strip $(QS_DEFALUT_COUNTRY_DEFINED)),)
PRODUCT_PROPERTY_OVERRIDES += persist.sys.country=$(strip $(QS_DEFALUT_COUNTRY_DEFINED))
endif

ifneq ($(strip $(QS_DEFALUT_TIMEZONE_DEFINED)),)
PRODUCT_PROPERTY_OVERRIDES += persist.sys.timezone=$(strip $(QS_DEFALUT_TIMEZONE_DEFINED))
endif



PRODUCT_PACKAGES += zlingyin-res \
 		    		zlingyin.framework \
 		    		zlingyin.common \
 		    		libjzscustom_p_jni \
 		    		libjzscustom_p_jni.so \
 		    		GoogleServiceTest \
				    libqssensorcali_jni \
				    libqssensorcali_jni.so \
				    QsSystemInit \
				    QsSettingsProvider \
				    QsSystemUpdateAssistant \
				    QsGoogleOtaUpdater \
				    googlelingyinservice \
				    libjzslyglobal \
				    libjzslyglobal.so \
				    qsinitinfowz \
				    qssu \
				    QsExtSettings 

PRODUCT_PACKAGES += libsettings_em_sensor_jni 

ifeq ($(strip $(QS_THEME_MANAGER_APP)), yes)
# 
endif

ifneq ($(strip $(QS_ENABLE_EXT_APPS)),)
PRODUCT_PACKAGES += $(QS_ENABLE_EXT_APPS)
endif

ifeq ($(QS_USE_HTC_CLOCK_WIDGET), yes)
PRODUCT_PACKAGES += QsWidgetHtcClock 
endif

ifeq ($(QS_BATTERY_CLOCK_WIDGET), yes)
PRODUCT_PACKAGES += QsWidgetBatteryClock 
endif

ifeq ($(QS_USE_FILE_EXPLORER), yes)
PRODUCT_PACKAGES += QsFileExplorer 
endif

ifeq ($(QS_USE_IPHONE_LAUNCHER_APP), yes)
PRODUCT_PACKAGES += QsIphoneLauncher
endif

ifeq ($(QS_HTCLAUNCHER_APP), yes)
PRODUCT_PACKAGES += QsHtcLauncher2
endif

ifneq ($(QS_USE_QSLAUNCHER_APP), )
PRODUCT_PACKAGES += QsLauncher 
endif

ifeq ($(QS_USE_QSHOME_APP), yes)
PRODUCT_PACKAGES += QsHome
endif

ifeq ($(QS_HTV_START), yes)
PRODUCT_PACKAGES += QsHTVStart 
endif

ifeq ($(QS_APP_PROTIPS), yes)
PRODUCT_PACKAGES +=	Protips
endif
    
ifeq ($(QS_QUICK_SEARCH_BOX), yes)
PRODUCT_PACKAGES +=	QuickSearchBox
endif


ifeq ($(MTK_LAUNCHER2_APP), yes)
PRODUCT_PACKAGES += Launcher2
endif

ifeq ($(QS_TWZLAUNCHER_APP), yes)
PRODUCT_PACKAGES += QsTwzLauncher
endif

ifeq ($(QS_WPLAUNCHER_APP), yes)
PRODUCT_PACKAGES += QsWpLauncher
endif

ifeq ($(QS_APP_WORLDCLOCK), yes)
PRODUCT_PACKAGES += WorldClock
endif

ifeq ($(QS_APP_LED_FLASHLIGHT), yes)
PRODUCT_PACKAGES += QsLedFlashlight
endif
     
ifeq ($(QS_APP_CALENDAR), yes)
PRODUCT_PACKAGES += Calendar
endif

ifeq ($(QS_APP_FILEEXPLORER), yes)
PRODUCT_PACKAGES +=	QsFileExplorer
endif

ifeq ($(QS_WALLPAPER_APP), yes)
PRODUCT_PACKAGES +=	QsWallPaper
endif

ifeq ($(QS_FMRADIOSS_APP), yes)
PRODUCT_PACKAGES +=	FMRadioSs
endif

ifeq ($(QS_FMRADIOIPHONE_APP), yes)
PRODUCT_PACKAGES +=	FMRadioIphone
endif

ifeq ($(QS_MUSICIPHONE_APP), yes)
PRODUCT_PACKAGES +=	MusicIphone
endif

ifeq ($(QS_ENABLE_ROOT_USER), yes)
PRODUCT_PACKAGES +=	su
endif

ifeq ($(QS_PASSBOOK_APP), yes)
PRODUCT_PACKAGES +=	Passbook
endif

ifeq ($(strip $(QS_IP_DESKCLOCK)),yes)
  PRODUCT_PACKAGES += DeskClock_ip
endif
ifeq ($(QS_ITUNES_APP), yes)
PRODUCT_PACKAGES +=	iTunes
endif

ifeq ($(QS_FACTORYTEST_APP), yes)
PRODUCT_PACKAGES +=	QsFactoryTest
endif

ifeq ($(QS_QSSYSTEMUPDATE_APP), yes)
PRODUCT_PACKAGES +=	QsSystemUpdate
endif

ifeq ($(QS_PERMISSION_MANAGER_APP), yes)
PRODUCT_PACKAGES +=	QsPermissionManager
endif

ifeq ($(QS_MTPLAUNCHER_APP), yes)
PRODUCT_PACKAGES +=	QsMtpLauncher \
										QsMtpLauncher.ics \
										QsMtpLauncher.htc \
										QsMtpLauncher.samsung
										
endif

ifeq ($(QS_WEATHER_WIDGET_APP), yes)
PRODUCT_PACKAGES += QsWeatherWidget 
endif

ifeq ($(QS_FMRADIO_APP), yes)
PRODUCT_PACKAGES += QsFMRadio 
endif

ifeq ($(QS_SUPPORT_COVER), yes)
# PRODUCT_PACKAGES += QsClockDreams 
endif

ifneq ($(QS_FONT_SCALE_PERCENT),)
PRODUCT_PACKAGES += packages-compat.xml 
endif

ifeq ($(QS_LAOREN_LAUNCHER), yes)
PRODUCT_PACKAGES += QsVideoChatLauncher 
endif

ifeq ($(QS_NAVIGATION_LAUNCHER), yes)
PRODUCT_PACKAGES += NavigationLauncher 
endif

ifeq ($(QS_SOS_APP), yes)
PRODUCT_PACKAGES += SOS2 
endif

ifeq ($(QS_FM_TRANSMITTER), yes)
PRODUCT_PACKAGES += QsFMTransmitter 
endif


ifeq ($(QS_HEART_TEST), yes)
PRODUCT_PACKAGES += HeartTester 
endif


ifeq ($(strip $(MTK_INPUTMETHOD_OPENWNN_APP)), yes)
PRODUCT_PACKAGES += OpenWnn
PRODUCT_PACKAGES += libWnnEngDic \
  										libWnnJpnDic \
  										libwnndict 
endif

ifeq ($(QS_OPEN_PREBUILD_FILE), yes)
PRODUCT_PACKAGES += OpenPrebuildFile 
endif

ifeq ($(LY_SUPPORT_FFMPEG_SW_PLUGIN), yes)
PRODUCT_PACKAGES += liblyffmpeg_utils libLyFFmpegExtractor \
					libstagefright_soft_ffmpegvdec libstagefright_soft_ffmpegadec \
					media_codecs_ffmpeg.xml \
					libavcodec libavformat libswresample libavutil libswscale
endif

ifeq ($(strip $(QS_S3D_SUPPORT)), yes)
	PRODUCT_PACKAGES += libsurfaceflingerext libguiextbase libgem libgem_converter QsVideoPlayer
ifneq ($(QS_DISABLE_DEBUGABLE), yes)
	PRODUCT_PACKAGES += test3dgrating \
  										libqs3dgrating_jni \
  										libqs3dgrating_jni.so 
endif
endif

ifeq ($(QS_ENABLE_STEREO_VIDEOPLAYER), yes)
	PRODUCT_PACKAGES += QsStereoWorld
endif

#mCube start
ifeq ($(strip $(MCUBE_CALIB_SUPPORT)), yes)
PRODUCT_PACKAGES += mCubeAcc \
					libsensorcontrol \
					sensorcontrol.default \
					gsCalibrated 
endif
#mCube end	

ifneq ($(filter mc64xx%, $(CUSTOM_KERNEL_MAGNETOMETER)),)
PRODUCT_PACKAGES += mc64xxd
endif

ifneq ($(filter bmm056_auto, $(CUSTOM_KERNEL_MAGNETOMETER)),)
PRODUCT_PACKAGES += bmm056d
endif



ifeq ($(strip $(MTK_S3D_SUPPORT)), yes)
ifneq ($(QS_DISABLE_DEBUGABLE), yes)
PRODUCT_PACKAGES += test3dgrating \
  										libqs3dgrating_jni \
  										libqs3dgrating_jni.so 
endif  										
endif


ifneq ($(strip $(CUSTOM_KERNEL_GESTURE)),)
ifneq ($(strip $(USE_JZS_GLOBAL_VERSION)),)
ifneq ($(strip $(USE_JZS_GLOBAL_VERSION)),001)
PRODUCT_PACKAGES += QsGestureSettings \
										libjzsgesture_p_jni \
										libjzsgesture_p_jni.so 
endif
endif
endif

ifeq ($(strip $(MTK_BICR_SUPPORT)), yes)
PRODUCT_PACKAGES += QsSmartPhoneTools.iso
# PRODUCT_PROPERTY_OVERRIDES += sys.usb.mtk_bicr_support=yes
endif

ifeq ($(strip $(QS_MULTI_USER_SUPPORT)), yes)
	PRODUCT_PROPERTY_OVERRIDES += \
    fw.max_users=5
endif

ifeq ($(strip $(QS_DISABLE_USB_MASS_STORAGE)), yes)
PRODUCT_PROPERTY_OVERRIDES += ro.sys.usb.storage.type=mtp
else

JZS_LOCAL_USB_STORAGE_TYPE := mtp,mass_storage
ifeq ($(strip $(MTK_SHARED_SDCARD)), yes)
ifeq ($(strip $(QS_SUPPORT_SDCARD2)), no)
JZS_LOCAL_USB_STORAGE_TYPE := mtp
endif
endif

PRODUCT_PROPERTY_OVERRIDES += ro.sys.usb.storage.type=$(strip $(JZS_LOCAL_USB_STORAGE_TYPE))
endif


ifneq ($(strip $(MTK_LOCKSCREEN_TYPE)),)
  PRODUCT_PROPERTY_OVERRIDES += \
    persist.qs.curlockscreen=$(MTK_LOCKSCREEN_TYPE)
endif

PRODUCT_COPY_FILES += \
					$(JZS_PATH_SOURCES)/build/core/jzsbackupdir.txt:system/etc/jzsbackupdir.txt \
					$(JZS_PATH_SOURCES)/build/tools/jzsinit.sh:system/etc/qsinit.sh 


ifneq ($(strip $(USE_JZS_GLOBAL_VERSION)),)
$(call inherit-product-if-exists, $(JZS_PATH_SOURCES)/build/product/core.ver.$(strip $(USE_JZS_GLOBAL_VERSION)).mk)
endif


jzsqishanguistyle  := 0

ifneq ($(QS_DEFAULT_FRAMEWORK_STYLE), )
jzsqishanguistyle  := $(strip $(QS_DEFAULT_FRAMEWORK_STYLE)) 
endif

PRODUCT_PROPERTY_OVERRIDES += \
    persist.qs.uistyle=$(strip $(jzsqishanguistyle))
    
ifneq ($(strip $(QS_CUSTOMER_TYPE_ID)),)
PRODUCT_PROPERTY_OVERRIDES += persist.qs.customer=$(strip $(QS_CUSTOMER_TYPE_ID)) 
endif  
    
ifneq ($(strip $(QS_CUSTOM_DENSITY)),)
PRODUCT_PROPERTY_OVERRIDES += ro.sf.lcd_density=$(strip $(QS_CUSTOM_DENSITY))
endif

ifneq ($(strip $(QS_PRJ)),)
PRODUCT_PROPERTY_OVERRIDES += ro.qs.projectname=$(strip $(QS_PRJ))
endif

ifeq ($(QS_DISABLE_DEBUGABLE), yes)

PRODUCT_PACKAGES +=	qssu

ifeq ($(QS_ENABLE_WITH_DEXPREOPT), yes)
WITH_DEXPREOPT := true
endif
endif

ifeq ($(MTK_SHARED_SDCARD), yes)
PRODUCT_PROPERTY_OVERRIDES += sys.ipo.pwrdncap=3
else
PRODUCT_PROPERTY_OVERRIDES += sys.ipo.pwrdncap=2
endif

ifneq ($(strip $(QS_BT_NAME)),)  
  PRODUCT_PROPERTY_OVERRIDES += ro.dui.bt.name=$(strip $(QS_BT_NAME))
else
	PRODUCT_PROPERTY_OVERRIDES += ro.dui.bt.name=ANDROID_BT
endif

ifneq ($(strip $(QS_WLAN_SSID_NAME)),)  
  PRODUCT_PROPERTY_OVERRIDES += ro.dui.wlan.ssid.name=$(strip $(QS_WLAN_SSID_NAME))
else
	PRODUCT_PROPERTY_OVERRIDES += ro.dui.wlan.ssid.name=AndroidAP
endif

ifeq ($(ENABLE_JZS_JARS), yes)

PRODUCT_PACKAGES += zlingyin.policy \
 		    		zlingyin-base.xml \
 		    		QsLockscreenBase \
 		    		QsThemeManager \
						QsSystemTheme.default \
				    QsSystemTheme.htc \
				    QsSystemTheme.qishang \
				    QsSystemTheme.samsung \
				    QsSystemTheme.laoren \
				    QiShangSlideLockScreen \
				    QiShangLaoRenLockScreen 



ifeq ($(JZS_ENABLE_CUSTOM_APPS), yes)
PRODUCT_PROPERTY_OVERRIDES += ro.build.jzs.customapp=1
else
PRODUCT_PROPERTY_OVERRIDES += ro.build.jzs.customapp=0
endif

endif


define jzs-get-valid-child-dir
$(if $(2),$(if $(wildcard $(1)/$(2)),$(1)/$(2),$(1)/default),$(1)/default)
endef

define jzs-get-valid-dir
$(if $(wildcard $(1)),$(1),$(2))
endef

define jzs-to-lowercase
$(shell echo $(1) | tr 'A-Z' 'a-z')
endef

$(eval _zz_droid_overlay_dir := $(call jzs-get-valid-child-dir,$(JZS_PATH_SOURCES)/theme/res/droid,$(call jzs-to-lowercase,$(strip $(word 2,$(subst ., ,$(MTK_BRANCH))))))) \
$(if $(wildcard $(_zz_droid_overlay_dir)), \
  $(eval PRODUCT_PACKAGE_OVERLAYS += $(_zz_droid_overlay_dir)) \
  , \
 ) 

$(foreach cf,$(QS_SUPPORT_THEME_STYLE_RES), \
  $(eval _theme_overlay_dir := $(call jzs-get-valid-child-dir,$(JZS_PATH_SOURCES)/theme/res/$(cf),$(call jzs-to-lowercase,$(strip $(word 2,$(subst ., ,$(MTK_BRANCH))))))) \
  $(if $(wildcard $(_theme_overlay_dir)), \
    $(eval PRODUCT_PACKAGE_OVERLAYS += $(_theme_overlay_dir)) \
    , \
   ) \
 )
 
$(foreach cf,$(QS_OVERRIDE_RES_DIRECTORYS_NAME), \
  $(eval _project_overlay_dir := $(call jzs-get-valid-dir,$(JZS_PATH_SOURCES)/config/project/$(cf),$(MTK_ROOT_CONFIG)/qishang/project/$(cf))) \
  $(if $(wildcard $(_project_overlay_dir)), \
    $(eval PRODUCT_PACKAGE_OVERLAYS += $(_project_overlay_dir)) \
    , \
   ) \
 )

 


ifeq ($(MTK_EMMC_SUPPORT), yes)
jzslocalflashsize := $(strip $(QS_CUSTOM_MEMORY_EXT_INFO))
else
jzslocalflashsize := $(strip $(MTK_NAND_MEMORY_SIZE))
endif


ifneq ($(filter %G32G, $(jzslocalflashsize)),)

PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.heapgrowthlimit=256m \
    dalvik.vm.heapsize=512m 
    
else ifneq ($(filter %G16G, $(jzslocalflashsize)),)

PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.heapgrowthlimit=192m \
    dalvik.vm.heapsize=512m 
    
else ifneq ($(filter %G8G, $(jzslocalflashsize)),)

PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.heapgrowthlimit=128m \
    dalvik.vm.heapsize=256m 

else ifneq ($(filter %G4G, $(jzslocalflashsize)),)

PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.heapstartsize=5m \
    dalvik.vm.heapgrowthlimit=96m \
    dalvik.vm.heapsize=128m \
    dalvik.vm.heapmaxfree=2m
    
else

PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.heapgrowthlimit=128m \
    dalvik.vm.heapsize=256m 
    
endif



$(call inherit-product-if-exists, $(JZS_PATH_SOURCES)/build/product/$(TARGET_PRODUCT).mk)
$(call inherit-product-if-exists, $(JZS_PATH_SOURCES)/frameworks/data/AllUserData.mk)


