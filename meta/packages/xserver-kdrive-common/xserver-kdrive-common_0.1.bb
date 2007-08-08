DESCRIPTION = "Common X11 scripts"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS_${PN} = "xmodmap libxrandr xdpyinfo xtscal xinit formfactor"
PR = "r19"

SRC_URI = "file://etc"
S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
	cp -R ${S}/etc ${D}/etc
	chmod -R 755 ${D}/etc
	find ${D}/etc -type d -name .svn -prune -exec rm -rf {} \;
	find ${D}/etc -type f -name \*~ -exec rm -rf {} \;
}
