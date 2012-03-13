DESCRIPTION = "OpenedHand Widget Library video widget"
HOMEPAGE = "http://o-hand.com/"
BUGTRACKER = "http://bugzilla.o-hand.com/"
LICENSE = "LGPLv2.1 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ac14b7ca45afea5af040da54db270eb0 \
                    file://src/video.c;endline=22;md5=e8e9f23c3691c11af7d8fc03264ca9da \
                    file://src/bacon-volume.c;endline=20;md5=798804562b24e30bac482ba91c45e46d"
SECTION = "x11"
DEPENDS = "libowl-av"

SRCREV = "f133472318970796fae1ea3e98ac062156768baf"
PV = "0.1+git${SRCPV}"
PR = "r1"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://gtk_multithread_safe.patch \
           file://owl-video-widget.png \
           file://stock_media-play.png \
           file://stock_volume-0.png \
           file://stock_volume-min.png \
           file://stock_volume-med.png \
           file://stock_volume-max.png \
           file://owl-video-widget.desktop \
	   file://make-382.patch"

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
