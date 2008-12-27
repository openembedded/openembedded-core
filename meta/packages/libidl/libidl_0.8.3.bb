LICENSE = "LGPL"
DESCRIPTION = "Library for parsing CORBA IDL files"
SECTION = "gnome/libs"
DEPENDS = "glib-2.0"
PR = "r4"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libIDL/0.8/libIDL-${PV}.tar.bz2"
S = "${WORKDIR}/libIDL-${PV}"

BINCONFIG_GLOB = "*-config-2"
inherit autotools_stage pkgconfig binconfig

FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dev += " ${bindir}" 
