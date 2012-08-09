SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ecc0551bf54ad97f6b541720f84d6569"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog-${PV}.tar.gz \
	   file://fix-ping-failure.patch"

SRC_URI[md5sum] = "cea28bea70e54f3625062bc808aef9af"
SRC_URI[sha256sum] = "862d5da6ab34568bbd4ea695316f063fecabe107d0939ceea6c67c9ec0b1a08e"

inherit autotools

RRECOMMENDS_${PN} = "kernel-module-softdog"
