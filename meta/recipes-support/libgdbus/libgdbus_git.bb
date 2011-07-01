SUMMARY = "Library for simple D-Bus integration with GLib"
DESCRIPTION = "Library for simple D-Bus integration with GLib"
HOMEPAGE = "http://www.bluez.org"
SECTION = "libs"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://COPYING.LIB;md5=fb504b67c50331fc78734fed90fb0e09"

DEPENDS = "glib-2.0 dbus"

SRCREV = "aeab6e3c0185b271ca343b439470491b99cc587f"
PV = "0.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.kernel.org/pub/scm/bluetooth/libgdbus.git;protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
