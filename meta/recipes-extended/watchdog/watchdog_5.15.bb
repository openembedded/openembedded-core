SUMMARY = "Software watchdog"
DESCRIPTION = "Watchdog is a daemon that checks if your system is still \
working. If programs in user space are not longer executed \
it will reboot the system."
HOMEPAGE = "http://watchdog.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=172030&atid=860194"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ecc0551bf54ad97f6b541720f84d6569"

SRC_URI = "${SOURCEFORGE_MIRROR}/watchdog/watchdog-${PV}.tar.gz \
	   file://watchdog-init.patch \
	   file://watchdog-conf.patch \
"

SRC_URI[md5sum] = "678c32f6f35a0492c9c1b76b4aa88828"
SRC_URI[sha256sum] = "ffdc865137ad5d8e53664bd22bad4de6ca136d1b4636720320cb52af0c18947c"

UPSTREAM_CHECK_URI = "http://sourceforge.net/projects/watchdog/files/watchdog/"
UPSTREAM_CHECK_REGEX = "/watchdog/(?P<pver>(\d+[\.\-_]*)+)/"

inherit autotools
inherit update-rc.d

DEPENDS_append_libc-musl = " libtirpc "
CFLAGS_append_libc-musl = " -I${STAGING_INCDIR}/tirpc "
LDFLAGS_append_libc-musl = " -ltirpc "
EXTRA_OECONF_append_libc-musl = " --disable-nfs "

INITSCRIPT_NAME = "watchdog.sh"
INITSCRIPT_PARAMS = "start 15 1 2 3 4 5 . stop 85 0 6 ."

RRECOMMENDS_${PN} = "kernel-module-softdog"

do_install_append() {
	install -D ${S}/redhat/watchdog.init ${D}/${sysconfdir}/init.d/watchdog.sh
}
