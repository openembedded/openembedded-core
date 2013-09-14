# cdrtools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)
SUMMARY = "A set of tools for CD recording, including cdrecord"
DESCRIPTION = "A set of tools for CD recording, including cdrecord"
HOMEPAGE = "http://cdrecord.berlios.de/private/cdrecord.html"
SECTION = "console/utils"
LICENSE = "GPLv2 & CDDL-1.0 & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=32f68170be424c2cd64804337726b312"

SRC_URI = "ftp://ftp.berlios.de/pub/cdrecord/alpha/cdrtools-${PV}.tar.bz2"

SRC_URI[md5sum] = "4cef9db0cf15a770c52d65b00bbee2db"
SRC_URI[sha256sum] = "3d613965b213ad83e4be0ba2535e784901839ea4d11a20a2beb6765f0eb76dfa"

S = "${WORKDIR}/cdrtools-3.01"

inherit native

FILESPATH = "${FILE_DIRNAME}/cdrtools-native/"

do_install() {
	make install GMAKE_NOWARN=true INS_BASE=${prefix} DESTDIR=${D}
}
