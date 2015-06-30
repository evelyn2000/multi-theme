#!/bin/bash
# Copyright Statement:
#
# This software/firmware and related documentation ("MediaTek Software") are
# protected under relevant copyright laws. The information contained herein
# is confidential and proprietary to MediaTek Inc. and/or its licensors.
# Without the prior written permission of MediaTek inc. and/or its licensors,
# any reproduction, modification, use or disclosure of MediaTek Software,
# and information contained herein, in whole or in part, shall be strictly prohibited.


MKTOPDIR=$1
   
  
if [ -z ${TARGET_PRODUCT} ]; then
    echo "*** TARGET_PRODUCT is not set. stop"
    exit
fi

if [ -z ${QS_BUILD_VERNO} ]; then QS_BUILD_VERNO="1.00"; fi

if [ -z ${QS_DISABLE_DEBUGABLE} ]; then QS_DISABLE_DEBUGABLE="no"; fi

if [ -z ${MTK_CTA_SUPPORT} ]; then MTK_CTA_SUPPORT="no"; fi

# declare -l QS_MODEMTYPE="";
if [ -z ${MTK_MODEM_SUPPORT} ]; then 
QS_MODEMTYPE="modem_3g";
else
QS_MODEMTYPE=${MTK_MODEM_SUPPORT}; 
fi

