DESCRIPTON = "A live image init script"

SRC_URI = "file://init-boot.sh"

PR = "r0"

do_install() {
        install -m 0755 ${WORKDIR}/init-boot.sh ${D}/init
}

PACKAGE_ARCH = "all"
FILES_${PN} += " /init "
