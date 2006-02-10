DESCRIPTION = "Common X11 scripts"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS_${PN} = "xmodmap xrandr xdpyinfo xtscal"
PR = "r3"

SRC_URI = "file://etc"
S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
	cp -R ${S}/etc ${D}/etc
	chmod -R 755 ${D}/etc
}