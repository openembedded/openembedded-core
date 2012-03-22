require gst-plugins.inc

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=622921ffad8cb18ab906c56052788a3f \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

DEPENDS += "gst-plugins-base gconf cairo jpeg libpng gtk+ zlib libid3tag flac \
	    speex libsoup-2.4 pulseaudio"
PR = "r1"

inherit gettext gconf

EXTRA_OECONF += "--disable-aalib --disable-esd --disable-shout2 --disable-libcaca --disable-hal --without-check \
                 --disable-orc"

do_configure_prepend() {
	# This m4 file contains nastiness which conflicts with libtool 2.2.2
	rm ${S}/m4/lib-link.m4 || true
}

SRC_URI[md5sum] = "24f98a294a2b521e1b29412bdadae2e6"
SRC_URI[sha256sum] = "7e27840e40a7932ef2dc032d7201f9f41afcaf0b437daf5d1d44dc96d9e35ac6"

FILES_${PN}-gconfelements += "${sysconfdir}/gconf/schemas/gstreamer-0.10.schemas"
