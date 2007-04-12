DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "matchbox matchbox-applet-startup-monitor gtk-sato-engine matchbox-theme-sato gtk-theme-sato matchbox-panel-2"
PR = "r8"

SRC_URI = "file://etc"
S = ${WORKDIR}

do_install() {
	cp -R ${S}/etc ${D}/etc
	rm -fR ${D}/etc/.svn
	rm -fR ${D}/etc/matchbox/.svn
	chmod -R 755 ${D}/etc
}

pkg_postinst_matchbox-sato () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/theme Sato
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/icon_theme Sato
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/font_name "Sans 9"
}
