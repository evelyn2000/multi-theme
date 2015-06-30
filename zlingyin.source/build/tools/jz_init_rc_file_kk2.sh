#!/system/bin/sh

function jzs_function_init_rc()
{
	LOCAL_INIT_RC_FILE=${1};
	
	if [ "${ENABLE_JZS_POLICY}" = "yes" ]; then
		LOCAL_JZS_SYSTEM_JAR_LIST=":\/system\/framework\/zlingyin.common.jar:\/system\/framework\/zlingyin.framework.jar:\/system\/framework\/zlingyin.policy.jar";
	else
		LOCAL_JZS_SYSTEM_JAR_LIST=":\/system\/framework\/zlingyin.common.jar:\/system\/framework\/zlingyin.framework.jar";
	fi
	
	if [ -e ${LOCAL_INIT_RC_FILE} ]; then
		#echo "#=file:${LOCAL_INIT_RC_FILE}=exist=jar=${ENABLE_JZS_JARS}=";
		if [ "${ENABLE_JZS_JARS}" = "yes" ]; then 
			if [ ! -z "`cat ${LOCAL_INIT_RC_FILE} | grep :jzszlingyinframeworkjars`" ]; then 
				sed -i "s/:jzszlingyinframeworkjars/${LOCAL_JZS_SYSTEM_JAR_LIST}/g" ${LOCAL_INIT_RC_FILE}; 
			fi
		else
			if [ ! -z "`cat ${LOCAL_INIT_RC_FILE} | grep :jzszlingyinframeworkjars`" ]; then 
				sed -i "s/:jzszlingyinframeworkjars/ /g" ${LOCAL_INIT_RC_FILE}; 
			fi
		fi
		
		if [ -e ${JZS_PATH_SOURCES}/build/core/jzs_init_kk2.rc ]; then 
			if [ -z "`cat ${LOCAL_INIT_RC_FILE} | grep /system/bin/qsinitinfowz`" ]; then 
				cat ${JZS_PATH_SOURCES}/build/core/jzs_init_kk2.rc >> ${LOCAL_INIT_RC_FILE}; 
			fi 
		fi
	else
		echo "#=file:${LOCAL_INIT_RC_FILE}=not exist==";
	fi
	
}

jzs_function_init_rc $1