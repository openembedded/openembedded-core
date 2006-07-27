DESCRIPTION = "Simple dockapps created with monolaunch and shell script"
DEPENDS = "matchbox xmodmap"
SRC_URI = "file://xrandr-panelapp.sh \
	   file://xrandr-panelapp.desktop \
	   file://mb-applet-home \
	   file://home-panelapp.desktop \
	   file://xrandr.png"
SECTION = "x11/wm"
PRIORITY = "optional"
PR = "r7"
LICENSE = "MIT"

do_install() {
        install -d ${D}${bindir}
        install -d ${D}${datadir}/applications
        install -d ${D}${datadir}/pixmaps
	install -m 0755 ${WORKDIR}/xrandr-panelapp.sh ${D}${bindir}/
	install -m 0644 ${WORKDIR}/xrandr-panelapp.desktop ${D}${datadir}/applications/
	install -m 0644 ${WORKDIR}/xrandr.png ${D}${datadir}/pixmaps/
	install -m 0755 ${WORKDIR}/mb-applet-home ${D}${bindir}/
	install -m 0644 ${WORKDIR}/home-panelapp.desktop ${D}${datadir}/applications/

}
