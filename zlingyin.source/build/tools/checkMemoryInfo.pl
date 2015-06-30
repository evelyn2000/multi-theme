#!/usr/bin/perl
my $project = $ARGV[0];

my $QsExtName = "";
my $QsTempParam = lc($ENV{"MTK_EMMC_SUPPORT"});
if($QsTempParam eq "yes"){
	$QsExtName = ".emmc";
} else {
	$QsExtName = ".nand";
	$QsTempParam = uc($ENV{"MTK_NAND_MEMORY_SIZE"});
	if($QsTempParam && ($QsTempParam ne " ")){
		$QsExtName = $QsExtName.".${QsTempParam}";
	}
	
	$QsTempParam = uc($ENV{"MTK_NAND_PAGE_SIZE"});
	if($QsTempParam && ($QsTempParam ne "2K")){
		$QsExtName = $QsExtName.".${QsTempParam}";
	}
}

$QsTempParam = $ENV{"QS_CUSTOM_MEMORY_EXT_INFO"};
if($QsTempParam && ($QsTempParam ne " ")){
	$QsExtName = $QsExtName.".${QsTempParam}";
}

my $memoryfile = "mediatek/custom/${project}/preloader/inc/custom_MemoryDevice${QsExtName}.h";

if(-e $memoryfile){
#	printf "${memoryfile}==exist=\r\n";
	system("cp  -f ${memoryfile} mediatek/custom/${project}/preloader/inc/custom_MemoryDevice.h");
} else {
	printf "error:can't find:${memoryfile}\r\n";
}

exit 0;
