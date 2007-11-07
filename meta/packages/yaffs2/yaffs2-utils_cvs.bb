DESCRIPTION = "Tools for managing 'yaffs2' file systems."
SECTION = "base"
HOMEPAGE = "http://www.yaffs.net"
LICENSE = "GPLv2"
PV = "0.0.0+cvs${SRCDATE}"
PR = "r0"

#
# NOTE: This needs pretty recent mtd-utils otherwise it fails to compile
#

SRC_URI = "cvs://anonymous@cvs.aleph1.co.uk/home/aleph1/cvs;module=yaffs2 \
           file://mkyaffs2image.patch;patch=1"
S = "${WORKDIR}/yaffs2"

CFLAGS += "-I.. -DCONFIG_YAFFS_UTIL"

do_compile() {
	cd utils && oe_runmake
}

do_install() {
	install -d ${D}${sbindir}
    for i in mkyaffsimage mkyaffs2image; do
        install -m 0755 utils/$i ${D}${sbindir}
    done
}
