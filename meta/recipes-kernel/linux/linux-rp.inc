DESCRIPTION = "2.6 Linux Development Kernel for Zaurus devices and iPAQ hx2750."
SECTION = "kernel"
LICENSE = "GPL"

inherit kernel

DEPENDS_append_collie = " bc-native"
RPROVIDES_kernel-base += "hostap-modules"

DOSRC = "http://www.do13.de/openzaurus/patches"
RPSRC = "http://www.rpsys.net/openzaurus/patches/archive"
JLSRC = "http://www.cs.wisc.edu/~lenz/zaurus/files"
BASRC = "http://www.orca.cx/zaurus/patches"
CHSRC = "http://oz.drigon.com/patches"
TKSRC = "http://www.informatik.hu-berlin.de/~tkunze/zaurus/patches"

COMPATIBLE_HOST = "(arm|i.86|mips).*-linux"
COMPATIBLE_MACHINE = '(collie|poodle|c7x0|akita|spitz|tosa|hx2000|qemuarm|qemux86|qemumips|htcuniversal|zylonite)'

KERNEL_CUSTOM_NAME ?= ""
KERNEL_DEPLOY_NAME ?= "${KERNEL_IMAGETYPE}-${PV}-${KERNEL_CUSTOM_NAME}${MACHINE}-${DATETIME}.bin"
KERNEL_SYMLINK_NAME ?=  "${KERNEL_IMAGETYPE}-${KERNEL_CUSTOM_NAME}${MACHINE}.bin"
KERNEL_DEFCONFIG ?= "defconfig-${MACHINE}"

CMDLINE_CON = "console=ttyS0,115200n8 console=tty1 noinitrd"
CMDLINE_CON_collie = "console=ttySA0,115200n8 console=tty1 noinitrd"
CMDLINE_CON_qemuarm = "console=ttyAMA0,115200n8 console=tty1 noinitrd"
CMDLINE_CON_zylonite = "console=ttyS0,38400"
CMDLINE_ROOT ?= "root=/dev/mtdblock2 rootfstype=jffs2"
CMDLINE_ROOT_spitz ?= "root=/dev/hda1 rootfstype=ext3 rootdelay=1 rw"
#CMDLINE_ROOT_spitz = "root=/dev/mmcblk0p2 rootfstype=ext2 rootdelay=3 rw"
CMDLINE_OTHER = "dyntick=enable"
CMDLINE_DEBUG = '${@base_contains("IMAGE_FEATURES", "debug-tweaks", "debug", "quiet", d)}'

##############################################################
# Configure memory/ramdisk split for collie
#
export mem = '${@bb.data.getVar("COLLIE_MEMORY_SIZE",d,1) or "64"}'
export rd  = '${@bb.data.getVar("COLLIE_RAMDISK_SIZE",d,1) or "0"}'

CMDLINE_MEM_collie = "mem=${mem}M"
CMDLINE_MEM_zylonite = "mem=64M"
CMDLINE_ROTATE_spitz = "fbcon=rotate:1"
CMDLINE_ROTATE_akita = "fbcon=rotate:1"
CMDLINE_ROTATE_collie = "fbcon=rotate:1"
CMDLINE_ROTATE_poodle = "fbcon=rotate:1"
CMDLINE = "${CMDLINE_CON} ${CMDLINE_ROOT} ${CMDLINE_MEM} ${CMDLINE_ROTATE} ${CMDLINE_OTHER} ${CMDLINE_DEBUG}"

###############################################################
# module configs specific to this kernel
#
module_autoload_pxaficp_ir = "pxaficp_ir"
module_autoload_snd-pcm-oss = "snd-pcm-oss"
module_autoload_pcmcia_core = "pcmcia_core"
module_autoload_pxa2xx_cs = "pxa2xx_cs"
module_autoload_ohci-hcd_tosa = "ohci-hcd"
module_autoload_snd-soc-corgi_c7x0 = "snd-soc-corgi"
module_autoload_snd-soc-spitz_akita = "snd-soc-spitz"
module_autoload_snd-soc-spitz_spitz = "snd-soc-spitz"
module_autoload_snd-soc-poodle_poodle = "snd-soc-poodle"
module_autoload_locomo-spi_collie = "locomo-spi"
module_autoload_mmc_block_collie = "mmc_block"
module_autoload_mmc_spi = "mmc-spi"
module_autoload_locomokbd_collie = "locomokbd"
module_autoload_sa1100-cs_collie = "sa1100_cs"
module_autoload_collie-ts_collie = "collie-ts"
module_autoload_leds-locomo_collie = "leds-locomo"
module_autoload_power_collie = "power"

