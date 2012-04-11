# boot-directdisk.bbclass
# (loosly based off bootimg.bbclass Copyright (C) 2004, Advanced Micro Devices, Inc.)
#
# Create an image which can be placed directly onto a harddisk using dd and then
# booted.
#
# This uses syslinux. extlinux would have been nice but required the ext2/3 
# partition to be mounted. grub requires to run itself as part of the install 
# process.
#
# The end result is a 512 boot sector populated with an MBR and partition table
# followed by an msdos fat16 partition containing syslinux and a linux kernel
# completed by the ext2/3 rootfs.
#
# We have to push the msdos parition table size > 16MB so fat 16 is used as parted
# won't touch fat12 partitions.

# External variables needed

# ${ROOTFS} - the rootfs image to incorporate

do_bootdirectdisk[depends] += "dosfstools-native:do_populate_sysroot \
                               syslinux:do_populate_sysroot \
                               syslinux-native:do_populate_sysroot \
                               parted-native:do_populate_sysroot \
                               mtools-native:do_populate_sysroot "

PACKAGES = " "
EXCLUDE_FROM_WORLD = "1"

HDDDIR = "${S}/hdd/boot"
HDDIMG = "${S}/hdd.image"

BOOTDD_VOLUME_ID   ?= "boot"
BOOTDD_EXTRA_SPACE ?= "16384"

# Get the build_syslinux_cfg() function from the syslinux class

AUTO_SYSLINUXCFG = "1"
SYSLINUX_ROOT ?= "root=/dev/sda2"
SYSLINUX_TIMEOUT ?= "10"

inherit syslinux
		
build_boot_dd() {
	IMAGE=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hdddirect

	install -d ${HDDDIR}
	install -m 0644 ${STAGING_DIR_HOST}/kernel/bzImage ${HDDDIR}/vmlinuz
	install -m 0644 ${S}/syslinux.cfg ${HDDDIR}/syslinux.cfg
	install -m 444 ${STAGING_LIBDIR}/syslinux/ldlinux.sys ${HDDDIR}/ldlinux.sys

	BLOCKS=`du -bks ${HDDDIR} | cut -f 1`
	BLOCKS=`expr $BLOCKS + ${BOOTDD_EXTRA_SPACE}`

	# Ensure total sectors is an integral number of sectors per
	# track or mcopy will complain. Sectors are 512 bytes, and we
	# generate images with 32 sectors per track. This calculation is
	# done in blocks, thus the mod by 16 instead of 32.
	BLOCKS=$(expr $BLOCKS + $(expr 16 - $(expr $BLOCKS % 16)))

	mkdosfs -n ${BOOTDD_VOLUME_ID} -S 512 -C ${HDDIMG} $BLOCKS 
	mcopy -i ${HDDIMG} -s ${HDDDIR}/* ::/

	syslinux ${HDDIMG}
	chmod 644 ${HDDIMG}

	ROOTFSBLOCKS=`du -Lbks ${ROOTFS} | cut -f 1`
	TOTALSIZE=`expr $BLOCKS + $ROOTFSBLOCKS`
	END1=`expr $BLOCKS \* 1024`
	END2=`expr $END1 + 512`
	END3=`expr \( $ROOTFSBLOCKS \* 1024 \) + $END1`

	echo $ROOTFSBLOCKS $TOTALSIZE $END1 $END2 $END3
	rm -rf $IMAGE
	dd if=/dev/zero of=$IMAGE bs=1024 seek=$TOTALSIZE count=1

	parted $IMAGE mklabel msdos
	parted $IMAGE mkpart primary fat16 0 ${END1}B
	parted $IMAGE unit B mkpart primary ext2 ${END2}B ${END3}B
	parted $IMAGE set 1 boot on 
	parted $IMAGE print

	OFFSET=`expr $END2 / 512`
	dd if=${STAGING_LIBDIR}/syslinux/mbr.bin of=$IMAGE conv=notrunc
	dd if=${HDDIMG} of=$IMAGE conv=notrunc seek=1 bs=512
	dd if=${ROOTFS} of=$IMAGE conv=notrunc seek=$OFFSET bs=512

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.hdddirect
	ln -s ${IMAGE_NAME}.hdddirect ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.hdddirect
} 

python do_bootdirectdisk() {
	bb.build.exec_func('build_syslinux_cfg', d)
	bb.build.exec_func('build_boot_dd', d)
}

addtask bootdirectdisk before do_build
do_bootdirectdisk[nostamp] = "1"
