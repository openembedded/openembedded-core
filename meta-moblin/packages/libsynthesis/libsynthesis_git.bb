DESCRIPTION = "Synthesis SyncML Engine"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git;branch=syncevolution-0-9-x"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r2"

DEPENDS = "libpcre sqlite3 zlib"

S = "${WORKDIR}/git"

inherit autotools

do_configure_prepend () {
	cd ${S}/src
	${S}/src/gen-makefile-am.sh
	cd ${S}
}

