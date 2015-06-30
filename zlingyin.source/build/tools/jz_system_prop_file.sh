#!/system/bin/sh

function jzs_function_mark_prop()
{
	LOCAL_PROP_FILE=${1};
	#echo "#=PROP_FILE:${LOCAL_PROP_FILE}=";
	LOCAL_CHECK_STR='(ro.sys.usb.storage.type=mtp,mass_storage|dalvik.vm.heapgrowthlimit=128m|dalvik.vm.heapsize=256m)';
	if [ -e ${LOCAL_PROP_FILE} ]; then
		if [ ! -z "`cat ${LOCAL_PROP_FILE} | grep -E ${LOCAL_CHECK_STR}`" ]; then 
		
			sed -i "s/ro.sys.usb.storage.type=mtp,mass_storage/#ro.sys.usb.storage.type= mtp,mass_storage/g; \
						s/dalvik.vm.heapgrowthlimit=128m/#dalvik.vm.heapgrowthlimit= 128m/g; \
						s/dalvik.vm.heapsize=256m/#dalvik.vm.heapsize= 256m/g; \
						" ${LOCAL_PROP_FILE};
						
		fi
	fi
	
}

jzs_function_mark_prop $1