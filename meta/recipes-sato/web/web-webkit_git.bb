DESCRIPTION = "Multi-platform web browsing application."
HOMEPAGE = "http://o-hand.com/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

SECTION = "x11"
DEPENDS = "libxml2 glib-2.0 gtk+ libglade webkit-gtk curl gconf js libowl"

# To access https web pages
RRECOMMENDS_${PN} += "glib-networking"

SRCREV = "0f7019acd9db5383c732d0524c7c3a5eaec9be2b"
PV = "0.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://link-with-g++.patch \
	   file://make-382.patch"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--enable-libowl"

inherit autotools pkgconfig gconf

do_configure_prepend() {
    touch ${S}/src/dummy.cpp
}

FILES_${PN} += "${datadir}/web2"
