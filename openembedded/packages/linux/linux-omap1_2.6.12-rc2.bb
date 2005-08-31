PR      = "r3"
SECTION = "kernel"
DESCRIPTION = "Linux kernel for OMAP processors"
LICENSE = "GPL"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/testing/linux-2.6.12-rc2.tar.bz2 \
           http://www.muru.com/linux/omap/patches/patch-2.6.12-rc2-omap1.bz2;patch=1 \
	   file://defconfig"


S = "${WORKDIR}/linux-2.6.12-rc2"

KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"
#KERNEL_CCSUFFIX = "-3.3.4"

#DEPENDS = "uboot"

inherit kernel

python __anonymous () {
	import re
	host = bb.data.getVar('HOST_SYS', d, 1)
	if not re.match('arm.*-linux', host):
		raise bb.parse.SkipPackage("incompatible with host %s" % host)
}


do_configure_prepend() {
	install -m 0644 ${WORKDIR}/defconfig ${S}/.config
        oe_runmake oldconfig
}

do_deploy_omap5912osk() {
        install -d ${DEPLOY_DIR}/images
        arm-linux-objcopy -O binary -R .note -R .comment -S arch/arm/boot/compressed/vmlinux ${DEPLOY_DIR}/linux.bin
        gzip -f -9 ${DEPLOY_DIR}/linux.bin
        mkimage -A arm -O linux -T kernel -C gzip -a 0x10c08000 -e 0x10c08000 -n "OE" -d ${DEPLOY_DIR}/linux.bin.gz ${DEPLOY_DIR}/uImage_bb.cc
        cp ${DEPLOY_DIR}/uImage_bb.cc /tftpboot
#        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR}/images/${KERNEL_IMAGETYPE}-${MACHINE}-${DATETIME}.bin
}


do_deploy[dirs] = "${S}"

addtask deploy before do_build after do_compile
