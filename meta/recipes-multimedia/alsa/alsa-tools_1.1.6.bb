SUMMARY = "Advanced tools for certain ALSA sound card drivers"
HOMEPAGE = "http://www.alsa-project.org"
BUGTRACKER = "http://alsa-project.org/main/index.php/Bug_Tracking"
SECTION = "console/utils"
LICENSE = "GPLv2 & LGPLv2+"
DEPENDS = "alsa-lib ncurses glib-2.0"

LIC_FILES_CHKSUM = "file://hdsploader/COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://ld10k1/COPYING.LIB;md5=a916467b91076e631dd8edb7424769c7 \
                    "

SRC_URI = "ftp://ftp.alsa-project.org/pub/tools/${BP}.tar.bz2 \
           file://autotools.patch \
           ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', 'file://makefile_no_gtk.patch', d)} \
           file://gitcompile_hdajacksensetest \
           file://0002-Fix-clang-Wreserved-user-defined-literal-warnings.patch \
           "

SRC_URI[md5sum] = "5ca8c9437ae779997cd62fb2815fef19"
SRC_URI[sha256sum] = "d69c4dc2fb641a974d9903e9eb78c94cb0c7ac6c45bae664f0c9d6c0a1593227"

inherit autotools-brokensep pkgconfig

CLEANBROKEN = "1"

EXTRA_OEMAKE += "GITCOMPILE_ARGS='--host=${HOST_SYS} --build=${BUILD_SYS} --target=${TARGET_SYS} --with-libtool-sysroot=${STAGING_DIR_HOST} --prefix=${prefix}'"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'gtk+', '', d)}"
PACKAGECONFIG[gtk+] = ",,gtk+ gtk+3,"

# configure.ac/.in doesn't exist so force copy
AUTOTOOLS_COPYACLOCAL = "1"

do_compile_prepend () {
    #Automake dir is not correctly detected in cross compilation case
    export AUTOMAKE_DIR="$(automake --print-libdir)"
    export ACLOCAL_FLAGS="--system-acdir=${ACLOCALDIR}/ ${ACLOCALEXTRAPATH}"

    cp ${WORKDIR}/gitcompile_hdajacksensetest ${S}/hdajacksensetest/gitcompile
}

do_install_append() {
    sed -i -e "s|/usr/bin/python2|/usr/bin/env python2|g" ${D}${bindir}/hwmixvolume
}

PACKAGES =+ "${PN}-hwmixvolume"

FILES_${PN}-hwmixvolume = "${bindir}/hwmixvolume"

FILES_${PN} += "${datadir}/ld10k1 \
                ${datadir}/icons/hicolor \
               "

RDEPENDS_${PN}-hwmixvolume += "python"

