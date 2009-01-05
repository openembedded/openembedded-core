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
export base_prefix = "${STAGING_DIR_NATIVE}"
export prefix = "${STAGING_DIR_NATIVE}${layout_prefix}"
export exec_prefix = "${STAGING_DIR_NATIVE}${layout_exec_prefix}"

# Base paths
export base_bindir = "${STAGING_DIR_NATIVE}${layout_base_bindir}"
export base_sbindir = "${STAGING_DIR_NATIVE}${layout_base_sbindir}"
export base_libdir = "${STAGING_DIR_NATIVE}${layout_base_libdir}"

# Architecture independent paths
export datadir = "${STAGING_DIR_NATIVE}${layout_datadir}"
export sysconfdir = "${STAGING_DIR_NATIVE}${layout_sysconfdir}"
export sharedstatedir = "${STAGING_DIR_NATIVE}${layout_sharedstatedir}"
export localstatedir = "${STAGING_DIR_NATIVE}${layout_localstatedir}"
export infodir = "${STAGING_DIR_NATIVE}${layout_infodir}"
export mandir = "${STAGING_DIR_NATIVE}${layout_mandir}"
export docdir = "${STAGING_DIR_NATIVE}${layout_docdir}"
export servicedir = "${STAGING_DIR_NATIVE}${layout_servicedir}"

# Architecture dependent paths
export bindir = "${STAGING_DIR_NATIVE}${layout_bindir}"
export sbindir = "${STAGING_DIR_NATIVE}${layout_sbindir}"
export libexecdir = "${STAGING_DIR_NATIVE}${layout_libexecdir}"
export libdir = "${STAGING_DIR_NATIVE}${layout_libdir}"
export includedir = "${STAGING_DIR_NATIVE}${layout_includedir}"
export oldincludedir = "${STAGING_DIR_NATIVE}${layout_includedir}"

do_stage () {
	if [ "${INHIBIT_NATIVE_STAGE_INSTALL}" != "1" ]
	then
		# If autotools is active, use the autotools staging function, else 
		# use our "make install" equivalent
		if [ "${AUTOTOOLS_NATIVE_STAGE_INSTALL}" != "1" ]
		then
			oe_runmake install
		else
			autotools_stage_all
		fi
	fi
}

do_install () {
	true
}

PKG_CONFIG_PATH .= "${EXTRA_NATIVE_PKGCONFIG_PATH}"
PKG_CONFIG_SYSROOT_DIR = ""

python __anonymous () {
    pn = bb.data.getVar("PN", d, True)
    depends = bb.data.getVar("DEPENDS", d, True)
    deps = bb.utils.explode_deps(depends)
    if "native" in (bb.data.getVar('BBCLASSEXTEND', d, True) or ""):
        autoextend = True
    else:
        autoextend = False
    for dep in deps:
        if dep.endswith("-cross"):
            if autoextend:
                depends = depends.replace(dep, dep.replace("-cross", "-native"))
            else:
                bb.note("%s has depends %s which ends in -cross?" % (pn, dep))

        if not dep.endswith("-native"):
            if autoextend:
                depends = depends.replace(dep, dep + "-native")
            else:
                bb.note("%s has depends %s which doesn't end in -native?" % (pn, dep))
    bb.data.setVar("DEPENDS", depends, d)
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
}

