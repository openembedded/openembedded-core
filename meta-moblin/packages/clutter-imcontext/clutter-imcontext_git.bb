SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://clutter10.patch;patch=1;notrev=69cae7b2b9f863fe1e0d392825508e63b8a107a4"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r2"

DEPENDS = "clutter glib-2.0"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
    echo "EXTRA_DIST=" > ${S}/gtk-doc.make
    echo "CLEANFILES=" >> ${S}/gtk-doc.make
}