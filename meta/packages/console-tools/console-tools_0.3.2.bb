SECTION = "base"
LICENSE = "GPL"
DESCRIPTION = "Allows you to set-up and manipulate the Linux console."
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/lct/console-tools-${PV}.tar.gz \
           file://codepage.patch;patch=1 \
           file://configure.patch;patch=1 \
           file://compile.patch;patch=1 \
           file://kbdrate.patch;patch=1 \
           file://uclibc-fileno.patch;patch=1 \
           file://config/*.m4"

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

inherit autotools 

do_install () {
	autotools_do_install
	mv ${D}${bindir}/chvt ${D}${bindir}/chvt.${PN}
	mv ${D}${bindir}/deallocvt ${D}${bindir}/deallocvt.${PN}
	mv ${D}${bindir}/openvt ${D}${bindir}/openvt.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${bindir}/chvt chvt chvt.${PN} 100
	update-alternatives --install ${bindir}/deallocvt deallocvt deallocvt.${PN} 100
	update-alternatives --install ${bindir}/openvt openvt openvt.${PN} 100
}

pkg_prerm_${PN} () {
	update-alternatives --remove chvt chvt.${PN}
	update-alternatives --remove deallocvt deallocvt.${PN}
	update-alternatives --remove openvt openvt.${PN}
}

