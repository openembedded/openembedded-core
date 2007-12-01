# IceCream distributed compiling support
#
# Stages directories with symlinks from gcc/g++ to icecc, for both
# native and cross compilers. Depending on each configure or compile,
# the directories are added at the head of the PATH list and ICECC_CXX
# and ICEC_CC are set.
#
# For the cross compiler, creates a tar.gz of our toolchain and sets
# ICECC_VERSION accordingly.
#
#The class now handles all 3 different compile 'stages' (i.e native ,cross-kernel and target) creating the
#necessary enviroment tar.gz file to be used by the remote machines.
#It also supports meta-toolchain generation
#
#If ICECC_PATH is not set in local.conf then the class will try to locate it using 'which'
#but nothing is sure ;)
#
#If ICECC_ENV_EXEC is set in local.conf should point to the icecc-create-env script provided by the user
#or the default one provided by icecc-create-env.bb  will be used
#(NOTE that this is a modified version of the script need it and *not the one that comes with icecc*
#
#User can specify if specific packages or packages belonging to class should not use icecc to distribute
#compile jobs to remote machines, but handled localy, by defining ICECC_USER_CLASS_BL and ICECC_PACKAGE_BL
#with the appropriate values in local.conf
#########################################################################################
#Error checking is kept to minimum so double check any parameters you pass to the class
###########################################################################################


def icc_determine_gcc_version(gcc):
    """
    Hack to determine the version of GCC

    'i686-apple-darwin8-gcc-4.0.1 (GCC) 4.0.1 (Apple Computer, Inc. build 5363)'
    """
    import os
    return os.popen("%s --version" % gcc ).readline().split()[2]

def create_cross_env(bb,d):
    """
    Create a tar.bz2 of the current toolchain
    """

    # Constin native-native compilation no environment needed if
    # host prefix is empty (let us duplicate the query for ease)
    prefix = bb.data.expand('${HOST_PREFIX}', d)
    if len(prefix) == 0:
        return ""

    import tarfile, socket, time, os
    ice_dir = bb.data.expand('${CROSS_DIR}', d)
    prefix  = bb.data.expand('${HOST_PREFIX}' , d)
    distro  = bb.data.expand('${DISTRO}', d)
    target_sys = bb.data.expand('${TARGET_SYS}',  d)
    target_prefix = bb.data.expand('${TARGET_PREFIX}',  d)
    float   = bb.data.getVar('TARGET_FPU', d) or "hard"
    name    = socket.gethostname()
  

    # Stupid check to determine if we have built a libc and a cross
    # compiler.
    try:
        os.stat(os.path.join(ice_dir, target_sys, 'lib', 'libc.so'))
        os.stat(os.path.join(ice_dir, target_sys, 'bin', 'g++'))
    except: # no cross compiler built yet
        return ""

    VERSION = icc_determine_gcc_version( os.path.join(ice_dir,target_sys,"bin","g++") )
    cross_name = prefix + distro + "-" + target_sys + "-" + float + "-" + VERSION + "-" + name
    tar_file = os.path.join(ice_dir, 'ice', cross_name + '.tar.gz')

    try:
        os.stat(tar_file)
	# tar file already exists
        return tar_file
    except: 
        try:
            os.makedirs(os.path.join(ice_dir,'ice'))
        except:
            # directory already exists, continue
            pass


    #check if user has specified a specific icecc-create-env script
    #if not use the OE provided one
    cr_env_script = bb.data.getVar('ICECC_ENV_EXEC',  d) or  bb.data.expand('${STAGING_DIR}', d)+"/ice/icecc-create-env"
    #call the modified create-env script
    result=os.popen("%s %s %s %s %s %s" %(cr_env_script,
           "--silent",
           os.path.join(ice_dir,target_sys,'bin','gcc'),
           os.path.join(ice_dir,target_sys,'bin','g++'),
           os.path.join(ice_dir,target_sys,'bin','as'),
           os.path.join(ice_dir,"ice",cross_name) ) )
    return tar_file


