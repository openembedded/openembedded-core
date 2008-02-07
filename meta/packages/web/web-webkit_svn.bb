DESCRIPTION = "Multi-platform web browsing application."
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade webkit-gtk curl gconf js libowl"

PV = "0.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/web/branches;module=webkit;proto=http \
           file://link-with-g++.patch;patch=1"

S = "${WORKDIR}/webkit"

EXTRA_OECONF = "--enable-libowl"

inherit autotools pkgconfig gconf

do_configure_prepend() {
    touch ${S}/src/dummy.cpp
}

FILES_${PN} += "${datadir}/web2"
