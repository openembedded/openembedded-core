SUMMARY = "RPC program number mapper"
HOMEPAGE = "http://neil.brown.name/portmap/"
SECTION = "console/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://portmap.c;beginline=2;endline=31;md5=51ff67e66ec84b2009b017b1f94afbf4 \
                    file://from_local.c;beginline=9;endline=35;md5=1bec938a2268b8b423c58801ace3adc1"
DEPENDS = "virtual/fakeroot-native"

SRC_URI = "${DEBIAN_MIRROR}/main/p/portmap/portmap_5.orig.tar.gz \
	${DEBIAN_MIRROR}/main/p/portmap/portmap_${PV}.diff.gz \
	file://portmap.init \
	file://make.patch;apply=yes"

S = "${WORKDIR}/portmap_5beta"

INITSCRIPT_NAME = "portmap"
INITSCRIPT_PARAMS = "start 10 2 3 4 5 . stop 32 0 1 6 ."

inherit update-rc.d systemd

SYSTEMD_SERVICE_${PN} = "portmap.service"

sbindir = "/sbin"

fakeroot do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${base_sbindir}
	install -m 0755 ${WORKDIR}/portmap.init ${D}${sysconfdir}/init.d/portmap
	oe_runmake 'docdir=${docdir}/portmap' 'DESTDIR=${D}' install
}

PACKAGES =+ "portmap-utils"
FILES_portmap-utils = "/sbin/pmap_set /sbin/pmap_dump"
FILES_${PN}-doc += "${docdir}"
