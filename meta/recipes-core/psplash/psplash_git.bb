SUMMARY = "Userspace framebuffer boot logo based on usplash."
DESCRIPTION = "PSplash is a userspace graphical boot splash screen for mainly embedded Linux devices supporting a 16bpp or 32bpp framebuffer. It has few dependencies (just libc), supports basic images and text and handles rotation. Its visual look is configurable by basic source changes. Also included is a 'client' command utility for sending information to psplash such as boot progress information."
HOMEPAGE = "http://git.yoctoproject.org/cgit/cgit.cgi/psplash"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://psplash.h;md5=a87c39812c1e37f3451567cc29a29c8f"

SRCREV = "e05374aae945bcfc6d962ed0d7b2774b77987e1d"
PV = "0.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://psplash-init \
	   file://psplash-poky-img.h"

S = "${WORKDIR}/git"

inherit autotools pkgconfig update-rc.d

FILES_${PN} += "/mnt/.psplash"

do_configure_prepend () {
	cp -f ${WORKDIR}/psplash-poky-img.h ${S}/
}

do_install_prepend() {
	install -d ${D}/mnt/.psplash/
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/psplash-init ${D}${sysconfdir}/init.d/psplash.sh
}

INITSCRIPT_NAME = "psplash.sh"
INITSCRIPT_PARAMS = "start 0 S . stop 20 0 1 6 ."