def create_native_env(bb,d):

    import tarfile, socket, time, os
    ice_dir = bb.data.expand('${CROSS_DIR}', d)
    prefix  = bb.data.expand('${HOST_PREFIX}' , d)
    distro  = bb.data.expand('${DISTRO}', d)
    target_sys = bb.data.expand('${TARGET_SYS}',  d)
    target_prefix = bb.data.expand('${TARGET_PREFIX}',  d)
    float   = bb.data.getVar('TARGET_FPU', d) or "hard"
    name    = socket.gethostname()
  
    
    archive_name = "local-host-env" + "-" + name
    tar_file = os.path.join(ice_dir, 'ice', archive_name + '.tar.gz')

    try:
        os.stat(tar_file)
	# tar file already exists
        return tar_file
    except: 
        try:
            #os.makedirs(os.path.join(ice_dir))
            os.makedirs(os.path.join(ice_dir,'ice'))
        except:
            # directory already exists, continue
            pass


    #check if user has specified a specific icecc-create-env script
    #if not use the OE provided one
    cr_env_script = bb.data.getVar('ICECC_ENV_EXEC',  d) or  bb.data.expand('${STAGING_DIR}', d)+"/ice/icecc-create-env"
    result=os.popen("%s %s %s %s %s %s" %(cr_env_script,
           "--silent",
           os.popen("%s gcc" % "which").read()[:-1],
           os.popen("%s g++" % "which").read()[:-1],
           os.popen("%s as" % "which").read()[:-1],
           os.path.join(ice_dir,"ice",archive_name) ) )
    return tar_file



def create_cross_kernel_env(bb,d):

    import tarfile, socket, time, os
    ice_dir = bb.data.expand('${CROSS_DIR}', d)
    prefix  = bb.data.expand('${HOST_PREFIX}' , d)
    distro  = bb.data.expand('${DISTRO}', d)
    target_sys = bb.data.expand('${TARGET_SYS}',  d)
    target_prefix = bb.data.expand('${TARGET_PREFIX}',  d)
    float   = bb.data.getVar('TARGET_FPU', d) or "hard"
    name    = socket.gethostname()
    kernel_cc = bb.data.expand('${KERNEL_CC}', d)
    kernel_cc = kernel_cc[:-1]
  

    # Stupid check to determine if we have built a libc and a cross
    # compiler.
    try:
       os.stat(os.path.join(ice_dir, 'bin', kernel_cc))
    except: # no cross compiler built yet
        return ""

    VERSION = icc_determine_gcc_version( os.path.join(ice_dir,"bin",kernel_cc) )
    cross_name = prefix + distro + "-" + target_sys + "-" + float + "-" + VERSION + "-" + name
    tar_file = os.path.join(ice_dir, 'ice', cross_name + '.tar.gz')

    try:
        os.stat(tar_file)
	# tar file already exists
        return tar_file
    except: 
        try:
            os.makedirs(os.path.join(ice_dir,'ice'))
        except:
            # directory already exists, continue
            pass


    #check if user has specified a specific icecc-create-env script
    #if not use the OE provided one
    cr_env_script = bb.data.getVar('ICECC_ENV_EXEC',  d) or  bb.data.expand('${STAGING_DIR}', d)+"/ice/icecc-create-env"
    result=os.popen("%s %s %s %s %s %s" %(cr_env_script,
           "--silent",
           os.path.join(ice_dir,'bin',kernel_cc),
           os.path.join(ice_dir,target_sys,'bin','g++'),
           os.path.join(ice_dir,target_sys,'bin','as'),
           os.path.join(ice_dir,"ice",cross_name) ) )
    return tar_file


def create_env(bb,d):

        #return create_cross_kernel_env(bb,d) 

        if bb.data.inherits_class("native", d):
          return create_native_env(bb,d)
        elif bb.data.inherits_class("kernel", d):
          return create_cross_kernel_env(bb,d)
        elif bb.data.inherits_class("cross", d):
          return create_native_env(bb,d)
        elif bb.data.inherits_class("sdk", d):
          return create_native_env(bb,d)
        else:  
          return create_cross_env(bb,d)
        
       
