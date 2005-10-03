DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "matchbox matchbox-applet-startup-monitor gtk-theme-clearlooks"
PR = "r6"

SRC_URI = "file://etc"
S = ${WORKDIR}

do_install() {
	cp -R ${S}/etc ${D}/etc
	chmod -R 755 ${D}/etc
}
