SRC_URI = "git://anongit.freedesktop.org/git/ccss.git;protocol=git"
PV = "0.1+git${SRCPV}"
PR = "r1"

DEPENDS = "glib-2.0 cairo librsvg libsoup-2.4 gettext"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
	echo "EXTRA_DIST=" > ${S}/gtk-doc.make
	echo "CLEANFILES=" >> ${S}/gtk-doc.make
}
