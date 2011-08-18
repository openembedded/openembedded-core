SUMMARY = "Utility for displaying and setting hard disk parameters"
DESCRIPTION = "hdparm is a system utility for viewing \
and manipulating various IDE drive and driver parameters."
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=910a8a42c962d238619c75fdb78bdb24"

SRC_URI = "${SOURCEFORGE_MIRROR}/hdparm/hdparm-${PV}.tar.gz \
	   file://bswap.patch \
	   file://uclibc.patch"

SRC_URI[md5sum] = "0c12672f3a09c14ad0b0882f15fc9389"
SRC_URI[sha256sum] = "08688a6a46ba495494bf838f8f26103e797584c1888eca94e43a171e1b37246d"

do_install () {
	install -d ${D}/${sbindir} ${D}/${mandir}/man8
	oe_runmake 'DESTDIR=${D}' install
}
