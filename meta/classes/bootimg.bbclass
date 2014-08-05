# Copyright (C) 2004, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

# Creates a bootable image using syslinux, your kernel and an optional
# initrd

#
# End result is two things:
#
# 1. A .hddimg file which is an msdos filesystem containing syslinux, a kernel,
# an initrd and a rootfs image. These can be written to harddisks directly and
# also booted on USB flash disks (write them there with dd).
#
# 2. A CD .iso image

# Boot process is that the initrd will boot and process which label was selected
# in syslinux. Actions based on the label are then performed (e.g. installing to
# an hdd)

# External variables (also used by syslinux.bbclass)
# ${INITRD} - indicates a list of filesystem images to concatenate and use as an initrd (optional)
# ${COMPRESSISO} - Transparent compress ISO, reduce size ~40% if set to 1
# ${NOISO}  - skip building the ISO image if set to 1
# ${NOHDD}  - skip building the HDD image if set to 1
# ${HDDIMG_ID} - FAT image volume-id
# ${ROOTFS} - indicates a filesystem image to include as the root filesystem (optional)

do_bootimg[depends] += "dosfstools-native:do_populate_sysroot \
                        mtools-native:do_populate_sysroot \
                        cdrtools-native:do_populate_sysroot \
                        ${@oe.utils.ifelse(d.getVar('COMPRESSISO'),'zisofs-tools-native:do_populate_sysroot','')}"

PACKAGES = " "
EXCLUDE_FROM_WORLD = "1"

HDDDIR = "${S}/hddimg"
ISODIR = "${S}/iso"
EFIIMGDIR = "${S}/efi_img"
COMPACT_ISODIR = "${S}/iso.z"
COMPRESSISO ?= "0"

BOOTIMG_VOLUME_ID   ?= "boot"
BOOTIMG_EXTRA_SPACE ?= "512"

EFI = "${@bb.utils.contains("MACHINE_FEATURES", "efi", "1", "0", d)}"
EFI_PROVIDER ?= "grub-efi"
EFI_CLASS = "${@bb.utils.contains("MACHINE_FEATURES", "efi", "${EFI_PROVIDER}", "", d)}"

# Include legacy boot if MACHINE_FEATURES includes "pcbios" or if it does not
# contain "efi". This way legacy is supported by default if neither is
# specified, maintaining the original behavior.
def pcbios(d):
    pcbios = bb.utils.contains("MACHINE_FEATURES", "pcbios", "1", "0", d)
    if pcbios == "0":
        pcbios = bb.utils.contains("MACHINE_FEATURES", "efi", "0", "1", d)
    return pcbios

PCBIOS = "${@pcbios(d)}"

# The syslinux is required for the isohybrid command and boot catalog
inherit syslinux
inherit ${EFI_CLASS}

populate() {
	DEST=$1
	install -d ${DEST}

	# Install bzImage, initrd, and rootfs.img in DEST for all loaders to use.
	install -m 0644 ${STAGING_KERNEL_DIR}/bzImage ${DEST}/vmlinuz
	
	# initrd is made of concatenation of multiple filesystem images
	if [ -n "${INITRD}" ]; then
		rm -f ${DEST}/initrd
		for fs in ${INITRD}
		do
			if [ -s "${fs}" ]; then
				cat ${fs} >> ${DEST}/initrd
			else
				bbfatal "${fs} is invalid. initrd image creation failed."
			fi
		done
		chmod 0644 ${DEST}/initrd
	fi

	if [ -n "${ROOTFS}" ] && [ -s "${ROOTFS}" ]; then
		install -m 0644 ${ROOTFS} ${DEST}/rootfs.img
	fi

}

build_iso() {
	# Only create an ISO if we have an INITRD and NOISO was not set
	if [ -z "${INITRD}" ] || [ "${NOISO}" = "1" ]; then
		bbnote "ISO image will not be created."
		return
	fi
	# ${INITRD} is a list of multiple filesystem images
	for fs in ${INITRD}
	do
		if [ ! -s "${fs}" ]; then
			bbnote "ISO image will not be created. ${fs} is invalid."
			return
		fi
	done


	populate ${ISODIR}

	if [ "${PCBIOS}" = "1" ]; then
		syslinux_iso_populate ${ISODIR}
	fi
	if [ "${EFI}" = "1" ]; then
		efi_iso_populate ${ISODIR}
		build_fat_img ${EFIIMGDIR} ${ISODIR}/efi.img
	fi

	# EFI only
	if [ "${PCBIOS}" != "1" ] && [ "${EFI}" = "1" ] ; then
		# Work around bug in isohybrid where it requires isolinux.bin
		# In the boot catalog, even though it is not used
		mkdir -p ${ISODIR}/${ISOLINUXDIR}
		install -m 0644 ${STAGING_DATADIR}/syslinux/isolinux.bin ${ISODIR}${ISOLINUXDIR}
	fi

	if [ "${COMPRESSISO}" = "1" ] ; then
		# create compact directory, compress iso
		mkdir -p ${COMPACT_ISODIR}
		mkzftree -z 9 -p 4 -F ${ISODIR}/rootfs.img ${COMPACT_ISODIR}/rootfs.img

		# move compact iso to iso, then remove compact directory
		mv ${COMPACT_ISODIR}/rootfs.img ${ISODIR}/rootfs.img
		rm -Rf ${COMPACT_ISODIR}
		mkisofs_compress_opts="-R -z -D -l"
	else
		mkisofs_compress_opts="-r"
	fi

	if [ "${PCBIOS}" = "1" ] && [ "${EFI}" != "1" ] ; then
		# PCBIOS only media
		mkisofs -V ${BOOTIMG_VOLUME_ID} \
		        -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.iso \
			-b ${ISO_BOOTIMG} -c ${ISO_BOOTCAT} \
			$mkisofs_compress_opts \
			${MKISOFS_OPTIONS} ${ISODIR}
	else
		# EFI only OR EFI+PCBIOS
		mkisofs -A ${BOOTIMG_VOLUME_ID} -V ${BOOTIMG_VOLUME_ID} \
		        -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.iso \
			-b ${ISO_BOOTIMG} -c ${ISO_BOOTCAT} \
			$mkisofs_compress_opts ${MKISOFS_OPTIONS} \
			-eltorito-alt-boot -eltorito-platform efi \
			-b efi.img -no-emul-boot \
			${ISODIR}
		isohybrid_args="-u"
	fi

	isohybrid $isohybrid_args ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.iso

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.iso
	ln -s ${IMAGE_NAME}.iso ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.iso
}

