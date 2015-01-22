
# The default aligment of the size of the rootfs is set to 1KiB. In case
# you're using the SD card emulation of a QEMU system simulator you may
# set this value to 2048 (2MiB alignment).
IMAGE_ROOTFS_ALIGNMENT ?= "1"

def imagetypes_getdepends(d):
    def adddep(depstr, deps):
        for i in (depstr or "").split():
            if i not in deps:
                deps.append(i)

    deps = []
    ctypes = d.getVar('COMPRESSIONTYPES', True).split()
    for type in (d.getVar('IMAGE_FSTYPES', True) or "").split():
        if type == "vmdk" or type == "live" or type == "iso" or type == "hddimg":
            type = "ext3"
        basetype = type
        for ctype in ctypes:
            if type.endswith("." + ctype):
                basetype = type[:-len("." + ctype)]
                adddep(d.getVar("COMPRESS_DEPENDS_%s" % ctype, True), deps)
                break
        adddep(d.getVar('IMAGE_DEPENDS_%s' % basetype, True) , deps)

    depstr = ""
    for dep in deps:
        depstr += " " + dep + ":do_populate_sysroot"
    return depstr


XZ_COMPRESSION_LEVEL ?= "-e -6"
XZ_INTEGRITY_CHECK ?= "crc32"
XZ_THREADS ?= "-T 0"

JFFS2_SUM_EXTRA_ARGS ?= ""
IMAGE_CMD_jffs2 = "mkfs.jffs2 --root=${IMAGE_ROOTFS} --faketime --output=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2 ${EXTRA_IMAGECMD}"

IMAGE_CMD_cramfs = "mkfs.cramfs ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cramfs ${EXTRA_IMAGECMD}"

