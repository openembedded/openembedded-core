DESCRIPTION = "Moblin toolkit library for netbooks"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r2"

DEPENDS = "clutter-1.0 libccss clutter-imcontext"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
	echo "EXTRA_DIST=" > ${S}/gtk-doc.make
	touch ${S}/INSTALL
}
