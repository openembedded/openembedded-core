# cdrtools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)
SUMMARY = "A set of tools for CD recording, including cdrecord"
DESCRIPTION = "A set of tools for CD recording, including cdrecord"
HOMEPAGE = "http://cdrecord.berlios.de/private/cdrecord.html"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r2"

SRC_URI = "ftp://ftp.berlios.de/pub/cdrecord/cdrtools-${PV}.tar.bz2 \
           file://no_usr_src.patch \
           file://glibc-conflict-rename.patch"

SRC_URI[md5sum] = "d44a81460e97ae02931c31188fe8d3fd"
SRC_URI[sha256sum] = "728b6175069a77c4d7d92ae60108cbda81fbbf7bc7aa02e25153ccf2092f6c22"

inherit native

STAGE_TEMP = "${WORKDIR}/image-temp"

do_install() {
	install -d ${STAGE_TEMP}
	make install INS_BASE=${STAGE_TEMP}

	install -d ${D}${bindir}/
	install ${STAGE_TEMP}/bin/* ${D}${bindir}/
}
