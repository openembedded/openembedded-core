SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://clutter10.patch;patch=1;notrev=86022316d975cf6679c093a6872962c909adff0b"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r1"

DEPENDS = "clutter glib-2.0"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
    echo "EXTRA_DIST=" > ${S}/gtk-doc.make
    echo "CLEANFILES=" >> ${S}/gtk-doc.make
}