QS_MODEMTYPE=${QS_MODEMTYPE#*_};

QS_SIMCARD_NUMBER="1sim";
if [ "${GEMINI}" = "yes" ]; then 
QS_SIMCARD_NUMBER="2sim";
fi

DEST_IMG_DIR_NAME="";
if [ -z ${QS_PRJ} ]; then
  DEST_IMG_DIR_NAME=${TARGET_PRODUCT};
else
	DEST_IMG_DIR_NAME=${QS_PRJ}
fi

DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_${QS_MODEMTYPE}_${QS_SIMCARD_NUMBER};


if [ ! -z ${QS_LCD_SIZE} ]; then 
DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_${QS_LCD_SIZE}; 
fi

if [ -z ${QS_DEFALUT_LANGUAGE_DEFINED} ]; then 
DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_eng; 
else
DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_${QS_DEFALUT_LANGUAGE_DEFINED}; 
fi

if [ "${MTK_CTA_SUPPORT}" = "yes" ]; then
	DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_cta
fi

if [ ! -z ${MTK_BUILD_VERNO} ]; then 
TEMP_VERNO_KEY=${MTK_BUILD_VERNO#ALPS.}
if [ "${TEMP_VERNO_KEY}" != "" ]; then
TEMP_VERNO_KEY=`tr '[A-Z]' '[a-z]' <<<"$TEMP_VERNO_KEY"`;
DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_${TEMP_VERNO_KEY}.${QS_BUILD_VERNO}; 
else
DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_v${QS_BUILD_VERNO}; 
fi
else
DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_v${QS_BUILD_VERNO}; 
fi

if [ -z ${TARGET_BUILD_VARIANT} ]; then 
	DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_debug
else	
	if [ "${TARGET_BUILD_VARIANT}" != "user" ]; then
		DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_debug
	else
		if [ ! -z ${MTK_MTKLOGGER_SUPPORT} ]; then 
			if [ "${MTK_MTKLOGGER_SUPPORT}" = "yes" ]; then
				DEST_IMG_DIR_NAME=${DEST_IMG_DIR_NAME}_loger
			fi
		fi
	fi
fi




TAR_SHOWTIMECMD=$(date +%Y%m%d_%H)
DEST_IMG_DIR=${MKTOPDIR}/out/Download/${DEST_IMG_DIR_NAME}

#echo ==${QS_PRJ}==${TARGET_PRODUCT}==${TAR_SHOWTIMECMD}===${DEST_IMG_DIR_NAME}===${QS_MODEMTYPE}==
#exit;

function copy_to_legacy_download_folder() {
  for item in $@; do
    if [ -e $item ]; then
      # chmod u+w $item
      # cp -f $item $legacy_download_path/
      echo =====$item=====
    fi
  done
}


function zip_download_folder() {

  rm -f ${DEST_IMG_DIR_NAME}_${TAR_SHOWTIMECMD}.zip
	rm -rf ${DEST_IMG_DIR}
	mkdir -p ${DEST_IMG_DIR}
	cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/*.img ${DEST_IMG_DIR}/
	
	if [ "${TARGET_PRODUCT}" != "generic" ]; then
		cp -fp ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/*.bin ${DEST_IMG_DIR}/
		cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/*Android_scatter*.txt ${DEST_IMG_DIR}/
		
		if [ -e ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/DSP_BL ]; then
			cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/DSP_BL ${DEST_IMG_DIR}/
		fi
		
		if [ -e ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/obj/CODEGEN/cgen ]; then
			cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/obj/CODEGEN/cgen/APDB_* ${DEST_IMG_DIR}/
		else
			cp -f ${MKTOPDIR}/mediatek/cgen/APDB_* ${DEST_IMG_DIR}/
		fi
		
		arrfiles=($CUSTOM_MODEM)
		for s in ${arrfiles[@]}
		do
	    if [ -e  ${MKTOPDIR}/mediatek/custom/common/modem/${s} ]; then
	    	cp -fp ${MKTOPDIR}/mediatek/custom/common/modem/${s}/BPLGUInfoCustomApp*  ${DEST_IMG_DIR}/
	    fi
		done
			
		if [ ! -z ${MTK_EMMC_SUPPORT} ]; then
			if [ "${MTK_EMMC_SUPPORT}" = "yes" ]; then
				cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/EBR* ${DEST_IMG_DIR}/
#				cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/EBR1 ${DEST_IMG_DIR}/
#				cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/EBR2 ${DEST_IMG_DIR}/
				cp -f ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/MBR ${DEST_IMG_DIR}/
			fi
		fi
		
		rm -f ${DEST_IMG_DIR}/APDB_*_ENUM
	fi
	
	if [ ! -z ${MTK_ADUPS_FOTA_SUPPORT} ]; then 
		if [ "${MTK_ADUPS_FOTA_SUPPORT}" = "yes" ]; then
			cp -fp ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/adups-*.zip ${DEST_IMG_DIR}/
		fi
	fi
	
	if [ -e ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/target_files-package.zip ]; then 
		cp -fp ${MKTOPDIR}/out/target/product/${TARGET_PRODUCT}/target_files-package.zip ${DEST_IMG_DIR}/
	fi
	
	OUT_CHANGENOTES_FILES_COUNT=0
	OUT_CHANGENOTES_FILES=""
	arrfiles=($QS_CHANGE_NOTES_FILE_DEF)
	for s in ${arrfiles[@]}
	do
    if [ -e ${MKTOPDIR}/mediatek/config/qishang/changenotes/${s} ]; then
    	OUT_CHANGENOTES_FILES_COUNT=1
    	OUT_CHANGENOTES_FILES+=${s}
    	OUT_CHANGENOTES_FILES+=" "
    	cp -f ${MKTOPDIR}/mediatek/config/qishang/changenotes/${s} ${DEST_IMG_DIR}/changenotes_for_${s}
    fi
	done
	
	arrfiles=($QS_CHANGE_NOTES_FILE)
	for s in ${arrfiles[@]}
	do
    if [ -e ${MKTOPDIR}/mediatek/config/qishang/changenotes/${s} ]; then
    	OUT_CHANGENOTES_FILES_COUNT=1
    	OUT_CHANGENOTES_FILES+=${s}
    	OUT_CHANGENOTES_FILES+=" "
    	
    	cp -f ${MKTOPDIR}/mediatek/config/qishang/changenotes/${s} ${DEST_IMG_DIR}/changenotes_for_${s}
    fi
	done

	if [ $OUT_CHANGENOTES_FILES_COUNT != "0" ]; then
		cd ${MKTOPDIR}/mediatek/config/qishang/changenotes
		cat ${OUT_CHANGENOTES_FILES} > ${DEST_IMG_DIR}/changenotes_all_in_one.txt
	fi
	
	# if [ -e ${MKTOPDIR}/out/host/linux-x86/bin/CKSM_Gen ]; then
	if [ -e ${MKTOPDIR}/zlingyin.source/build/tools/cksmgen/CKSM_Gen_kk2 ]; then
		chmod a+x ${MKTOPDIR}/zlingyin.source/build/tools/cksmgen/CKSM_Gen_kk2
	  ${MKTOPDIR}/zlingyin.source/build/tools/cksmgen/CKSM_Gen_kk2 ${DEST_IMG_DIR} ${DEST_IMG_DIR}
  fi

	cd ${MKTOPDIR}/out/Download

	if [ -d ${DEST_IMG_DIR_NAME} ]; then
    echo ========start zip images=============
    chmod +r ${DEST_IMG_DIR_NAME}/*
		# tar -czf ${DEST_IMG_DIR_NAME}_${TAR_SHOWTIMECMD}.tar.gz ${DEST_IMG_DIR_NAME}
		zip -j9 ${DEST_IMG_DIR_NAME}_${TAR_SHOWTIMECMD}.zip ${DEST_IMG_DIR_NAME}/*
		echo zip completed:[out/Download/${DEST_IMG_DIR_NAME}_${TAR_SHOWTIMECMD}.zip]
	fi

	cd ${MKTOPDIR}
}

zip_download_folder