SUMMARY = "Implementation of XDG Sound Theme and Name Specifications"
DESCRIPTION = "Libcanberra is an implementation of the XDG Sound Theme and Name Specifications, for generating event sounds on free desktops."
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/canberra.h;beginline=7;endline=24;md5=c616c687cf8da540a14f917e0d23ab03"

DEPENDS = "gtk+ pulseaudio alsa-lib libtool"
PR = "r0"

inherit gconf autotools

SRC_URI = "http://0pointer.de/lennart/projects/libcanberra/libcanberra-${PV}.tar.gz"

SRC_URI[md5sum] = "ee2c66ada7c851a4e7b6eb1682285a24"
SRC_URI[sha256sum] = "4b5d8d2c2835133620adbc53745dd107b6e58b9a2963059e8f457143fee00982"

EXTRA_OECONF = " --disable-oss --disable-ltdl-install" 

do_configure_prepend () {
	rm -f ${S}/libltdl/configure*
}

FILES_${PN} += "${libdir}/gtk-2.0/modules/ ${datadir}/gnome"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"

