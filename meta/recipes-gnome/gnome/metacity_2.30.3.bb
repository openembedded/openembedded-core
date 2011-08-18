SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/include/main.h;endline=24;md5=c2242df552c880280315989bab626b90"

DEPENDS = "startup-notification gtk+ gconf gdk-pixbuf-native libcanberra gnome-doc-utils"
PR = "r2"

inherit gnome update-alternatives

SRC_URI += "file://crosscompile.patch"
SRC_URI[archive.md5sum] = "553784f376d96b902e19ff437cd5b339"
SRC_URI[archive.sha256sum] = "08f887018fa5e447cf184d03bae3fe2c05fdb7583bed6768e3b4d66392fc18dd"

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/metacity"
ALTERNATIVE_PRIORITY = "10"

EXTRA_OECONF += "--disable-verbose \
	         --disable-xinerama"

do_configure_prepend() {
	sed -i -e 's:$ZENITY:$NOZENITY:g' -e 's:-Werror::g' ${S}/configure.in
}
export CC_FOR_BUILD = "${BUILD_CC}"
export CFLAGS_FOR_BUILD = "${BUILD_CFLAGS} -I${STAGING_LIBDIR_NATIVE}/glib-2.0/include -I${STAGING_INCDIR_NATIVE}/glib-2.0 -I${STAGING_INCDIR_NATIVE}/glib-2.0/include -I${STAGING_INCDIR_NATIVE}"
export LDFLAGS_FOR_BUILD = "${BUILD_LDFLAGS} -L${STAGING_LIBDIR_NATIVE} -lglib-2.0"

FILES_${PN} += "${datadir}/themes ${datadir}/gnome-control-center ${datadir}/gnome"

