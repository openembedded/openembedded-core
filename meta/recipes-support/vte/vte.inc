DESCRIPTION = "vte is a virtual terminal emulator"
BUGTRACKER = "https://bugzilla.gnome.org/buglist.cgi?product=vte"
LICENSE = "LGPLv2.1+"
DEPENDS = " glib-2.0 gtk+ intltool-native ncurses"
RDEPENDS_libvte = "vte-termcap"

inherit gnome

SRC_URI += "file://introspection.patch"

EXTRA_OECONF = "--disable-gtk-doc --disable-python --disable-introspection"

PACKAGES =+ "libvte vte-termcap"
FILES_libvte = "${libdir}/*.so.* ${libexecdir}/gnome-pty-helper"
FILES_vte-dbg =+ "${libexecdir}/.debug"
FILES_vte-termcap = "${datadir}/vte/termcap-0.0"
