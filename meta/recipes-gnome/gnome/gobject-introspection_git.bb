# NOTE: WIP! This recipe does not cross-compile atm., only -native
DEPENDS = "glib-2.0 libffi python-native gobject-introspection-native"
DEPENDS_virtclass-native = "glib-2.0-native libffi-native python-native bison-native flex-native"
BBCLASSEXTEND = "native"

SRC_URI = "git://git.gnome.org/gobject-introspection;protocol=git \
  file://use-usr-bin-env-for-python.patch \
"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING.tools;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://COPYING.lib;md5=3bf50002aefd002f49e7bb854063f7e7"

SRCREV = "8d64bc23d2b837421ecf9c7b0e4b8d5d95ca0d21"
PV = "1.29.0+gitr${SRCPV}"
PR = "r1"

DEFAULT_PREFERENCE = "-1"

S = "${WORKDIR}/git"

inherit autotools gtk-doc pythonnative

BBCLASSEXTEND = "native"

EXTRA_OECONF = "\
  --disable-tests \
"
