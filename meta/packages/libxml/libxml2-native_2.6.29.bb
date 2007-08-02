DESCRIPTION = "GNOME XML library"
PR = "r1"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxml2-${PV}.tar.gz"

DEPENDS = "python-native"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libxml2-${PV}"
S = "${WORKDIR}/libxml2-${PV}"

inherit autotools native pkgconfig

EXTRA_OECONF = "--with-python=${STAGING_INCDIR}/python2.4 --without-debug --without-legacy --without-catalog --without-docbook --with-c14n"

do_stage () {
	oe_runmake install
}
