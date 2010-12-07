DESCRIPTION = "GNOME 2 default icon themes"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv3 | BY-SAv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=e7e289d90fc8bdceed5e3f142f98229e"

SECTION = "x11/gnome"
DEPENDS = "icon-naming-utils-native glib-2.0 intltool-native"
RDEPENDS = "hicolor-icon-theme"
RRECOMMENDS = "librsvg-gtk"
PR = "r0"

FILES_${PN} += "${datadir}/*"

SRC_URI = "${GNOME_MIRROR}/${PN}/2.31/${PN}-${PV}.tar.bz2 \
           file://iconpath-option.patch"

EXTRA_OECONF = "--disable-hicolor-check --with-iconmap=${STAGING_LIBDIR_NATIVE}/../libexec/icon-name-mapping"

inherit autotools

# We can't do this until the output is shared into all target sysroots
#PACKAGE_ARCH = "all"
