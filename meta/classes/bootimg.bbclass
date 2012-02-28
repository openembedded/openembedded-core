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
# ${INITRD} - indicates a filesystem image to use as an initrd (optional)
# ${NOISO}  - skip building the ISO image if set to 1
# ${ROOTFS} - indicates a filesystem image to include as the root filesystem (optional)

do_bootimg[depends] += "dosfstools-native:do_populate_sysroot \
                        mtools-native:do_populate_sysroot \
                        cdrtools-native:do_populate_sysroot"

PACKAGES = " "
EXCLUDE_FROM_WORLD = "1"

HDDDIR = "${S}/hddimg"
ISODIR = "${S}/iso"

BOOTIMG_VOLUME_ID   ?= "boot"
BOOTIMG_EXTRA_SPACE ?= "512"

EFI = "${@base_contains("MACHINE_FEATURES", "efi", "1", "0", d)}"
EFI_CLASS = "${@base_contains("MACHINE_FEATURES", "efi", "grub-efi", "dummy", d)}"

# Include legacy boot if MACHINE_FEATURES includes "pcbios" or if it does not
# contain "efi". This way legacy is supported by default if neither is
# specified, maintaining the original behavior.
def pcbios(d):
	pcbios = base_contains("MACHINE_FEATURES", "pcbios", "1", "0", d)
	if pcbios == "0":
		pcbios = base_contains("MACHINE_FEATURES", "efi", "0", "1", d)
	return pcbios

def pcbios_class(d):
	if d.getVar("PCBIOS", True) == "1":
		return "syslinux"
	return "dummy"

PCBIOS = "${@pcbios(d)}"
PCBIOS_CLASS = "${@pcbios_class(d)}"

inherit ${PCBIOS_CLASS}
inherit ${EFI_CLASS}

populate() {
	DEST=$1
	install -d ${DEST}

	# Install bzImage, initrd, and rootfs.img in DEST for all loaders to use.
	install -m 0644 ${STAGING_DIR_HOST}/kernel/bzImage ${DEST}/vmlinuz

	if [ -n "${INITRD}" ] && [ -s "${INITRD}" ]; then
		install -m 0644 ${INITRD} ${DEST}/initrd
	fi

	if [ -n "${ROOTFS}" ] && [ -s "${ROOTFS}" ]; then
		install -m 0644 ${ROOTFS} ${DEST}/rootfs.img
	fi

}

build_iso() {
	# Only create an ISO if we have an INITRD and NOISO was not set
	if [ -z "${INITRD}" ] || [ ! -s "${INITRD}" ] || [ "${NOISO}" = "1" ]; then
		bbnote "ISO image will not be created."
		return
	fi

	populate ${ISODIR}

	if [ "${PCBIOS}" = "1" ]; then
		syslinux_iso_populate
	fi
	if [ "${EFI}" = "1" ]; then
		grubefi_iso_populate
	fi

	if [ "${PCBIOS}" = "1" ]; then
		mkisofs -V ${BOOTIMG_VOLUME_ID} \
		        -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.iso \
			-b ${ISO_BOOTIMG} -c ${ISO_BOOTCAT} -r \
			${MKISOFS_OPTIONS} ${ISODIR}
	else
		bbnote "EFI-only ISO images are untested, please provide feedback."
		mkisofs -V ${BOOTIMG_VOLUME_ID} \
		        -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.iso \
			-r ${ISODIR}
	fi

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.iso
	ln -s ${IMAGE_NAME}.iso ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.iso
}

build_hddimg() {
	# Create an HDD image
	if [ "${NOHDD}" != "1" ] ; then
		populate ${HDDDIR}

		if [ "${PCBIOS}" = "1" ]; then
			syslinux_hddimg_populate
		fi
		if [ "${EFI}" = "1" ]; then
			grubefi_hddimg_populate
		fi

		# Calculate the size required for the final image including the
		# data and filesystem overhead.
		# Sectors: 512 bytes
		#  Blocks: 1024 bytes

		# Determine the sector count just for the data
		SECTORS=$(expr $(du --apparent-size -ks ${HDDDIR} | cut -f 1) \* 2)

		# Account for the filesystem overhead. This includes directory
		# entries in the clusters as well as the FAT itself.
		# Assumptions:
		#   FAT32 (12 or 16 may be selected by mkdosfs, but the extra
		#   padding will be minimal on those smaller images and not
		#   worth the logic here to caclulate the smaller FAT sizes)
		#   < 16 entries per directory
		#   8.3 filenames only

		# 32 bytes per dir entry
		DIR_BYTES=$(expr $(find ${HDDDIR} | tail -n +2 | wc -l) \* 32)
		# 32 bytes for every end-of-directory dir entry
		DIR_BYTES=$(expr $DIR_BYTES + $(expr $(find ${HDDDIR} -type d | tail -n +2 | wc -l) \* 32))
		# 4 bytes per FAT entry per sector of data
		FAT_BYTES=$(expr $SECTORS \* 4)
		# 4 bytes per FAT entry per end-of-cluster list
		FAT_BYTES=$(expr $FAT_BYTES + $(expr $(find ${HDDDIR} -type d | tail -n +2 | wc -l) \* 4))

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

		IMG=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hddimg
		mkdosfs -n ${BOOTIMG_VOLUME_ID} -S 512 -C ${IMG} ${BLOCKS}
		# Copy HDDDIR recursively into the image file directly
		mcopy -i ${IMG} -s ${HDDDIR}/* ::/

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
		bb.build.exec_func('build_grub_cfg', d)
	bb.build.exec_func('build_hddimg', d)
	bb.build.exec_func('build_iso', d)
}

addtask bootimg before do_build
do_bootimg[nostamp] = "1"
