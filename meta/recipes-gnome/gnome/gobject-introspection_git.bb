SRC_URI = "git://git.gnome.org/gobject-introspection;protocol=git \
           file://configure.patch \
           file://pathfix.patch"

SRC_URI_virtclass-native = "git://git.gnome.org/gobject-introspection;protocol=git \
                            file://pathfix.patch"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING.tools;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://COPYING.lib;md5=3bf50002aefd002f49e7bb854063f7e7"

SRCREV = "efa7266bcf78478ce62e8dd778a4f0417bfd4d15"
PV = "0.10.8+git${SRCPV}"
PR = "r4"

S = "${WORKDIR}/git"

DEPENDS = "libffi python-native gobject-introspection-native"
DEPENDS_virtclass-native = "libffi-native python-native bison-native flex-native"

inherit autotools

TARGET_CFLAGS += "-I${STAGING_INCDIR_NATIVE}/python2.5"

do_configure_prepend () {
	echo "EXTRA_DIST = " > ${S}/gtk-doc.make
}

BBCLASSEXTEND = "native"
