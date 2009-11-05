# Native packages are built indirectly via dependency,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

PACKAGES = ""
PACKAGE_ARCH = "${BUILD_ARCH}"

BASE_PACKAGE_ARCH = "${BUILD_ARCH}"
BASEPKG_HOST_SYS = "${BUILD_ARCH}${BUILD_VENDOR}-${BUILD_OS}"
BASEPKG_TARGET_SYS = "${BUILD_ARCH}${BUILD_VENDOR}-${BUILD_OS}"

# When this class has packaging enabled, setting 
# RPROVIDES becomes unnecessary.
RPROVIDES = "${PN}"

TARGET_ARCH = "${BUILD_ARCH}"
TARGET_OS = "${BUILD_OS}"
TARGET_VENDOR = "${BUILD_VENDOR}"
TARGET_PREFIX = "${BUILD_PREFIX}"
TARGET_CC_ARCH = "${BUILD_CC_ARCH}"

HOST_ARCH = "${BUILD_ARCH}"
HOST_OS = "${BUILD_OS}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"

CPPFLAGS = "${BUILD_CPPFLAGS}"
CFLAGS = "${BUILD_CFLAGS}"
CXXFLAGS = "${BUILD_CFLAGS}"
LDFLAGS = "${BUILD_LDFLAGS}"
LDFLAGS_build-darwin = "-L${STAGING_LIBDIR_NATIVE} "

STAGING_BINDIR = "${STAGING_BINDIR_NATIVE}"
STAGING_BINDIR_CROSS = "${STAGING_BINDIR_NATIVE}"

# Don't use site files for native builds
export CONFIG_SITE = ""

# set the compiler as well. It could have been set to something else
export CC = "${CCACHE}${HOST_PREFIX}gcc ${HOST_CC_ARCH}"
export CXX = "${CCACHE}${HOST_PREFIX}g++ ${HOST_CC_ARCH}"
export F77 = "${CCACHE}${HOST_PREFIX}g77 ${HOST_CC_ARCH}"
export CPP = "${HOST_PREFIX}gcc -E"
export LD = "${HOST_PREFIX}ld"
export CCLD = "${CC}"
export AR = "${HOST_PREFIX}ar"
export AS = "${HOST_PREFIX}as"
export RANLIB = "${HOST_PREFIX}ranlib"
export STRIP = "${HOST_PREFIX}strip"

# Path prefixes
base_prefix = "${STAGING_DIR_NATIVE}"
prefix = "${STAGING_DIR_NATIVE}${prefix_native}"
exec_prefix = "${STAGING_DIR_NATIVE}${prefix_native}"

# Since we actually install these into situ there is no staging prefix
STAGING_DIR_HOST = ""
STAGING_DIR_TARGET = ""
SHLIBSDIR = "${STAGING_DIR_NATIVE}/shlibs"
PKG_CONFIG_DIR = "${libdir}/pkgconfig"

#
# If changing this function, please make sure packaged-staging.bbclass is
# updated too
#
do_stage_native () {
	# If autotools is active, use the autotools staging function, else 
	# use our "make install" equivalent
	if [ "${AUTOTOOLS_NATIVE_STAGE_INSTALL}" == "1" ]
	then
		autotools_stage_all
	else
		oe_runmake install
	fi
}

do_stage () {
	do_stage_native
}

PKG_CONFIG_PATH .= "${EXTRA_NATIVE_PKGCONFIG_PATH}"
PKG_CONFIG_SYSROOT_DIR = ""

python __anonymous () {
    # If we've a legacy native do_stage, we need to neuter do_install
    stagefunc = bb.data.getVar('do_stage', d, True)
    if (stagefunc.strip() != "do_stage_native" and stagefunc.strip() != "autotools_stage_all") and bb.data.getVar('AUTOTOOLS_NATIVE_STAGE_INSTALL', d, 1) == "1":
        bb.data.setVar("do_install", "      :", d)

    pn = bb.data.getVar("PN", d, True)
    depends = bb.data.getVar("DEPENDS", d, True)
    deps = bb.utils.explode_deps(depends)
    depends = bb.data.getVar("DEPENDS", d, True)
    deps = bb.utils.explode_deps(depends)
    newdeps = []
    if "native" in (bb.data.getVar('BBCLASSEXTEND', d, True) or ""):
        autoextend = True
    else:
        autoextend = False
    for dep in deps:
        if dep.endswith("-cross"):
            if autoextend:
                newdeps.append(dep.replace("-cross", "-native"))
            else:
                bb.note("%s has depends %s which ends in -cross?" % (pn, dep))
                newdeps.append(dep)
        elif not dep.endswith("-native"):
            if autoextend:
                newdeps.append(dep + "-native")
            else:
                bb.note("%s has depends %s which doesn't end in -native?" % (pn, dep))
                newdeps.append(dep)
        else:
            newdeps.append(dep)
    bb.data.setVar("DEPENDS", " ".join(newdeps), d)
    provides = bb.data.getVar("PROVIDES", d, True)
    for prov in provides.split():
        if prov.find(pn) != -1:
            continue
        if not prov.endswith("-native"):
            if autoextend:
                provides = provides.replace(prov, prov + "-native")
            #else:
            #    bb.note("%s has rouge PROVIDES of %s which doesn't end in -sdk?" % (pn, prov))
    bb.data.setVar("PROVIDES", provides, d)
    bb.data.setVar("OVERRIDES", bb.data.getVar("OVERRIDES", d, False) + ":virtclass-native", d)
}

