# IceCream distributed compiling support
#
# We need to create a tar.bz2 of our toolchain and set
# ICECC_VERSION, ICECC_CXX and ICEC_CC
#

def icc_determine_gcc_version(gcc):
    """
    Hack to determine the version of GCC

    'i686-apple-darwin8-gcc-4.0.1 (GCC) 4.0.1 (Apple Computer, Inc. build 5363)'
    """
    return os.popen("%s --version" % gcc ).readline().split()[2]

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
    float   = bb.data.getVar('${TARGET_FPU}', d) or "hard"
    name    = socket.gethostname()

    # Stupid check to determine if we have built a libc and a cross
    # compiler.
    try:
        os.stat(os.path.join(ice_dir, target_sys, 'lib', 'ld-linux.so.2'))
        os.stat(os.path.join(ice_dir, target_sys, 'bin', 'g++'))
    except:
        return ""

    VERSION = icc_determine_gcc_version( os.path.join(ice_dir,target_sys,"bin","g++") )
    cross_name = prefix + distro + target_sys + float +VERSION+ name
    tar_file = os.path.join(ice_dir, 'ice', cross_name + '.tar.bz2')

    try:
        os.stat(tar_file)
        return tar_file
    except:
        try:
            os.makedirs(os.path.join(ice_dir,'ice'))
        except:
            pass

    # FIXME find out the version of the compiler
    # Consider using -print-prog-name={cc1,cc1plus}
    # and            -print-file-name=specs

    # We will use the GCC to tell us which tools to use
    #  What we need is:
    #        -gcc
    #        -g++
    #        -as
    #        -cc1
    #        -cc1plus
    #  and we add them to /usr/bin

    tar = tarfile.open(tar_file, 'w:bz2')

    # Now add the required files
    tar.add(os.path.join(ice_dir,target_sys,'bin','gcc'),
            os.path.join("usr","bin","gcc") )
    tar.add(os.path.join(ice_dir,target_sys,'bin','g++'),
            os.path.join("usr","bin","g++") )
    tar.add(os.path.join(ice_dir,target_sys,'bin','as'),
            os.path.join("usr","bin","as") )

    # Now let us find cc1 and cc1plus
    cc1 = os.popen("%s -print-prog-name=cc1" % data.getVar('CC', d, True)).read()[:-1]
    cc1plus = os.popen("%s -print-prog-name=cc1plus" % data.getVar('CC', d, True)).read()[:-1]
    spec = os.popen("%s -print-file-name=specs" % data.getVar('CC', d, True)).read()[:-1]

    # CC1 and CC1PLUS should be there...
    tar.add(cc1, os.path.join('usr', 'bin', 'cc1'))
    tar.add(cc1plus, os.path.join('usr', 'bin', 'cc1plus'))

    # spec - if it exists
    if os.path.exists(spec):
        tar.add(spec)

    tar.close()
    return tar_file


def create_path(compilers, type, bb, d):
    """
    Create Symlinks for the icecc in the staging directory
    """
    import os

    staging = os.path.join(bb.data.expand('${STAGING_DIR}', d), "ice", type)
    icecc   = bb.data.getVar('ICECC_PATH', d)

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
    # Constin native native
    prefix = bb.data.expand('${HOST_PREFIX}', d)
    if len(prefix) == 0:
        return "no"


    blacklist = [ "cross", "native" ]

    for black in blacklist:
        if bb.data.inherits_class(black, d):
            return "no"

    return "yes"

def icc_path(bb,d,compile):
    native = bb.data.expand('${PN}', d)
    blacklist = [ "ulibc", "glibc", "ncurses" ]
    for black in blacklist:
        if black in native:
            return ""

    blacklist = [ "cross", "native" ]
    for black in blacklist:
        if bb.data.inherits_class(black, d):
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
