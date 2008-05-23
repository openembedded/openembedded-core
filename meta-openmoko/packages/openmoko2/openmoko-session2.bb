DESCRIPTION = "Custom Matchbox session files for Openmoko"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS_${PN} = "matchbox-panel-2 openmoko-common2 openmoko-today2 openmoko-dialer2"
RCONFLICTS_${PN} = "openmoko-session matchbox-common"
PR = "r69"

SRC_URI = "\
  file://etc \
  file://matchbox-session \
"

S = "${WORKDIR}"

inherit update-alternatives

do_install() {
        install -d ${D}${bindir}
        install -m 0655 ${WORKDIR}/matchbox-session ${D}${bindir}
        install -d ${D}${sysconfdir}
        cp -R ${S}/etc/* ${D}${sysconfdir}
        rm -fR ${D}${sysconfdir}/.svn
        rm -fR ${D}${sysconfdir}/matchbox/.svn
        chmod -R 755 ${D}${sysconfdir}/
}

PACKAGE_ARCH = "all"

pkg_postinst_openmoko-session2 () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/theme Moko
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/icon_theme openmoko-standard
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/font_name "Sans 5"
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string --set /desktop/poky/interface/wallpaper ${datadir}/pixmaps/wallpaper.png
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type bool --set /desktop/poky/interface/digital_clock 1
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type int --set /desktop/poky/peripherals/mouse/drag_threshold 8
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type int --set /desktop/openmoko/neod/power_management 2
# gstreamer audio settings
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct -t string --set /system/gstreamer/0.10/default/audiosink pulsesink
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct -t string --set /system/gstreamer/0.10/default/audiosrc pulsesrc

}

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_PATH = "${bindir}/matchbox-session"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PRIORITY = "11"

CONFFILES_${PN} = "${sysconfdir}/matchbox/session"
