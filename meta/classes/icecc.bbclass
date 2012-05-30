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
#necessary environment tar.gz file to be used by the remote machines.
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

ICECC_ENV_EXEC ?= "${STAGING_BINDIR_NATIVE}/icecc-create-env"

def icecc_dep_prepend(d):
    # INHIBIT_DEFAULT_DEPS doesn't apply to the patch command.  Whether or  not
    # we need that built is the responsibility of the patch function / class, not
    # the application.
    if not d.getVar('INHIBIT_DEFAULT_DEPS'):
        return "icecc-create-env-native"
    return ""

DEPENDS_prepend += "${@icecc_dep_prepend(d)} "

def get_cross_kernel_cc(bb,d):
    kernel_cc = d.expand('${KERNEL_CC}')
    kernel_cc = kernel_cc.replace('ccache', '').strip()
    kernel_cc = kernel_cc.split(' ')[0]
    kernel_cc = kernel_cc.strip()
    return kernel_cc

def create_path(compilers, bb, d):
    """
    Create Symlinks for the icecc in the staging directory
    """
    staging = os.path.join(d.expand('${STAGING_BINDIR}'), "ice")
    if icc_is_kernel(bb, d):
        staging += "-kernel"

    #check if the icecc path is set by the user
    icecc   = d.getVar('ICECC_PATH') or os.popen("which icecc").read()[:-1]

    # Create the dir if necessary
    try:
        os.stat(staging)
    except:
        try:
            os.makedirs(staging)
        except:
            pass

    for compiler in compilers:
        gcc_path = os.path.join(staging, compiler)
        try:
            os.stat(gcc_path)
        except:
            try:
                os.symlink(icecc, gcc_path)
            except:
                pass

    return staging

def use_icc(bb,d):
    package_tmp = d.expand('${PN}')

    system_class_blacklist = [ "none" ] 
    user_class_blacklist = (d.getVar('ICECC_USER_CLASS_BL') or "none").split()
    package_class_blacklist = system_class_blacklist + user_class_blacklist

    for black in package_class_blacklist:
        if bb.data.inherits_class(black, d):
            #bb.note(package_tmp, ' class ', black, ' found in blacklist, disable icecc')
            return "no"

    #"system" package blacklist contains a list of packages that can not distribute compile tasks
    #for one reason or the other
    system_package_blacklist = [ "uclibc", "glibc", "gcc", "bind", "u-boot", "dhcp-forwarder", "enchant", "connman", "orbit2" ]
    user_package_blacklist = (d.getVar('ICECC_USER_PACKAGE_BL') or "").split()
    package_blacklist = system_package_blacklist + user_package_blacklist

    for black in package_blacklist:
        if black in package_tmp:
            #bb.note(package_tmp, ' found in blacklist, disable icecc')
            return "no"

    if d.getVar('PARALLEL_MAKE') == "":
        bb.note(package_tmp, " ", d.expand('${PV}'), " has empty PARALLEL_MAKE, disable icecc")
        return "no"

    return "yes"

def icc_is_kernel(bb, d):
    return \
        bb.data.inherits_class("kernel", d);

def icc_is_native(bb, d):
    return \
        bb.data.inherits_class("cross", d) or \
        bb.data.inherits_class("native", d);

def icc_version(bb, d):
    if use_icc(bb, d) == "no":
        return ""

    parallel = d.getVar('ICECC_PARALLEL_MAKE') or ""
    d.setVar("PARALLEL_MAKE", parallel)

    if icc_is_native(bb, d):
        archive_name = "local-host-env"
    elif d.expand('${HOST_PREFIX}') == "":
        bb.fatal(d.expand("${PN}"), " NULL prefix")
    else:
        prefix = d.expand('${HOST_PREFIX}' )
        distro = d.expand('${DISTRO}')
        target_sys = d.expand('${TARGET_SYS}')
        float = d.getVar('TARGET_FPU') or "hard"
        archive_name = prefix + distro + "-"        + target_sys + "-" + float
        if icc_is_kernel(bb, d):
            archive_name += "-kernel"

    import socket
    ice_dir = d.expand('${STAGING_DIR_NATIVE}${prefix_native}')
    tar_file = os.path.join(ice_dir, 'ice', archive_name + "-@VERSION@-" + socket.gethostname() + '.tar.gz')

    return tar_file

def icc_path(bb,d):
    if icc_is_kernel(bb, d):
        return create_path( [get_cross_kernel_cc(bb,d), ], bb, d)

    else:
        prefix = d.expand('${HOST_PREFIX}')
        return create_path( [prefix+"gcc", prefix+"g++"], bb, d)      

def icc_get_tool(bb, d, tool):
    if icc_is_native(bb, d):
        return os.popen("which %s" % tool).read()[:-1]
    elif icc_is_kernel(bb, d):
        return os.popen("which %s" % get_cross_kernel_cc(bb, d)).read()[:-1]
    else:
        ice_dir = d.expand('${STAGING_BINDIR_TOOLCHAIN}')
        target_sys = d.expand('${TARGET_SYS}')
        return os.path.join(ice_dir, "%s-%s" % (target_sys, tool))

set_icecc_env() {
    ICECC_VERSION="${@icc_version(bb, d)}"
    if [ "x${ICECC_VERSION}" = "x" ]
    then
        return
    fi

    ICE_PATH="${@icc_path(bb, d)}"
    if [ "x${ICE_PATH}" = "x" ]
    then
        return
    fi

    ICECC_CC="${@icc_get_tool(bb,d, "gcc")}"
    ICECC_CXX="${@icc_get_tool(bb,d, "g++")}"
    if [ ! -x "${ICECC_CC}" -o ! -x "${ICECC_CXX}" ]
    then
        return
    fi

    ICE_VERSION=`$ICECC_CC -dumpversion`
    ICECC_VERSION=`echo ${ICECC_VERSION} | sed -e "s/@VERSION@/$ICE_VERSION/g"`
    if [ ! -x "${ICECC_ENV_EXEC}" ]
    then
        return
    fi

    ICECC_AS="`${ICECC_CC} -print-prog-name=as`"
    if [ "`dirname "${ICECC_AS}"`" = "." ]
    then
        ICECC_AS="`which as`"
    fi

    if [ ! -r "${ICECC_VERSION}" ]
    then
        mkdir -p "`dirname "${ICECC_VERSION}"`"
        ${ICECC_ENV_EXEC} "${ICECC_CC}" "${ICECC_CXX}" "${ICECC_AS}" "${ICECC_VERSION}"
    fi

    export ICECC_VERSION ICECC_CC ICECC_CXX
    export PATH="$ICE_PATH:$PATH"
    export CCACHE_PATH="$PATH"
}

do_configure_prepend() {
    set_icecc_env
}

do_compile_prepend() {
    set_icecc_env
}

do_compile_kernelmodules_prepend() {
    set_icecc_env
}

#do_install_prepend() {
#    set_icecc_env
#}
