DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "matchbox matchbox-applet-inputmanager matchbox-applet-startup-monitor"
PR = "r1"

SRC_URI = "file://etc"
S = ${WORKDIR}

do_install() {
	cp -R ${S}/etc ${D}/etc
	chmod -R 755 ${D}/etc
}
