LICENSE = "LGPL"
DEPENDS = "glib-2.0 gnutls"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Loudmouth is a lightweight and easy-to-use C library for programming with the Jabber protocol."

SRC_URI = "http://ftp.imendio.com/pub/imendio/${PN}/src/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig

