DESCRIPTION = "Multi-platform web browsing application."
HOMEPAGE = "http://o-hand.com/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade webkit-gtk curl gconf js libowl"

PV = "0.0+svnr${SRCPV}"
PR = "r3"

SRC_URI = "svn://svn.o-hand.com/repos/web/branches;module=webkit;proto=http \
           file://link-with-g++.patch \
	   file://make-382.patch"

S = "${WORKDIR}/webkit"

EXTRA_OECONF = "--enable-libowl"

inherit autotools pkgconfig gconf

do_configure_prepend() {
    touch ${S}/src/dummy.cpp
}

FILES_${PN} += "${datadir}/web2"
