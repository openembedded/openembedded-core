SECTION = "x11/base"
DESCRIPTION = "Sato theme engine for GTK"
LICENSE = "LGPL"
DEPENDS = "gtk+"
PR = "r0"

SRC_URI = "http://pokylinux.org/releases/sato/sato-engine-0.1.tar.gz"
S = "${WORKDIR}/sato-engine-0.1"

PACKAGES += "gtk-theme-sato"
FILES_${PN} = "${libdir}/gtk-2.0/*/engines/*.so "
FILES_${PN}-dev = "${libdir}/gtk-2.0/*/engines/*"
FILES_${PN}-dbg = "${libdir}/gtk-2.0/*/engines/.debug"
FILES_gtk-theme-sato = "${datadir}/icons ${datadir}/themes"

inherit autotools  pkgconfig

do_configure_prepend() {
	for i in `ls gtk-common`; do
		ln -sf ../gtk-common/$i gtk2-engine/$i
	done
}

