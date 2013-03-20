DESCRIPTION = "Alsa Tools package contains advanced tools for certain sound cards."
HOMEPAGE = "http://www.alsa-project.org"
BUGTRACKER = "https://bugtrack.alsa-project.org/alsa-bug/login_page.php"
SECTION = "console/utils"
LICENSE = "GPLv2 & LGPLv2+"
DEPENDS = "alsa-lib ncurses"

PR = "r0"

LIC_FILES_CHKSUM = "file://hdsploader/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://ld10k1/COPYING.LIB;md5=7fbc338309ac38fefcd64b04bb903e34"

SRC_URI = "ftp://ftp.alsa-project.org/pub/tools/alsa-tools-${PV}.tar.bz2 \
           file://mips_has_no_io_h.patch \
           file://autotools.patch"

SRC_URI[md5sum] = "805526ea5d6c40e1f2c94cee86141230"
SRC_URI[sha256sum] = "553338693707fe6ddfc430b9edc4cd2677390e200c9e38de82ede3394e733841"

inherit autotools

EXTRA_OEMAKE += "GITCOMPILE_ARGS='--host=${HOST_SYS} --build=${BUILD_SYS} --target=${TARGET_SYS} --with-libtool-sysroot=${STAGING_DIR_HOST} --prefix=${prefix}' ACLOCAL_FLAGS='-I ${STAGING_DATADIR}/aclocal'"

do_compile_prepend () {
    #Automake dir is not correctly detected in cross compilation case
    export AUTOMAKE_DIR=${STAGING_DATADIR_NATIVE}/$(ls ${STAGING_DATADIR_NATIVE} | grep automake)
}

FILES_${PN} += "${datadir}/ld10k1"
