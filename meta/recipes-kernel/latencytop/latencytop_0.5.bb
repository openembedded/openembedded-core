DESCRIPTION = "LatencyTOP, a Linux tool measuring and fixing latency"
HOMEPAGE = "http://www.latencytop.org/"
BUGTRACKER = "n/a"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://latencytop.c;endline=23;md5=ee9ea9b1415356e5734adad4a87dc7fa"

DEPENDS = "virtual/libintl ncurses glib-2.0 gtk+"

PR = "r2"

SRC_URI = "http://www.latencytop.org/download/latencytop-${PV}.tar.gz \
            file://latencytop-makefile.patch"

SRC_URI[md5sum] = "73bb3371c6ee0b0e68e25289027e865c"
SRC_URI[sha256sum] = "9e7f72fbea7bd918e71212a1eabaad8488d2c602205d2e3c95d62cd57e9203ef"

CFLAGS += "${LDFLAGS}"

do_install() {
    oe_runmake install DESTDIR=${D}
}
