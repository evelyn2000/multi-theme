#!/usr/bin/perl
my $action=$ARGV[0];
my $project = $ARGV[1];
my $QsSubProjectName=$ARGV[2];
$QsSubProjectName="" if($QsSubProjectName eq "none");

my $QsInRootDir = $ENV{"JZS_CONFIG_ROOT_PATH"};
if(($QsInRootDir eq "") or (!-e $QsInRootDir)){
	$QsInRootDir="mediatek/config/qishang";
}

my $QsPrjFileRootDir = "${QsInRootDir}/project";
my $QsOutRootTempDir = $ENV{"JZS_OUT_COVER_DIR"};
$QsOutRootTempDir="out/qishangtempprjout" if($QsOutRootTempDir eq "");

my $QsOutRootDir = "${QsOutRootTempDir}/out/target/product/$project";



if ($project eq "emulator")
{
	$QsOutRootDir = "${QsOutRootTempDir}/out/target/product/generic";
}

# print "${action}=${project}=${QsSubProjectName}=\r\n";

&JzCheckMemoryInfo;

if($action =~ /^(new|n|bm_new|remake|r|bm_remake|clean|c|cleanqs|syncqs|systemimage)/){

	JzRemoveLogFile("qs_sync_make.log", 2);
	JzRemoveLogFile("qs_sync.log", 0);
	
	if($action eq "cleanqs"){
	 exit 0;
	}

	if(($action ne "clean") and ($action ne "c") ){
		&JzStartSync;
	}
	
	if($action eq "syncqs"){
	 exit 0;
	}
}

exit 0;

