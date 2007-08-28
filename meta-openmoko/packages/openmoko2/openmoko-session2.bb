DESCRIPTION = "Custom MB session files for OpenMoko"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "matchbox-common matchbox-applet-startup-monitor matchbox-panel-2"
RDEPENDS += "openmoko-common2 openmoko-today2 openmoko-dialer2"
RCONFLICTS = "openmoko-session"
PR = "r29"

SRC_URI = "file://etc"
S = ${WORKDIR}

do_install() {
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
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type int --set /desktop/openmoko/neod/power_management 2

}

PACKAGE_ARCH = "all"
