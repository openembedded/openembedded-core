DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "matchbox matchbox-applet-startup-monitor gtk-theme-clearlooks"
PR = "r12"

SRC_URI = "file://etc"
S = ${WORKDIR}

do_install() {
	cp -R ${S}/etc ${D}/etc
	chmod -R 755 ${D}/etc
}

pkg_postinst_matchbox-poky () {
#!/bin/sh -e
gconftool-2 --config-source=xml::${IMAGE_ROOTFS}/${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/theme Clearlooks
}
