# dosfstools OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION = "DOS FAT Filesystem Utilities"

SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://mkdosfs/COPYING;md5=cbe67f08d6883bff587f615f0cc81aa8"
PR = "r3"

SRC_URI = "ftp://ftp.uni-erlangen.de/pub/Linux/LOCAL/dosfstools/dosfstools-${PV}.src.tar.gz \
	   file://alignment_hack.patch \
	   file://dosfstools-2.10-kernel-2.6.patch \
           file://msdos_fat12_undefined.patch \
	   file://include-linux-types.patch"

SRC_URI[md5sum] = "59a02f311a891af8787c4c9e28c6b89b"
SRC_URI[sha256sum] = "55a7b2f5ea4506bde935ee3145573e1773427fc72283a36796c7c2cf861dd064"

do_install () {
	oe_runmake "PREFIX=${D}" "SBINDIR=${D}${sbindir}" \
		   "MANDIR=${D}${mandir}/man8" install
}
