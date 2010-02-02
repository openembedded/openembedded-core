DESCRIPTION = "Moblin toolkit library for netbooks"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r1"
DEPENDS = "clutter-1.0 clutter-imcontext"
S = "${WORKDIR}/git"

inherit autotools_stage

EXTRA_OECONF = "--disable-introspection --without-clutter-gesture"

do_configure_prepend () {
	echo "EXTRA_DIST=" > ${S}/gtk-doc.make
	touch ${S}/INSTALL
}
