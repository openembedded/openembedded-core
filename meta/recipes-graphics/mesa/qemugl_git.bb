DESCRIPTION = "QEMU i386 OpenGL passtrough"
HOMEPAGE = "http://savannah.nongnu.org/projects/qemugl"
SECTION = "x11/drivers"
LICENSE = "GPL"

DEPENDS = "virtual/libx11 xproto glproto libxfixes"

COMPATIBLE_HOST = '(x86_64.*|i.86.*)-(linux|freebsd.*)'

SRC_URI = "git://git.o-hand.com/qemugl.git;protocol=git \
           file://versionfix.patch \
           file://remove-x11r6-lib-dir.patch"
S = "${WORKDIR}/git"

PV = "0.0+git${SRCPV}"
PR = "r7"

DEFAULT_PREFERENCE = "-1"

do_install () {
	install -d ${D}${libdir}/
    if [ "${PN}" != "qemugl-nativesdk" ]; then
        install -m 0755 ${S}/libGL.so.1.2 ${D}${libdir}/libGL-qemu.so.1.2
    else
	    install -m 0755 ${S}/libGL.so.1.2 ${D}${libdir}/libGL.so.1.2
	    ln -s libGL.so.1.2 ${D}${libdir}/libGL.so.1
	    ln -s libGL.so.1 ${D}${libdir}/libGL.so
    fi
}

pkg_postinst_${PN} () {
    if [ "${PN}" != "qemugl-nativesdk" ]; then
        rm -f $D${libdir}/libGL.so.1.2
        ln -s libGL-qemu.so.1.2 $D${libdir}/libGL.so.1.2
    fi
}

BBCLASSEXTEND = "nativesdk"
