SECTION = "libs"
LICENSE = "BSD"
DESCRIPTION = "A library for configuring and customizing font access."
DEPENDS = "expat freetype freetype-native zlib fontconfig-native"

SRC_URI = "http://pdx.freedesktop.org/fontconfig/release/fontconfig-${PV}.tar.gz \
           file://fc-glyphname.patch;patch=1 \
           file://fc-lang.patch;patch=1 \
	   file://local.conf"
PR = "r1"

PACKAGES =+ "fontconfig-utils "
FILES_fontconfig-utils = "${bindir}/*"

S = "${WORKDIR}/fontconfig-${PV}"

inherit autotools pkgconfig

export HASDOCBOOK="no"

EXTRA_OECONF = " --disable-docs "
EXTRA_OEMAKE = "FC_LANG=fc-lang FC_GLYPHNAME=fc-glyphname"

do_stage () {
	oe_libinstall -so -a -C src libfontconfig ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/fontconfig
	for i in ${S}/fontconfig/*.h; do install -m 0644 $i ${STAGING_INCDIR}/fontconfig/; done
}

do_install () {
	autotools_do_install
	install -m 0644 ${WORKDIR}/local.conf ${D}${sysconfdir}/fonts/local.conf
}

