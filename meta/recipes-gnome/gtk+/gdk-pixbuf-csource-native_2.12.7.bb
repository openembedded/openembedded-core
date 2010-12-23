require gtk+_${PV}.bb
inherit native
DEPENDS = "jpeg-native libpng-native gettext-native glib-2.0-native libx11-native"
S = "${WORKDIR}/gtk+-${PV}"
FILESPATH = "${FILE_DIRNAME}/gdk-pixbuf-csource:${FILE_DIRNAME}/gtk+-${PV}:${FILE_DIRNAME}/files"
SRC_URI += "file://reduce-dependencies.patch;patch=1"
PR = "r13"

#clear recommends for uclibc builds
RRECOMMENDS_${PN} = " "
RRECOMMENDS_${PN}_linux = "  "
RRECOMMENDS_${PN}_linux-gnueabi = " "

EXTRA_OECONF = "\
  --with-gdktarget=x11 \
  --without-libtiff \
  --with-libjpeg \
  --with-libpng \
  --x-includes=${STAGING_INCDIR} \
  --x-libraries=${STAGING_LIBDIR} \
"

PACKAGES_DYNAMIC = ""

do_compile() {
	cd gdk-pixbuf && oe_runmake
}

do_install() {
	cd gdk-pixbuf
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${sysconfdir}/gtk-2.0
	GDK_PIXBUF_MODULEDIR=${D}${libdir}/gtk-2.0/2.10.0/loaders ${D}${bindir}/gdk-pixbuf-query-loaders > ${D}${sysconfdir}/gtk-2.0/gdk-pixbuf.loaders
	sed -i -e 's#${D}##g' ${D}${sysconfdir}/gtk-2.0/gdk-pixbuf.loaders
	find ${D}${libdir} -name "libpixbufloader-*.la" -exec rm \{\} \;
}

