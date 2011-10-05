# mtools OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION="Mtools is a collection of utilities for accessing MS-DOS disks from Unix without mounting them."
HOMEPAGE="http://mtools.linux.lu"
LICENSE="GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=92b58ec77696788ce278b044d2a8e9d3"
PR = "r4"

#http://mtools.linux.lu/mtools-${PV}.tar.gz 
SRC_URI="http://folks.o-hand.com/richard/poky/sources/mtools-${PV}.tar.gz \
	file://mtools-makeinfo.patch \
	file://mtools.patch \
	file://no-x11.patch"

S = "${WORKDIR}/mtools-${PV}"

inherit autotools

EXTRA_OECONF = "--without-x"

PARALLEL_MAKEINST = ""

BBCLASSEXTEND = "native"
