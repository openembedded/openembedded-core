DESCRIPTION = "CORBA ORB"
PR = "r0"
LICENSE = "LGPL GPL"
SECTION = "x11/gnome/libs"
SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/ORBit2/2.14/ORBit2-${PV}.tar.bz2 \
           file://configure-lossage.patch;patch=1;pnum=1 \
	   file://gtk-doc.m4 \
	   file://gtk-doc.make"
DEPENDS = "libidl-native popt-native gtk-doc"

S = "${WORKDIR}/ORBit2-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/orbit2"

PARALLEL_MAKE = ""
inherit autotools native pkgconfig

EXTRA_OECONF = "--disable-gtk-doc"

do_configure_prepend() {
	mkdir -p m4
	install ${WORKDIR}/gtk-doc.m4 ./m4/
	install ${WORKDIR}/gtk-doc.make ./
}
