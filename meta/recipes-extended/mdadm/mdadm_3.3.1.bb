SUMMARY = "Tool for managing software RAID under Linux"
HOMEPAGE = "http://www.kernel.org/pub/linux/utils/raid/mdadm/"

# Some files are GPLv2+ while others are GPLv2.
LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://mdmon.c;beginline=4;endline=18;md5=af7d8444d9c4d3e5c7caac0d9d34039d \
                    file://mdadm.h;beglinlne=4;endline=22;md5=462bc9936ac0d3da110191a3f9994161"


SRC_URI = "${KERNELORG_MIRROR}/linux/utils/raid/mdadm/${BPN}-${PV}.tar.xz \
           file://mdadm-3.2.2_fix_for_x32.patch \
           file://gcc-4.9.patch \
	  "

SRC_URI[md5sum] = "4227d48de62dfb217c92fa0c54171bbe"
SRC_URI[sha256sum] = "d8c74112cfd77bdc1dbc1291fe8d9243c76d91bfa276fcb95f2a75ca7717ab02"

CFLAGS += "-fno-strict-aliasing"

inherit autotools-brokensep

# We don't DEPEND on binutils for ansidecl.h so ensure we don't use the header
do_configure_prepend () {
	sed -i -e '/.*ansidecl.h.*/d' ${S}/sha1.h
}

EXTRA_OEMAKE = "CHECK_RUN_DIR=0"
# PPC64 and MIPS64 uses long long for u64 in the kernel, but powerpc's asm/types.h
# prevents 64-bit userland from seeing this definition, instead defaulting
# to u64 == long in userspace. Define __SANE_USERSPACE_TYPES__ to get
# int-ll64.h included
EXTRA_OEMAKE_append_powerpc64 = ' CFLAGS=-D__SANE_USERSPACE_TYPES__'
EXTRA_OEMAKE_append_mips64 = ' CFLAGS=-D__SANE_USERSPACE_TYPES__'

do_compile() {
	oe_runmake
}

do_install() {
	export STRIP=""
	autotools_do_install
}

FILES_${PN} += "${base_libdir}/udev/rules.d/*.rules"
