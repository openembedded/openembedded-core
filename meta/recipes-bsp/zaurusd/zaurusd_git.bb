DESCRIPTION = "Daemon to handle device specifc features."
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS = "tslib"
RDEPENDS_${PN} = "xrandr"

SRCREV = "85d941d87f0c41b196766a173feaafffa6de2dc2"
PV = "0.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "zaurusd"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
