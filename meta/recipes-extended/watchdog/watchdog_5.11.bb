SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPL-1.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8a7258c60a71a2f04b67fb01f495889c"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog_${PV}.tar.gz"

SRC_URI[md5sum] = "02c764219b3bdb2373091cbd67109eb6"
SRC_URI[sha256sum] = "723a7966e0c3d58e3f4df20943a5c9aa1553381f46aa0dbcf832016756e62792"

inherit autotools

RRECOMMENDS_${PN} = "kernel-module-softdog"
