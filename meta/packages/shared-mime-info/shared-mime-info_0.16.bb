SECTION = "base"
PR = "r2"

DEPENDS = "libxml2 intltool-native glib-2.0"

SRC_URI = "http://freedesktop.org/~jrb/shared-mime-info-${PV}.tar.gz"
LICENSE = "GPL"
inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/mime"
#FILES_${PN}-doc += " ${datadir}/gtk-doc"

EXTRA_OECONF = "--disable-update-mimedb"

pkg_postinst () {
  echo "Updating MIME database in $D${datadir}/mime ... (This may take a while.)"
  ${bindir}/update-mime-database $D${datadir}/mime
}