# ==============================================================================================
sub JzStartSync
{
	my $QsTempParam;
	if (-e $QsOutRootTempDir)
	{
		system("rm -rf $QsOutRootTempDir");
	}
	
	system("mkdir -p $QsOutRootTempDir");
	
	my $skip_dr_mk_file = "${QsOutRootTempDir}/Android.mk";
	die "can NOT open $skip_dr_mk_file:$!" if ( ! open OUT_FILE, ">$skip_dr_mk_file");
	print OUT_FILE "# empty \n\n";
	close OUT_FILE;
	
	
	$QsTempParam = $ENV{"QS_ENABLE_CDROM_ISO_FILE"};
	if($QsTempParam){
		system("rm -f system/mobile_toolkit/QsSmartPhoneTools.iso");
		if(-e "$QsInRootDir/project/${QsTempParam}"){
    	system("cp -f system/mobile_toolkit/${QsTempParam} system/mobile_toolkit/QsSmartPhoneTools.iso");
    } elsif (-e "system/mobile_toolkit/QsSmartPhoneTools.def.iso") {
    	system("cp -f system/mobile_toolkit/QsSmartPhoneTools.def.iso system/mobile_toolkit/QsSmartPhoneTools.iso");
    }
	}
		
	#====================================================
	if ($QsSubProjectName)
	{
		system("mkdir -p $QsOutRootDir");
		system("mkdir -p $QsOutRootDir/system/etc");
		system("mkdir -p $QsOutRootDir/system/media");
		system("mkdir -p $QsOutRootDir/system/media/images");
		system("mkdir -p $QsOutRootDir/system/app");
		system("mkdir -p $QsOutRootDir/system/lib");
		system("mkdir -p $QsOutRootDir/system/vendor/app");
		system("mkdir -p $QsOutRootDir/system/vendor/bin");
		system("mkdir -p $QsOutRootDir/system/vendor/lib");
		system("mkdir -p $QsOutRootDir/system/vendor/operator/app");
		system("mkdir -p $QsOutRootDir/data/app");
		# -P qs000000999999
    system("zip -o -j $QsOutRootDir/system/etc/outqsprjcfg.bin mediatek/config/${project}/ProjectConfig.mk");
    system("cp -f $QsOutRootDir/system/etc/outqsprjcfg.bin $QsOutRootDir/");
		
		#=====================================================
	  $QsTempParam = lc($ENV{"QS_DEVICES_SOUND_STYLE"});
	  if($QsTempParam)
	  {		
	    if(!-e "$QsOutRootDir/system/media/audio")
	    {
	    	system("mkdir -p $QsOutRootDir/system/media/audio");
	    }
			
			foreach $var (split /\s+/, $QsTempParam)
	    {
	      next if ($var eq "");
	      next if ($var eq "skipdefault");
				if(-e "$QsInRootDir/sound/$var")
		    {
		    	system("cp -rf $QsInRootDir/sound/$var/* $QsOutRootDir/system/media/audio");
		    }
	    }
		}
		
		$QsTempParam = $ENV{"QS_DEFAULT_WALLPAPER"};
	  if($QsTempParam)
	  {
	  	if(-e "$QsInRootDir/wallpaper/$QsTempParam")
	  	{
	  		if(!-e "$QsOutRootDir/system/media/images")
		    {
		    	system("mkdir -p $QsOutRootDir/system/media/images");
		    }
		    $qssubfilepos = rindex($QsTempParam, ".");
        if($qssubfilepos > 0)
        {
            $qsfileextname = substr($QsTempParam, $qssubfilepos, length($QsTempParam));
            system("cp -f $QsInRootDir/wallpaper/$QsTempParam $QsOutRootDir/system/media/images/default_wallpaper${qsfileextname}");
        }
	  	}
	  }
	  
	  $QsTempParam = $ENV{"QS_DEFAULT_LOCKSCREEN_WALLPAPER"};
	  if($QsTempParam)
	  {
	  	if(-e "$QsInRootDir/wallpaper/$QsTempParam")
	  	{
	  		if(!-e "$QsOutRootDir/system/media/images")
		    {
		    	system("mkdir -p $QsOutRootDir/system/media/images");
		    }
		    $qssubfilepos = rindex($QsTempParam, ".");
        if($qssubfilepos > 0)
        {
            $qsfileextname = substr($QsTempParam, $qssubfilepos, length($QsTempParam));
            system("cp -f $QsInRootDir/wallpaper/$QsTempParam $QsOutRootDir/system/media/images/default_lock_wallpaper${qsfileextname}");
        }
	  	}
	  }
		
		$QsTempParam = $ENV{"QS_DEVICES_POWER_ON_TONE"};
	  if($QsTempParam)
	  {
	  	if(-e "$QsInRootDir/sound/bootsound/$QsTempParam")
	  	{
	  		if(!-e "$QsOutRootDir/system/media")
		    {
		    	system("mkdir -p $QsOutRootDir/system/media");
		    }
	  		system("cp -f $QsInRootDir/sound/bootsound/$QsTempParam $QsOutRootDir/system/media/bootaudio.mp3");
	  	}
	  }
	  
	  $QsTempParam = $ENV{"QS_DEVICES_POWER_OFF_TONE"};
	  if($QsTempParam)
	  {
	  	if(-e "$QsInRootDir/sound/bootsound/$QsTempParam")
	  	{
	  		if(!-e "$QsOutRootDir/system/media")
		    {
		    	system("mkdir -p $QsOutRootDir/system/media");
		    }
	  		system("cp -f $QsInRootDir/sound/bootsound/$QsTempParam $QsOutRootDir/system/media/shutaudio.mp3");
	  	}
	  }
		
		$QsTempParam = $ENV{"QS_OVERRIDE_DIRECTORYS_NAME"};
		#print "=QS_OVERRIDE_DIRECTORYS_NAME=${QsTempParam}===\n";
		if($QsTempParam)
		{
			#print "=000=${QsTempParam}===\n";
			foreach $var (split /\s+/, $QsTempParam)
	    {
	        next if ($var eq "");
	        next if (!-e "$QsPrjFileRootDir/$var");
					system("cp -rf $QsPrjFileRootDir/$var/* $QsOutRootTempDir/");
	    }
	    
	    if(-e "$QsOutRootTempDir/outproject"){
	    	system("cp -rf $QsOutRootTempDir/outproject/* $QsOutRootDir/");
	    	system("rm -rf $QsOutRootTempDir/outproject");
	    }
	    
	    if(-e "$QsOutRootTempDir/makeproject"){
	    	system("mkdir -p $QsOutRootTempDir/mediatek/config/${project}");
	    	system("cp -rf $QsOutRootTempDir/makeproject/* $QsOutRootTempDir/mediatek/config/${project}/");
	    	system("rm -rf $QsOutRootTempDir/makeproject");
	    }
	  }
	  
	  #=====================================================
	  $QsTempBuiltInFiles = "";
	  $QsTempParam = $ENV{"USE_JZS_GLOBAL_VERSION"};
	  if($QsTempParam && ($QsTempParam > 001)){
	  	#print "=000=${QsTempParam} > 001===\n";
	  } else {
	  	#print "=111=${QsTempParam} <= 001===\n";
		  $QsTempParam = $ENV{"QS_ENABLE_GOOGLE_FACELOCK"};
		  if($QsTempParam && ($QsTempParam ne " ") && ($QsTempParam ne "no")){
		  	if($QsTempParam eq "yes"){
		  	 $QsTempBuiltInFiles = "facelock.jb2.txt";
			 } else {
		  		$QsTempBuiltInFiles = $QsTempBuiltInFiles . " facelock.${QsTempParam}.txt";
		  	}
		  }
	  
		  $QsTempParam = $ENV{"QS_ENABLE_GOOGLE_BASE_APPS"};
		  if($QsTempParam && ($QsTempParam ne " ") && ($QsTempParam ne "no")){
		  	if($QsTempParam eq "yes"){
		  	 	$QsTempBuiltInFiles = $QsTempBuiltInFiles . " gsf.jb2.txt";
		  	} else {
		  		$QsTempBuiltInFiles = $QsTempBuiltInFiles . " gsf.${QsTempParam}.txt";
		  	}
		  }
	  
		  $QsTempParam = $ENV{"QS_ENABLE_GOOGLE_TTS_VOICES_APPS"};
		  if($QsTempParam&& ($QsTempParam ne " ") && ($QsTempParam ne "no")){
			  if($QsTempParam eq "yes"){
			  	 $QsTempBuiltInFiles = $QsTempBuiltInFiles . " gsf_ext.jb2.txt";
			  }  else {
		  		$QsTempBuiltInFiles = $QsTempBuiltInFiles . " gsf_ext.${QsTempParam}.txt";
		  	}
		  }
	  }
	  
	  $QsTempParam = $ENV{"QS_OVERRIDE_APPLICATIONS_FILE"};
	  if($QsTempParam)
	  {
	  	if($QsTempBuiltInFiles){
	  		$QsTempBuiltInFiles = $QsTempBuiltInFiles . " " . $QsTempParam;
	  	} else {
	  		$QsTempBuiltInFiles = $QsTempParam;
	  	}
	  }
	  
	  if($QsTempBuiltInFiles){
	  
	  	# print "=111=${QsTempBuiltInFiles}===\n";
	  	foreach $var (split /\s+/, $QsTempBuiltInFiles)
	    {
	    		# print "=222=${var}===\n";
	        next if ($var eq "");
					if(-e "$QsPrjFileRootDir/$var")
		    	{
		    		# print "=333=${var}===\n";
		  			my $qsparentdirname = "";
			    	my $qssubfilepos = "";
			    	open (FILE_HANDLE, "<$QsPrjFileRootDir/$var") or die "cannot open $QsPrjFileRootDir/$var\n";
			      while (<FILE_HANDLE>) 
			      {
			          if(!/^#/)
			          {
			              # if (/(.*)\s>\s(.*)/) 
			              # if (/^(\S+)\s*>\s*([^\n\t\r\f]+)/) 
			              if (/^(\S+)\s*>\s*(\S+)/) 
			              {
			              	# print "==${1}=${2}==\n";
			              	# support for delete file or folder, format : rm > /abc
		              		if(lc($1) eq "rm")
		              		{
		              			system("rm -rf $QsOutRootDir/$2");
		              		}
		              		else
		              		{
			              		$qssubfilepos = rindex($2, "/");
			                  if($qssubfilepos > 0)
			                  {
			                      $qsparentdirname = substr($2, 0, $qssubfilepos);
			                      if(!-e "$QsOutRootDir/$qsparentdirname")
			                      {
			                        system("mkdir -p $QsOutRootDir/$qsparentdirname");
			                      }
			                  }
			                  
												if(-e "$QsInRootDir/$1"){
					                
					                system("cp -rf $QsInRootDir/$1 $QsOutRootDir/$2");
					                
					              }
				                else
				                { 
				                	# support format: /abc/* > /abc
				                	$qssubfilepos = length($1);
				                	if(($qssubfilepos > 0) && (substr($1, $qssubfilepos-1, $qssubfilepos) eq "*")){
				                		
					                	system("cp -rf $QsInRootDir/$1 $QsOutRootDir/$2");
					                	
					                }
				                }
			                }
	
			              }
			          }
			      }
			      close FILE_HANDLE;
		    	}
	    }
	  	
	  }

	  $QsTempParam = $ENV{"QS_BOOTANIMATION_STYLE"};
	  if($QsTempParam)
	  {
	  	if(-e "$QsInRootDir/bootanimation/$QsTempParam")
	  	{
	  		if(!-e "$QsOutRootDir/system/media")
		    {
		    	system("mkdir -p $QsOutRootDir/system/media");
		    }
	  		system("cp -f $QsInRootDir/bootanimation/$QsTempParam $QsOutRootDir/system/media/bootanimation.zip");
	  	}
	  }
	  
	  $QsTempParam = $ENV{"QS_BOOTANIMATION_STYLE_EXT"};
    if($QsTempParam)
    {
    	if(!-e "$QsOutRootDir/system/media/bootant")
	    {
	    	system("mkdir -p $QsOutRootDir/system/media/bootant");
	    }

    	foreach $var (split /\s+/, $QsTempParam)
      {
          next if ($var eq "");
        #  print "=111=var:${var}===\n";
          my @vargroup = split(":", $var);
          my $destfile = @vargroup[1];
			    $var = @vargroup[0];
			   # print "=222=var:${var}===destfile:${destfile}===\n";
					if(-e "$QsInRootDir/bootanimation/$var")
			    {
			    	if($destfile){
			    		system("cp -f $QsInRootDir/bootanimation/$var $QsOutRootDir/system/media/bootant/$destfile");
			    	} else {
			    		system("cp -f $QsInRootDir/bootanimation/$var $QsOutRootDir/system/media/bootant/$var");
			    	}
			    }
      }
    }
	  
	  $QsTempParam = $ENV{"QS_SHUTDOWNANIMATION_STYLE"};
	  if($QsTempParam)
	  {
	  	if(-e "$QsInRootDir/bootanimation/$QsTempParam")
	  	{
	  		if(!-e "$QsOutRootDir/system/media")
		    {
		    	system("mkdir -p $QsOutRootDir/system/media");
		    }
	  		system("cp -f $QsInRootDir/bootanimation/$QsTempParam $QsOutRootDir/system/media/shutanimation.zip");
	  	}
	  }
	  
	  $QsTempParam = $ENV{"QS_SHUTDOWNANIMATION_STYLE_EXT"};
    if($QsTempParam)
    {
    	if(!-e "$QsOutRootDir/system/media/shutant")
	    {
	    	system("mkdir -p $QsOutRootDir/system/media/shutant");
	    }

    	foreach $var (split /\s+/, $QsTempParam)
      {
          next if ($var eq "");
					my @vargroup = split(":", $var);
		      my $destfile = @vargroup[1];
			    $var = @vargroup[0];
					if(-e "$QsInRootDir/bootanimation/$var")
			    {
			    	if($destfile){
			    		system("cp -f $QsInRootDir/bootanimation/$var $QsOutRootDir/system/media/shutant/$destfile");
			    	} else {
			    		system("cp -f $QsInRootDir/bootanimation/$var $QsOutRootDir/system/media/shutant/$var");
			    	}
			    }
      }
    }
	
		#=====================================================
	  
	  if(-e "$QsInRootDir/default/skipdirfile.txt")
	  {
	  	system("cp -f $QsInRootDir/default/skipdirfile.txt $QsOutRootDir/system/etc/.skipdirfile");
	  }
	  #=====================================================
	  $qs_log_write_filename = "$QsOutRootDir/.qsdefaultbackupfiles.txt";
	  die "can NOT open $qs_log_write_filename:$!" if ( ! open QS_BK_OUT_FILE, ">$qs_log_write_filename");
	  print QS_BK_OUT_FILE ".qsdefaultbackupfiles.txt\n";
	  listdir("$QsOutRootDir/data");
	  close QS_BK_OUT_FILE;
	  
	  system("mv -f $qs_log_write_filename $QsOutRootDir/data");
	  
	
	  #=====================================================
	  $QsTempParam = $ENV{"QS_SPECIAL_CUSTOM_INFOMATION"};
		if($QsTempParam && ($QsTempParam ne " ") )
		{
			foreach $var (split /\s+/, $QsTempParam)
		  {
		      next if ($var eq "");
		      #next if (!-e "mediatek/config/qishang/project/${var}");
					#system("rsync -av --exclude=.svn --exclude=.git --exclude=.cvs mediatek/config/qishang/project/${var}/* ./ > qs_sync_mk.log 2>&1");
					
	        next if (!-e "$QsPrjFileRootDir/$var");
					system("cp -rf $QsPrjFileRootDir/$var/* $QsOutRootTempDir/");
		  }
		}
	}
  $qs_log_write_filename = "qs_sync_make.log";
  die "can NOT open $qs_log_write_filename:$!" if ( ! open QS_BK_LOG_FILE, ">$qs_log_write_filename");
  
  my $folder;
  opendir ( $folder, $QsOutRootTempDir ) || die print 'error';
  my @flist = readdir( $folder );
  closedir( $folder );
  for( my $i = 0; $i < scalar( @flist ); $i++ ) {

      next if($flist[ $i ] eq ".");
      next if($flist[ $i ] eq "..");
      next if($flist[ $i ] eq ".svn");
      next if($flist[ $i ] eq ".cvs");
      next if($flist[ $i ] eq ".git");
      $srcDir = "$QsOutRootTempDir/$flist[ $i ]";
      $dstDir = "$flist[ $i ]";
      if (-d $srcDir)
      {
      	#print "=dir=srcDir:$srcDir===\n";
      	if($dstDir eq "out"){
    			logsubfile_del($srcDir);
    		}else{
   				logsubfile($srcDir);
   			}
     		
      	
      	system("rsync -av --exclude=.svn --exclude=Thumbs.db --exclude=.git --exclude=.cvs $srcDir/ $dstDir/ > auto_sync.log 2>&1");
      }
  }
  
  close QS_BK_LOG_FILE;
}

sub logsubfile{
	my @arr, $j = 0, $file, $subDirCount;
	my $qssubfilepos = length("$QsOutRootTempDir/");
	for($i=0;$i<=$#_;$i++) 
	{
	  if(-d $_[$i]) 
	  {
		   if(opendir($handle, $_[$i])) 
		   {
		   		$subDirCount = 0;
			    while($dir = readdir($handle)) 
			    {
			    	 next if($dir eq ".");
			    	 next if($dir eq "..");
			    	 next if($dir eq ".svn");
			    	 next if($dir eq ".cvs");
			    	 next if($dir eq ".git");
				     if(!($dir =~ m/^\.$/) and !($dir =~ m/^(\.\.)$/) ) 
				     {
				     		$file = $_[$i]."/$dir";
					      #print "=dir==$file====\n";
					      if(-d $file) 
					      {
					       	$arr[$j++] = $file;
					       	$subDirCount++;
					       #print "\t[DIR]"
					      }
					      elsif(-e $file)
					      {
					      	$file = substr($_[$i], $qssubfilepos)."/$dir";
					      	if(-e $file)
					      	{
					      		print QS_BK_LOG_FILE "U > ${file}\n";
					      	}
					      	else
					      	{
					      		print QS_BK_LOG_FILE "D > ${file}\n";
					      	}
					      }
					      #print "\n";
				     }
			    }
			    closedir($handle);
			    
			    if($subDirCount == 0)
			    {	
			    	$file = substr($_[$i], $qssubfilepos);
			    	if(substr($file, 0, 3) ne "out")
			    	{
			    		# print "=no sub dir==$file====\n";
			    		if(!-e $file)
			    		{
			    			print QS_BK_LOG_FILE "D > ${file}\n";
			    		}
			    	}
			    }
		   }
	  }
	  elsif(-e $_[$i])
	  {
	  	$file = substr($_[$i], $qssubfilepos);
	  	#print "=file==${file}====\n";
	  	if(-e $file)
    	{
    		print QS_BK_LOG_FILE "U > ${file}\n";
    	}
    	else
    	{
    		print QS_BK_LOG_FILE "D > ${file}\n";
    	}
	  }
	}
	
	if($j>0) {
		logsubfile (@arr);
	}
}

sub logsubfile_del{
	my @arr, $j = 0, $file;
	my $qssubfilepos = length("$QsOutRootTempDir/");
	for($i=0;$i<=$#_;$i++) 
	{
	  if(-d $_[$i]) 
	  {
		   if(opendir($handle, $_[$i])) 
		   {
			    while($dir = readdir($handle)) 
			    {
			    	 next if($dir eq ".");
			    	 next if($dir eq "..");
			    	 next if($dir eq ".svn");
			    	 next if($dir eq ".git");
			    	 next if($dir eq ".cvs");
				     if(!($dir =~ m/^\.$/) and !($dir =~ m/^(\.\.)$/) ) 
				     {
				     		$file = $_[$i]."/$dir";
					      #print "=dir==$file====\n";
					      if(-d $file) 
					      {
					       $arr[$j++] = $file;
					       #print "\t[DIR]"
					      }
					      elsif(-e $file)
					      {
					      	$file = substr($_[$i], $qssubfilepos)."/$dir";
					      	print QS_BK_LOG_FILE "D > ${file}\n";
					      }
					      #print "\n";
				     }
			    }
			    closedir($handle);
		   }
	  }
	  elsif(-e $_[$i])
	  {
	  	$file = substr($_[$i], $qssubfilepos);
	  	#print "=file==${file}====\n";
	  	print QS_BK_LOG_FILE "D > ${file}\n";
	  }
	}
	
	if($j>0) {
		logsubfile_del (@arr);
	}
}

sub JzRemoveLogFile
{
	my ($file, $version) = @_;
	
	# print "==JzRemoveLogFile: ${file}==${version}==\n";
	
	if(-e "$file"){
		
		if (open FILE_HANDLE, "<$file")
	  {
	  	if($version > 0){
	  	
		    while (<FILE_HANDLE>)
				{
					if (/^(\S+)\s*>\s*(\S+)/)
					{
						# print "== $1 > $2 ==\n";
						next if (!-e "$2");
						
						if($1 eq "D")
						{
							# print "== delete : $2 ==\n";
							if(-d "$2"){
								# $dir = $2."/.svn";
								# if(!-e "$dir"){
								if(checkIsEmptyDir($2)){
								# print "== delete dir: $2 ==\n";
									system("rm -rf $2 > qs_del.log 2>&1");
								}
							} else {
								system("rm -f $2 > qs_del.log 2>&1");
							}
						}
						elsif($1 eq "U")
						{
								next if (-d "$2");
						#		print "== revert ==\n";
								system("svn -q revert $2 > qs_svn.log 2>&1");
						}
					}			
				}
				
			}else{
			
				while (<FILE_HANDLE>)
				{
					chomp($_);
					next if (!-e "$_");
					
					if(substr($_, 0, 3) eq "out")
					{
						system("rm -f $_ > qs_del.log 2>&1");
					}
					else
					{
						next if (-d "$_");
							
					# print "==== $_ ===\n";
							
						system("svn -q revert $_ > qs_svn.log 2>&1");
					}
				}
			}
			
	    close FILE_HANDLE;
	  }

		system("rm -f $file");
	}
}

sub listdir {
 my @arr, $j = 0, $file;
 my $qssubfilepos = length("$QsOutRootDir/data/");
 for($i=0;$i<=$#_;$i++) 
 {
	  if(-d $_[$i]) 
	  {
		   if(opendir($handle, $_[$i])) 
		   {
			    while($dir = readdir($handle)) 
			    {
			    	 next if($dir eq ".");
			    	 next if($dir eq "..");
			    	 next if($dir eq ".svn");
			    	 next if($dir eq ".qsbootinit");
			    	 next if($dir eq ".git");
			    	 next if($dir eq ".cvs");
				     if(!($dir =~ m/^\.$/) and !($dir =~ m/^(\.\.)$/) ) 
				     {
					      #print "$dir";
					      if(-d $_[$i]."/$dir") 
					      {
					       $arr[$j++] = $_[$i]."/$dir";
					       #print "\t[DIR]"
					      }
					      else
					      {
					      	if($qssubfilepos >= length("$_[$i]"))
					      	{
					      		$file = "$dir";
					      	}
					      	else
					      	{
					      		$file = substr($_[$i], $qssubfilepos)."/$dir";
					      	}
					      	#$file = substr($_[$i], $qssubfilepos)."/$dir";
					      	print QS_BK_OUT_FILE "${file}\n";
					      }
					      #print "\n";
				     }
			    }
			    closedir($handle);
		   }
	  }
 }
 
 if($j>0) {
  listdir (@arr);
 }
}

sub JzCopyFiles{
    my ($rootpath) = @_;
    my $tempfiles = "";
    my @default_cover_folders = qw(cleanspec frameworks bootable packages build outproject);
    foreach $sub (@default_cover_folders) {
     if ($sub) {

       if(-e "$rootpath/$sub")
       {
       	 #print "==folder:$rootpath/$sub===\n";
         if ($sub eq "outproject")
         {
						system("cp  -rf $rootpath/$sub/* $QsOutRootDir > qs_make_log.log 2>&1");
					#	system("rsync -av --exclude=.svn --exclude=.git --exclude=.cvs $rootpath/$sub/ $QsOutRootTempDir/ > auto_sync.log 2>&1");
         }
         elsif($sub eq "cleanspec")
         {
         	 
         		open (FILE_HANDLE, "<$rootpath/$sub") or die "cannot open $rootpath/$sub\n";
         		while (<FILE_HANDLE>) 
			      {
			          if(!/^#/)
			          {
			          		$tempfiles = $_;
			          	  #print "remove files:$tempfiles====\n";
			          	  next if($tempfiles eq "");
			          	  
			          		if(-e "$tempfiles")
			          		{
			          			system("rm -rf $tempfiles");
			          		}
			          		else
			          		{
			          			# print "remove files:$tempfiles==not exist==\n";
			          		}
			          }
			      }
			      close FILE_HANDLE;
         }
         elsif(-e "$rootpath/$sub")
         {
         	 if(-e "$QsOutRootTempDir/$sub")
         	 {
           	system("cp  -rf $rootpath/$sub/* $QsOutRootTempDir/$sub");
           }
           else
           {
           	system("cp  -rf $rootpath/$sub $QsOutRootTempDir/$sub");
           }
           #system("rsync -av --exclude=.svn --exclude=.git --exclude=.cvs $rootpath/$sub/ $sub/ > auto_sync.log 2>&1");
         }
       }
       else
       {
           # print "folder:$rootpath/$sub is not exist...\n";
       }
     }
    }
}

sub checkIsEmptyDir
{
	my $testdirname = @_[0];
	my @testdir_files = <$testdirname/*>;
	# print "==1===$testdirname=========\n";
	if ( @testdir_files ) {
		return 0;
	} 
	
	# print "==2===$testdirname is empty. \n";
	
	return 1;
}

sub JzCheckMemoryInfo
{
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
	# printf "${memoryfile}==no exist=";
	if(-e $memoryfile){
		system("cp  -f ${memoryfile} mediatek/custom/${project}/preloader/inc/custom_MemoryDevice.h");
	} else {
		printf "error:can't find:${memoryfile}\r\n";
	}
	
	$QsTempParam = $ENV{"MTK_PLATFORM"};
	if($QsTempParam =~ /^(MT6592)/){
		$QsTempParam = $ENV{"QS_SUB_PROJECT_DWS_FILENAME"};
		if($QsTempParam && ($QsTempParam ne " ")){
			my $src_filedct_file = "mediatek/custom/${project}/kernel/dct/dct/${QsTempParam}.dws";
			if(-e $src_filedct_file){
				my $custom_dct = $ENV{"CUSTOM_KERNEL_DCT"};
				$custom_dct = "dct" if(!$QsTempParam || ($custom_dct eq " "));
				system("cp  -f ${src_filedct_file} mediatek/custom/${project}/kernel/dct/${custom_dct}/codegen.dws");
			}
		}
	}
}



