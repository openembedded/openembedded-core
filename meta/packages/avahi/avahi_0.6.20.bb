require avahi.inc

RDEPENDS_avahi-daemon = "sysvinit-pidof"
PR = "r4"

FILES_avahi-autoipd = "${sbindir}/avahi-autoipd \
                       ${sysconfdir}/avahi/avahi-autoipd.action \
		       ${sysconfdir}/dhcp3/*/avahi-autoipd"

do_stage() {
	autotools_stage_all
}
