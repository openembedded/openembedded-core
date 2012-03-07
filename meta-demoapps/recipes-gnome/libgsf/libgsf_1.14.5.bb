DESCRIPTION = "GNOME Structured File Library"
LICENSE = "GPL"
SECTION = "libs"
PR = "r3"

LIC_FILES_CHKSUM = "file://COPYING;md5=dc7371b50816c96e145fa0f8ade8e24d \
                    file://COPYING.LIB;md5=61464cfe342798eeced82efe9ae55f63 \
                    file://gsf/gsf.h;endline=25;md5=15cf6d31ad023167779ab5f0bbb76f49"

DEPENDS= "libxml2 bzip2 glib-2.0 zlib"
RDEPENDS_${PN} = "gconf"


PACKAGES =+ "${PN}-gnome ${PN}-gnome-dev "

FILES_${PN}-gnome = "${libdir}/libgsf-gnome-1.so.*"
FILES_${PN}-gnome-dev = "${libdir}/libgsf-gnome-1.* ${includedir}/libgsf-1/gsf-gnome"

inherit autotools pkgconfig gnome gconf

EXTRA_OECONF = "\
		--without-python \
		--without-gnome \
		--disable-gtk-doc \
		--with-bz2"
