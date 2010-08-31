DESCRIPTION = " A tool for managing Soft RAID under Linux"
HOMEPAGE = "http://www.kernel.org/pub/linux/utils/raid/mdadm/"
BUGTRACKER = "n/a"

#A few files are GPLv2+ while most files are GPLv2.
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://mdmon.c;beginline=4;endline=18;md5=af7d8444d9c4d3e5c7caac0d9d34039d \
                    file://mdadm.h;beglinlne=4;endline=22;md5=462bc9936ac0d3da110191a3f9994161"

PR = "r0"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/raid/mdadm/${PN}-${PV}.tar.bz2"

CFLAGS += "-fno-strict-aliasing"

inherit autotools

do_compile() {
	export CROSS_COMPILE="${TARGET_PREFIX}"
	oe_runmake
}

do_install() {
	export STRIP=""
	autotools_do_install
}

