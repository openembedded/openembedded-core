DESCRIPTION = "A live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = "file://init-live.sh"

PR = "r5"

do_install() {
        install -m 0755 ${WORKDIR}/init-live.sh ${D}/init
}

inherit allarch

FILES_${PN} += " /init "
