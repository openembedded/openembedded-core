LICENSE = "LGPL"
DEPENDS = "glib-2.0 gtk+ gconf dbus"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Evolution database backend server"
PV = "1.4.0cvs${CVSDATE}"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"
S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-openldap --disable-nntp --disable-gtk-doc --enable-groupwise=no --with-dbus=yes --with-libgnome=no"

