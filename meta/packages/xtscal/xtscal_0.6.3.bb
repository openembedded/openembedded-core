LICENSE = "GPL"
DESCRIPTION = "Touchscreen calibration utility"
SECTION = "x11/base"

DEPENDS = "virtual/libx11 libxft libxrandr xcalibrate"

PR = "r2"

SRC_URI = "${GPE_MIRROR}/xtscal-${PV}.tar.bz2 \
           file://change-cross.patch;patch=1 \
           file://xtscal-cxk.patch;patch=1"

inherit autotools
