DESCRIPTION = "Gtk2 theme files to accompany default Matchbox themes"
LICENSE = "GPL"
DEPENDS = "gtk-engines"
RDEPENDS_${PN} = "matchbox-wm gtk-engine-clearlooks"
SECTION = "x11/base"
PR = "r3"

SRC_URI = "file://gtkrc"

FILES_${PN} = "${datadir}/themes"

do_install() {
	install -d ${D}${datadir}/themes/blondie/gtk-2.0
	install -d ${D}${datadir}/themes/MBOpus/gtk-2.0
	install -m 644 ${WORKDIR}/gtkrc ${D}${datadir}/themes/blondie/gtk-2.0/
	install -m 644 ${WORKDIR}/gtkrc ${D}${datadir}/themes/MBOpus/gtk-2.0/
}
