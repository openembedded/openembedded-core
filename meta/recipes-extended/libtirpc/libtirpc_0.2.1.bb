SUMMARY = "Transport-Independent RPC library"
DESCRIPTION = "Libtirpc is a port of Suns Transport-Independent RPC library to Linux"
SECTION = "libs/network"
PRIORITY = "optional"
HOMEPAGE = "http://sourceforge.net/projects/libtirpc/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=183075&atid=903784"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=1c32c8e351f97e43e1ad6cf7f62de3bf \
                    file://src/netname.c;beginline=1;endline=27;md5=f8a8cd2cb25ac5aa16767364fb0e3c24"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libtirpc/libtirpc-${PV}.tar.bz2"

SRC_URI[md5sum] = "d77eb15f464bf9d6e66259eaf78b2a4e"
SRC_URI[sha256sum] = "ea77cadd63941fc4edbee7863d2c7094e6a18263d2a2c8922319aee91352ff41"

inherit autotools

do_install_append() {
	chown root:root ${D}${sysconfdir}/netconfig
}
