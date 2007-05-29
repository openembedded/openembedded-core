DESCRIPTION = "Common X11 scripts"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS_${PN} = "xmodmap libxrandr xdpyinfo xtscal xinit formfactor"
PR = "r17"

SRC_URI = "file://etc"
S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
	cp -R ${S}/etc ${D}/etc
	rm -fR ${D}/etc/.svn
	rm -fR ${D}/etc/*/.svn
	rm -fR ${D}/etc/*/*/.svn
	chmod -R 755 ${D}/etc
}