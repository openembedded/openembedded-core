DESCRIPTION = "2.6 Linux Development Kernel for Zaurus devices and iPAQ hx2750."
SECTION = "kernel"
MAINTAINER = "Richard Purdie <rpurdie@rpsys.net>, Michael 'Mickey' Lauer <mickey@vanille.de>"
LICENSE = "GPL"

inherit kernel

RPROVIDES_kernel-image += "hostap-modules"

DOSRC = "http://www.do13.de/openzaurus/patches"
RPSRC = "http://www.rpsys.net/openzaurus/patches/archive"
JLSRC = "http://www.cs.wisc.edu/~lenz/zaurus/files"
BASRC = "http://www.orca.cx/zaurus/patches"
CHSRC = "http://oz.drigon.com/patches"

##############################################################
# Compensate for sucky bootloader on all Sharp Zaurus models
#
FILES_kernel-image = ""
ALLOW_EMPTY = 1

EXTRA_OEMAKE = "OPENZAURUS_RELEASE=-${DISTRO_VERSION}"
COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = '(collie|poodle|c7x0|akita|spitz|tosa|ipaq-pxa270|qemuarm)'

CMDLINE_CON = "console=ttyS0,115200n8 console=tty1 noinitrd"
CMDLINE_ROOT = "root=/dev/mtdblock2 rootfstype=jffs2"
CMDLINE_ROOT_spitz = "root=/dev/hda1 rootfstype=ext3 rw"
CMDLINE_OTHER = "dyntick=enable"
CMDLINE_DEBUG = '${@base_conditional("DISTRO_TYPE", "release", "quiet", "debug",d)}'

##############################################################
# Configure memory/ramdisk split for collie
#
export mem = ${@bb.data.getVar("COLLIE_MEMORY_SIZE",d,1) or "32"}
export rd  = ${@bb.data.getVar("COLLIE_RAMDISK_SIZE",d,1) or "32"}

CMDLINE_MEM_collie = "mem=${mem}M"
CMDLINE_ROTATE_spitz = "fbcon=rotate:1"
CMDLINE_ROTATE_akita = "fbcon=rotate:1"
CMDLINE_ROTATE_collie = "fbcon=rotate:1"
CMDLINE_ROTATE_poodle = "fbcon=rotate:1"
CMDLINE = "${CMDLINE_CON} ${CMDLINE_ROOT} ${CMDLINE_MEM} ${CMDLINE_ROTATE} ${CMDLINE_OTHER} ${CMDLINE_DEBUG}"

###############################################################
# Enable or disable ELPP via local.conf - default is "no"
#
ENABLE_ELPP = ${@bb.data.getVar("OZ_KERNEL_ENABLE_ELPP",d,1) or "no"}

###############################################################
# module configs specific to this kernel
#
module_autoload_pxaficp_ir = "pxaficp_ir"
module_autoload_snd-pcm-oss = "snd-pcm-oss"
module_autoload_snd-soc-corgi_c7x0 = "snd-soc-corgi"
module_autoload_snd-soc-spitz_akita = "snd-soc-spitz"
module_autoload_snd-soc-spitz_spitz = "snd-soc-spitz"

do_configure() {
	rm -f ${S}/.config

	if [ "${MACHINE}" == "tosa" ]; then
		gcc_version=`${KERNEL_CC} -dumpversion`		
		if [ "${gcc_version}" == "4.0.1" ] || [ "${gcc_version}" == "4.0.2" ]; then
			die "tosa kernel wont work with gcc 4.0.x"
		fi
	fi

	if [ ! -e ${WORKDIR}/defconfig-${MACHINE} ]; then
		die "No default configuration for ${MACHINE} available."
	fi

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

	#if [ "${ENABLE_ELPP}" == "yes" ]; then
        #        echo "# Enhanced Linux Progress Patch"  >> ${S}/.config
        #        echo "CONFIG_FB_ELPP=y"                 >> ${S}/.config
        #        echo "CONFIG_LOGO=y"                    >> ${S}/.config
        #        echo "CONFIG_LOGO_LINUX_CLUT224=y"      >> ${S}/.config
	#else
	#	echo "# CONFIG_FB_ELPP is not set"	>> ${S}/.config
	#fi

	if [ "${TARGET_OS}" == "linux-gnueabi" ]; then
		echo "CONFIG_AEABI=y"                   >> ${S}/.config
		echo "CONFIG_OABI_COMPAT=y"             >> ${S}/.config
	else 
		echo "# CONFIG_AEABI is not set"        >> ${S}/.config
		echo "# CONFIG_OABI_COMPAT is not set"  >> ${S}/.config
	fi

	sed -e '/CONFIG_AEABI/d' \
	    -e '/CONFIG_OABI_COMPAT=/d' \
	    -e '/CONFIG_CMDLINE=/d' \
	    -e '/CONFIG_MTD_MTDRAM_SA1100=/d' \
	    -e '/CONFIG_MTDRAM_TOTAL_SIZE=/d' \
	    -e '/CONFIG_MTDRAM_ERASE_SIZE=/d' \
	    -e '/CONFIG_MTDRAM_ABS_POS=/d' \
	    '${WORKDIR}/defconfig-${MACHINE}' >>'${S}/.config'

	yes '' | oe_runmake oldconfig
}

# Check the kernel is below the 1264*1024 byte limit for the PXA Zaurii
do_compile_append() {
	case ${MACHINE} in
		c7x0 | akita | poodle | spitz | tosa )
			size=`ls -l arch/${ARCH}/boot/${KERNEL_IMAGETYPE} | awk '{ print $5}'`
			if [ $size -ge 1294336 ]; then	
				rm arch/${ARCH}/boot/${KERNEL_IMAGETYPE}
				echo "Size is $size"
				die "This kernel is too big for your PXA Zaurus and will destroy data if you flash it. Please reduce the size of the kernel by making more of it modular."
			fi
			;;
		collie )
			size=`ls -l arch/${ARCH}/boot/${KERNEL_IMAGETYPE} | awk '{ print $5}'`
			if [ $size -ge 1048576 ]; then	
				rm arch/${ARCH}/boot/${KERNEL_IMAGETYPE}
				echo "Size is $size"
				die "This kernel is too big for your Collie and will not be flashed. Please reduce the size of the kernel by making more of it modular."
			fi
			;;
        	*)
			;;
	esac
}

do_deploy() {
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
        tar -cvzf ${DEPLOY_DIR_IMAGE}/modules-${KERNEL_RELEASE}-${MACHINE}.tgz -C ${D} lib	
}

do_deploy[dirs] = "${S}"

addtask deploy before do_package after do_install