build_fat_img() {
	FATSOURCEDIR=$1
	FATIMG=$2

	# Calculate the size required for the final image including the
	# data and filesystem overhead.
	# Sectors: 512 bytes
	#  Blocks: 1024 bytes

	# Determine the sector count just for the data
	SECTORS=$(expr $(du --apparent-size -ks ${FATSOURCEDIR} | cut -f 1) \* 2)

	# Account for the filesystem overhead. This includes directory
	# entries in the clusters as well as the FAT itself.
	# Assumptions:
	#   FAT32 (12 or 16 may be selected by mkdosfs, but the extra
	#   padding will be minimal on those smaller images and not
	#   worth the logic here to caclulate the smaller FAT sizes)
	#   < 16 entries per directory
	#   8.3 filenames only

	# 32 bytes per dir entry
	DIR_BYTES=$(expr $(find ${FATSOURCEDIR} | tail -n +2 | wc -l) \* 32)
	# 32 bytes for every end-of-directory dir entry
	DIR_BYTES=$(expr $DIR_BYTES + $(expr $(find ${FATSOURCEDIR} -type d | tail -n +2 | wc -l) \* 32))
	# 4 bytes per FAT entry per sector of data
	FAT_BYTES=$(expr $SECTORS \* 4)
	# 4 bytes per FAT entry per end-of-cluster list
	FAT_BYTES=$(expr $FAT_BYTES + $(expr $(find ${FATSOURCEDIR} -type d | tail -n +2 | wc -l) \* 4))

	# Use a ceiling function to determine FS overhead in sectors
	DIR_SECTORS=$(expr $(expr $DIR_BYTES + 511) / 512)
	# There are two FATs on the image
	FAT_SECTORS=$(expr $(expr $(expr $FAT_BYTES + 511) / 512) \* 2)
	SECTORS=$(expr $SECTORS + $(expr $DIR_SECTORS + $FAT_SECTORS))

	# Determine the final size in blocks accounting for some padding
	BLOCKS=$(expr $(expr $SECTORS / 2) + ${BOOTIMG_EXTRA_SPACE})

	# Ensure total sectors is an integral number of sectors per
	# track or mcopy will complain. Sectors are 512 bytes, and we
	# generate images with 32 sectors per track. This calculation is
	# done in blocks, thus the mod by 16 instead of 32.
	BLOCKS=$(expr $BLOCKS + $(expr 16 - $(expr $BLOCKS % 16)))

	# mkdosfs will sometimes use FAT16 when it is not appropriate,
	# resulting in a boot failure from SYSLINUX. Use FAT32 for
	# images larger than 512MB, otherwise let mkdosfs decide.
	if [ $(expr $BLOCKS / 1024) -gt 512 ]; then
		FATSIZE="-F 32"
	fi

	if [ -z "${HDDIMG_ID}" ]; then
		mkdosfs ${FATSIZE} -n ${BOOTIMG_VOLUME_ID} -S 512 -C ${FATIMG} \
			${BLOCKS}
	else
		mkdosfs ${FATSIZE} -n ${BOOTIMG_VOLUME_ID} -S 512 -C ${FATIMG} \
		${BLOCKS} -i ${HDDIMG_ID}
	fi

	# Copy FATSOURCEDIR recursively into the image file directly
	mcopy -i ${FATIMG} -s ${FATSOURCEDIR}/* ::/
}

build_hddimg() {
	# Create an HDD image
	if [ "${NOHDD}" != "1" ] ; then
		populate ${HDDDIR}

		if [ "${PCBIOS}" = "1" ]; then
			syslinux_hddimg_populate ${HDDDIR}
		fi
		if [ "${EFI}" = "1" ]; then
			efi_hddimg_populate ${HDDDIR}
		fi

		build_fat_img ${HDDDIR} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hddimg

		if [ "${PCBIOS}" = "1" ]; then
			syslinux_hddimg_install
		fi

		chmod 644 ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hddimg

		cd ${DEPLOY_DIR_IMAGE}
		rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.hddimg
		ln -s ${IMAGE_NAME}.hddimg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.hddimg
	fi
}

python do_bootimg() {
    if d.getVar("PCBIOS", True) == "1":
        bb.build.exec_func('build_syslinux_cfg', d)
    if d.getVar("EFI", True) == "1":
        bb.build.exec_func('build_efi_cfg', d)
    bb.build.exec_func('build_hddimg', d)
    bb.build.exec_func('build_iso', d)
}

IMAGE_TYPEDEP_iso = "ext3"
IMAGE_TYPEDEP_hddimg = "ext3"
IMAGE_TYPES_MASKED += "iso hddimg"

addtask bootimg before do_build
