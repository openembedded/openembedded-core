# mtools OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION = "Mtools is a collection of utilities for accessing MS-DOS disks from Unix without mounting them."
HOMEPAGE = "http://www.gnu.org/software/mtools/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=92b58ec77696788ce278b044d2a8e9d3"
PR = "r5"

#http://mtools.linux.lu/mtools-${PV}.tar.gz 
SRC_URI = "http://downloads.yoctoproject.org/mirror/sources/mtools-${PV}.tar.gz \
	file://mtools-makeinfo.patch \
	file://mtools.patch \
	file://no-x11.patch"

SRC_URI[md5sum] = "3e68b857b4e1f3a6521d1dfefbd30a36"
SRC_URI[sha256sum] = "af083a73425d664d4607ef6c6564fd9319a0e47ee7c105259a45356cb834690e"

S = "${WORKDIR}/mtools-${PV}"

inherit autotools

EXTRA_OECONF = "--without-x"

PARALLEL_MAKEINST = ""

BBCLASSEXTEND = "native"
