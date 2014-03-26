def get_imagecmds(d):
    cmds = "\n"
    old_overrides = d.getVar('OVERRIDES', 0)

    alltypes = d.getVar('IMAGE_FSTYPES', True).split()
    types = []
    ctypes = d.getVar('COMPRESSIONTYPES', True).split()
    cimages = {}

    # The elf image depends on the cpio.gz image already having
    # been created, so we add that explicit ordering here.

    if "elf" in alltypes:
        alltypes.remove("elf")
        if "cpio.gz" not in alltypes:
                alltypes.append("cpio.gz")
        alltypes.append("elf")

    # Filter out all the compressed images from alltypes
    for type in alltypes:
        basetype = None
        for ctype in ctypes:
            if type.endswith("." + ctype):
                basetype = type[:-len("." + ctype)]
                if basetype not in types:
                    types.append(basetype)
                if basetype not in cimages:
                    cimages[basetype] = []
                if ctype not in cimages[basetype]:
                    cimages[basetype].append(ctype)
                break
        if not basetype and type not in types:
            types.append(type)

    # Live and VMDK images will be processed via inheriting
    # bbclass and does not get processed here.
    # vmdk depend on live images also depend on ext3 so ensure its present
    # Note: we need to ensure ext3 is in alltypes, otherwise, subimages may
    # not contain ext3 and the .rootfs.ext3 file won't be created.
    if "vmdk" in types:
        if "ext3" not in types:
            types.append("ext3")
        if "ext3" not in alltypes:
            alltypes.append("ext3")
        types.remove("vmdk")
    if "live" in types:
        if "ext3" not in types:
            types.append("ext3")
        if "ext3" not in alltypes:
            alltypes.append("ext3")
        types.remove("live")

    if d.getVar('IMAGE_LINK_NAME', True):
        if d.getVar('RM_OLD_IMAGE', True) == "1":
            # Remove the old image
            cmds += "\trm -f `find ${DEPLOY_DIR_IMAGE} -maxdepth 1 -type l -name ${IMAGE_LINK_NAME}'.*' -exec readlink -f {} \;`"
        # Remove the symlink
        cmds += "\n\trm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.*"

    for type in types:
        ccmd = []
        subimages = []
        localdata = bb.data.createCopy(d)
        localdata.setVar('OVERRIDES', '%s:%s' % (type, old_overrides))
        bb.data.update_data(localdata)
        localdata.setVar('type', type)
        if type in cimages:
            for ctype in cimages[type]:
                ccmd.append("\t" + localdata.getVar("COMPRESS_CMD_" + ctype, True))
                subimages.append(type + "." + ctype)
        if type not in alltypes:
            ccmd.append(localdata.expand("\trm ${IMAGE_NAME}.rootfs.${type}"))
        else:
            subimages.append(type)
        localdata.setVar('ccmd', "\n".join(ccmd))
        localdata.setVar('subimages', " ".join(subimages))
        cmd = localdata.getVar("IMAGE_CMD", True)
        localdata.setVar('cmd', cmd)
        cmds += "\n" + localdata.getVar("runimagecmd", True)
    return cmds

# The default aligment of the size of the rootfs is set to 1KiB. In case
# you're using the SD card emulation of a QEMU system simulator you may
# set this value to 2048 (2MiB alignment).
IMAGE_ROOTFS_ALIGNMENT ?= "1"

runimagecmd () {
	# Image generation code for image type ${type}
	# The base_size gets calculated:
	#  - initial size determined by `du -ks` of the IMAGE_ROOTFS
	#  - then multiplied by the IMAGE_OVERHEAD_FACTOR
	#  - tested against IMAGE_ROOTFS_SIZE
	#  - round up ROOTFS_SIZE to IMAGE_ROOTFS_ALIGNMENT
	ROOTFS_SIZE=`du -ks ${IMAGE_ROOTFS} | awk '{base_size = $1 * ${IMAGE_OVERHEAD_FACTOR}; base_size = ((base_size > ${IMAGE_ROOTFS_SIZE} ? base_size : ${IMAGE_ROOTFS_SIZE}) + ${IMAGE_ROOTFS_EXTRA_SPACE}); if (base_size != int(base_size)) base_size = int(base_size + 1); base_size = base_size + ${IMAGE_ROOTFS_ALIGNMENT} - 1; base_size -= base_size % ${IMAGE_ROOTFS_ALIGNMENT}; print base_size }'`
	${cmd}
	# Now create the needed compressed versions
	cd ${DEPLOY_DIR_IMAGE}/
        ${ccmd}
        # And create the symlinks
        if [ -n "${IMAGE_LINK_NAME}" ]; then
            for type in ${subimages}; do
                if [ -e ${IMAGE_NAME}.rootfs.$type ]; then
                    ln -s ${IMAGE_NAME}.rootfs.$type ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.$type
                fi
            done
        fi
}

def imagetypes_getdepends(d):
    def adddep(depstr, deps):
        for i in (depstr or "").split():
            if i not in deps:
                deps.append(i)

    deps = []
    ctypes = d.getVar('COMPRESSIONTYPES', True).split()
    for type in (d.getVar('IMAGE_FSTYPES', True) or "").split():
        if type == "vmdk" or type == "live":
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

