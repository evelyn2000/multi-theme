#!/usr/bin/perl

# jz

@jzs_actions = 
	qw(zipbin z cleanqs syncqs);

@QS_SUPPORT_MORE_VALUES_LIST = 
	qw(QS_OVERRIDE_APPLICATIONS_FILE QS_OVERRIDE_DIRECTORYS_NAME QS_SPECIAL_CUSTOM_INFOMATION 
		QS_INHERIT_PARENT_PROJECTS_NAME QS_DEVICES_SOUND_STYLE 
		QS_INIT_THIRD_PART_APP_LIST QS_INIT_THIRD_PART_APP_LIST_DEF
		QS_BOOTANIMATION_STYLE_EXT QS_SHUTDOWNANIMATION_STYLE_EXT 
		QS_SUPPORT_THEME_STYLE_RES QS_OVERRIDE_RES_DIRECTORYS_NAME QS_SUPPORT_ALL_THEME_STYLE_NAME);

my $JZS_CONFIG_ROOT_DIR = "mediatek/config/qishang";
my $JZS_OUT_COVER_DIR = "vendor/out.qs";

$ENV{"JZS_PATH_SOURCES"} = ${JZS_PATH_SOURCES};
$ENV{"JZS_CONFIG_ROOT_PATH"} = ${JZS_CONFIG_ROOT_DIR};
$ENV{"JZS_OUT_COVER_DIR"} = ${JZS_OUT_COVER_DIR};
$ENV{"USE_CCACHE"} = 1;

# ==============================================================================================
sub JzsFunctionTrim {
    my @out = @_;
    for (@out) {
        s/^\s+//;          # trim left
        s/\s+$//;          # trim right
    }
    
    return @out == 1
              ? $out[0]   # only one to return
              : @out;     # or many
}

sub JzsFunctionGetProjectFilePath
{
	my ($localproject, $localMainOrSub) = @_;
	#printf "JzsFunctionGetProjectFilePath=${localproject}=${localMainOrSub}=\n";
	if($localMainOrSub and ($localMainOrSub > 0)){
		return "${JZS_CONFIG_ROOT_DIR}/mak/${localproject}.mak";
	}
	
	return "mediatek/config/${localproject}/ProjectConfig.mk";
}

sub JzsFunctionCheckIsMainProject
{
	my $localproject = @_[0];
	if($localproject){
		#printf "===JzsFunctionCheckIsMainProject=${localproject}==\n";
		
		$localproject = "generic" if ($localproject eq "emulator");
		if(($localproject eq "generic") or ($localproject =~ m/^qishang\d{2,4}_/)){
			my $projectconfig = JzsFunctionGetProjectFilePath($localproject, 0);
			if (-e $projectconfig){
				return 1; # main project
			}
		}
	}

	return 0;
}

sub JzsFunctionCheckIsSubProject
{
	my $localproject = $_[0];
	#printf "===JzsFunctionCheckIsSubProject==='${localproject}'==\n";
	if($localproject){
		my $subprojectconfig = JzsFunctionGetProjectFilePath($localproject, 1);
		if (-e $subprojectconfig){
			return 1;
		}
	}

	return 0;
}

