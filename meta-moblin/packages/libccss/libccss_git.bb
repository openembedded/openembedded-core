SRC_URI = "git://anongit.freedesktop.org/git/ccss.git;protocol=git"
PV = "0.0+git${SRCREV}"
PR = "r0"

DEPENDS = "glib-2.0 cairo librsvg libsoup-2.4"

S = "${WORKDIR}/git"

inherit autotools_stage

do_configure_prepend () {
	echo "EXTRA_DIST=" > ${S}/gtk-doc.make
	echo "CLEANFILES=" >> ${S}/gtk-doc.make
}
