SRC_URI = "git://git.gnome.org/gobject-introspection;protocol=git \
           file://configure.patch;patch=1 \
           file://pathfix.patch;patch=1"

SRC_URI_virtclass-native = "git://git.gnome.org/gobject-introspection;protocol=git \
                            file://pathfix.patch;patch=1"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING.tools;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://COPYING.lib;md5=3bf50002aefd002f49e7bb854063f7e7"
PV = "0.0+git${SRCPV}"
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