sub JzsFunctionGetMainProjectBySubProject
{
	my ($subproject, $defaultproject) = @_;
	#printf "===JzsFunctionGetMainProjectBySubProject==${subproject}==def:${defaultproject}==\n";
	#$prjkey = JzsFunctionTrim($prjkey) if($prjkey);
	
	my %prj_list = %JZSMTKPROJECTNAMES;
	#printf "JzsFunctionGetMainProject=0000==(@keyArrays)==${prj_list{'default'}}===${JZSMTKPROJECTNAMES{'default'}}=\n";
	if(!$defaultproject or JzsFunctionCheckIsMainProject($defaultproject) == 0){

		if(%JZS_PLATFORMS_LIST) {
			#printf "===JzsFunctionGetMainProjectBySubProject==333333==(@keyArrays)==\n";
			my @keyArrays = (keys %JZS_PLATFORMS_LIST);
			if(@keyArrays > 1){
				foreach $key (@keyArrays){
					#printf "JzsFunctionGetMainProject=222==${key}===\n";
					if($subproject =~ /_$key/){
						%prj_list = %{$JZS_PLATFORMS_LIST{$key}};
						last;
					}
				}
			}
		}
		
		$defaultproject = $prj_list{"default"};
	} 

	@keyArrays = (keys %prj_list);
	my $length = @keyArrays;
	#printf "JzsFunctionGetMainProject===${subproject}==def:${defaultproject}=(@keyArrays)=length:${length}=\n";

	if($length <= 1){
		return $defaultproject;
	}
	
	if($subproject){
		my $subprojectconfig = JzsFunctionGetProjectFilePath($subproject, 1);
		if (-e $subprojectconfig){
			open (FILE_HANDLE, "<${subprojectconfig}");
			while (<FILE_HANDLE>) 
			{
				chomp;
				next if(/^\#/);
				next if(/^\s*$/);
				if (/^(\w+)\s*=\s*(\w+)/) {
					#print "=='$1' = '$2' ==\n";
					if($2 eq "yes"){
						foreach $key (@keyArrays){
							if($1 eq $key){
								close FILE;
								return $prj_list{$key};
							}
						}
					}
				}
			}
			close FILE;
		} else {
			#print "==${subprojectconfig} not exist==\n";
		}
	}
	
	return $defaultproject;
	#$JZS_MTK_PROJECT_NAMES{"default"};
}

sub removedataimage
{
	my $rmjni = @_;
	#print "===action:$rmjni====\n";	
	if($rmjni && $rmjni > 0){
		system("rm -rf $out_dir/target/product/mediatek/frameworks-ext/base/core/jni");
	}
	
	if(-e "$out_dir/target/product/$project/userdata.img"){
		system("mv -f $out_dir/target/product/$project/userdata.img $out_dir/target/product/$project/userdata.img.bk");
	}
}

sub JzPreMakeInfo
{
	my ($localproject, $localsubproject, $localaction, $localOptsArray) = @_;
	#print "==localproject:${localproject}==localaction:${localaction}==sub:${localsubproject}==\n";
	
	push(@$localOptsArray, "QS_PRJ=${localsubproject}");
	push(@$localOptsArray, "QSPROJECT=${localsubproject}");

	if($localsubproject)
	{
		if($ENV{"QS_DISABLE_DEBUGABLE"} eq "yes")
		{
			push(@$localOptsArray, "TARGET_BUILD_VARIANT=user");
		}
	}
	
	$localaction = "none" if($localaction eq "");
	$localsubproject = "none" if($localsubproject eq "");
	
	&p_system("perl ${QsLyBuildRootDir}/tools/jzsMakeExt.pl ${localaction} ${localproject} ${localsubproject}");
}

sub JzMergeProjectInfo
{
	my ($localproject, $localsubproject, $jzcurplatform) = @_;
	$jzcurplatform = uc($jzcurplatform) if($jzcurplatform);
#	print "==localproject:${localproject}==jzcurplatform:${jzcurplatform}==sub:${localsubproject}==\n";
	die "project can't be null\n" if ($localproject eq "");
	
	my $QsCustomRootProjectMakeFile = "";
	my $QsSubProjectMakeFile = "";
	my $QsBaseProjectMakeFile = "mediatek/config/${localproject}/ProjectConfig_mtk.mk";
	$QsBaseProjectMakeFile = "" if(!-e $QsBaseProjectMakeFile);
	my $QsDestProjectMakeFile = "mediatek/config/${localproject}/ProjectConfig.mk";
	
	if ($localsubproject){
		 $QsSubProjectMakeFile = "${JZS_CONFIG_ROOT_DIR}/mak/${localsubproject}.mak";
		 $QsSubProjectMakeFile = "" if(!-e $QsSubProjectMakeFile);
	}
	
	if(-e $QsLyBuildRootDir){
		if($jzcurplatform && $jzcurplatform ne " "){
			$QsCustomRootProjectMakeFile = "${QsLyBuildRootDir}/product/ProjectConfig.${jzcurplatform}.mk";
			$QsCustomRootProjectMakeFile = "${QsLyBuildRootDir}/product/ProjectConfig.mk"  if(!-e $QsCustomRootProjectMakeFile);
		} else {
			$QsCustomRootProjectMakeFile = "${QsLyBuildRootDir}/product/ProjectConfig.mk";
		}
	}
	
	if($QsCustomRootProjectMakeFile || $QsSubProjectMakeFile){
		system("python mediatek/build/tools/config/merge-project.py ${QsBaseProjectMakeFile} ${QsCustomRootProjectMakeFile} ${QsSubProjectMakeFile} > ${QsDestProjectMakeFile}");
	} elsif ($QsBaseProjectMakeFile){
		system("cp -f ${QsBaseProjectMakeFile} ${QsDestProjectMakeFile}");
	}
	
#	&p_system("perl ${QsLyBuildRootDir}/tools/mergeProject.pl ${localproject} ${localsubproject} ${jzcurplatform}");
}

sub JzFunctionSetMoreSupportKeys
{
	my ($key, $line) = @_;
	#print "==key:${key}==line:${line}====\n";
	if($key and $line){		
		foreach $sKey (@QS_SUPPORT_MORE_VALUES_LIST)
	  {
	    if ($key eq $sKey)
	    {
	    	$_ = $line . " ";
	      if (/^(\S+)\s*=\s*([^\n\t\r\f]+)/) 
		    {
		    	#print "==$1 = $2==old:$ENV{$1}=\n";
		      $ENV{$1} = $2;
		    }
	      last;
	    }
	  }
	}
}
# ==============================================================================================