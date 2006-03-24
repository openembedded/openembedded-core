LICENSE = "GPL"
SECTION = "base"
MAINTAINER = "Richard Purdie <rpurdie@openedhand.com>"
DESCRIPTION = "Daemon to handle device specifc features."
PV = "0.0+svn${SRCDATE}"
PR = "r0"
DEPENDS = "tslib"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=zaurusd;proto=http"
S = "${WORKDIR}/${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig

inherit update-rc.d

INITSCRIPT_NAME = "zaurusd"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
