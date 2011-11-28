DESCRIPTION = " A compact getty program for virtual consoles only"
SECTION = "console/utils"
HOMEPAGE = "http://sourceforge.net/projects/mingetty/"
LICENSE = "GPLv2"
PR = "r1"

LIC_FILES_CHKSUM = "file://COPYING;md5=0c56db0143f4f80c369ee3af7425af6e"
SRC_URI = "http://cdnetworks-kr-1.dl.sourceforge.net/project/mingetty/mingetty/${PV}/mingetty-${PV}.tar.gz"

SRC_URI[md5sum] = "2a75ad6487ff271424ffc00a64420990"
SRC_URI[sha256sum] = "0f55c90ba4faa913d91ef99cbf5cb2eb4dbe2780314c3bb17953f849c8cddd17"

EXTRA_OEMAKE = "CC='${CC}'"

do_install(){
    mkdir -p ${D}${mandir}/man8
    mkdir -p ${D}/sbin
    oe_runmake install DESTDIR=${D}
}

inherit update-alternatives

ALTERNATIVE_NAME = "getty"
ALTERNATIVE_LINK = "${base_sbindir}/getty"
ALTERNATIVE_PATH = "${base_sbindir}/mingetty"
ALTERNATIVE_PRIORITY = "50"
