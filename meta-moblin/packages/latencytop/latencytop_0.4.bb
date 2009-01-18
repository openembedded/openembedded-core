DESCRIPTION = "LatencyTOP, a Linux tool measuring and fixing latency"
HOMEPAGE = "http://www.latencytop.org/"
LICENSE = "GPLv2"
DEPENDS = "virtual/libintl ncurses glib-2.0"
PR = "r1"

SRC_URI = "http://www.latencytop.org/download/latencytop-${PV}.tar.gz \
	   file://latencytop-makefile.patch;patch=1"

CFLAGS += "${LDFLAGS}"

do_install() {
    oe_runmake install DESTDIR=${D}
}
