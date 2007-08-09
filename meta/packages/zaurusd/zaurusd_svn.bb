DESCRIPTION = "Daemon to handle device specifc features."
SECTION = "base"
MAINTAINER = "Richard Purdie <rpurdie@openedhand.com>"
LICENSE = "GPL"
DEPENDS = "tslib"
RDEPENDS = "xrandr"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=zaurusd;proto=http"

S = "${WORKDIR}/${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "zaurusd"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
