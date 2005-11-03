SECTION = "base"
PR = "r1"

DEPENDS = "libxml2 intltool-native"

SRC_URI = "http://freedesktop.org/~jrb/shared-mime-info-${PV}.tar.gz"
LICENSE = "GPL"
inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/mime"
#FILES_${PN}-doc += " ${datadir}/gtk-doc"

EXTRA_OECONF = "--disable-update-mimedb"

pkg_postinst () {
  echo "Updating MIME database... this may take a while."
  ${bindir}/update-mime-database ${datadir}/mime
}
