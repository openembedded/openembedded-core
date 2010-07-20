DESCRIPTION = "LatencyTOP, a Linux tool measuring and fixing latency"
HOMEPAGE = "http://www.latencytop.org/"
BUGTRACKER = "n/a"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://latencytop.c;endline=23;md5=ee9ea9b1415356e5734adad4a87dc7fa"

DEPENDS = "virtual/libintl ncurses glib-2.0"

PR = "r0"

SRC_URI = "http://www.latencytop.org/download/latencytop-${PV}.tar.gz \
            file://latencytop-makefile.patch"

CFLAGS += "${LDFLAGS}"

do_install() {
    oe_runmake install DESTDIR=${D}
}
