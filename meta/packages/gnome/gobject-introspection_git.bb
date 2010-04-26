SRC_URI = "git://git.gnome.org/gobject-introspection;protocol=git \
           file://configure.patch;patch=1 \
           file://pathfix.patch;patch=1"

SRC_URI_virtclass-native = "git://git.gnome.org/gobject-introspection;protocol=git \
                            file://pathfix.patch;patch=1"

PV = "0.0+git${SRCREV}"
PR = "r3"

S = "${WORKDIR}/git"

DEPENDS = "libffi python-native gobject-introspection-native"
DEPENDS_virtclass-native = "libffi-native python-native"

inherit autotools

TARGET_CFLAGS += "-I${STAGING_INCDIR_NATIVE}/python2.5"

do_configure_prepend () {
	echo "EXTRA_DIST = " > ${S}/gtk-doc.make
}

BBCLASSEXTEND = "native"