DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "matchbox-applet-startup-monitor gtk-sato-engine matchbox-theme-sato gtk-theme-sato matchbox-panel-2 matchbox-desktop-sato"
RCONFLICTS = "matchbox-common matchbox-sato"
PR = "r19"

SRC_URI = "file://etc file://matchbox-session"
S = ${WORKDIR}

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${S}/matchbox-session ${D}/${bindir}
	cp -R ${S}/etc ${D}/etc
	rm -fR ${D}/etc/.svn
	rm -fR ${D}/etc/matchbox/.svn
	chmod -R 755 ${D}/etc
}

pkg_postinst_openmoko-session2 () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/theme openmoko-standard-2
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/icon_theme openmoko-standard
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/font_name "Sans 5"
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type int --set /desktop/poky/peripherals/mouse/drag_threshold 8
}
