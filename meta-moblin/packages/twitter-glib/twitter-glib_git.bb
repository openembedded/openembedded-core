HOMEPAGE = "http://live.gnome.org/TwitterGlib"
SRC_URI = "git://github.com/ebassi/${PN}.git;protocol=git \
           file://nodolt.patch;patch=1"
PV = "0.1+git${SRCPV}"
PR = "r1"

DEPENDS = "glib-2.0 gtk+ json-glib libsoup-2.4"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-shave --disable-introspection --disable-maintainer-flags"

inherit autotools_stage

do_configure_prepend () {
	 echo "EXTRA_DIST=" > ${S}/gtk-doc.make
}
