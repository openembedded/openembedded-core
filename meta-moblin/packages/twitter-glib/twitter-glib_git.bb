HOMEPAGE = "http://live.gnome.org/TwitterGlib"
SRC_URI = "git://github.com/ebassi/${PN}.git;protocol=git"
PV = "0.0+git${SRCREV}"
PR = "r0"

DEPENDS = "glib-2.0 gtk+ json-glib"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-shave --disable-introspection"

inherit autotools_stage

do_configure_prepend () {
	 echo "EXTRA_DIST=" > ${S}/gtk-doc.make
}