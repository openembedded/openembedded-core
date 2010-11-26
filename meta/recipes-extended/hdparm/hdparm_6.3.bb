DESCRIPTION = "hdparm is a Linux shell utility for viewing \
and manipulating various IDE drive and driver parameters."
SECTION = "console/utils"
PRIORITY = "optional"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=910a8a42c962d238619c75fdb78bdb24"

SRC_URI = "${SOURCEFORGE_MIRROR}/hdparm/hdparm-${PV}.tar.gz \
	   file://bswap.patch;patch=1 \
	   file://uclibc.patch;patch=1"

do_install () {
	install -d ${D}/${sbindir} ${D}/${mandir}/man8
	oe_runmake 'DESTDIR=${D}' install
}