IMAGE_CMD_jffs2 = "mkfs.jffs2 --root=${IMAGE_ROOTFS} --faketime --output=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2 -n ${EXTRA_IMAGECMD}"
IMAGE_CMD_sum.jffs2 = "${IMAGE_CMD_jffs2} && sumtool -i ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2 \
	-o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.sum.jffs2 -n ${EXTRA_IMAGECMD}"

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
	mkfs.$fstype -F $extra_imagecmd ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.$fstype
	populate-extfs.sh ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.$fstype
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
IMAGE_CMD_tar = "cd ${IMAGE_ROOTFS} && tar -cvf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.tar ."

CPIO_TOUCH_INIT () {
	if [ ! -L ${IMAGE_ROOTFS}/init ]
	then
	touch ${IMAGE_ROOTFS}/init
	fi
}
IMAGE_CMD_cpio () {
	${CPIO_TOUCH_INIT}
	cd ${IMAGE_ROOTFS} && (find . | cpio -o -H newc >${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cpio)
}

ELF_KERNEL ?= "${STAGING_DIR_HOST}/usr/src/kernel/${KERNEL_IMAGETYPE}"
ELF_APPEND ?= "ramdisk_size=32768 root=/dev/ram0 rw console="

IMAGE_CMD_elf () {
	test -f ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.elf && rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.elf
	mkelfImage --kernel=${ELF_KERNEL} --initrd=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cpio.gz --output=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.elf --append='${ELF_APPEND}' ${EXTRA_IMAGECMD}
}

UBI_VOLNAME ?= "${MACHINE}-rootfs"

IMAGE_CMD_ubi () {
	echo \[ubifs\] > ubinize.cfg 
	echo mode=ubi >> ubinize.cfg
	echo image=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubifs >> ubinize.cfg 
	echo vol_id=0 >> ubinize.cfg 
	echo vol_type=dynamic >> ubinize.cfg 
	echo vol_name=${UBI_VOLNAME} >> ubinize.cfg 
	echo vol_flags=autoresize >> ubinize.cfg
	mkfs.ubifs -r ${IMAGE_ROOTFS} -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubifs ${MKUBIFS_ARGS} && ubinize -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubi ${UBINIZE_ARGS} ubinize.cfg
}
IMAGE_CMD_ubifs = "mkfs.ubifs -r ${IMAGE_ROOTFS} -o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ubifs ${MKUBIFS_ARGS}"

EXTRA_IMAGECMD = ""

inherit siteinfo
JFFS2_ENDIANNESS ?= "${@base_conditional('SITEINFO_ENDIANNESS', 'le', '--little-endian', '--big-endian', d)}"
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
IMAGE_DEPENDS_sum.jffs2 = "mtd-utils-native"
IMAGE_DEPENDS_cramfs = "util-linux-native"
IMAGE_DEPENDS_ext2 = "e2fsprogs-native"
IMAGE_DEPENDS_ext3 = "e2fsprogs-native"
IMAGE_DEPENDS_ext4 = "e2fsprogs-native"
IMAGE_DEPENDS_btrfs = "btrfs-tools-native"
IMAGE_DEPENDS_squashfs = "squashfs-tools-native"
IMAGE_DEPENDS_squashfs-xz = "squashfs-tools-native"
IMAGE_DEPENDS_elf = "virtual/kernel mkelfimage-native"
IMAGE_DEPENDS_ubi = "mtd-utils-native"
IMAGE_DEPENDS_ubifs = "mtd-utils-native"

# This variable is available to request which values are suitable for IMAGE_FSTYPES
IMAGE_TYPES = "jffs2 sum.jffs2 cramfs ext2 ext2.gz ext2.bz2 ext3 ext3.gz ext2.lzma btrfs live squashfs squashfs-xz ubi ubifs tar tar.gz tar.bz2 tar.xz cpio cpio.gz cpio.xz cpio.lzma vmdk elf"

COMPRESSIONTYPES = "gz bz2 lzma xz"
COMPRESS_CMD_lzma = "lzma -k -f -7 ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_CMD_gz = "gzip -f -9 -c ${IMAGE_NAME}.rootfs.${type} > ${IMAGE_NAME}.rootfs.${type}.gz"
COMPRESS_CMD_bz2 = "bzip2 -f -k ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_CMD_xz = "xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_THREADS} --check=${XZ_INTEGRITY_CHECK} ${IMAGE_NAME}.rootfs.${type} > ${IMAGE_NAME}.rootfs.${type}.xz"
COMPRESS_DEPENDS_lzma = "xz-native"
COMPRESS_DEPENDS_gz = ""
COMPRESS_DEPENDS_bz2 = ""
COMPRESS_DEPENDS_xz = "xz-native"

RUNNABLE_IMAGE_TYPES ?= "ext2 ext3"
RUNNABLE_MACHINE_PATTERNS ?= "qemu"

DEPLOYABLE_IMAGE_TYPES ?= "hddimg iso" 

# Use IMAGE_EXTENSION_xxx to map image type 'xxx' with real image file extension name(s) for Hob
IMAGE_EXTENSION_live = "hddimg iso"
