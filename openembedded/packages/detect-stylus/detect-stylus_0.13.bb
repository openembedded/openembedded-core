inherit gpe pkgconfig
LICENSE = "GPL"

PR = "r2"

DEPENDS = "libx11 xcursor-transparent-theme xrdb"
SECTION = "gpe"
RDEPENDS = "xrdb"

DESCRIPTION = "Touchscreen detection utility"
MAINTAINER = "Florian Boor <florian.boor@kernelconcepts.de>"

SRC_URI = "${GPE_MIRROR}/${PN}-${PV}.tar.gz \
           file://access.patch;patch=1;pnum=0 \
	   file://extra-device-check.patch;patch=1 \
	   file://correct-theme-name.patch;patch=1"

export CVSBUILD="no"
