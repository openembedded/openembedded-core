# Beecrypt OE build file
# Copyright (C) 2004-2005, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION="Beecrypt is a general-purpose cryptography library."
HOMEPAGE="http://sourceforge.net/projects/beecrypt"
SRC_URI="${SOURCEFORGE_MIRROR}/beecrypt/beecrypt-${PV}.tar.gz \
	 file://x64fix.patch \
	 file://disable-icu-check.patch \
	 file://fix-security.patch"
LICENSE = "GPLv2&LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=9894370afd5dfe7d02b8d14319e729a1 \
	            file://COPYING.LIB;md5=dcf3c825659e82539645da41a7908589"
DEPENDS = "icu"

PR = "r0"

inherit autotools
acpaths=""

EXTRA_OECONF="--with-arch=${TARGET_ARCH} --without-python --enable-shared --enable-static"

BBCLASSEXTEND = "native"
