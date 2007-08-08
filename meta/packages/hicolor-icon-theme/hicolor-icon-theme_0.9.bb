SECTION = "unknown"
LICENSE = "GPL"
inherit gnome

PR = "r1"

SRC_URI = "http://icon-theme.freedesktop.org/releases/${P}.tar.gz"

PACKAGE_ARCH = "all"

FILES_${PN} += "${datadir}/icons"
