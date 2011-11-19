SECTION = "console/network"
DESCRIPTION = "Enables PPP dial-in through a serial connection"
DEPENDS = "ppp"
RDEPENDS_${PN} = "ppp"
PR = "r8"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://host-peer \
           file://ppp-dialin"

inherit allarch useradd

do_install() {
	install -d ${D}${sysconfdir}/ppp/peers
	install -m 0644 ${WORKDIR}/host-peer ${D}${sysconfdir}/ppp/peers/host

	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/ppp-dialin ${D}${sbindir}
}

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --home /dev/null \
                       --no-create-home --shell ${sbindir}/ppp-dialin \
                       --no-user-group --gid nogroup ppp"
