DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "matchbox-common matchbox-applet-startup-monitor gtk-theme-clearlooks"
PR = "r24"

SRC_URI = "file://etc"
S = ${WORKDIR}

do_install() {
	cp -R ${S}/etc ${D}/etc
	rm -fR ${D}/etc/.svn
	rm -fR ${D}/etc/matchbox/.svn
	chmod -R 755 ${D}/etc
}

pkg_postinst_matchbox-poky () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/theme Clearlooks
}
