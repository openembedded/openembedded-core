HOMEPAGE = "http://www.moblin.org/projects/projects_connman.php"
SUMMARY  = "Moblin Glib D-Bus integration"
LICENSE  = "GPL LGPL"
PV       = "0.0+git${SRCREV}"
S        = "${WORKDIR}/git"

RDEPENDS  = "connman"

SRC_URI  = "git://moblin.org/repos/projects/connman-gnome.git;protocol=http"

inherit autotools pkgconfig

FILES_${PN} = "${datadir}/icons/hicolor/22x22/apps \
		${bindir}/* \
		${top_builddir}/common \
		${sysconfdir}/xdg "
