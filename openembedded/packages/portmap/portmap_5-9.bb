DESCRIPTION = "RPC program number mapper."
SECTION = "console/network"
LICENSE = "GPL"
PR = "r5"

SRC_URI = "http://www.uk.debian.org/debian/pool/main/p/portmap/portmap_5.orig.tar.gz \
	http://www.uk.debian.org/debian/pool/main/p/portmap/portmap_${PV}.diff.gz;patch=1 \
	file://no-libwrap.patch;patch=1;pnum=0 \
	file://portmap.init \
	file://make.patch;patch=1"
S = "${WORKDIR}/portmap_5beta"

PACKAGES =+ "portmap-utils"
FILES_portmap-utils = "/sbin/pmap_set /sbin/pmap_dump"
FILES_${PN}-doc += "${docdir}"

INITSCRIPT_NAME = "portmap"
INITSCRIPT_PARAMS = "start 43 S . start 32 0 6 . stop 81 1 ."

inherit update-rc.d

sbindir = "/sbin"

do_compile() {
	oe_runmake
}

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/portmap.init ${D}${sysconfdir}/init.d/portmap
	oe_runmake 'docdir=${docdir}/portmap' \
		   'DESTDIR=${D}' install
}
