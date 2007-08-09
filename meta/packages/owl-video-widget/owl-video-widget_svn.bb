DESCRIPTION = "OpenedHand Widget Library video widget"
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "gtk+ gstreamer gst-plugins-base"
RDEPENDS = "gst-meta-base"
RRECOMMENDS = "gst-meta-audio gst-meta-video"

PV = "0.1+svnr${SRCREV}"
S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http \
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
