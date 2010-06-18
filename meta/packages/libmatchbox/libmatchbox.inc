DESCRIPTION = "Matchbox window manager core library"
HOMEPAGE = "http://matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://libmb/mbexp.c;endline=20;md5=28c0aef3b23e308464f5dae6a11b0d2f \
                    file://libmb/mbdotdesktop.c;endline=21;md5=5a287156b3207e851c1d68d09c439b51"

SECTION = "x11/libs"
DEPENDS = "virtual/libx11 libxext expat libxft jpeg libpng zlib libxsettings-client startup-notification"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-jpeg --enable-expat --enable-xsettings --enable-startup-notification"

S = "${WORKDIR}/libmatchbox-${PV}"
