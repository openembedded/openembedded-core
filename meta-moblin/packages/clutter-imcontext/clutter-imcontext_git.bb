SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r3"

DEPENDS = "clutter glib-2.0"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
    echo "EXTRA_DIST=" > ${S}/gtk-doc.make
    echo "CLEANFILES=" >> ${S}/gtk-doc.make
}