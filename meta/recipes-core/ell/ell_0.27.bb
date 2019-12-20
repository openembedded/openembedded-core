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
SRC_URI[md5sum] = "e090d6a910a43c2a32114ba1cdc9f1f2"
SRC_URI[sha256sum] = "afc5441d6f6ece512a188b1e6aeabd7153de4f38989c897a2197ae50fc46da96"

do_configure_prepend () {
    mkdir -p ${S}/build-aux
}
