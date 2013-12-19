SUMMARY = "OpenedHand Widget Library video widget"
HOMEPAGE = "http://o-hand.com/"
BUGTRACKER = "http://bugzilla.yoctoproject.org/"
LICENSE = "LGPLv2.1 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ac14b7ca45afea5af040da54db270eb0 \
                    file://src/video.c;endline=22;md5=e8e9f23c3691c11af7d8fc03264ca9da \
                    file://src/bacon-volume.c;endline=20;md5=798804562b24e30bac482ba91c45e46d"
SECTION = "x11"
DEPENDS = "libowl-av"

SRCREV = "07ab48f331a5e6e0d169e4892c7eb1fc22dc0b9d"
PV = "0.1+git${SRCPV}"
PR = "r2"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/${BPN} \
           file://owl-video-widget.png \
           file://stock_media-play.png \
           file://stock_volume-0.png \
           file://stock_volume-min.png \
           file://stock_volume-med.png \
           file://stock_volume-max.png \
           file://owl-video-widget.desktop"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_media-play.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_volume-0.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_volume-min.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_volume-med.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/stock_volume-max.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/owl-video-widget.png ${D}/${datadir}/pixmaps

	install -d ${D}/${datadir}/applications
	install -m 0644 ${WORKDIR}/owl-video-widget.desktop ${D}/${datadir}/applications
}

FILES_${PN} += "${datadir}/video"
