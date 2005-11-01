DESCRIPTION = "hdparm is a Linux shell utility for viewing \
and manipulating various IDE drive and driver parameters."
SECTION = "console/utils"
PRIORITY = "optional"
LICENSE = "BSD"

SRC_URI = "http://www.ibiblio.org/pub/Linux/system/hardware/hdparm-${PV}.tar.gz \
	   file://bswap.patch;patch=1 \
	   file://uclibc.patch;patch=1"

do_install () {
	install -d ${D}/${sbindir} ${D}/${mandir}/man8
	oe_runmake 'DESTDIR=${D}' install
}
