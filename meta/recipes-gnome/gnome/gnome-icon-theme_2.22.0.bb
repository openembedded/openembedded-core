LICENSE = "GPL"
SECTION = "x11/gnome"
DEPENDS = "icon-naming-utils-native glib-2.0 intltool-native"
RDEPENDS = "hicolor-icon-theme"
RRECOMMENDS = "librsvg-gtk"
PR = "r2"

FILES_${PN} += "${datadir}/*"

SRC_URI = "${GNOME_MIRROR}/${PN}/2.22/${PN}-${PV}.tar.bz2 \
           file://iconpath-option.patch"

EXTRA_OECONF = "--disable-hicolor-check --with-iconmap=${STAGING_LIBDIR_NATIVE}/../libexec/icon-name-mapping"

inherit autotools

PACKAGE_ARCH = "all"
