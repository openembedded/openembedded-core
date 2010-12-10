DESCRIPTION = "A live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${POKYBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = "file://init-install.sh"

PR = "r3"

RDEPENDS="grub parted e2fsprogs-mke2fs"

do_install() {
        install -m 0755 ${WORKDIR}/init-install.sh ${D}/install.sh
}

PACKAGE_ARCH = "all"
FILES_${PN} = " /install.sh "

# Alternatives to grub need adding for other arch support
COMPATIBLE_HOST = "(i.86|x86_64).*-linux"
