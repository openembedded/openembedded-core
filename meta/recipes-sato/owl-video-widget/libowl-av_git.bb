DESCRIPTION = "OpenedHand Widget Library Audio/Video"
HOMEPAGE = "http://www.o-hand.com"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ac14b7ca45afea5af040da54db270eb0 \
                    file://libowl-av/owl-video-widget.h;endline=22;md5=0d4caab10952acdf470086c25c7f70c8 \
                    file://libowl-av/owl-audio-player.h;endline=22;md5=4af2d44f206a086f9f03881236f7390b"

SECTION = "x11"
DEPENDS = "gtk+ gstreamer gst-plugins-base"
RDEPENDS_${PN} = "gst-meta-base"
RRECOMMENDS_${PN} = "gst-meta-audio gst-meta-video"

SRCREV = "1379fd230a32509e94971961183f1031f469737a"
PV = "0.1+git${SRCPV}"
PR = "r2"


SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://gst_change_state.patch \
	   file://make-382.patch"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
