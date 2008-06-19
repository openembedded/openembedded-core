DESCRIPTION = "Liboil is a library of simple functions that are optimized for various CPUs."
HOMEPAGE = "http://liboil.freedesktop.org/"
LICENSE = "various"
DEPENDS = "glib-2.0"
PR = "r1"

SRC_URI = "http://liboil.freedesktop.org/download/${P}.tar.gz \
           file://no-tests.patch;patch=1"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}

ARM_INSTRUCTION_SET = "arm"
