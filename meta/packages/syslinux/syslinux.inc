# syslinux-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION="A multi-purpose linux bootloader"
HOMEPAGE="http://syslinux.zytor.com/"
LICENSE="GPL"

# If you really want to run syslinux, you need mtools.  We just want the
# ldlinux.* stuff for now, so skip mtools-native
DEPENDS="nasm-native"

S="${WORKDIR}/syslinux-${PV}"


inherit native

do_stage() {
	install -d ${STAGING_BINDIR}
	install -m 755 ${S}/syslinux ${STAGING_BINDIR}

	# When building media, the syslinux binary isn't nearly as useful
	# as the DOS data files, so we copy those into a special location
	# for usage during a image build stage

	install -d ${STAGING_DATADIR}/syslinux
	install -m 644 ${S}/ldlinux.sys ${STAGING_DATADIR}/syslinux/ldlinux.sys
	install -m 644 ${S}/ldlinux.bss ${STAGING_DATADIR}/syslinux/ldlinux.bss
}
