DESCRIPTION = "2.6 Linux Android Kernel from Google"
SECTION = "kernel"
LICENSE = "GPL"

inherit kernel

PR = "r0"

SRC_URI = "http://android.googlecode.com/files/linux-2.6.23-android-m3-rc20.tar.gz \
           file://binutils-buildid-arm.patch;patch=1 \
           file://defconfig-android-goldfish"

S = "${WORKDIR}/kernel"

RPROVIDES_kernel-base += "hostap-modules"

RPSRC = "http://www.rpsys.net/openzaurus/patches/archive"

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = 'android'

KERNEL_CUSTOM_NAME ?= ""
KERNEL_DEPLOY_NAME ?= "${KERNEL_IMAGETYPE}-${PV}-${KERNEL_CUSTOM_NAME}${MACHINE}-${DATETIME}.bin"
KERNEL_SYMLINK_NAME ?=  "${KERNEL_IMAGETYPE}-${KERNEL_CUSTOM_NAME}${MACHINE}.bin"
KERNEL_DEFCONFIG ?= "defconfig-${MACHINE}"

CMDLINE = ""

do_configure() {
	rm -f ${S}/.config

	if [ ! -e ${WORKDIR}/${KERNEL_DEFCONFIG} ]; then
		die "No default configuration for ${MACHINE} available."
	fi

	echo "CONFIG_CMDLINE=\"${CMDLINE}\"" >> ${S}/.config

	if [ "${TARGET_OS}" = "linux-gnueabi" -o "${TARGET_OS}" = "linux-uclibcgnueabi" ]; then
		echo "CONFIG_AEABI=y"                   >> ${S}/.config
		echo "CONFIG_OABI_COMPAT=y"             >> ${S}/.config
	else 
		echo "# CONFIG_AEABI is not set"        >> ${S}/.config
		echo "# CONFIG_OABI_COMPAT is not set"  >> ${S}/.config
	fi

	if [ "${DISTRO}" = "poky" -a "${MACHINE}" != "collie" ]; then
		echo "CONFIG_LOGO=y"                          >> ${S}/.config
		echo "CONFIG_LOGO_OHAND_CLUT224=y"            >> ${S}/.config
		echo "# CONFIG_LOGO_LINUX_CLUT224 is not set" >> ${S}/.config
	else 
		echo "# CONFIG_LOGO is not set"               >> ${S}/.config
		echo "# CONFIG_LOGO_OHAND_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_LINUX_CLUT224 is not set" >> ${S}/.config
	fi

	sed -e '/CONFIG_AEABI/d' \
	    -e '/CONFIG_OABI_COMPAT=/d' \
	    -e '/CONFIG_CMDLINE=/d' \
	    -e '/CONFIG_MTD_MTDRAM_SA1100=/d' \
	    -e '/CONFIG_MTDRAM_TOTAL_SIZE=/d' \
	    -e '/CONFIG_MTDRAM_ERASE_SIZE=/d' \
	    -e '/CONFIG_MTDRAM_ABS_POS=/d' \
	    -e '/CONFIG_LOGO=/d' \
	    -e '/CONFIG_LOGO_LINUX_CLUT224=/d' \
	    -e '/CONFIG_LOGO_OHAND_CLUT224=/d' \
	    '${WORKDIR}/${KERNEL_DEFCONFIG}' >>'${S}/.config'

	yes '' | oe_runmake oldconfig
}

do_deploy() {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR_IMAGE}/${KERNEL_DEPLOY_NAME}
	cd ${DEPLOY_DIR_IMAGE}
	ln -sf ${KERNEL_DEPLOY_NAME} ${KERNEL_SYMLINK_NAME}
	#tar -cvzf ${DEPLOY_DIR_IMAGE}/modules-${KERNEL_VERSION}-${MACHINE}.tgz -C ${D} lib	
}

do_deploy[dirs] = "${S}"

addtask deploy before do_package after do_install
