# NOTE: WIP! This recipe does not cross-compile atm., only -native
DEPENDS = "glib-2.0 libffi python-native gobject-introspection-native"
DEPENDS_virtclass-native = "glib-2.0-native libffi-native python-native bison-native flex-native"
BBCLASSEXTEND = "native"

PR = "r1"

SHRT_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"
SRC_URI = "\
  ${GNOME_MIRROR}/${BPN}/${SHRT_VER}/${BP}.tar.xz \
  file://use-usr-bin-env-for-python.patch \
"

SRC_URI[md5sum] = "7bbdb696c37bb98aef5af02c4b8975e3"
SRC_URI[sha256sum] = "44f3fb933f76e4728818cc360cb5f9e2edcbdf9bc8a8f9aded99b3e3ef5cb858"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=90d577535a3898e1ae5dbf0ae3509a8c \
                    file://COPYING.GPL;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LGPL;md5=3bf50002aefd002f49e7bb854063f7e7"

inherit autotools gtk-doc pythonnative

do_configure_prepend () {
        echo "EXTRA_DIST = " > ${S}/gtk-doc.make
}

EXTRA_OECONF = "--disable-tests"
