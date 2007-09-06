SECTION = "base"
LICENSE = "GPL"
DEPENDS = "libxml2 intltool-native"
PR = "r0"

SRC_URI = "http://freedesktop.org/~hadess/shared-mime-info-${PV}.tar.bz2"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-update-mimedb"

FILES_${PN} += "${datadir}/mime"
FILES_${PN}-dev += "${datadir}/pkgconfig/shared-mime-info.pc"

pkg_postinst () {
if [ "x$D" != "x" ]; then
        exit 1
fi
  echo "Updating MIME database... this may take a while."
  ${bindir}/update-mime-database ${datadir}/mime
}
