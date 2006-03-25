SECTION = "x11/libs"
DESCRIPTION = "Matchbox window manager core library"
LICENSE = "GPL"
DEPENDS = "libx11 libxext expat libxft jpeg libpng zlib libxsettings-client"
PR="r2"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-jpeg --enable-expat --enable-xsettings"

S = "${WORKDIR}/libmatchbox-${PV}"

headers = "hash.h mbconfig.h mbdotdesktop.h mbexp.h \
	   mb.h mbmenu.h mbpixbuf.h mbtray.h mbutil.h"

do_stage () {
	install -d ${STAGING_INCDIR}/libmb
	for h in ${headers}; do
		install -m 0644 ${S}/libmb/$h ${STAGING_INCDIR}/libmb/
	done

	oe_libinstall -a -so -C libmb libmb ${STAGING_LIBDIR}
}
