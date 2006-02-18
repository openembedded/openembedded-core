LICENSE = "GPL"
SECTION = "base"
MAINTAINER = "Richard Purdie <rpurdie@openedhand.com>"
DESCRIPTION = "Daemon to handle device specifc features."
PR = "r0"

SRC_URI = "file:///tmp/devmand-0.0.tgz"

#svn://svn.o-hand.com/repos/misc/trunk;module=chkhinge26;proto=http \
#S = "${WORKDIR}/${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig

inherit update-rc.d

INITSCRIPT_NAME = "devmand"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
