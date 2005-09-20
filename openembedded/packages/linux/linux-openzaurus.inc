DESCRIPTION = "2.6 Linux Development Kernel for Zaurus devices and iPAQ hx2750."
SECTION = "kernel"
MAINTAINER = "Richard Purdie <rpurdie@rpsys.net>, Michael 'Mickey' Lauer <mickey@vanille.de>"
LICENSE = "GPL"

DOSRC = "http://www.do13.in-berlin.de/openzaurus/patches"
RPSRC = "http://www.rpsys.net/openzaurus/patches"
JLSRC = "http://www.cs.wisc.edu/~lenz/zaurus/files"
BASRC = "http://www.orca.cx/zaurus/patches"

inherit kernel

##############################################################
# Compensate for sucky bootloader on all Sharp Zaurus models
#
FILES_kernel-image = ""
ALLOW_EMPTY = 1

EXTRA_OEMAKE = "OPENZAURUS_RELEASE=-${DISTRO_VERSION}"
COMPATIBLE_HOST = "arm.*-linux"

CMDLINE_CON = "console=ttyS0,115200n8 console=tty1 noinitrd"
CMDLINE_ROOT = "root=/dev/mtdblock2 rootfstype=jffs2 "
CMDLINE_ROOT_poodle = "root=/dev/mtdblock1 rootfstype=jffs2 "
CMDLINE_ROOT_spitz = "root=/dev/hda1 rootfstype=ext3 rw "

##############################################################
# Configure memory/ramdisk split for collie
#
export mem = ${@bb.data.getVar("COLLIE_MEMORY_SIZE",d,1) or "32"}
export rd  = ${@bb.data.getVar("COLLIE_RAMDISK_SIZE",d,1) or "32"}

CMDLINE_MEM_collie = "mem=${mem}M"
CMDLINE = "${CMDLINE_CON} ${CMDLINE_ROOT} ${CMDLINE_MEM} debug"

###############################################################
# Enable or disable ELPP via local.conf - default is "no"
#
ENABLE_ELPP = ${@bb.data.getVar("OZ_KERNEL_ENABLE_ELPP",d,1) or "no"}

###############################################################
# module configs specific to this kernel
#
module_autoload_pxaficp_ir = "pxaficp_ir"

do_configure() {

	install -m 0644 ${WORKDIR}/defconfig-${MACHINE} ${S}/.config || die "No default configuration for ${MACHINE} available."

	if [ "${MACHINE}" == "collie" ]; then
		mempos=`echo "obase=16; $mem * 1024 * 1024" | bc`
		rdsize=`echo "$rd * 1024" | bc`
		total=`expr $mem + $rd`
		addr=`echo "obase=16; ibase=16; C0000000 + $mempos" | bc`
	 	if [ "$rd" == "0" ]
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

	if [ "${ENABLE_ELPP}" == "yes" ]; then
                echo "# Enhanced Linux Progress Patch"  >> ${S}/.config
                echo "CONFIG_FB_ELPP=y"                 >> ${S}/.config
                echo "CONFIG_LOGO=y"                    >> ${S}/.config
                echo "CONFIG_LOGO_LINUX_CLUT224=y"      >> ${S}/.config
	else
		echo "# CONFIG_FB_ELPP is not set"	>> ${S}/.config
	fi

	yes '' | oe_runmake oldconfig
}

# Check the kernel is below the 1272*1024 byte limit for the PXA Zaurii
do_compile_append() {
	case ${MACHINE} in
		c7x0 | akita | poodle | spitz | borzoi | tosa )
			size=`ls arch/${ARCH}/boot/${KERNEL_IMAGETYPE} -s | cut -d ' ' -f 1`
			if [ $size -ge 1271 ]; then	
				rm arch/${ARCH}/boot/${KERNEL_IMAGETYPE}
				die "This kernel is too big for the c7x0 and will destroy your machine if you flash it!!!"
			fi
			;;
        	*)
			;;
	esac
}

do_deploy() {
        install -d ${DEPLOY_DIR}/images
        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR}/images/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
}

do_deploy[dirs] = "${S}"

addtask deploy before do_build after do_compile
