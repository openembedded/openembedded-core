DESCRIPTION = "Central control for Telepathy connection managers"
HOMEPAGE = "http://mission-control.sourceforge.net/"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d8045f3b8f929c1cb29a1e3fd737b499 \
                    file://src/request.h;beginline=1;endline=21;md5=f80534d9af1c33291b3b79609f196eb2"
SECTION = "libs"
DEPENDS = "libtelepathy dbus-glib gconf libxslt-native"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-mission-control/telepathy-mission-control-${PV}.tar.gz"

inherit autotools pkgconfig pythonnative

PACKAGES =+ " \
	libmissioncontrol \
	libmissioncontrol-config \
	libmissioncontrol-server \
	libmissioncontrol-dev \
	libmissioncontrol-config-dev \
	libmissioncontrol-server-dev \
	libmissioncontrol-dbg \
	libmissioncontrol-config-dbg \
	libmissioncontrol-server-dbg \
"

FILES_${PN} += "${datadir}/dbus* ${datadir}/glib-2.0/schemas"

FILES_libmissioncontrol = "${libdir}/libmissioncontrol.so.*"
FILES_libmissioncontrol-config = "${libdir}/libmissioncontrol-config.so.*"
FILES_libmissioncontrol-server = "${libdir}/libmissioncontrol-server.so.*"

FILES_libmissioncontrol-dev = "${libdir}/libmissioncontrol.* \
                               ${includedir}/libmissioncontrol/ \
                               ${libdir}/pkgconfig/libmissioncontrol.pc"
FILES_libmissioncontrol-config-dev = "${libdir}/libmissioncontrol-config.*"
FILES_libmissioncontrol-server-dev = "${libdir}/libmissioncontrol-server.*"

FILES_libmissioncontrol-dbg = "${libdir}/.debug/libmissioncontrol.so.*"
FILES_libmissioncontrol-config-dbg = "${libdir}/.debug/libmissioncontrol-config.so.*"
FILES_libmissioncontrol-server-dbg = "${libdir}/.debug/libmissioncontrol-server.so.*"

SRC_URI[md5sum] = "483fa8860891815a540ecf1bb9155122"
SRC_URI[sha256sum] = "58e0047f5292836c2c35e1603a908b3ac20a5c0b183869514c63de339fd48a30"
