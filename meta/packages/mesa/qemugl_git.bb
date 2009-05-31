DESCRIPTION = "QEMU i386 OpenGL passtrough"
HOMEPAGE = "http://savannah.nongnu.org/projects/qemugl"
SECTION = "x11/drivers"
LICENSE = "GPL"

DEPENDS = "virtual/libx11 xproto glproto libxfixes"

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

SRC_URI = "git://git.o-hand.com/qemugl.git;protocol=git \
           file://headers.tgz \
           file://gl.pc"
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
	install -d ${D}{includedir}/GL/
	cp -pPR ${WORKDIR}/headers/* ${D}{includedir}/GL/
	install -d ${D}${libdir}/pkgconfig/
	cp ${WORKDIR}/gl.pc ${D}${libdir}/pkgconfig/
}

do_stage () {
	install -d ${STAGING_LIBDIR}/
	install -m 0755 ${S}/libGL.so ${STAGING_LIBDIR}/
	ln -s libGL.so ${STAGING_LIBDIR}/libGL.so.1
	install -d ${STAGING_INCDIR}/GL/
	cp -pPR ${WORKDIR}/headers/* ${STAGING_INCDIR}/GL/
	install -d ${STAGING_LIBDIR}/pkgconfig/
	cp ${WORKDIR}/gl.pc ${STAGING_LIBDIR}/pkgconfig/
}
