def get_imagecmds(d):
    cmds = "\n"
    old_overrides = d.getVar('OVERRIDES', 0)

    alltypes = d.getVar('IMAGE_FSTYPES', True).split()
    types = []
    ctypes = d.getVar('COMPRESSIONTYPES', True).split()
    cimages = {}

    # Filter out all the compressed images from types
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
        cmds += "	rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.*"

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
	#  - then rounded up to IMAGE_ROOTFS_ALIGNMENT
	#  - finally tested against IMAGE_ROOTFS_SIZE
	ROOTFS_SIZE=`du -ks ${IMAGE_ROOTFS}|awk '{base_size = $1 * ${IMAGE_OVERHEAD_FACTOR} + ${IMAGE_ROOTFS_ALIGNMENT} - 1; base_size -= base_size % ${IMAGE_ROOTFS_ALIGNMENT}; print ((base_size > ${IMAGE_ROOTFS_SIZE} ? base_size : ${IMAGE_ROOTFS_SIZE}) + ${IMAGE_ROOTFS_EXTRA_SPACE}) }'`
	${cmd}
	# Now create the needed compressed versions
	cd ${DEPLOY_DIR_IMAGE}/
        ${ccmd}
        # And create the symlinks
        if [ -n "${IMAGE_LINK_NAME}" ]; then
            for type in ${subimages}; do
                ln -s ${IMAGE_NAME}.rootfs.$type ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.$type
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


XZ_COMPRESSION_LEVEL ?= "-e -9"
XZ_INTEGRITY_CHECK ?= "crc32"

IMAGE_CMD_jffs2 = "mkfs.jffs2 --root=${IMAGE_ROOTFS} --faketime --output=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2 -n ${EXTRA_IMAGECMD}"
IMAGE_CMD_sum.jffs2 = "${IMAGE_CMD_jffs2} && sumtool -i ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2 \
	-o ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.sum.jffs2 -n ${EXTRA_IMAGECMD}"

IMAGE_CMD_cramfs = "mkcramfs ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.cramfs ${EXTRA_IMAGECMD}"

IMAGE_CMD_ext2 () {
	rm -rf ${DEPLOY_DIR_IMAGE}/tmp.gz-${PN} && mkdir ${DEPLOY_DIR_IMAGE}/tmp.gz-${PN}
	genext2fs -b $ROOTFS_SIZE -d ${IMAGE_ROOTFS} ${EXTRA_IMAGECMD} ${DEPLOY_DIR_IMAGE}/tmp.gz-${PN}/${IMAGE_NAME}.rootfs.ext2
	mv ${DEPLOY_DIR_IMAGE}/tmp.gz-${PN}/${IMAGE_NAME}.rootfs.ext2 ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext2
	rmdir ${DEPLOY_DIR_IMAGE}/tmp.gz-${PN}
}

IMAGE_CMD_ext3 () {
	genext2fs -b $ROOTFS_SIZE -d ${IMAGE_ROOTFS} ${EXTRA_IMAGECMD} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3
	tune2fs -j ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3
}

oe_mkext4fs () {
	genext2fs -b $ROOTFS_SIZE -d ${IMAGE_ROOTFS} ${EXTRA_IMAGECMD} $1
	tune2fs -O extents,uninit_bg,dir_index,has_journal $1
	e2fsck -yfDC0 $1 || chk=$?
	case $chk in
	0|1|2)
	    ;;
	*)
	    return $chk
	    ;;
	esac
}

IMAGE_CMD_ext4 () {
	oe_mkext4fs ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext4
}

IMAGE_CMD_btrfs () {
	mkfs.btrfs -b `expr ${ROOTFS_SIZE} \* 1024` ${EXTRA_IMAGECMD} -r ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.btrfs
}

IMAGE_CMD_squashfs = "mksquashfs ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.squashfs ${EXTRA_IMAGECMD} -noappend"
IMAGE_CMD_squashfs-lzma = "mksquashfs-lzma ${IMAGE_ROOTFS} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.squashfs-lzma ${EXTRA_IMAGECMD} -noappend"
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
EXTRA_IMAGECMD_jffs2 ?= "--pad --little-endian --eraseblock=0x40000"
# Change these if you want default genext2fs behavior (i.e. create minimal inode number)
EXTRA_IMAGECMD_ext2 ?= "-i 8192"
EXTRA_IMAGECMD_ext3 ?= "-i 8192"
EXTRA_IMAGECMD_ext4 ?= "-i 8192"
EXTRA_IMAGECMD_btrfs ?= ""

IMAGE_DEPENDS = ""
IMAGE_DEPENDS_jffs2 = "mtd-utils-native"
IMAGE_DEPENDS_sum.jffs2 = "mtd-utils-native"
IMAGE_DEPENDS_cramfs = "cramfs-native"
IMAGE_DEPENDS_ext2 = "genext2fs-native"
IMAGE_DEPENDS_ext3 = "genext2fs-native e2fsprogs-native"
IMAGE_DEPENDS_ext4 = "genext2fs-native e2fsprogs-native"
IMAGE_DEPENDS_btrfs = "btrfs-tools-native"
IMAGE_DEPENDS_squashfs = "squashfs-tools-native"
IMAGE_DEPENDS_squashfs-lzma = "squashfs-lzma-tools-native"
IMAGE_DEPENDS_ubi = "mtd-utils-native"
IMAGE_DEPENDS_ubifs = "mtd-utils-native"

# This variable is available to request which values are suitable for IMAGE_FSTYPES
IMAGE_TYPES = "jffs2 sum.jffs2 cramfs ext2 ext2.gz ext2.bz2 ext3 ext3.gz ext2.lzma btrfs live squashfs squashfs-lzma ubi tar tar.gz tar.bz2 tar.xz cpio cpio.gz cpio.xz cpio.lzma vmdk"

COMPRESSIONTYPES = "gz bz2 lzma xz"
COMPRESS_CMD_lzma = "lzma -k -f -7 ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_CMD_gz = "gzip -f -9 -c ${IMAGE_NAME}.rootfs.${type} > ${IMAGE_NAME}.rootfs.${type}.gz"
COMPRESS_CMD_bz2 = "bzip2 -f -k ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_CMD_xz = "xz -f -k -c ${XZ_COMPRESSION_LEVEL} --check=${XZ_INTEGRITY_CHECK} ${IMAGE_NAME}.rootfs.${type}"
COMPRESS_DEPENDS_lzma = "xz-native"
COMPRESS_DEPENDS_gz = ""
COMPRESS_DEPENDS_bz2 = ""
COMPRESS_DEPENDS_xz = "xz-native"

RUNNABLE_IMAGE_TYPES ?= "ext2 ext3"
RUNNABLE_MACHINE_PATTERNS ?= "qemu"

DEPLOYABLE_IMAGE_TYPES ?= "hddimg iso" 
