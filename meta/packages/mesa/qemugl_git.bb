DESCRIPTION = "QEMU i386 OpenGL passtrough"
HOMEPAGE = "http://savannah.nongnu.org/projects/qemugl"
SECTION = "x11/drivers"
LICENSE = "GPL"

DEPENDS = "virtual/libx11 xproto glproto"

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

SRC_URI = "git://git.o-hand.com/qemugl.git;protocol=git"
S = "${WORKDIR}/git"

PV = "0.0+git${SRCREV}"
PR = "r1"

PROVIDES = "virtual/libgl"

DEFAULT_PREFERENCE = "-1"

# Multiple virtual/gl providers being built breaks staging
EXCLUDE_FROM_WORLD = "1"

do_install () {
	install -d ${D}${libdir}
	install -m 0755 ${S}/libGL.so ${D}${libdir}/
	ln -s libGL.so ${D}${libdir}/libGL.so.1
}
