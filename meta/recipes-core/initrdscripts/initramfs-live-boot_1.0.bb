DESCRIPTON = "A live image init script"
LICENSE = "MIT"

SRC_URI = "file://init-live.sh"

PR = "r2"

do_install() {
        install -m 0755 ${WORKDIR}/init-live.sh ${D}/init
}

PACKAGE_ARCH = "all"
FILES_${PN} += " /init "
