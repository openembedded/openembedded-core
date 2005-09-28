DESCRIPTION = "Gtk2 theme files to accompany default Matchbox themes"
LICENSE = "GPL"
RDEPENDS = "matchbox-wm gtk2-theme-angelistic"
SECTION = "x11/base"

SRC_URI = "file://gtkrc"

FILES_${PN} = "${datadir}/themes"

do_install() {
	install -d ${D}${datadir}/themes/blondie/gtk-2.0
	install -d ${D}${datadir}/themes/MBOpus/gtk-2.0
	install -m 644 ${WORKDIR}/gtkrc ${D}${datadir}/themes/blondie/gtk-2.0/
	install -m 644 ${WORKDIR}/gtkrc ${D}${datadir}/themes/MBOpus/gtk-2.0/
}
