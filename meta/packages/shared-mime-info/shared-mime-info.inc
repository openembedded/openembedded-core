SECTION = "base"
LICENSE = "GPL"

SRC_URI = "http://freedesktop.org/~hadess/shared-mime-info-${PV}.tar.bz2"
DEPENDS = "libxml2 intltool-native glib-2.0 shared-mime-info-native"
DEPENDS_virtclass-native = "libxml2-native intltool-native glib-2.0-native"

PR = "r2"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-update-mimedb"

FILES_${PN} += "${datadir}/mime"
FILES_${PN}-dev += "${datadir}/pkgconfig/shared-mime-info.pc"

do_install_append() {
    update-mime-database ${D}${datadir}/mime

    # we do not need it on device and it is huge
    rm ${D}${datadir}/mime/packages/freedesktop.org.xml
}

BBCLASSEXTEND = "native"
