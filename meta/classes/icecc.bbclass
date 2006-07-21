# IceCream distributed compiling support
# 
# We need to create a tar.bz2 of our toolchain and set
# ICECC_VERSION, ICECC_CXX and ICEC_CC
#

def create_env(bb,d):
    """
    Create a tar.bz of the current toolchain
    """

    # Constin native-native compilation no environment needed if
    # host prefix is empty (let us duplicate the query for ease)
    prefix = bb.data.expand('${HOST_PREFIX}', d)
    if len(prefix) == 0:
    	return ""

    import tarfile
    import socket
    import time
    import os
    ice_dir = bb.data.expand('${CROSS_DIR}', d)
    prefix  = bb.data.expand('${HOST_PREFIX}' , d)
    distro  = bb.data.expand('${DISTRO}', d)
    target_sys = bb.data.expand('${TARGET_SYS}',  d)
    #float   = bb.data.getVar('${TARGET_FPU}', d)
    float   = "anyfloat"
    name    = socket.gethostname()

    try:
	os.stat(ice_dir + '/' + target_sys + '/lib/ld-linux.so.2')
	os.stat(ice_dir + '/' + target_sys + '/bin/g++')
    except:
	return ""

    VERSION = '3.4.3'    
    cross_name = prefix + distro + target_sys + float +VERSION+ name
    tar_file = ice_dir + '/ice/' + cross_name + '.tar.bz2'

    try:
        os.stat(tar_file)
        return tar_file
    except:
	try:
	    os.makedirs(ice_dir+'/ice')
	except:
	    pass

    # FIXME find out the version of the compiler
    tar = tarfile.open(tar_file, 'w:bz2')
    tar.add(ice_dir + '/' + target_sys + '/lib/ld-linux.so.2', 
            target_sys + 'cross/lib/ld-linux.so.2')
    tar.add(ice_dir + '/' + target_sys + '/lib/ld-linux.so.2',
            target_sys + 'cross/lib/ld-2.3.3.so')
    tar.add(ice_dir + '/' + target_sys + '/lib/libc-2.3.3.so',
            target_sys + 'cross/lib/libc-2.3.3.so')
    tar.add(ice_dir + '/' + target_sys + '/lib/libc.so.6',
           target_sys + 'cross/lib/libc.so.6')
    tar.add(ice_dir + '/' + target_sys + '/bin/gcc',
            target_sys + 'cross/usr/bin/gcc')
    tar.add(ice_dir + '/' + target_sys + '/bin/g++',
            target_sys + 'cross/usr/bin/g++')
    tar.add(ice_dir + '/' + target_sys + '/bin/as',
            target_sys + 'cross/usr/bin/as')
    tar.add(ice_dir + '/lib/gcc/' + target_sys +'/'+ VERSION + '/specs',
            target_sys+'cross/usr/lib/gcc/'+target_sys+'/'+VERSION+'/lib/specs')
    tar.add(ice_dir + '/libexec/gcc/'+target_sys+'/' + VERSION + '/cc1',
            target_sys + 'cross/usr/lib/gcc/'+target_sys+'/'+VERSION+'/lib/cc1')
    tar.add(ice_dir + '/libexec/gcc/arm-linux/' + VERSION + '/cc1plus',
            target_sys+'cross/usr/lib/gcc/'+target_sys+'/'+VERSION+'/lib/cc1plus')
    tar.close()
    return tar_file


def create_path(compilers, type, bb, d):
    """
    Create Symlinks for the icecc in the staging directory
    """
    import os

    staging = bb.data.expand('${STAGING_DIR}', d) + "/ice/" + type
    icecc   = bb.data.getVar('ICECC_PATH', d)

    # Create the dir if necessary
    try:
        os.stat(staging)
    except:
        os.makedirs(staging)


    for compiler in compilers:
        gcc_path = staging + "/" + compiler
        try:
            os.stat(gcc_path)
        except:
            os.symlink(icecc, gcc_path)

    return staging + ":"


def use_icc_version(bb,d):
    # Constin native native
    prefix = bb.data.expand('${HOST_PREFIX}', d)
    if len(prefix) == 0:
	return "no"
	
	
    native = bb.data.expand('${PN}', d)	
    blacklist = [ "-cross", "-native" ]

    for black in blacklist:
        if black in native:
	    return "no"

    return "yes"

def icc_path(bb,d,compile):
    native = bb.data.expand('${PN}', d)
    blacklist = [ "ulibc", "glibc", "ncurses" ]
    for black in blacklist:
    	if black in native:
    	    return ""

    if "-native" in native:
	compile = False
    if "-cross"  in native:
    	compile = False

    prefix = bb.data.expand('${HOST_PREFIX}', d)
    if compile and len(prefix) != 0:
        return create_path( [prefix+"gcc", prefix+"g++"], "cross", bb, d )
    elif not compile or len(prefix) == 0:
        return create_path( ["gcc", "g++"], "native", bb, d)


def icc_version(bb,d):
    return create_env(bb,d)


#
# set the IceCream  environment variables
do_configure_prepend() {
    export PATH=${@icc_path(bb,d,False)}$PATH
    export ICECC_CC="gcc"
    export ICECC_CXX="g++"
}

do_compile_prepend() {
    export PATH=${@icc_path(bb,d,True)}$PATH
    export ICECC_CC="${HOST_PREFIX}gcc"
    export ICECC_CXX="${HOST_PREFIX}g++"

    if [ "${@use_icc_version(bb,d)}" = "yes" ]; then
    	export ICECC_VERSION="${@icc_version(bb,d)}"
    fi
}
