# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"

#
# Update BASE_PACKAGE_ARCH and PACKAGE_ARCHS
#
OLD_PACKAGE_ARCH := ${BASE_PACKAGE_ARCH}
BASE_PACKAGE_ARCH = "${SDK_ARCH}-nativesdk"
python () {
    archs = bb.data.getVar('PACKAGE_ARCHS', d, True).split()
    sdkarchs = []
    for arch in archs:
        sdkarchs.append(arch + '-nativesdk')
    bb.data.setVar('PACKAGE_ARCHS', " ".join(sdkarchs), d)
}

#STAGING_DIR_HOST = "${STAGING_DIR}/${HOST_SYS}-nativesdk"
#STAGING_DIR_TARGET = "${STAGING_DIR}/${BASEPKG_TARGET_SYS}-nativesdk"

CROSS_DIR = "${TMPDIR}/cross/${HOST_ARCH}"

HOST_ARCH = "${SDK_ARCH}"
HOST_VENDOR = "${SDK_VENDOR}"
HOST_OS = "${SDK_OS}"
HOST_PREFIX = "${SDK_PREFIX}"
HOST_CC_ARCH = "${SDK_CC_ARCH}"
#HOST_SYS = "${HOST_ARCH}${TARGET_VENDOR}-${HOST_OS}"

TARGET_ARCH = "${SDK_ARCH}"
TARGET_VENDOR = "${SDK_VENDOR}"
TARGET_OS = "${SDK_OS}"
TARGET_PREFIX = "${SDK_PREFIX}"
TARGET_CC_ARCH = "${SDK_CC_ARCH}"

CPPFLAGS = "${BUILDSDK_CPPFLAGS}"
CFLAGS = "${BUILDSDK_CFLAGS}"
CXXFLAGS = "${BUILDSDK_CFLAGS}"
LDFLAGS = "${BUILDSDK_LDFLAGS}"

# Change to place files in SDKPATH
prefix = "${SDKPATH}"
exec_prefix = "${SDKPATH}"
base_prefix = "${SDKPATH}"

FILES_${PN} += "${prefix}"
FILES_${PN}-dbg += "${prefix}/.debug \
                    ${prefix}/bin/.debug \
                   "

export PKG_CONFIG_DIR = "${STAGING_DIR_HOST}${libdir}/pkgconfig"
export PKG_CONFIG_SYSROOT_DIR = "${STAGING_DIR_HOST}"

ORIG_DEPENDS := "${DEPENDS}"
DEPENDS_virtclass-nativesdk ?= "${ORIG_DEPENDS}"

python __anonymous () {
    pn = bb.data.getVar("PN", d, True)
    depends = bb.data.getVar("DEPENDS_virtclass-nativesdk", d, True)
    deps = bb.utils.explode_deps(depends)
    newdeps = []
    for dep in deps:
        if dep.endswith("-native") or dep.endswith("-cross"):
            newdeps.append(dep)
        elif dep.endswith("-gcc-intermediate") or dep.endswith("-gcc-initial") or dep.endswith("-gcc"):
            newdeps.append(dep + "-crosssdk")
        elif not dep.endswith("-nativesdk"):
            newdeps.append(dep + "-nativesdk")
        else:
            newdeps.append(dep)
    bb.data.setVar("DEPENDS_virtclass-nativesdk", " ".join(newdeps), d)
    provides = bb.data.getVar("PROVIDES", d, True)
    for prov in provides.split():
        if prov.find(pn) != -1:
            continue
        if not prov.endswith("-nativesdk"):
            provides = provides.replace(prov, prov + "-nativesdk")
    bb.data.setVar("PROVIDES", provides, d)
    bb.data.setVar("OVERRIDES", bb.data.getVar("OVERRIDES", d, False) + ":virtclass-nativesdk", d)
}


