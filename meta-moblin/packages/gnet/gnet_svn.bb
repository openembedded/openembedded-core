DESCRIPTION = "GNet is a simple network library. It is written in C, object-oriented, and built upon GLib."
LICENSE = "LGPL"
SECTION = "libs/network"
HOMEPAGE = "http://www.gnetlibrary.org"
DEPENDS = "glib-2.0"
PV = "2.0.7+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.gnome.org/svn/${PN}/;module=trunk;proto=http \
           file://buildfix.patch;patch=1 \
           file://configure_fix.patch;patch=1 \
           file://pkgconfig_fix.patch;patch=1 "

S = "${WORKDIR}/trunk"

EXTRA_OECONF =	"--disable-pthreads"

FILES_${PN}-dev += "${libdir}/gnet-2.0"

inherit autotools_stage pkgconfig
