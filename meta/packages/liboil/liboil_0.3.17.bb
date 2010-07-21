DESCRIPTION = "Liboil is a library of simple functions that are optimized for various CPUs."
HOMEPAGE = "http://liboil.freedesktop.org/"
BUGTRACKER = "https://bugs.freedesktop.org/"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ad80780d9c5205d63481a0184e199a15 \
                    file://liboil/liboil.h;endline=28;md5=95c794a66b88800d949fed17e437d9fb \
                    file://liboil/liboilcpu.c;endline=28;md5=89da69a61d88eedcba066f42353fb75a \
                    file://examples/example1.c;endline=29;md5=9d4dad9fcbbdf0441ee063f8af5170c9 \
                    file://testsuite/trans.c;endline=29;md5=380ecd43121fe3dcc0d8d7e5984f283d"

DEPENDS = "glib-2.0"
PR = "r2"

SRC_URI = "http://liboil.freedesktop.org/download/${P}.tar.gz \
           file://no-tests.patch;patch=1"

inherit autotools pkgconfig

ARM_INSTRUCTION_SET = "arm"
