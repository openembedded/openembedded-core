DESCRIPTION = "A lightweight terminal emulator based on VTE and Gtk+"
HOMEPAGE = "http://gtkterm.feige.net/"
AUTHOR = "Oliver Feige"
SECTION = "x11/terminals"
DEPENDS = "gtk+ vte"

SRC_URI = "${SOURCEFORGE_MIRROR}/gtkterm/gtkterm2-${PV}.tar.gz"

inherit autotools
