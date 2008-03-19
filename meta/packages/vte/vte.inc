DESCRIPTION = "vte is a virtual terminal emulator"
LICENSE = "LGPL"
DEPENDS += " glib-2.0 gtk+ intltool-native"
RDEPENDS = "termcap"

inherit gnome

SRC_URI += "file://vte-pkgconfig-fixes.patch;patch=1"

EXTRA_OECONF = "--disable-gtk-doc --disable-python"

do_stage() {
	autotools_stage_all
}

PACKAGES =+ "libvte"
FILES_libvte = "${libdir}/*.so*"
