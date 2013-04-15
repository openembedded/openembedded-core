SUMMARY = "Provides on-device script for interaction with the Anjuta IDE"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://anjuta-remote-run"

S = "${WORKDIR}"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 anjuta-remote-run ${D}${bindir}/
}

PACKAGES = "${PN}"
RDEPENDS_${PN} = "dbus rsync"

