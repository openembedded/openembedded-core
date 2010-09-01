DESCRIPTION = " A compact getty program for virtual consoles only"
SECTION = "console/utils"
PRIORITY = "required"
HOMEPAGE = "http://sourceforge.net/projects/mingetty/"
LICENSE = "GPLv2"
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=0c56db0143f4f80c369ee3af7425af6e"
SRC_URI = "http://cdnetworks-kr-1.dl.sourceforge.net/project/mingetty/mingetty/${PV}/mingetty-${PV}.tar.gz"

EXTRA_OEMAKE = "CC='${CC}'"

do_install(){
    mkdir -p ${D}${mandir}/man8
    mkdir -p ${D}/sbin
    oe_runmake install DESTDIR=${D}
}
