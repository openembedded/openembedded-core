DESCRIPTION = "Alsa sound library"
HOMEPAGE = "http://www.alsa-project.org"
SECTION = "libs/multimedia"
LICENSE = "GPL"

PR = "r2"

# configure.in sets -D__arm__ on the command line for any arm system
# (not just those with the ARM instruction set), this should be removed,
# (or replaced by a permitted #define).
#FIXME: remove the following
ARM_INSTRUCTION_SET = "arm"

SRC_URI = "ftp://ftp.alsa-project.org/pub/lib/alsa-lib-${PV}.tar.bz2 \
           file://fix-tstamp-declaration.patch;patch=1 \
           file://unbreak_plugindir.patch;patch=1"

inherit autotools pkgconfig

require alsa-fpu.inc
EXTRA_OECONF += "${@get_alsa_fpu_setting(bb, d)} "

EXTRA_OECONF = "--with-cards=pdaudiocf --with-oss=yes"

do_stage () {
	oe_libinstall -so -C src libasound ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/alsa/sound
	install -m 0644 include/*.h ${STAGING_INCDIR}/alsa/
	install -m 0644 include/sound/ainstr*.h ${STAGING_INCDIR}/alsa/sound/
	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 utils/alsa.m4 ${STAGING_DATADIR}/aclocal/
}

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
