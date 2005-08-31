LICENSE = "GPL"
DESCRIPTION = "Touchscreen calibration utility"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
SECTION = "x11/base"

DEPENDS = "x11 libxft libxrandr xcalibrate"

PR = "r1"

SRC_URI = "${GPE_MIRROR}/xtscal-${PV}.tar.bz2 \
           file://xtscal-cxk.patch;patch=1"

inherit autotools
