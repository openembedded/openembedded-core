SUMMARY = "Free Lossless Audio Codec"
DESCRIPTION = "FLAC stands for Free Lossless Audio Codec, an audio format similar to MP3, but lossless."
HOMEPAGE = "http://flac.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=13478&atid=113478"
SECTION = "libs"
LICENSE = "FDLv1.2 & GPLv2+ & LGPLv2.1+ & BSD"
LIC_FILES_CHKSUM = "file://COPYING.FDL;md5=ad1419ecc56e060eccf8184a87c4285f \
                    file://src/Makefile.am;beginline=1;endline=16;md5=8dee151a56a3122f064a9dce771db37d \
                    file://COPYING.GPL;md5=079b27cd65c86dbc1b6997ffde902735 \
                    file://src/flac/main.c;beginline=1;endline=17;md5=756c4234516e4266ea45ee7bbbd798cf \
                    file://COPYING.LGPL;md5=fbc093901857fcd118f065f900982c24 \
                    file://src/plugin_common/all.h;beginline=1;endline=17;md5=b2e7960da6b43e4eccabf999bcf7f3a9 \
                    file://COPYING.Xiph;md5=df8975c0225f83ed7b567587ed953b83 \
                    file://include/FLAC/all.h;beginline=64;endline=69;md5=64474f2b22e9e77b28d8b8b25c983a48"
DEPENDS = "libogg"

PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/flac/flac-${PV}.tar.gz \
           file://disable-xmms-plugin.patch \
           file://flac-gcc43-fixes.patch \
           file://xmms.m4 \
           file://0001-No-AltiVec-on-SPE.patch"

SRC_URI[md5sum] = "153c8b15a54da428d1f0fadc756c22c7"
SRC_URI[sha256sum] = "9635a44bceb478bbf2ee8a785cf6986fba525afb5fad1fd4bba73cf71f2d3edf"

S = "${WORKDIR}/flac-${PV}"

inherit autotools

EXTRA_OECONF = "--disable-oggtest --disable-id3libtest \
                --with-ogg-libraries=${STAGING_LIBDIR} \
                --with-ogg-includes=${STAGING_INCDIR} \
                --without-xmms-prefix \
                --without-xmms-exec-prefix \
                --without-libiconv-prefix \
                --without-id3lib"
EXTRA_OECONF_prepend_e500mc = "--disable-altivec "
EXTRA_OECONF_prepend_e5500 = "--disable-altivec "
EXTRA_OECONF_prepend_e5500-64b = "--disable-altivec "

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
