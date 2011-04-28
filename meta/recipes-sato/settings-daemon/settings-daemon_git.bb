DESCRIPTION = "Settings-daemon provides a bridge between gconf and xsettings"
HOMEPAGE = "http://svn.o-hand.com/view/matchbox/trunk/settings-daemon/"
BUGTRACKER = "http://bugzilla.openedhand.com/"
LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://xsettings-manager.h;endline=22;md5=7cfac9d2d4dc3694cc7eb605cf32a69b \
                    file://xsettings-common.h;endline=22;md5=7cfac9d2d4dc3694cc7eb605cf32a69b"
DEPENDS = "gconf glib-2.0 gtk+"
SECTION = "x11"
SRCREV = "0f174f463dfed500b65533e249cd22942e439c77"
PV = "0.0+git${SRCPV}"

PR = "r0"

SRC_URI = "git://git.yoctoproject.org/xsettings-daemon;protocol=git \
           file://addsoundkeys.patch;apply=yes \
           file://70settings-daemon.sh \
           file://dso_linking_change_build_fix.patch"

S = "${WORKDIR}/git"

inherit autotools pkgconfig gconf

FILES_${PN} = 	"${bindir}/* ${sysconfdir}"

do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d
	install -m 755 ${WORKDIR}/70settings-daemon.sh ${D}/${sysconfdir}/X11/Xsession.d/
}
