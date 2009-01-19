DESCRIPTION = "QEMU i386 OpenGL passtrough"
HOMEPAGE = "http://savannah.nongnu.org/projects/qemugl"
SECTION = "x11/drivers"
LICENSE = "GPL"

DEPENDS = "virtual/xserver-xf86 xproto glproto"

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

SRC_URI = "git://git.o-hand.com/qemugl.git;protocol=git"
S = "${WORKDIR}/git"

PV = "0.0+git${SRCREV}"

FILES_${PN} += " ${libdir}/qemu/*"
FILES_${PN}-dbg += " ${libdir}/qemu/.debug/*"

do_install () {
	install -d ${D}${libdir}/qemu
	install -m 0755 ${S}/libGL.so ${D}${libdir}/qemu
	ln -s libGL.so ${D}${libdir}/qemu/libGL.so.1
}
