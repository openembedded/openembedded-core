LICENSE = "LGPL"
DEPENDS = "glib-2.0 gtk+ gconf dbus db gnome-common libglade libiconv"
RDEPENDS = "gtk+ gconf db libiconv"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Evolution database backend server"
PV = "1.4.0cvs${CVSDATE}"
PR = "r3"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
           file://external_libdb.patch;patch=1 \
	   file://no_gnomevfs.patch;patch=1 \
	   file://no_iconv_test.patch;patch=1 \
	   file://iconv-detect.h"

S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-openldap --disable-nntp --disable-gtk-doc --with-dbus=yes --with-libgnome=no --enable-soup=no --with-libdb43=${STAGING_DIR}/${HOST_SYS} --enable-smime=no --enable-nss=no"

acpaths = " -I ${STAGING_DATADIR}/aclocal/gnome-macros "

do_configure_append = " cp ${WORKDIR}/iconv-detect.h ${S} "