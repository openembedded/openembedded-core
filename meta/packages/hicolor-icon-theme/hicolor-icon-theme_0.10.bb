SECTION = "unknown"
LICENSE = "GPL"
inherit gnome

PR = "r2"

SRC_URI = "http://icon-theme.freedesktop.org/releases/${P}.tar.gz \
        file://index.theme"

PACKAGE_ARCH = "all"

FILES_${PN} += "${datadir}/icons"

do_install_append () {
	install -m 0644 ${WORKDIR}/index.theme ${D}/${datadir}/icons/hicolor
}
