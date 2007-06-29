LICENSE = "GPL"
DESCRIPTION = "Touchscreen calibration utility"
SECTION = "x11/base"

DEPENDS = "virtual/libx11 libxft libxrandr xcalibrate"
RDEPENDS = "formfactor"

PR = "r3"

SRC_URI = "${GPE_MIRROR}/xtscal-${PV}.tar.bz2 \
           file://change-cross.patch;patch=1 \
           file://formfactor.patch;patch=1 \
	   file://cleanup.patch;patch=1"

inherit autotools
