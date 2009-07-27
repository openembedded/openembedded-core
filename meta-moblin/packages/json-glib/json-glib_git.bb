HOMEPAGE = "http://live.gnome.org/JsonGlib"
SRC_URI = "git://github.com/ebassi/${PN}.git;protocol=git"
PV = "0.7.2+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0"

inherit autotools_stage

acpaths = "-I ${S}/build/autotools "

do_configure_prepend () {
    echo "EXTRA_DIST = " > ${S}/gtk-doc.make
}

