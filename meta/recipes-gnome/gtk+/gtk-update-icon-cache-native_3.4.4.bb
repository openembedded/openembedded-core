SUMMARY = "gtk-update-icon-cache built natively"
DESCRIPTION = "Just gtk-update-icon-cache built from GTK+ natively, for on-host postinst script execution."
SECTION = "libs"

DEPENDS = "gdk-pixbuf-native"

LICENSE = "LGPLv2 & LGPLv2+ & LGPLv2.1+"

LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
                    file://gtk/gtk.h;endline=25;md5=1d8dc0fccdbfa26287a271dce88af737 \
                    file://gdk/gdk.h;endline=25;md5=c920ce39dc88c6f06d3e7c50e08086f2 \
                    file://tests/testgtk.c;endline=25;md5=cb732daee1d82af7a2bf953cf3cf26f1"

SRC_URI = "http://download.gnome.org/sources/gtk+/3.4/gtk+-${PV}.tar.xz"
SRC_URI[md5sum] = "1b2cf29502a6394e8d4b30f7f5bb9131"
SRC_URI[sha256sum] = "f154e460075034da4c0ce89c320025dcd459da2a1fdf32d92a09522eaca242c7"

S = "${WORKDIR}/gtk+-${PV}"

inherit pkgconfig native

PKG_CONFIG_FOR_BUILD = "${STAGING_BINDIR_NATIVE}/pkg-config-native"

do_configure() {
	# Quite ugly but defines enough to compile the tool.
	if ! test -f gtk/config.h; then
		echo "#define GETTEXT_PACKAGE \"gtk30\"" >> gtk/config.h
		echo "#define HAVE_UNISTD_H 1" >> gtk/config.h
		echo "#define HAVE_FTW_H 1" >> gtk/config.h

	fi
}

do_compile() {
	${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS} ${S}/gtk/updateiconcache.c \
	$(${PKG_CONFIG_FOR_BUILD} --cflags --libs gdk-pixbuf-2.0) \
		-o gtk-update-icon-cache 
}

do_install() {
	install -d ${D}${bindir}
        install -m 0755 ${B}/gtk-update-icon-cache ${D}${bindir}

	create_wrapper ${D}/${bindir}/gtk-update-icon-cache \
		GDK_PIXBUF_MODULE_FILE=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/2.10.0/loaders.cache

}
