DESCRIPTION = "Common X11 scripts"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "x11"
RDEPENDS_${PN} = "xmodmap xdpyinfo xtscal xinit formfactor"
PR = "r40"

SRC_URI = "file://etc \
           file://gplv2-license.patch"

S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
	cp -R ${S}/etc ${D}/etc
	chmod -R 755 ${D}/etc
	find ${D}/etc -type d -name .svn -prune -exec rm -rf {} \;
	find ${D}/etc -type f -name \*~ -exec rm -rf {} \;
}
