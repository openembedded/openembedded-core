DESCRIPTION = "Alsa sound library"
SECTION = "libs/multimedia"
LICENSE = "GPL"

# configure.in sets -D__arm__ on the command line for any arm system
# (not just those with the ARM instruction set), this should be removed,
# (or replaced by a permitted #define).
#FIXME: remove the following
ARM_INSTRUCTION_SET = "arm"

SRC_URI = "ftp://ftp.alsa-project.org/pub/lib/alsa-lib-${PV}.tar.bz2"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-cards=pdaudiocf --with-oss=yes"

do_stage () {
	oe_libinstall -so -C src libasound ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/alsa/sound
	install -m 0644 include/*.h ${STAGING_INCDIR}/alsa/
	install -m 0644 include/sound/ainstr*.h ${STAGING_INCDIR}/alsa/sound/
	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 utils/alsa.m4 ${STAGING_DATADIR}/aclocal/
}

PACKAGES = "alsa-dbg libasound alsa-server alsa-conf alsa-doc alsa-dev"
FILES_alsa-dbg = "${FILES_${PN}-dbg}"
FILES_libasound = "${libdir}/*.so.* ${libdir}/alsa-lib/smixer/*.so"
FILES_alsa-server = "${bindir}/*"
FILES_alsa-conf = "${datadir}"
FILES_alsa-dev = "${libdir}/*.so ${libdir}/pkgconfig/ ${includedir}/"

RDEPENDS_libasound = "alsa-conf"
