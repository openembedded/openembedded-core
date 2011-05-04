DESCRIPTION = "Tools for managing 'yaffs2' file systems."
SECTION = "base"
HOMEPAGE = "http://www.yaffs.net"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://yaffs_fs.c;beginline=1;endline=19;md5=9e87e2e9244febcc0dffb752cb7679e1"
PV = "0.0.0+cvs${SRCDATE}"
PR = "r0"
DEPENDS = "mtd-utils"

SRC_URI = "cvs://anonymous@cvs.aleph1.co.uk/home/aleph1/cvs;module=yaffs2 \
           file://mkyaffs2image.patch;patch=1"
S = "${WORKDIR}/yaffs2"

CFLAGS += "-I.. -DCONFIG_YAFFS_UTIL"
CFLAGS_append_virtclass-native = " -I.. -DCONFIG_YAFFS_UTIL"

do_compile() {
	cd utils && oe_runmake
}

do_install() {
	install -d ${D}${sbindir}/
	for i in mkyaffsimage mkyaffs2image; do
		install -m 0755 utils/$i ${D}${sbindir}/
	done
}

BBCLASSEXTEND = "native"
