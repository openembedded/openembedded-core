DESCRIPTION = "QEMU i386 OpenGL passtrough"
HOMEPAGE = "http://savannah.nongnu.org/projects/qemugl"
SECTION = "x11/drivers"
LICENSE = "GPL"

DEPENDS = "virtual/libx11 xproto glproto libxfixes"

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

SRC_URI = "git://git.o-hand.com/qemugl.git;protocol=git \
           file://versionfix.patch;patch=1"
S = "${WORKDIR}/git"

PV = "0.0+git${SRCREV}"
PR = "r5"

DEFAULT_PREFERENCE = "-1"

do_install () {
	install -d ${D}${libdir}/
	install -m 0755 ${S}/libGL.so.1.2 ${D}${libdir}/libGL-qemu.so.1.2
}

pkg_postinst_${PN} () {
    rm -f $D${libdir}/libGL.so.1.2
    ln -s libGL-qemu.so.1.2 $D${libdir}/libGL.so.1.2
}

BBCLASSEXTEND = "nativesdk"
