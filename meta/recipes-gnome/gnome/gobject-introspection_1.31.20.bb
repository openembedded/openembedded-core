# NOTE: WIP! This recipe does not cross-compile atm., only -native
DEPENDS = "glib-2.0 libffi python-native gobject-introspection-native"
DEPENDS_virtclass-native = "glib-2.0-native libffi-native python-native bison-native flex-native"
BBCLASSEXTEND = "native"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/gobject-introspection/1.31/gobject-introspection-1.31.20.tar.xz \
  file://use-usr-bin-env-for-python.patch \
"

SRC_URI[md5sum] = "05d9ac99a5929d002867b86a2a69b8b5"
SRC_URI[sha256sum] = "e1552884b642e7e5a56a175ae85bfdebfd16c29a7bbe4f6ca9cdf591e333f070"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://COPYING.LGPL;md5=3bf50002aefd002f49e7bb854063f7e7"

inherit autotools

do_configure_prepend () {
        echo "EXTRA_DIST = " > ${S}/gtk-doc.make
}

EXTRA_OECONF = "\
  --disable-gtk-doc \
  --disable-gtk-doc-html \
  --disable-gtk-doc-pdf \
  --disable-tests \
"
