DESCRIPTION = "The libtheora reference implementation provides the standard encoder and decoder under a BSD license."
HOMEPAGE = "http://xiph.org/"
BUGTRACKER = "https://trac.xiph.org/newticket"
SECTION = "libs"
LICENSE = "BSD"

DEPENDS = "libogg"

PR = "r1"

SRC_URI = "http://downloads.xiph.org/releases/theora/libtheora-${PV}.tar.bz2 \
           file://no-docs.patch"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-examples"