oe_mkext234fs () {
	fstype=$1
	extra_imagecmd=""

	if [ $# -gt 1 ]; then
		shift
		extra_imagecmd=$@
	fi

	# Create a sparse image block
	dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.$fstype seek=$ROOTFS_SIZE count=0 bs=1k
	mkfs.$fstype -F $extra_imagecmd ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.$fstype -d ${IMAGE_ROOTFS}
}

IMAGE_CMD_ext2 = "oe_mkext234fs ext2 ${EXTRA_IMAGECMD}"
IMAGE_CMD_ext3 = "oe_mkext234fs ext3 ${EXTRA_IMAGECMD}"
IMAGE_CMD_ext4 = "oe_mkext234fs ext4 ${EXTRA_IMAGECMD}"

IMAGE_CMD_btrfs () {
	touch ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.btrfs
	mkfs.btrfs -b `expr ${ROOTFS_SIZE} \* 1024` ${EXTRA_IMAGECMD} -r ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.btrfs
}

IMAGE_CMD_squashfs = "mksquashfs ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.squashfs ${EXTRA_IMAGECMD} -noappend"
IMAGE_CMD_squashfs-xz = "mksquashfs ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.squashfs-xz ${EXTRA_IMAGECMD} -noappend -comp xz"
IMAGE_CMD_squashfs-lzo = "mksquashfs ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.squashfs-lzo ${EXTRA_IMAGECMD} -noappend -comp lzo"
IMAGE_CMD_tar = "tar -cvf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.tar -C ${IMAGE_ROOTFS} ."

do_rootfs[cleandirs] += "${WORKDIR}/cpio_append"
IMAGE_CMD_cpio () {
	(cd ${IMAGE_ROOTFS} && find . | cpio -o -H newc >${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cpio)
	if [ ! -L ${IMAGE_ROOTFS}/init -a ! -e ${IMAGE_ROOTFS}/init ]; then
		if [ -L ${IMAGE_ROOTFS}/sbin/init -o -e ${IMAGE_ROOTFS}/sbin/init ]; then
			ln -sf /sbin/init ${WORKDIR}/cpio_append/init
		else
			touch ${WORKDIR}/cpio_append/init
		fi
		(cd  ${WORKDIR}/cpio_append && echo ./init | cpio -oA -H newc -F ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cpio)
	fi
}

ELF_KERNEL ?= "${STAGING_DIR_HOST}/usr/src/kernel/${KERNEL_IMAGETYPE}"
ELF_APPEND ?= "ramdisk_size=32768 root=/dev/ram0 rw console="

IMAGE_CMD_elf () {
	test -f ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.elf && rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.elf
	mkelfImage --kernel=${ELF_KERNEL} --initrd=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cpio.gz --output=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.elf --append='${ELF_APPEND}' ${EXTRA_IMAGECMD}
}
IMAGE_TYPEDEP_elf = "cpio.gz"

UBI_VOLNAME ?= "${MACHINE}-rootfs"

IMAGE_CMD_ubi () {
	echo \[ubifs\] > ubinize.cfg 
	echo mode=ubi >> ubinize.cfg
	echo image=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubifs >> ubinize.cfg 
	echo vol_id=0 >> ubinize.cfg 
	echo vol_type=dynamic >> ubinize.cfg 
	echo vol_name=${UBI_VOLNAME} >> ubinize.cfg 
	echo vol_flags=autoresize >> ubinize.cfg
	mkfs.ubifs -r ${IMAGE_ROOTFS} -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubifs ${MKUBIFS_ARGS}
	ubinize -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubi ${UBINIZE_ARGS} ubinize.cfg
}
IMAGE_TYPEDEP_ubi = "ubifs"

IMAGE_CMD_ubifs = "mkfs.ubifs -r ${IMAGE_ROOTFS} -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubifs ${MKUBIFS_ARGS}"

EXTRA_IMAGECMD = ""

inherit siteinfo
JFFS2_ENDIANNESS ?= "${@base_conditional('SITEINFO_ENDIANNESS', 'le', '-l', '-b', d)}"
JFFS2_ERASEBLOCK ?= "0x40000"
EXTRA_IMAGECMD_jffs2 ?= "--pad ${JFFS2_ENDIANNESS} --eraseblock=${JFFS2_ERASEBLOCK} --no-cleanmarkers"

# Change these if you want default mkfs behavior (i.e. create minimal inode number)
EXTRA_IMAGECMD_ext2 ?= "-i 4096"
EXTRA_IMAGECMD_ext3 ?= "-i 4096"
EXTRA_IMAGECMD_ext4 ?= "-i 4096"
EXTRA_IMAGECMD_btrfs ?= ""
EXTRA_IMAGECMD_elf ?= ""

IMAGE_DEPENDS = ""
IMAGE_DEPENDS_jffs2 = "mtd-utils-native"
IMAGE_DEPENDS_cramfs = "util-linux-native"
IMAGE_DEPENDS_ext2 = "e2fsprogs-native"
IMAGE_DEPENDS_ext3 = "e2fsprogs-native"
IMAGE_DEPENDS_ext4 = "e2fsprogs-native"
IMAGE_DEPENDS_btrfs = "btrfs-tools-native"
IMAGE_DEPENDS_squashfs = "squashfs-tools-native"
IMAGE_DEPENDS_squashfs-xz = "squashfs-tools-native"
IMAGE_DEPENDS_squashfs-lzo = "squashfs-tools-native"
IMAGE_DEPENDS_elf = "virtual/kernel mkelfimage-native"
IMAGE_DEPENDS_ubi = "mtd-utils-native"
IMAGE_DEPENDS_ubifs = "mtd-utils-native"

# This variable is available to request which values are suitable for IMAGE_FSTYPES
IMAGE_TYPES = "jffs2 jffs2.sum cramfs ext2 ext2.gz ext2.bz2 ext3 ext3.gz ext2.lzma btrfs iso hddimg squashfs squashfs-xz squashfs-lzo ubi ubifs tar tar.gz tar.bz2 tar.xz tar.lz4 cpio cpio.gz cpio.xz cpio.lzma cpio.lz4 vmdk elf"

COMPRESSIONTYPES = "gz bz2 lzma xz lz4 sum"
COMPRESS_CMD_lzma = "lzma -k -f -7 ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_CMD_gz = "gzip -f -9 -c ${IMAGE_NAME}.rootfs.${type} > ${IMAGE_NAME}.rootfs.${type}.gz"
COMPRESS_CMD_bz2 = "bzip2 -f -k ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_CMD_xz = "xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_THREADS} --check=${XZ_INTEGRITY_CHECK} ${IMAGE_NAME}.rootfs.${type} > ${IMAGE_NAME}.rootfs.${type}.xz"
COMPRESS_CMD_lz4 = "lz4c -9 -c ${IMAGE_NAME}.rootfs.${type} > ${IMAGE_NAME}.rootfs.${type}.lz4"
COMPRESS_CMD_sum = "sumtool -i ${IMAGE_NAME}.rootfs.${type} -o ${IMAGE_NAME}.rootfs.${type}.sum ${JFFS2_SUM_EXTRA_ARGS}"
COMPRESS_DEPENDS_lzma = "xz-native"
COMPRESS_DEPENDS_gz = ""
COMPRESS_DEPENDS_bz2 = ""
COMPRESS_DEPENDS_xz = "xz-native"
COMPRESS_DEPENDS_lz4 = "lz4-native"
COMPRESS_DEPENDS_sum = "mtd-utils-native"

RUNNABLE_IMAGE_TYPES ?= "ext2 ext3"
RUNNABLE_MACHINE_PATTERNS ?= "qemu"

DEPLOYABLE_IMAGE_TYPES ?= "hddimg iso" 

# Use IMAGE_EXTENSION_xxx to map image type 'xxx' with real image file extension name(s) for Hob
IMAGE_EXTENSION_live = "hddimg iso"

# The IMAGE_TYPES_MASKED variable will be used to mask out from the IMAGE_FSTYPES,
# images that will not be built at do_rootfs time: vmdk, hddimg, iso, etc.
IMAGE_TYPES_MASKED ?= ""
