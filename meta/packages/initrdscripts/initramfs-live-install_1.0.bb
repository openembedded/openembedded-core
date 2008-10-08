DESCRIPTON = "A live image init script"

SRC_URI = "file://init-install.sh"

PR = "r1"

RDEPENDS="grub parted e2fsprogs-mke2fs"

do_install() {
        install -m 0755 ${WORKDIR}/init-install.sh ${D}/install.sh
}

PACKAGE_ARCH = "all"
FILES_${PN} = " /install.sh "
