# mtools OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION="Mtools is a collection of utilities for accessing MS-DOS disks from Unix without mounting them."
HOMEPAGE="http://mtools.linux.lu"
LICENSE="GPL"
PR = "r2"

#http://mtools.linux.lu/mtools-${PV}.tar.gz 
SRC_URI="http://folks.o-hand.com/richard/poky/sources/mtools-${PV}.tar.gz \
	file://mtools-makeinfo.patch;patch=1 \
	file://mtools.patch;patch=1"

#DEPENDS = "tetex-native"

inherit autotools
