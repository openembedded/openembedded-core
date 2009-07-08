SECTION = "x11/libs"
DESCRIPTION = "Matchbox window manager core library"
LICENSE = "GPL"
DEPENDS = "virtual/libx11 libxext expat libxft jpeg libpng zlib libxsettings-client startup-notification"

inherit autotools_stage pkgconfig

EXTRA_OECONF = "--enable-jpeg --enable-expat --enable-xsettings --enable-startup-notification"

S = "${WORKDIR}/libmatchbox-${PV}"
