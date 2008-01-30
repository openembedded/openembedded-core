DESCRIPTION = "FLAC is a Free Lossless Audio Codec."
LICENSE = "BSD GPL"
SECTION = "libs"
DEPENDS = "libogg"
PR = "r5"

SRC_URI = "${SOURCEFORGE_MIRROR}/flac/flac-${PV}.tar.gz \
	   file://disable-xmms-plugin.patch;patch=1 \
	   file://xmms.m4"

S = "${WORKDIR}/flac-${PV}"

inherit autotools

EXTRA_OECONF = "--disable-oggtest --disable-id3libtest \
		--with-ogg-libraries=${STAGING_LIBDIR} \
		--with-ogg-includes=${STAGING_INCDIR} \
		--without-xmms-prefix \
		--without-xmms-exec-prefix \
		--without-libiconv-prefix \
		--without-id3lib"

PACKAGES += "libflac libflac++ liboggflac liboggflac++"
FILES_${PN} = "${bindir}/*"
FILES_libflac = "${libdir}/libFLAC.so.*"
FILES_libflac++ = "${libdir}/libFLAC++.so.*"
FILES_liboggflac = "${libdir}/libOggFLAC.so.*"
FILES_liboggflac++ = "${libdir}/libOggFLAC++.so.*"

do_configure () {
	install -d ${S}/m4
	install -m 0644 ${WORKDIR}/xmms.m4 ${S}/m4/
	autotools_do_configure
	# removes '-read-only-relocs' which is enabled for PowerPC builds.
	# It makes the build fail, other archs are not affected. Fixes #1775.
	sed -i 's/-Wl,-read_only_relocs,warning//g' src/libFLAC/Makefile
}

do_stage () {
	install -d ${STAGING_DATADIR}/aclocal

	oe_libinstall -a -so -C src/libOggFLAC libOggFLAC ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/OggFLAC
	install -m 0644 ${S}/include/OggFLAC/export.h ${STAGING_INCDIR}/OggFLAC/export.h

	install -m 0644 ${S}/include/OggFLAC/all.h ${STAGING_INCDIR}/OggFLAC/all.h
	install -m 0644 ${S}/include/OggFLAC/stream_encoder.h ${STAGING_INCDIR}/OggFLAC/stream_encoder.h
	install -m 0644 ${S}/include/OggFLAC/stream_decoder.h ${STAGING_INCDIR}/OggFLAC/stream_decoder.h
	install -m 0644 ${S}/src/libOggFLAC/libOggFLAC.m4 ${STAGING_DATADIR}/aclocal/

	oe_libinstall -a -so -C src/libFLAC libFLAC ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/FLAC
	install -m 0644 ${S}/include/FLAC/export.h ${STAGING_INCDIR}/FLAC/export.h
	install -m 0644 ${S}/include/FLAC/metadata.h ${STAGING_INCDIR}/FLAC/metadata.h
	install -m 0644 ${S}/include/FLAC/all.h ${STAGING_INCDIR}/FLAC/all.h
	install -m 0644 ${S}/include/FLAC/format.h ${STAGING_INCDIR}/FLAC/format.h
	install -m 0644 ${S}/include/FLAC/stream_encoder.h ${STAGING_INCDIR}/FLAC/stream_encoder.h
	install -m 0644 ${S}/include/FLAC/stream_decoder.h ${STAGING_INCDIR}/FLAC/stream_decoder.h
	install -m 0644 ${S}/include/FLAC/ordinals.h ${STAGING_INCDIR}/FLAC/ordinals.h
	install -m 0644 ${S}/include/FLAC/seekable_stream_encoder.h ${STAGING_INCDIR}/FLAC/seekable_stream_encoder.h
	install -m 0644 ${S}/include/FLAC/file_encoder.h ${STAGING_INCDIR}/FLAC/file_encoder.h
	install -m 0644 ${S}/include/FLAC/seekable_stream_decoder.h ${STAGING_INCDIR}/FLAC/seekable_stream_decoder.h
	install -m 0644 ${S}/include/FLAC/file_decoder.h ${STAGING_INCDIR}/FLAC/file_decoder.h
	install -m 0644 ${S}/include/FLAC/assert.h ${STAGING_INCDIR}/FLAC/assert.h
	install -m 0644 ${S}/include/FLAC/callback.h ${STAGING_INCDIR}/FLAC/callback.h
	install -m 0644 ${S}/src/libFLAC/libFLAC.m4 ${STAGING_DATADIR}/aclocal/

	oe_libinstall -a -so -C src/libFLAC++ libFLAC++ ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/FLAC++
	install -m 0644 ${S}/include/FLAC++/export.h ${STAGING_INCDIR}/FLAC++/export.h
	install -m 0644 ${S}/include/FLAC++/metadata.h ${STAGING_INCDIR}/FLAC++/metadata.h
	install -m 0644 ${S}/include/FLAC++/all.h ${STAGING_INCDIR}/FLAC++/all.h
	install -m 0644 ${S}/include/FLAC++/encoder.h ${STAGING_INCDIR}/FLAC++/encoder.h
	install -m 0644 ${S}/include/FLAC++/decoder.h ${STAGING_INCDIR}/FLAC++/decoder.h
	install -m 0644 ${S}/src/libFLAC++/libFLAC++.m4 ${STAGING_DATADIR}/aclocal/

	oe_libinstall -a -so -C src/libOggFLAC++ libOggFLAC++ ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/OggFLAC++
	install -m 0644 ${S}/include/OggFLAC++/export.h ${STAGING_INCDIR}/OggFLAC++/export.h
	install -m 0644 ${S}/include/OggFLAC++/all.h ${STAGING_INCDIR}/OggFLAC++/all.h
	install -m 0644 ${S}/include/OggFLAC++/encoder.h ${STAGING_INCDIR}/OggFLAC++/encoder.h
	install -m 0644 ${S}/include/OggFLAC++/decoder.h ${STAGING_INCDIR}/OggFLAC++/decoder.h
	install -m 0644 ${S}/src/libOggFLAC++/libOggFLAC++.m4 ${STAGING_DATADIR}/aclocal/
}
