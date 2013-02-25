# Copyright (C) 2012 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "The kconfig-frontends project aims at centralising \
the effort of keeping an up-to-date, out-of-tree, packaging of the \
kconfig infrastructure, ready for use by third-party projects. \
The kconfig-frontends package provides the kconfig parser, as well as all \
the frontends"
HOMEPAGE = "http://ymorin.is-a-geek.org/projects/kconfig-frontends"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=9b8cf60ff39767ff04b671fca8302408"
SECTION = "devel"
DEPENDS += "ncurses flex bison gperf pkgconfig-native"
PR = "r0"
SPIN = ".0"
SRC_URI = "http://ymorin.is-a-geek.org/download/${BPN}/${BPN}-${PV}${SPIN}.tar.xz"

SRC_URI[md5sum] = "73598aea99d01d9c422b335e9b963271"
SRC_URI[sha256sum] = "b3ff325338aa291c9129dbf33c69ff321e30105aa06ff957be6f958acdc72b42"

S = "${WORKDIR}/${BPN}-${PV}${SPIN}"

inherit autotools
do_configure_prepend () {
	mkdir -p scripts/.autostuff/m4
}

do_install_append() {
	ln -s kconfig-conf ${D}${bindir}/conf
	ln -s kconfig-mconf ${D}${bindir}/mconf
}

EXTRA_OECONF += "--disable-gconf --disable-qconf"

# Some packages have the version preceeding the .so instead properly
# versioned .so.<version>, so we need to reorder and repackage.
SOLIBS = "-${PV}.so"
FILES_SOLIBSDEV = "${libdir}/libkconfig-parser.so"

BBCLASSEXTEND = "native"
