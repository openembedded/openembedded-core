SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ecc0551bf54ad97f6b541720f84d6569"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog-${PV}.tar.gz \
           file://fixsepbuild.patch \
	   file://fix-ping-failure.patch"
SRC_URI[md5sum] = "153455f008f1cf8f65f6ad9586a21ff1"
SRC_URI[sha256sum] = "141e0faf3ee4d8187a6ff4e00b18ef7b7a4ce432a2d4c8a6e6fdc62507fc6eb0"

inherit autotools

RRECOMMENDS_${PN} = "kernel-module-softdog"
