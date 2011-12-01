DESCRIPTION = " A tool for managing Soft RAID under Linux"
HOMEPAGE = "http://www.kernel.org/pub/linux/utils/raid/mdadm/"
BUGTRACKER = "n/a"

#A few files are GPLv2+ while most files are GPLv2.
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://mdmon.c;beginline=4;endline=18;md5=af7d8444d9c4d3e5c7caac0d9d34039d \
                    file://mdadm.h;beglinlne=4;endline=22;md5=462bc9936ac0d3da110191a3f9994161"

PR = "r3"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/raid/mdadm/${BPN}-${PV}.tar.bz2 \
	    file://0001-mdadm-fix-build-failures-ppc64.patch \
	    file://mdadm-3.2.2_fix_for_x32.patch \
	  "

SRC_URI[md5sum] = "12ee2fbf3beddb60601fb7a4c4905651"
SRC_URI[sha256sum] = "0d1a04e688b082bc88846e3f524abd50bc782b6ffc06123140f7d358c8f9b906"

CFLAGS += "-fno-strict-aliasing"

inherit autotools

do_compile() {
	oe_runmake
}

do_install() {
	export STRIP=""
	autotools_do_install
}

FILES_${PN} += "${base_libdir}/udev/rules.d/*.rules"
