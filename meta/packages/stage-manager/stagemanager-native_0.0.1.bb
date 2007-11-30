DESCRIPTION = "Helper script for packaged-staging.bbclass"
PR = "r7"

SRC_URI = "file://stage-manager"
LICENSE = "GPLv2"

PACKAGE_ARCH = "all"

inherit native

DEPENDS = " "
PACKAGE_DEPENDS = " "
PATCHTOOL = ""
INHIBIT_DEFAULT_DEPS = "1"

do_install() {
	install -d ${STAGING_BINDIR}
	install -m 0755 ${WORKDIR}/stage-manager ${STAGING_BINDIR}
}

do_stage() {
:
}
