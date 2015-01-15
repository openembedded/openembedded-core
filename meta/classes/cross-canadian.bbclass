#
# NOTE - When using this class the user is repsonsible for ensuring that
# TRANSLATED_TARGET_ARCH is added into PN. This ensures that if the TARGET_ARCH
# is changed, another nativesdk xxx-canadian-cross can be installed
#


# SDK packages are built either explicitly by the user,
# or indirectly via dependency.  No need to be in 'world'.
EXCLUDE_FROM_WORLD = "1"
CLASSOVERRIDE = "class-cross-canadian"
STAGING_BINDIR_TOOLCHAIN = "${STAGING_DIR_NATIVE}${bindir_native}/${SDK_ARCH}${SDK_VENDOR}-${SDK_OS}:${STAGING_DIR_NATIVE}${bindir_native}/${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

#
# Update BASE_PACKAGE_ARCH and PACKAGE_ARCHS
#
PACKAGE_ARCH = "${SDK_ARCH}-${SDKPKGSUFFIX}"
CANADIANEXTRAOS = ""
MODIFYTOS ??= "1"
python () {
    archs = d.getVar('PACKAGE_ARCHS', True).split()
    sdkarchs = []
    for arch in archs:
        sdkarchs.append(arch + '-${SDKPKGSUFFIX}')
    d.setVar('PACKAGE_ARCHS', " ".join(sdkarchs))

    # Allow the following code segment to be disabled, e.g. meta-environment
    if d.getVar("MODIFYTOS", True) != "1":
        return
    # PowerPC can build "linux" and "linux-gnuspe"
    tarch = d.getVar("TARGET_ARCH", True)
    if tarch == "powerpc":
        tos = d.getVar("TARGET_OS", True)
        if (tos != "linux" and tos != "linux-gnuspe"
            and tos != "linux-uclibc" and tos != "linux-uclibcspe"
            and tos != "linux-musl" and tos != "linux-muslspe"):
            bb.fatal("Building cross-candian powerpc for an unknown TARGET_SYS (%s), please update cross-canadian.bbclass" % d.getVar("TARGET_SYS", True))
        # This is a bit ugly. We need to zero LIBC/ABI extension which will change TARGET_OS
        # however we need the old value in some variables. We expand those here first.
        d.setVar("DEPENDS", d.getVar("DEPENDS", True))
        d.setVar("STAGING_BINDIR_TOOLCHAIN", d.getVar("STAGING_BINDIR_TOOLCHAIN", True))
        for prefix in ["AR", "AS", "DLLTOOL", "CC", "CXX", "GCC", "LD", "LIPO", "NM", "OBJDUMP", "RANLIB", "STRIP", "WINDRES"]:
            n = prefix + "_FOR_TARGET"
            d.setVar(n, d.getVar(n, True))

        d.setVar("LIBCEXTENSION", "")
        d.setVar("ABIEXTENSION", "")
        d.setVar("CANADIANEXTRAOS", "linux-gnuspe")
}
MULTIMACH_TARGET_SYS = "${PACKAGE_ARCH}${HOST_VENDOR}-${HOST_OS}"

INHIBIT_DEFAULT_DEPS = "1"

STAGING_DIR_HOST = "${STAGING_DIR}/${HOST_ARCH}-${SDKPKGSUFFIX}${HOST_VENDOR}-${HOST_OS}"

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR}/${HOST_ARCH}-${SDKPKGSUFFIX}${HOST_VENDOR}-${HOST_OS}"

PATH_append = ":${TMPDIR}/sysroots/${HOST_ARCH}/${bindir_cross}"
PKGHIST_DIR = "${TMPDIR}/pkghistory/${HOST_ARCH}-${SDKPKGSUFFIX}${HOST_VENDOR}-${HOST_OS}/"

HOST_ARCH = "${SDK_ARCH}"
HOST_VENDOR = "${SDK_VENDOR}"
HOST_OS = "${SDK_OS}"
HOST_PREFIX = "${SDK_PREFIX}"
HOST_CC_ARCH = "${SDK_CC_ARCH}"
HOST_LD_ARCH = "${SDK_LD_ARCH}"
HOST_AS_ARCH = "${SDK_AS_ARCH}"

#assign DPKG_ARCH
DPKG_ARCH = "${SDK_ARCH}"

CPPFLAGS = "${BUILDSDK_CPPFLAGS}"
CFLAGS = "${BUILDSDK_CFLAGS}"
CXXFLAGS = "${BUILDSDK_CFLAGS}"
LDFLAGS = "${BUILDSDK_LDFLAGS} \
           -Wl,-rpath-link,${STAGING_LIBDIR}/.. \
           -Wl,-rpath,${libdir}/.. "

DEPENDS_GETTEXT = "gettext-native nativesdk-gettext"

#
# We need chrpath >= 0.14 to ensure we can deal with 32 and 64 bit
# binaries
#
DEPENDS_append = " chrpath-replacement-native"
EXTRANATIVEPATH += "chrpath-native"

# Path mangling needed by the cross packaging
# Note that we use := here to ensure that libdir and includedir are
# target paths.
target_base_prefix := "${base_prefix}"
target_prefix := "${prefix}"
target_exec_prefix := "${exec_prefix}"
target_base_libdir = "${target_base_prefix}/${baselib}"
target_libdir = "${target_exec_prefix}/${baselib}"
target_includedir := "${includedir}"

# Change to place files in SDKPATH
base_prefix = "${SDKPATHNATIVE}"
prefix = "${SDKPATHNATIVE}${prefix_nativesdk}"
exec_prefix = "${SDKPATHNATIVE}${prefix_nativesdk}"
bindir = "${exec_prefix}/bin/${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"
sbindir = "${bindir}"
base_bindir = "${bindir}"
base_sbindir = "${bindir}"
libdir = "${exec_prefix}/lib/${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"
libexecdir = "${exec_prefix}/libexec/${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

FILES_${PN} = "${prefix}"
FILES_${PN}-dbg += "${prefix}/.debug \
                    ${prefix}/bin/.debug \
                   "

export PKG_CONFIG_DIR = "${STAGING_DIR_HOST}${layout_libdir}/pkgconfig"
export PKG_CONFIG_SYSROOT_DIR = "${STAGING_DIR_HOST}"

do_populate_sysroot[stamp-extra-info] = ""
do_packagedata[stamp-extra-info] = ""

USE_NLS = "${SDKUSE_NLS}"

# We have to us TARGET_ARCH but we care about the absolute value
# and not any particular tune that is enabled.
TARGET_ARCH[vardepsexclude] = "TUNE_ARCH"

# If MLPREFIX is set by multilib code, shlibs
# points to the wrong place so force it
SHLIBSDIRS = "${PKGDATA_DIR}/nativesdk-shlibs2"
SHLIBSWORKDIR = "${PKGDATA_DIR}/nativesdk-shlibs2"

cross_canadian_bindirlinks () {
	for i in ${CANADIANEXTRAOS}
	do
		d=${D}${bindir}/../${TARGET_ARCH}${TARGET_VENDOR}-$i
		install -d $d
		for j in `ls ${D}${bindir}`
		do
			p=${TARGET_ARCH}${TARGET_VENDOR}-$i-`echo $j | sed -e s,${TARGET_PREFIX},,`
			ln -s ../${TARGET_SYS}/$j $d/$p
		done
       done
}
