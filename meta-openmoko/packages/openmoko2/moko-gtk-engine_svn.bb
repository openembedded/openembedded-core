DESCRIPTION = "Moko GTK+ theme engine"
SECTION = "openmoko/libs"
PV = "0.1.0+svnr${SRCREV}"
PR = "r0"

inherit openmoko2

PACKAGES += "moko-gtk-theme"
FILES_${PN} = "${libdir}/gtk-2.0/*/engines/*.so "
FILES_${PN}-dev = "${libdir}/gtk-2.0/*/engines/*"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/*/engines/.debug"
FILES_moko-gtk-theme = "${datadir}/themes"
RDEPENDS_${PN} = "moko-gtk-theme"

