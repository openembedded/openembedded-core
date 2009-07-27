DESCRIPTION = "Synthesis SyncML Engine"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r1"

DEPENDS = "libpcre"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
	cd ${S}/src
	${S}/src/gen-makefile-am.sh
	cd ${S}
}