do_configure() {
	rm -f ${S}/.config

	if [ "${MACHINE}" = "tosa" ]; then
		gcc_version=`${KERNEL_CC} -dumpversion`		
		if [ "${gcc_version}" = "4.0.1" ] || [ "${gcc_version}" = "4.0.2" ]; then
			die "tosa kernel wont work with gcc 4.0.x"
		fi
	fi

	if [ ! -e ${WORKDIR}/${KERNEL_DEFCONFIG} ]; then
		die "No default configuration for ${MACHINE} available."
	fi

	if [ "${MACHINE}" = "collie" ]; then
		mempos=`echo "obase=16; $mem * 1024 * 1024" | bc`
		rdsize=`echo "$rd * 1024" | bc`
		total=`expr $mem + $rd`
		addr=`echo "obase=16; ibase=16; C0000000 + $mempos" | bc`
	 	if [ "$rd" = "0" ]
	 	then
		    echo "No RAMDISK"
			echo "# CONFIG_MTD_MTDRAM_SA1100 is not set" >> ${S}/.config
		else
		    echo "RAMDIR = $rdsize on $addr"
			echo "CONFIG_MTD_MTDRAM_SA1100=y"           >> ${S}/.config
			echo "CONFIG_MTDRAM_TOTAL_SIZE=$rdsize"     >> ${S}/.config
			echo "CONFIG_MTDRAM_ERASE_SIZE=1"           >> ${S}/.config
			echo "CONFIG_MTDRAM_ABS_POS=$addr"          >> ${S}/.config
		fi
	fi

	echo "CONFIG_CMDLINE=\"${CMDLINE}\"" >> ${S}/.config

	if [ "${TARGET_OS}" = "linux-gnueabi" -o "${TARGET_OS}" = "linux-uclibcgnueabi" ]; then
		echo "CONFIG_AEABI=y"                   >> ${S}/.config
		if [ "${MACHINE}" = "qemuarm" -o "${MACHINE}" = "qemuarmv6" -o "${MACHINE}" = "qemuarmv7" -o ]; then
		    echo "CONFIG_OABI_COMPAT=y"             >> ${S}/.config
		else
		    echo "# CONFIG_OABI_COMPAT is not set"  >> ${S}/.config
		fi
	else 
		echo "# CONFIG_AEABI is not set"        >> ${S}/.config
		echo "# CONFIG_OABI_COMPAT is not set"  >> ${S}/.config
	fi

	if [ "${DISTRO}" = "poky" -a "${MACHINE}" != "collie" ]; then
		echo "CONFIG_LOGO=y"                          >> ${S}/.config
		echo "CONFIG_LOGO_OHAND_CLUT224=y"            >> ${S}/.config
		echo "# CONFIG_LOGO_OZ240_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_OZ480_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_OZ640_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_LINUX_CLUT224 is not set" >> ${S}/.config
	else 
		echo "# CONFIG_LOGO is not set"               >> ${S}/.config
		echo "# CONFIG_LOGO_OHAND_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_LINUX_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_OZ240_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_OZ480_CLUT224 is not set" >> ${S}/.config
		echo "# CONFIG_LOGO_OZ640_CLUT224 is not set" >> ${S}/.config
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
	    -e '/CONFIG_LOGO_OZ240_CLUT224=/d' \
	    -e '/CONFIG_LOGO_OZ480_CLUT224=/d' \
	    -e '/CONFIG_LOGO_OZ640_CLUT224=/d' \
	    '${WORKDIR}/${KERNEL_DEFCONFIG}' >>'${S}/.config'

	yes '' | oe_runmake oldconfig
}

do_bootkern() {
	echo "Copying Kernel"
	scp ${DEPLOY_DIR_IMAGE}/${KERNEL_SYMLINK_NAME} root@${TARGET_DEVICE_IP}:/zImage-bootkern
	echo "Copying Modules"
	scp ${DEPLOY_DIR_IMAGE}/modules-${KERNEL_VERSION}-${MACHINE}.tgz root@${TARGET_DEVICE_IP}:/
	echo "Unpacking Modules"
	ssh root@${TARGET_DEVICE_IP} "cd /; tar -xvzf modules-${KERNEL_VERSION}-${MACHINE}.tgz; rm modules-${KERNEL_VERSION}-${MACHINE}.tgz"
	echo "Loading Kernel into kexec"
	ssh root@${TARGET_DEVICE_IP} "/usr/sbin/kexec -l /zImage-bootkern" 
	echo "Launching kernel"
	ssh root@${TARGET_DEVICE_IP} "/usr/sbin/kexec -f -e" 
}
addtask bootkern after do_deploy
do_bootkern[nostamp] = "1"

# wlan-ng stuff need compiled kernel sources
do_rm_work() {
}
