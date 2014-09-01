# We want native packages to be relocatable
inherit relocatable

# Native packages are built indirectly via dependency,
# no need for them to be a direct target of 'world'
EXCLUDE_FROM_WORLD = "1"

PACKAGES = ""
PACKAGES_class-native = ""
PACKAGES_DYNAMIC = ""
PACKAGES_DYNAMIC_class-native = ""
PACKAGE_ARCH = "${BUILD_ARCH}"

# used by cmake class
OECMAKE_RPATH = "${libdir}"
OECMAKE_RPATH_class-native = "${libdir}"

# When this class has packaging enabled, setting 
# RPROVIDES becomes unnecessary.
RPROVIDES = "${PN}"

TARGET_ARCH = "${BUILD_ARCH}"
TARGET_OS = "${BUILD_OS}"
TARGET_VENDOR = "${BUILD_VENDOR}"
TARGET_PREFIX = "${BUILD_PREFIX}"
TARGET_CC_ARCH = "${BUILD_CC_ARCH}"
TARGET_LD_ARCH = "${BUILD_LD_ARCH}"
TARGET_AS_ARCH = "${BUILD_AS_ARCH}"
TARGET_CPPFLAGS = "${BUILD_CPPFLAGS}"
TARGET_CFLAGS = "${BUILD_CFLAGS}"
TARGET_CXXFLAGS = "${BUILD_CXXFLAGS}"
TARGET_LDFLAGS = "${BUILD_LDFLAGS}"
TARGET_FPU = ""

HOST_ARCH = "${BUILD_ARCH}"
HOST_OS = "${BUILD_OS}"
HOST_VENDOR = "${BUILD_VENDOR}"
HOST_PREFIX = "${BUILD_PREFIX}"
HOST_CC_ARCH = "${BUILD_CC_ARCH}"
HOST_LD_ARCH = "${BUILD_LD_ARCH}"
HOST_AS_ARCH = "${BUILD_AS_ARCH}"

CPPFLAGS = "${BUILD_CPPFLAGS}"
CFLAGS = "${BUILD_CFLAGS}"
CXXFLAGS = "${BUILD_CFLAGS}"
LDFLAGS = "${BUILD_LDFLAGS}"
LDFLAGS_build-darwin = "-L${STAGING_LIBDIR_NATIVE} "

STAGING_BINDIR = "${STAGING_BINDIR_NATIVE}"
STAGING_BINDIR_CROSS = "${STAGING_BINDIR_NATIVE}"

# native pkg doesn't need the TOOLCHAIN_OPTIONS.
TOOLCHAIN_OPTIONS = ""

DEPENDS_GETTEXT = "gettext-native"

# Don't build ptest natively
PTEST_ENABLED = "0"

# Don't use site files for native builds
export CONFIG_SITE = "${COREBASE}/meta/site/native"

# set the compiler as well. It could have been set to something else
export CC = "${CCACHE}${HOST_PREFIX}gcc ${HOST_CC_ARCH}"
export CXX = "${CCACHE}${HOST_PREFIX}g++ ${HOST_CC_ARCH}"
export FC = "${CCACHE}${HOST_PREFIX}gfortran ${HOST_CC_ARCH}"
export CPP = "${HOST_PREFIX}gcc ${HOST_CC_ARCH} -E"
export LD = "${HOST_PREFIX}ld ${HOST_LD_ARCH} "
export CCLD = "${CC}"
export AR = "${HOST_PREFIX}ar"
export AS = "${HOST_PREFIX}as ${HOST_AS_ARCH}"
export RANLIB = "${HOST_PREFIX}ranlib"
export STRIP = "${HOST_PREFIX}strip"

# Path prefixes
base_prefix = "${STAGING_DIR_NATIVE}"
prefix = "${STAGING_DIR_NATIVE}${prefix_native}"
exec_prefix = "${STAGING_DIR_NATIVE}${prefix_native}"

bindir = "${STAGING_BINDIR_NATIVE}"
sbindir = "${STAGING_SBINDIR_NATIVE}"
libdir = "${STAGING_LIBDIR_NATIVE}"
includedir = "${STAGING_INCDIR_NATIVE}"
sysconfdir = "${STAGING_ETCDIR_NATIVE}"
datadir = "${STAGING_DATADIR_NATIVE}"

baselib = "lib"

# Libtool's default paths are correct for the native machine
lt_cv_sys_lib_dlsearch_path_spec[unexport] = "1"

NATIVE_PACKAGE_PATH_SUFFIX ?= ""
bindir .= "${NATIVE_PACKAGE_PATH_SUFFIX}"
libdir .= "${NATIVE_PACKAGE_PATH_SUFFIX}"
libexecdir .= "${NATIVE_PACKAGE_PATH_SUFFIX}"

do_populate_sysroot[sstate-inputdirs] = "${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/"
do_populate_sysroot[sstate-outputdirs] = "${STAGING_DIR_NATIVE}/"

# Since we actually install these into situ there is no staging prefix
STAGING_DIR_HOST = ""
STAGING_DIR_TARGET = ""
PKG_CONFIG_DIR = "${libdir}/pkgconfig"

EXTRA_NATIVE_PKGCONFIG_PATH ?= ""
PKG_CONFIG_PATH .= "${EXTRA_NATIVE_PKGCONFIG_PATH}"
PKG_CONFIG_SYSROOT_DIR = ""

# we dont want libc-uclibc or libc-glibc to kick in for native recipes
LIBCOVERRIDE = ""
CLASSOVERRIDE = "class-native"
MACHINEOVERRIDES = ""

PATH_prepend = "${COREBASE}/scripts/native-intercept:"

python native_virtclass_handler () {
    classextend = e.data.getVar('BBCLASSEXTEND', True) or ""
    if "native" not in classextend:
        return

    pn = e.data.getVar("PN", True)
    if not pn.endswith("-native"):
        return

    def map_dependencies(varname, d, suffix = ""):
        if suffix:
            varname = varname + "_" + suffix
        deps = d.getVar(varname, True)
        if not deps:
            return
        deps = bb.utils.explode_deps(deps)
        newdeps = []
        for dep in deps:
            if "-cross-" in dep:
                newdeps.append(dep.replace("-cross", "-native"))
            elif not dep.endswith("-native"):
                newdeps.append(dep + "-native")
            else:
                newdeps.append(dep)
        d.setVar(varname, " ".join(newdeps))

    map_dependencies("DEPENDS", e.data)
    for pkg in [e.data.getVar("PN", True), "", "${PN}"]:
        map_dependencies("RDEPENDS", e.data, pkg)
        map_dependencies("RRECOMMENDS", e.data, pkg)
        map_dependencies("RSUGGESTS", e.data, pkg)
        map_dependencies("RPROVIDES", e.data, pkg)
        map_dependencies("RREPLACES", e.data, pkg)

    provides = e.data.getVar("PROVIDES", True)
    for prov in provides.split():
        if prov.find(pn) != -1:
            continue
        if not prov.endswith("-native"):
            provides = provides.replace(prov, prov + "-native")
    e.data.setVar("PROVIDES", provides)

    e.data.setVar("OVERRIDES", e.data.getVar("OVERRIDES", False) + ":virtclass-native")
}

addhandler native_virtclass_handler
native_virtclass_handler[eventmask] = "bb.event.RecipePreFinalise"

deltask package
deltask packagedata
deltask package_qa
deltask package_write_ipk
deltask package_write_deb
deltask package_write_rpm
deltask package_write

do_packagedata[stamp-extra-info] = ""
do_populate_sysroot[stamp-extra-info] = ""

USE_NLS = "no"
