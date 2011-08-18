SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING.kbd;md5=9b2d91511d3d80d4d20ac6e6b0137fe9"
SUMMARY = "Allows you to set-up and manipulate the Linux console."
DESCRIPTION = "Provides tools that enable the set-up and manipulation of the linux console and console-font files."
PR = "r4"

SRC_URI = "${SOURCEFORGE_MIRROR}/lct/console-tools-${PV}.tar.gz \
           file://codepage.patch \
           file://configure.patch \
           file://compile.patch \
           file://kbdrate.patch \
           file://uclibc-fileno.patch \
           file://config"

SRC_URI[md5sum] = "bf21564fc38b3af853ef724babddbacd"
SRC_URI[sha256sum] = "eea6b441672dacd251079fc85ed322e196282e0e66c16303ec64c3a2b1c126c2"

export SUBDIRS = "fontfiletools vttools kbdtools screenfonttools contrib \
		  examples po intl compat"

acpaths = "-I config"
do_configure_prepend () {
	mkdir -p config
	cp ${WORKDIR}/config/*.m4 config/
}

do_compile () {
	oe_runmake -C lib
	oe_runmake 'SUBDIRS=${SUBDIRS}'
}

inherit autotools gettext

do_install () {
	autotools_do_install
	mv ${D}${bindir}/chvt ${D}${bindir}/chvt.${PN}
	mv ${D}${bindir}/deallocvt ${D}${bindir}/deallocvt.${PN}
	mv ${D}${bindir}/openvt ${D}${bindir}/openvt.${PN}
	mv ${D}${bindir}/fgconsole ${D}${bindir}/fgconsole.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${bindir}/chvt chvt chvt.${PN} 100
	update-alternatives --install ${bindir}/deallocvt deallocvt deallocvt.${PN} 100
	update-alternatives --install ${bindir}/openvt openvt openvt.${PN} 100
	update-alternatives --install ${bindir}/fgconsole fgconsole fgconsole.${PN} 100
}

pkg_prerm_${PN} () {
	update-alternatives --remove chvt chvt.${PN}
	update-alternatives --remove deallocvt deallocvt.${PN}
	update-alternatives --remove openvt openvt.${PN}
	update-alternatives --remove fgconsole fgconsole.${PN}
}

RDEPENDS_${PN} = "bash"
