# bootimg.oeclass
# Copyright (C) 2004, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

# This creates a bootable image using syslinux, your kernel and an optional
# initrd

# External variables needed
# ${INITRD} - indicates a filesystem image to use as an initrd (optional)
# ${AUTO_SYSLINUXCFG} - set this to 1 to enable creating an automatic config
# ${LABELS} - a list of targets for the automatic config
# ${APPEND} - an override list of append strings for each label
# ${SYSLINUX_OPTS} - additional options to add to the syslinux file ';' delimited 

do_rootfs[depends] += "dosfstools-native:do_populate_staging \
                       syslinux-native:do_populate_staging \
		       mtools-native:do_populate_staging \
		       cdrtools-native:do_populate_staging"

BDIR   = "${WORKDIR}/boot"
ISODIR = "${IMAGE_ROOTFS}/isolinux/"

BOOTIMG_VOLUME_ID   ?= "oe"
BOOTIMG_EXTRA_SPACE ?= "64"

# Get the build_syslinux_cfg() function from the syslinux class

SYSLINUXCFG  = "${BDIR}/syslinux.cfg"
SYSLINUXMENU = "${BDIR}/menu"

inherit syslinux
		
build_boot_bin() {
	install -d ${BDIR}
	install -m 0644 ${STAGING_KERNEL_DIR}/bzImage \
	${BDIR}/vmlinuz

	if [ -n "${INITRD}" ] && [ -s "${INITRD}" ]; then 
    		install -m 0644 ${INITRD} ${BDIR}/initrd
	fi

	install -m 444 ${STAGING_DIR}/${BUILD_SYS}/share/syslinux/ldlinux.sys \
	${BDIR}/ldlinux.sys

	# Do a little math, bash style
	#BLOCKS=`du -s ${BDIR} | cut -f 1`
	BLOCKS=`du -bks ${BDIR} | cut -f 1`
	SIZE=`expr $BLOCKS + ${BOOTIMG_EXTRA_SPACE}`	

	mkdosfs -F 12 -n ${BOOTIMG_VOLUME_ID} -d ${BDIR} \
	-C ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-boot.bin $SIZE 

	syslinux ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-boot.bin

	#Create an ISO if we have an INITRD
	if [ -n "${INITRD}" ] && [ -s "${INITRD}" ] && [ "${NOISO}" != "1" ] ; then
		install -d ${ISODIR}

		# Install the kernel

		install -m 0644 ${STAGING_KERNEL_DIR}/bzImage \
		        ${ISODIR}/vmlinuz

		# Install the configuration files

		cp ${BDIR}/syslinux.cfg ${ISODIR}/isolinux.cfg

		if [ -f ${SYSLINUXMENU} ]; then
			cp ${SYSLINUXMENU} ${ISODIR}
		fi

		install -m 0644 ${INITRD} ${ISODIR}/initrd

		# And install the syslinux stuff 
		cp ${STAGING_DIR}/${BUILD_SYS}/share/syslinux/isolinux.bin \
		${ISODIR}

		mkisofs -V ${BOOTIMG_VOLUME_ID} \
		-o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.iso \
		-b isolinux/isolinux.bin -c isolinux/boot.cat -r \
		-no-emul-boot -boot-load-size 4 -boot-info-table \
		${IMAGE_ROOTFS}
	fi
} 

python do_bootimg() {
	bb.build.exec_func('build_syslinux_cfg', d)
	bb.build.exec_func('build_boot_bin', d)
}

# We need to run after bootsplash if it exists, so thats why this line
# is such.  Don't worry, if you don't do bootsplash, nobody will notice

addtask bootimg before do_build after do_bootsplash
