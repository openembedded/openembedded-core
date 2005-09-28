LICENSE = "LGPL"
DEPENDS = "glib-2.0 gtk+ gconf dbus db gnome-common libglade libiconv"
RDEPENDS = "glib-2.0 gtk+ gconf dbus db libglade libiconv"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Evolution database backend server"
PV = "1.4.0cvs${CVSDATE}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
           file://external_libdb.patch;patch=1 \
	   file://no_libgnome.patch;patch=1 \
	   file://no_iconv_test.patch;patch=1"
S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-openldap --disable-nntp --disable-gtk-doc --enable-groupwise=no --with-dbus=yes --with-libgnome=no --enable-soup=no --with-libdb43=${STAGING_DIR}/${HOST_SYS}"

acpaths = " -I ${STAGING_DATADIR}/aclocal/gnome-macros "
