DESCRIPTION = "X.Org X server configuration file"
HOMEPAGE = "http://www.x.org"
SECTION = "x11/base"
LICENSE = "MIT-X"
PR = "r5"

SRC_URI = "file://xorg.conf"

CONFFILES_${PN} += "${sysconfdir}/X11/xorg.conf"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install () {
	install -d ${D}/${sysconfdir}/X11
	install -m 0644 ${WORKDIR}/xorg.conf ${D}/${sysconfdir}/X11/
}

