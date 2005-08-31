DESCRIPTION = "Zlib Compression Library"
SECTION = "libs"
PRIORITY = "required"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
HOMEPAGE = "http://www.gzip.org/zlib/"
LICENSE = "zlib"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libpng/zlib-${PV}.tar.gz"

S = "${WORKDIR}/zlib-${PV}"

export LDSHARED = "${CC} -shared -Wl,-soname,libz.so.1"
LDFLAGS_append = " -L. -lz"
CFLAGS_prepend = "-fPIC "
AR_append = " rc"
EXTRA_OEMAKE = ""

do_compile() {
	./configure --prefix=${prefix} --exec_prefix=${exec_prefix} --shared --libdir=${libdir} --includedir=${includedir}
	oe_runmake -e MAKEFLAGS="" libz.so.${PV} libz.a
}

do_stage() {
	install -m 0644 zlib.h ${STAGING_INCDIR}/zlib.h
	install -m 0644 zconf.h ${STAGING_INCDIR}/zconf.h
	oe_libinstall -a -so libz ${STAGING_LIBDIR}
}

do_install() {
	install -d ${D}${prefix} ${D}${includedir} ${D}${libdir}
	oe_runmake "prefix=${D}${prefix}" "includedir=${D}${includedir}" \
		   "libdir=${D}${libdir}" install
}
