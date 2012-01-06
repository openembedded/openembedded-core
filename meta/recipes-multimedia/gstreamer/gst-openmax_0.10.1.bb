DEPENDS = "gstreamer"
RDEPENDS_${PN} = "libomxil"
LICENSE = "LGPLv2.1"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24 \
                    file://util/sem.h;beginline=1;endline=20;md5=accce5550d5583b839b441a0623f09fc"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-openmax/gst-openmax-${PV}.tar.bz2 \
           file://gcc_4.6.patch \
           "

inherit autotools

EXTRA_OECONF += "--disable-valgrind"

PR = "r3"

FILES_${PN} += "${libdir}/gstreamer-0.10/libgstomx.so"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/libgstomx.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-0.10/libgstomx.a"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug/"

SRC_URI[md5sum] = "4d0370bfe99dea20918c84347abadb4e"
SRC_URI[sha256sum] = "9074d5a0591995133d19cfb15144f19664f902c1623f996595695cf2c2070e1f"
