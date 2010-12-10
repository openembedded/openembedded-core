DESCRIPTION = "A live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = "file://init-live.sh"

PR = "r2"

do_install() {
        install -m 0755 ${WORKDIR}/init-live.sh ${D}/init
}

PACKAGE_ARCH = "all"
FILES_${PN} += " /init "
