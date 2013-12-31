# cdrtools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)
SUMMARY = "A set of tools for CD recording, including cdrecord"
HOMEPAGE = "http://cdrecord.berlios.de/private/cdrecord.html"
SECTION = "console/utils"
LICENSE = "GPLv2 & CDDL-1.0 & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=32f68170be424c2cd64804337726b312"

SRC_URI = "ftp://ftp.berlios.de/pub/cdrecord/alpha/cdrtools-${PV}.tar.bz2"

SRC_URI[md5sum] = "f8c6f0fdcba7df0606095498d10315a7"
SRC_URI[sha256sum] = "e399ea964b8048793721b71461271e46d81f242bd2feefb8dbd259c30e75a5a9"

S = "${WORKDIR}/cdrtools-3.01"

inherit native

do_install() {
	make install GMAKE_NOWARN=true INS_BASE=${prefix} DESTDIR=${D}
}
