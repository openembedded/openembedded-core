DESCRIPTION = "script to manage module configuration files."
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

RDEPENDS_${PN} = "module-init-tools-depmod"

PR = "r13"

SRC_URI = "file://update-modules \
           file://COPYING.GPL"

inherit allarch

pkg_postinst_${PN} () {
if [ "x$D" != "x" ]; then
	exit 1
fi
update-modules
}

do_install() {
	install -d ${D}${sbindir}
	install ${WORKDIR}/update-modules ${D}${sbindir}
}

