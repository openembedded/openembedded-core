DESCRIPTION = "A lightweight terminal emulator based on VTE and Gtk+"
HOMEPAGE = "http://gtkterm.feige.net/"
AUTHOR = "Oliver Feige"
SECTION = "x11/terminals"
DEPENDS = "gtk+ vte"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/gtkterm/gtkterm2-${PV}.tar.gz \
        file://gtkterm.desktop"

inherit autotools

do_install_append () {
        install -d ${D}/${datadir}/applications
        install -m 0644 ${WORKDIR}/gtkterm.desktop ${D}/${datadir}/applications
}
