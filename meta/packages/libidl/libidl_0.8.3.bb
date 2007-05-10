LICENSE = "LGPL"
DESCRIPTION = "Library for parsing CORBA IDL files"
SECTION = "gnome/libs"
DEPENDS = "glib-2.0"
PR = "r3"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libIDL/0.8/libIDL-${PV}.tar.bz2"
S = "${WORKDIR}/libIDL-${PV}"

BINCONFIG_GLOB = "*-config-2"
inherit autotools pkgconfig binconfig

# Firefox uses the libIDL-config-2 script instead of pkgconfig (for some
# strange reason - so we do some sed fu to fix the path there

do_stage() {
	oe_runmake install \
		prefix=${STAGING_DIR} \
		bindir=${STAGING_BINDIR} \
		includedir=${STAGING_INCDIR} \
		libdir=${STAGING_LIBDIR} \
		datadir=${STAGING_DATADIR} \
		infodir=${STAGING_INFODIR}
	
}

FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dev += " ${bindir}" 
