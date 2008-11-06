DESCRIPTION = "With FUSE it is possible to implement a fully functional filesystem in a userspace program"
HOMEPAGE = "http://fuse.sf.net"
LICENSE = "GPL"
DEPENDS = "fakeroot-native"
RRECOMMENDS_fuse = "${@base_contains("MACHINE_FEATURES","kernel26","kernel-module-fuse","fuse-module",d)}"

SRC_URI = "${SOURCEFORGE_MIRROR}/fuse/fuse-${PV}.tar.gz"

inherit autotools pkgconfig

fakeroot do_install() {
	oe_runmake install DESTDIR=${D}
}
