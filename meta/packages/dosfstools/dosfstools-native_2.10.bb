# dosfstools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

require dosfstools_${PV}.bb
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/dosfstools-${PV}"

S="${WORKDIR}/dosfstools-${PV}"

PR="r4"

SRC_URI = "ftp://ftp.uni-erlangen.de/pub/Linux/LOCAL/dosfstools/dosfstools-${PV}.src.tar.gz \
	file://mkdosfs-bootcode.patch;patch=1 \
	file://mkdosfs-dir.patch;patch=1 \
	file://alignment_hack.patch;patch=1 \
	file://dosfstools-2.10-kernel-2.6.patch;patch=1 \
	file://msdos_fat12_undefined.patch;patch=1 \
	file://dosfstools-msdos_fs-types.patch;patch=1 \
	file://include-linux-types.patch;patch=1 \
	file://2.6.20-syscall.patch;patch=1"

inherit native

do_stage() {
	install -m 755 ${S}/mkdosfs/mkdosfs ${STAGING_BINDIR}/mkdosfs
	install -m 755 ${S}/dosfsck/dosfsck ${STAGING_BINDIR}/dosfsck
}
