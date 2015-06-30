#!/usr/bin/perl
my $project = $ARGV[0];
my $QsSubProjectName=$ARGV[1];
my $projectplatform = $ARGV[2];
my $QsLyBuildRootDir = "zlingyin.source/build";

my $QsCustomRootProjectMakeFile = "";
my $QsSubProjectMakeFile = "";
if(-e $QsLyBuildRootDir){
	if($projectplatform && $projectplatform ne " "){
		$QsCustomRootProjectMakeFile = "${QsLyBuildRootDir}/product/${projectplatform}/ProjectConfig.mk";
		$QsCustomRootProjectMakeFile = "${QsLyBuildRootDir}/product/ProjectConfig.mk"  if(!-e $QsCustomRootProjectMakeFile);
	} else {
		$QsCustomRootProjectMakeFile = "${QsLyBuildRootDir}/product/ProjectConfig.mk";
	}
} 

if ($QsSubProjectName && $QsSubProjectName ne "none"){
	 $QsSubProjectMakeFile = "mediatek/config/qishang/mak/${QsSubProjectName}.mak";
	 $QsSubProjectMakeFile = "" if(!-e $QsSubProjectMakeFile);
}

my $QsBaseProjectMakeFile = "mediatek/config/${project}/ProjectConfig_mtk.mk";
$QsBaseProjectMakeFile = "" if(!-e $QsBaseProjectMakeFile);

my $QsDestProjectMakeFile = "mediatek/config/${project}/ProjectConfig.mk";

if($QsCustomRootProjectMakeFile || $QsSubProjectMakeFile){
	system("python mediatek/build/tools/config/merge-project.py ${QsBaseProjectMakeFile} ${QsCustomRootProjectMakeFile} ${QsSubProjectMakeFile} > ${QsDestProjectMakeFile}");
} elsif ($QsBaseProjectMakeFile){
	system("cp -f ${QsBaseProjectMakeFile} ${QsDestProjectMakeFile}");
}

exit 0;
