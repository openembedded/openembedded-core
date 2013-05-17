# cdrtools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)
SUMMARY = "A set of tools for CD recording, including cdrecord"
DESCRIPTION = "A set of tools for CD recording, including cdrecord"
HOMEPAGE = "http://cdrecord.berlios.de/private/cdrecord.html"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=8d16123ffd39e649a5e4a6bc1de60e6d"
PR = "r0"

SRC_URI = "ftp://ftp.berlios.de/pub/cdrecord/cdrtools-${PV}.tar.bz2 \
           file://no_usr_src.patch"

SRC_URI[md5sum] = "f9fbab08fbd458b0d2312976d8c5f558"
SRC_URI[sha256sum] = "7f9cb64820055573b880f77b2f16662a512518336ba95ab49228a1617973423d"

inherit native

STAGE_TEMP = "${WORKDIR}/image-temp"

FILESPATH = "${FILE_DIRNAME}/cdrtools-native/"

do_install() {
	install -d ${STAGE_TEMP}
	make install INS_BASE=${STAGE_TEMP}

	install -d ${D}${bindir}/
	install ${STAGE_TEMP}/bin/* ${D}${bindir}/
}
