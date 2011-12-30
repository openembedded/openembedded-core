DESCRIPTION = " A tool for managing Soft RAID under Linux"
HOMEPAGE = "http://www.kernel.org/pub/linux/utils/raid/mdadm/"
BUGTRACKER = "n/a"

# Some files are GPLv2+ while others are GPLv2.
LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://mdmon.c;beginline=4;endline=18;md5=af7d8444d9c4d3e5c7caac0d9d34039d \
                    file://mdadm.h;beglinlne=4;endline=22;md5=462bc9936ac0d3da110191a3f9994161"

PR = "r0"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/raid/mdadm/${BPN}-${PV}.tar.bz2 \
	    file://mdadm-3.2.2_fix_for_x32.patch \
	  "

SRC_URI[md5sum] = "d789d6ecb9c1d5ebcc64f0fc52bca92f"
SRC_URI[sha256sum] = "0ff49a04c6250834ce1b5be5a1581532e70e2221a87e1eedec46d6656b37ab40"

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