def create_path(compilers, type, bb, d):
    """
    Create Symlinks for the icecc in the staging directory
    """
    import os

    staging = os.path.join(bb.data.expand('${STAGING_DIR}', d), "ice", type)

    #check if the icecc path is set by the user
    icecc   = bb.data.getVar('ICECC_PATH', d) or os.popen("%s icecc" % "which").read()[:-1]

    
    # Create the dir if necessary
    try:
        os.stat(staging)
    except:
        os.makedirs(staging)

    for compiler in compilers:
        gcc_path = os.path.join(staging, compiler)
        try:
            os.stat(gcc_path)
        except:
            os.symlink(icecc, gcc_path)

    return staging + ":"





def use_icc_version(bb,d):

      icecc_ver = "yes"
      system_class_blacklist = [ "none" ] 
      
      for black in system_class_blacklist:
           if bb.data.inherits_class(black, d):
              icecc_ver = "no"


      user_class_blacklist =  bb.data.getVar('ICECC_USER_CLASS_BL', d) or "none"
      user_class_blacklist = user_class_blacklist.split()
      
      for black in user_class_blacklist:
           if bb.data.inherits_class(black, d):
              icecc_ver = "no"
 
      return icecc_ver



def icc_path(bb,d,compile):
    package_tmp = bb.data.expand('${PN}', d)

    #"system" package blacklist contains a list of packages that can not distribute compile tasks
    #for one reason or the other
    system_package_blacklist = [ "uclibc", "glibc-intermediate", "gcc", "qemu", "bind", "u-boot", "dhcp-forwarder", "enchant" ]

    for black in system_package_blacklist:
      if black in package_tmp:
         bb.data.setVar("PARALLEL_MAKE" , "", d) 
         return ""

    #user defined exclusion list
    user_package_blacklist = bb.data.getVar('ICECC_USER_PACKAGE_BL', d) or "none"   
    user_package_blacklist = user_package_blacklist.split()

    for black in user_package_blacklist:
      if black in package_tmp:
         bb.data.setVar("PARALLEL_MAKE" , "", d) 
         return ""


    prefix = bb.data.expand('${HOST_PREFIX}', d)

            
    if compile and bb.data.inherits_class("cross", d):
       return create_path( ["gcc", "g++"], "native", bb, d)

    elif compile and bb.data.inherits_class("native", d):
         return create_path( ["gcc", "g++"], "native", bb, d)

    elif compile and bb.data.inherits_class("kernel", d):
          return create_path( [get_cross_kernel_ver(bb,d), "foo"], "cross-kernel", bb, d)

    elif not compile or len(prefix) == 0:
           return create_path( ["gcc", "g++"], "native", bb, d)

    else:
           return create_path( [prefix+"gcc", prefix+"g++"], "cross", bb, d)      




def icc_version(bb,d):
    return create_env(bb,d)

def check_for_kernel(bb,d):     
     if  bb.data.inherits_class("kernel", d):
       return "yes"

       return "no"


def get_cross_kernel_ver(bb,d):

       return  bb.data.expand('${KERNEL_CC}', d).strip() or "gcc"

# set the icecream environment variables
do_configure_prepend() {
    export PATH=${@icc_path(bb,d,False)}$PATH
    export ICECC_CC="gcc"
    export ICECC_CXX="g++"
}

do_compile_prepend() {

    export PATH=${@icc_path(bb,d,True)}$PATH
 
 #check if we are building a kernel and select gcc-cross-kernel
 if [ "${@check_for_kernel(bb,d)}" = "yes" ]; then
    export ICECC_CC="${@get_cross_kernel_ver(bb,d)}"
    export ICECC_CXX="${HOST_PREFIX}g++"
 else
    export ICECC_CC="${HOST_PREFIX}gcc"
    export ICECC_CXX="${HOST_PREFIX}g++"
 fi

    if [ "${@use_icc_version(bb,d)}" = "yes" ]; then
        export ICECC_VERSION="${@icc_version(bb,d)}"
    else
        export ICECC_VERSION="NONE"
    fi
}

