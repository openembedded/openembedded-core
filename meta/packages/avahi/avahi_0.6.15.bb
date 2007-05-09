require avahi.inc

RDEPENDS_avahi-daemon = "sysvinit-pidof"
PR = "r1"

SRC_URI += "file://patch-avahi-daemon_dbus-protocol.c.patch;patch=1;pnum=0"

FILES_avahi-autoipd = "${sbindir}/avahi-autoipd \
                       ${sysconfdir}/avahi/avahi-autoipd.action"

do_stage() {
	autotools_stage_all
}
