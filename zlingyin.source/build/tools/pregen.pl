#!usr/bin/perl
my $jzs_config_Prj_File = $ARGV[0];
my $jzs_prj_name = $ARGV[1];
my $jzs_full_prj_name = $ARGV[2];

my $jzsrootpath = $ENV{"JZS_PATH_SOURCES"};
if(!$jzsrootpath){
	$jzsrootpath = "zlingyin.source";
}

system("perl ${jzsrootpath}/build/tools/javaoptgen.pl ${jzs_config_Prj_File}");


