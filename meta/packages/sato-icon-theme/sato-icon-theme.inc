require sato-icon-theme.inc

SECTION = "x11"
DESCRIPTION = "Sato Icon Theme"
LICENSE = "CC-BY-SA3"
DEPENDS = ""

SRC_URI = "http://pokylinux.org/releases/sato/sato-icon-theme-0.1.tar.gz"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}"

PACKAGE_ARCH = "all"

pkg_postinst_${PN} () {
        if [ "x$D" != "x" ]; then
                exit 1
        fi
        gtk-update-icon-cache -q /usr/share/icons/Sato
}
