SECTION = "x11/base"
DESCRIPTION = "Industrial theme engine for GTK"
LICENSE = "GPL2"
DEPENDS = "gtk+"
PR = "r1"

include gtk-engines_2.6.inc

EXTRA_OECONF = "--disable-clearlooks     \
		--disable-crux           \
		--disable-hc             \
		--disable-lighthouseblue \
		--disable-metal          \
		--disable-mist           \
		--disable-redmond        \
		--disable-smooth         \
		--disable-thinice        \
		"

PACKAGES += "gtk-theme-industrial"
FILES_${PN} = "${libdir}/gtk-2.0/*/engines/*.so"
FILES_${PN}-dev = "${libdir}/gtk-2.0/*/engines/*"
FILES_gtk-theme-industrial = "${datadir}/icons ${datadir}/themes"
FILES_${PN}-dbg = "${libdir}/gtk-2.0/*/engines/.debug"
