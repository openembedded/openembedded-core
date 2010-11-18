SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPLv1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8a7258c60a71a2f04b67fb01f495889c"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog-${PV}.tar.gz"

SRC_URI[md5sum] = "d7cae3c9829f5d9a680764f314234867"
SRC_URI[sha256sum] = "60d8e9180b8800e1b2386553a9f63a026bddad4997d21790f7c5dde7e77065ec"

inherit autotools

RRECOMMENDS_${PN} = "kernel-module-softdog"
