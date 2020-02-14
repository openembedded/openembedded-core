SUMMARY  = "Embedded Linux Library"
DESCRIPTION = "The Embedded Linux Library (ELL) provides core, \
low-level functionality for system daemons. It typically has no \
dependencies other than the Linux kernel, C standard library, and \
libdl (for dynamic linking). While ELL is designed to be efficient \
and compact enough for use on embedded Linux platforms, it is not \
limited to resource-constrained systems."
SECTION = "libs"
LICENSE  = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=fb504b67c50331fc78734fed90fb0e09"

DEPENDS = "dbus"

inherit autotools pkgconfig

SRC_URI = "https://mirrors.edge.kernel.org/pub/linux/libs/${BPN}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "e38089e54d4b41e508bbb7f37a4a59d9"
SRC_URI[sha256sum] = "6254fcb98d681cb2c3078dde462ba20bf90e6c10919204c6878a4381adcdb74b"

do_configure_prepend () {
    mkdir -p ${S}/build-aux
}
