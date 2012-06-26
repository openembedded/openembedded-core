DESCRIPTION = "Multi-platform web browsing application."
HOMEPAGE = "http://o-hand.com/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ webkit-gtk libowl"

# To access https web pages
RRECOMMENDS_${PN} += "glib-networking"

SRCREV = "b0676c190f876cbdb1df202fbcec42f212374503"
PV = "0.1+git${SRCPV}"
PR = "r2"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--enable-libowl"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/web2"
