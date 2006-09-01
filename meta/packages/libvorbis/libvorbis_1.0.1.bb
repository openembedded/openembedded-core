SECTION = "libs"
DEPENDS = "libogg"
DESCRIPTION = "Ogg Vorbis is a high-quality lossy audio codec \
that is free of intellectual property restrictions. libvorbis \
is the main vorbis codec library."
LICENSE = "BSD"
PR = "r2"

SRC_URI = "http://www.vorbis.com/files/${PV}/unix/libvorbis-${PV}.tar.gz \
file://m4.patch;patch=1"

inherit autotools  pkgconfig

# vorbisfile.c reveals a problem in the gcc register spilling for the
# thumb instruction set...
FULL_OPTIMIZATION_thumb = "-O0"

EXTRA_OECONF = "--with-ogg-libraries=${STAGING_LIBDIR} \
	        --with-ogg-includes=${STAGING_INCDIR}"

do_stage () {
	oe_libinstall -a -so -C lib libvorbis ${STAGING_LIBDIR}
	oe_libinstall -a -so -C lib libvorbisfile ${STAGING_LIBDIR}
	oe_libinstall -a -so -C lib libvorbisenc ${STAGING_LIBDIR}

	install -d ${STAGING_INCDIR}/vorbis
	install -m 0644 include/vorbis/vorbisenc.h \
			include/vorbis/vorbisfile.h \
			include/vorbis/codec.h ${STAGING_INCDIR}/vorbis/
	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 vorbis.m4 ${STAGING_DATADIR}/aclocal/
}
