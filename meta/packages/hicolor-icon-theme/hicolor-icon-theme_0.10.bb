DESCRIPTION = "default icon theme that all icon themes automatically inherit from."
HOMEPAGE = "http://icon-theme.freedesktop.org/wiki/HicolorTheme"
BUGTRACKER = "https://bugs.freedesktop.org/"

LICENSE = "GPLv2"

SECTION = "unknown"
inherit gnome

PR = "r3"

SRC_URI = "http://icon-theme.freedesktop.org/releases/${P}.tar.gz \
        file://index.theme"

PACKAGE_ARCH = "all"

FILES_${PN} += "${datadir}/icons"

do_install_append () {
	install -m 0644 ${WORKDIR}/index.theme ${D}/${datadir}/icons/hicolor
}
