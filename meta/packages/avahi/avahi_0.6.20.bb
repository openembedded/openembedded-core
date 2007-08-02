require avahi.inc

RDEPENDS_avahi-daemon = "sysvinit-pidof"
PR = "r1"

FILES_avahi-autoipd = "${sbindir}/avahi-autoipd \
                       ${sysconfdir}/avahi/avahi-autoipd.action"

do_stage() {
	autotools_stage_all
}
