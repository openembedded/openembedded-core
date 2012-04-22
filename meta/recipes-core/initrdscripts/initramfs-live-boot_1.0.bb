DESCRIPTION = "A live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
RDEPENDS = "udev"
DEPENDS = "virtual/kernel"
SRC_URI = "file://init-live.sh"

PR = "r10"

do_compile() {
	#if grep -q "CONFIG_UNION_FS=y" ${STAGING_KERNEL_DIR}/.config; then
	#	sed -i 's/UNIONFS="no"/UNIONFS="yes"/g' ${WORKDIR}/init-live.sh
	#fi
	:
}
 
do_install() {
        install -m 0755 ${WORKDIR}/init-live.sh ${D}/init
}

FILES_${PN} += " /init "

# Due to kernel depdendency
PACKAGE_ARCH = "${MACHINE_ARCH}"
