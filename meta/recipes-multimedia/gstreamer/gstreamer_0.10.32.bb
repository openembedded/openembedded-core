SUMMARY = "GStreamer multimedia framework"
DESCRIPTION = "GStreamer is a multimedia framework for encoding and decoding video and sound. \
It supports a wide range of formats including mp3, ogg, avi, mpeg and quicktime."
HOMEPAGE = "http://gstreamer.freedesktop.org/"
BUGTRACKER = "https://bugzilla.gnome.org/enter_bug.cgi?product=Gstreamer"
SECTION = "multimedia"
PRIORITY = "optional"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://gst/gst.h;beginline=1;endline=21;md5=8e5fe5e87d33a04479fde862e238eaa4"
DEPENDS = "glib-2.0 libxml2 bison-native flex-native"

PR = "r0"

SRC_URI = "http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.bz2 \
           file://check_fix.patch \
           file://gst-inspect-check-error.patch"

SRC_URI[md5sum] = "442bc3d37b8511a73379143e7531d726"
SRC_URI[sha256sum] = "3bf4e46a186ee9a1f5e212aaf651d67cffb4f5f05345a7c99ae71d5d992be133"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-docs-build --disable-dependency-tracking --with-check=no --disable-examples --disable-tests --disable-valgrind --disable-debug"

#do_compile_prepend () {
#	mv ${WORKDIR}/gstregistrybinary.[ch] ${S}/gst/
#}

RRECOMMENDS_${PN}_qemux86    += "kernel-module-snd-ens1370 kernel-module-snd-rawmidi"
RRECOMMENDS_${PN}_qemux86-64 += "kernel-module-snd-ens1370 kernel-module-snd-rawmidi"

FILES_${PN} += " ${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dev += " ${libdir}/gstreamer-0.10/*.la ${libdir}/gstreamer-0.10/*.a"
FILES_${PN}-dbg += " ${libdir}/gstreamer-0.10/.debug/"
