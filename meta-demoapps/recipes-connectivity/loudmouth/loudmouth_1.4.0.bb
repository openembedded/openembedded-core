SUMMARY = "XMPP/Jabber library"
DESCRIPTION = "Loudmouth is a lightweight and easy-to-use C library for programming with the XMPP/Jabber protocol."
HOMEPAGE = "http://www.loudmouth-project.org/"
LICENSE = "LGPL"
DEPENDS = "glib-2.0 gnutls libcheck"
PR = "r2"

SRC_URI = "http://ftp.imendio.com/pub/imendio/${BPN}/src/${BPN}-${PV}.tar.bz2"

inherit autotools pkgconfig
