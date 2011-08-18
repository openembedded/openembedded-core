# dosfstools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

require dosfstools_${PV}.bb

PR="r5"

SRC_URI = "ftp://ftp.uni-erlangen.de/pub/Linux/LOCAL/dosfstools/dosfstools-${PV}.src.tar.gz \
	file://mkdosfs-bootcode.patch \
	file://mkdosfs-dir.patch \
	file://alignment_hack.patch \
	file://dosfstools-2.10-kernel-2.6.patch \
	file://msdos_fat12_undefined.patch \
	file://dosfstools-msdos_fs-types.patch \
	file://include-linux-types.patch \
	file://2.6.20-syscall.patch"

inherit native
