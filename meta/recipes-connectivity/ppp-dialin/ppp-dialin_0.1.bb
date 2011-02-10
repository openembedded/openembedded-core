SECTION = "console/network"
DESCRIPTION = "Enables PPP dial-in through a serial connection"
DEPENDS = "ppp"
RDEPENDS_${PN} = "ppp"
PR = "r6"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://host-peer \
           file://ppp-dialin"

do_install() {
	install -d ${D}${sysconfdir}/ppp/peers
	install -m 0644 ${WORKDIR}/host-peer ${D}${sysconfdir}/ppp/peers/host

	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/ppp-dialin ${D}${sbindir}
}

PACKAGE_ARCH = "all"

pkg_postinst_${PN} () {
if test "x$D" != "x"; then
	exit 1
else
	adduser --system --home /dev/null --no-create-home --empty-password --ingroup nogroup -s ${sbindir}/ppp-dialin ppp
fi
}

pkg_postrm_${PN} () {
if test "x$D" != "x"; then
	exit 1
else
	deluser ppp
fi
}
