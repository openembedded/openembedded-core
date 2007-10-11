# cdrtools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

LICENSE="GPL"
DESCRIPTION="A set of tools for CD recording, including cdrecord"
HOMEPAGE="http://cdrecord.berlios.de/old/private/cdrecord.html"

SRC_URI="ftp://ftp.berlios.de/pub/cdrecord/cdrtools-${PV}.tar.bz2"

S="${WORKDIR}/cdrtools-${PV}"

inherit native

STAGE_TEMP="${WORKDIR}/stage_temp"

do_stage() {
	install -d ${STAGE_TEMP}
	make install INS_BASE=${STAGE_TEMP}

	install -d ${STAGING_BINDIR}
	install ${STAGE_TEMP}/bin/* ${STAGING_BINDIR}
}
