def gnome_verdir(v):
	import re
	m = re.match("([0-9]+)\.([0-9]+)\..*", v)
	return "%s.%s" % (m.group(1), m.group(2))

SECTION ?= "x11/gnome"
SRC_URI = "${GNOME_MIRROR}/${PN}/${@gnome_verdir("${PV}")}/${PN}-${PV}.tar.bz2"

DEPENDS += "gnome-common"

FILES_${PN} += "${datadir}/application-registry ${datadir}/mime-info \
	${datadir}/gnome-2.0"

inherit autotools pkgconfig gconf

EXTRA_AUTORECONF += "-I ${STAGING_DIR}/${HOST_SYS}/share/aclocal/gnome2-macros"

gnome_stage_includes() {
	autotools_stage_includes
}
