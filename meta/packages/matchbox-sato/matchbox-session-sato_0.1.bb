DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "formfactor gtk-sato-engine matchbox-theme-sato gtk-theme-sato matchbox-panel-2 matchbox-desktop-sato initscripts matchbox-session"
PR = "r24"

SRC_URI = "file://session"
S = "${WORKDIR}"

do_install() {
	install -d ${D}/${sysconfdir}/matchbox
	install -m 0755 ${S}/session ${D}/${sysconfdir}/matchbox/
}

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/theme Sato
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/icon_theme Sato
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type bool --set /desktop/poky/interface/touchscreen true

if [ "`machine_id`" = "gta01" ]
then
    gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/font_name "Sans 6"
else
    gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/font_name "Sans 9"
fi
}
