LICENSE = "GPL"
SECTION = "x11/gnome"
SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${PN}/0.4/${PN}-${PV}.tar.bz2"

inherit autotools pkgconfig

DEPENDS = "gtk+"

EXTRA_OECONF = "--disable-gtk-doc"

HEADERS = " \
gnome-keyring.h \
"

do_stage() {
	install -d ${STAGING_INCDIR}/gnome-keyring-1
	for i in ${HEADERS}; do
		install -m 0644 $i ${STAGING_INCDIR}/gnome-keyring-1/$i
	done
	oe_libinstall -so libgnome-keyring ${STAGING_LIBDIR}
}
