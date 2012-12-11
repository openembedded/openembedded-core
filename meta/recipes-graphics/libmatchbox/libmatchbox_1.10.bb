DESCRIPTION = "Matchbox window manager core library"
SECTION = "x11/libs"
HOMEPAGE = "http://matchbox-project.org/"
BUGTRACKER = "http://bugzilla.yoctoproject.com/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://libmb/mbexp.c;endline=20;md5=28c0aef3b23e308464f5dae6a11b0d2f \
                    file://libmb/mbdotdesktop.c;endline=21;md5=5a287156b3207e851c1d68d09c439b51"

DEPENDS = "virtual/libx11 libxext expat libxft jpeg libpng zlib libxsettings-client startup-notification"

SRC_URI = "http://downloads.yoctoproject.org/releases/matchbox/${BPN}/${PV}/${BPN}-${PV}.tar.gz \
           file://check.m4"
SRC_URI[md5sum] = "042c5874631dfb95151aa793dc1434b8"
SRC_URI[sha256sum] = "d14d4844840e3e1e4faa9f9e90060915d39b6033f6979464ab3ea3fe1c4f9293"

PR = "r0"

inherit autotools pkgconfig

S = "${WORKDIR}/libmatchbox-${PV}"

do_configure_prepend () {
	cp ${WORKDIR}/check.m4 ${S}/
}

EXTRA_OECONF = "--enable-jpeg --enable-expat --enable-xsettings --enable-startup-notification"
