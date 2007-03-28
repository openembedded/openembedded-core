DESCRIPTION = "Userspace framebuffer boot logo based on usplash."
SECTION = "base"
LICENSE = "GPL"
PV = "0.0+svn${SRCDATE}"
PR = "r3"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=psplash;proto=http \
           file://psplash-init"

S = "${WORKDIR}/psplash"

inherit autotools pkgconfig update-rc.d

FILES_${PN} += "/mnt/.psplash"

do_install_prepend() {
	install -d ${D}/mnt/.psplash/
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/psplash-init ${D}${sysconfdir}/init.d/psplash
}

INITSCRIPT_NAME = "psplash"
INITSCRIPT_PARAMS = "start 0 S . stop 20 0 1 6 ."
