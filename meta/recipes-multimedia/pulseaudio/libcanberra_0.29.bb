SUMMARY = "Implementation of XDG Sound Theme and Name Specifications"
DESCRIPTION = "Libcanberra is an implementation of the XDG Sound Theme and Name Specifications, for generating event sounds on free desktops."
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/canberra.h;beginline=7;endline=24;md5=c616c687cf8da540a14f917e0d23ab03"

DEPENDS = "gtk+ pulseaudio alsa-lib libtool libvorbis"
PR = "r0"

inherit gconf autotools

SRC_URI = "http://0pointer.de/lennart/projects/libcanberra/libcanberra-${PV}.tar.xz"

SRC_URI[md5sum] = "2594093a5d61047bd9cc87e955f86df8"
SRC_URI[sha256sum] = "127a5ef07805856d63758e5180ebfb241d1f80094fd301c287591a15b8cfcd72"

EXTRA_OECONF = " --disable-oss --disable-ltdl-install" 

do_configure_prepend () {
	rm -f ${S}/libltdl/configure*
}

PACKAGES += "${PN}-gnome"

FILES_${PN} += "${libdir}/gtk-2.0/modules/ ${datadir}/gnome"
FILES_${PN}-gnome += "${datadir}/gdm/autostart/LoginWindow/libcanberra-ready-sound.desktop \
	              ${libdir}/gnome-settings-daemon-3.0/gtk-modules/canberra-gtk-module.desktop"
FILES_${PN}-dev += "${libdir}/libcanberra-0.29/libcanberra-*.so \
		    ${libdir}/libcanberra-0.29/libcanberra-*.la \
		    ${datadir}/vala/vapi"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug ${libdir}/libcanberra-0.29/.debug"

