# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION = "A multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPL"
SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/boot/syslinux/syslinux-${PV}.tar.bz2 "

S = "${WORKDIR}/syslinux-${PV}"
STAGE_TEMP = "${WORKDIR}/stage_temp"

inherit native

do_compile() {
	oe_runmake installer
}

do_stage() {
	install -d ${STAGE_TEMP}
	oe_runmake install INSTALLROOT="${STAGE_TEMP}"

	install -d ${STAGING_BINDIR}
	install -m 755 ${STAGE_TEMP}/usr/bin/syslinux ${STAGING_BINDIR}
	install -m 755 ${STAGE_TEMP}/sbin/extlinux ${STAGING_BINDIR}
}
