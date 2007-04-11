DESCRIPTION = "Util-linux is a suite of essential utilities for any Linux system."
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "zlib-native ncurses-native"

inherit autotools native

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/util-linux/util-linux-${PV}.tar.bz2 \
           file://gcc34.patch;patch=1 \
           file://MCONFIG \
           file://make_include \
           file://swapargs.h \
	   file://fdiskbsdlabel_thumb.diff;patch=1 \
           file://defines.h"

S="${WORKDIR}/util-linux-${PV}"

EXTRA_OEMAKE="'OPT=${BUILD_CFLAGS}' 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'LDFLAGS=${BUILD_LDFLAGS}' SBINDIR=${base_sbindir} USRSBINDIR=${base_sbindir} LOGDIR=${localstatedir}/log VARPATH=${localstatedir} LOCALEDIR=${datadir}/locale"

do_compile () {
	set -e
	install ${WORKDIR}/MCONFIG ${S}/MCONFIG
	install ${WORKDIR}/make_include ${S}/make_include
	install ${WORKDIR}/swapargs.h ${S}/mount/swapargs.h
	install ${WORKDIR}/defines.h ${S}/defines.h
	oe_runmake
}

do_stage () {
	autotools_stage_all
}

