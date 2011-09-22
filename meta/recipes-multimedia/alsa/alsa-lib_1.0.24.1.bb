DESCRIPTION = "Alsa sound library"
HOMEPAGE = "http://www.alsa-project.org"
BUGTRACKER = "https://bugtrack.alsa-project.org/alsa-bug/login_page.php"
SECTION = "libs/multimedia"
LICENSE = "LGPLv2.1 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://src/socket.c;beginline=1;endline=26;md5=11ff89a8a7a4a690a5c78effe8159545"

BBCLASSEXTEND = "native"

# configure.in sets -D__arm__ on the command line for any arm system
# (not just those with the ARM instruction set), this should be removed,
# (or replaced by a permitted #define).
#FIXME: remove the following
ARM_INSTRUCTION_SET = "arm"

PR = "r0"

SRC_URI = "ftp://ftp.alsa-project.org/pub/lib/alsa-lib-${PV}.tar.bz2 \
           file://fix-tstamp-declaration.patch"

SRC_URI[md5sum] = "7cc05f25e1d5b65da8fb3fdcd540f226"
SRC_URI[sha256sum] = "a32f7c21015b6c71f9a80ff70a2b6a50e4ff4d5c77c744ff0793dea7ba7a2517"

inherit autotools pkgconfig

require alsa-fpu.inc
EXTRA_OECONF += "${@get_alsa_fpu_setting(bb, d)} "

EXTRA_OECONF = "--with-cards=pdaudiocf --with-oss=yes --disable-python"

EXTRA_OECONF_append_libc-uclibc = " --with-versioned=no "

PACKAGES =+ "alsa-server libasound alsa-conf-base alsa-conf alsa-doc alsa-dev"
FILES_${PN}-dbg += "${libdir}/alsa-lib/*/.debu*"
FILES_libasound = "${libdir}/libasound.so.*"
FILES_alsa-server = "${bindir}/*"
FILES_alsa-conf = "${datadir}/alsa/"
FILES_alsa-dev += "${libdir}/pkgconfig/ /usr/include/ ${datadir}/aclocal/*"
FILES_alsa-conf-base = "\
${datadir}/alsa/alsa.conf \
${datadir}/alsa/cards/aliases.conf \
${datadir}/alsa/pcm/default.conf \
${datadir}/alsa/pcm/dmix.conf \
${datadir}/alsa/pcm/dsnoop.conf"

RDEPENDS_libasound = "alsa-conf-base"
