DESCRIPTION = "Alsa sound library"
HOMEPAGE = "http://www.alsa-project.org"
BUGTRACKER = "https://bugtrack.alsa-project.org/alsa-bug/login_page.php"
SECTION = "libs/multimedia"
LICENSE = "LGPLv2.1 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://src/socket.c;beginline=1;endline=26;md5=11ff89a8a7a4a690a5c78effe8159545"

BBCLASSEXTEND = "native nativesdk"

# configure.in sets -D__arm__ on the command line for any arm system
# (not just those with the ARM instruction set), this should be removed,
# (or replaced by a permitted #define).
#FIXME: remove the following
ARM_INSTRUCTION_SET = "arm"

PR = "r1"

SRC_URI = "ftp://ftp.alsa-project.org/pub/lib/alsa-lib-${PV}.tar.bz2 \
           file://fix-tstamp-declaration.patch"

SRC_URI[md5sum] = "06fe5819020c6684b991dcffc5471304"
SRC_URI[sha256sum] = "5a1a805cf04106316d549ec650116ce6711a162e107ba8b3c551866680e286e6"

inherit autotools pkgconfig

require alsa-fpu.inc
EXTRA_OECONF += "${@get_alsa_fpu_setting(bb, d)} "

EXTRA_OECONF = "--with-cards=pdaudiocf --with-oss=yes --disable-python"

EXTRA_OECONF_append_libc-uclibc = " --with-versioned=no "

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

PACKAGES =+ "alsa-server${PKGSUFFIX} libasound${PKGSUFFIX} alsa-conf-base${PKGSUFFIX} alsa-conf${PKGSUFFIX} alsa-doc${PKGSUFFIX} alsa-dev${PKGSUFFIX}"
FILES_${PN}-dbg += "${libdir}/alsa-lib/*/.debu*"
FILES_libasound${PKGSUFFIX} = "${libdir}/libasound.so.*"
FILES_alsa-server${PKGSUFFIX} = "${bindir}/*"
FILES_alsa-conf${PKGSUFFIX} = "${datadir}/alsa/"
FILES_alsa-dev${PKGSUFFIX} += "${libdir}/pkgconfig/ /usr/include/ ${datadir}/aclocal/*"
FILES_alsa-conf-base${PKGSUFFIX} = "\
${datadir}/alsa/alsa.conf \
${datadir}/alsa/cards/aliases.conf \
${datadir}/alsa/pcm/default.conf \
${datadir}/alsa/pcm/dmix.conf \
${datadir}/alsa/pcm/dsnoop.conf"

RDEPENDS_libasound${PKGSUFFIX} = "alsa-conf-base${PKGSUFFIX}"
