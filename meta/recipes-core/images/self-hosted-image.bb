IMAGE_INSTALL = "task-core-boot task-core-apps-console task-core-ssh-openssh task-self-hosted"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r6"

CORE_IMAGE_EXTRA_INSTALL = "\
    task-self-hosted \
    "

IMAGE_FEATURES += "x11-mini package-management"

# Ensure there's enough space to do a core-image-minimal build, with rm_work enabled
IMAGE_ROOTFS_EXTRA_SPACE = "1048576"
#IMAGE_ROOTFS_EXTRA_SPACE = "2621440"
#IMAGE_ROOTFS_EXTRA_SPACE = "20971520"
#IMAGE_ROOTFS_EXTRA_SPACE = "5242880"

# Do a quiet boot with limited console messages
APPEND += "quiet"

IMAGE_FSTYPES = "vmdk"

inherit core-image

SRCREV = "26a46938d3ea1821e7bec4fa6cc8379babad238b"
SRC_URI = "git://git.yoctoproject.org/poky;protocol=git"

fakeroot do_populate_poky_src () {
	# Because fetch2's git's unpack uses -s cloneflag, the unpacked git repo
	# will become invalid in the target.
	rm -rf ${WORKDIR}/git/.git
	rm -f ${WORKDIR}/git/.gitignore

	cp -Rp ${WORKDIR}/git ${IMAGE_ROOTFS}/home/builder/poky

	mkdir -p ${IMAGE_ROOTFS}/home/builder/poky/build/conf
	cp -Rp ${DL_DIR} ${IMAGE_ROOTFS}/home/builder/poky/build
	echo "/usr/bin" > ${IMAGE_ROOTFS}/home/builder/poky/build/pseudodone
	echo "BB_NO_NETWORK = \"1\"" > ${IMAGE_ROOTFS}/home/builder/poky/build/conf/auto.conf
	echo "INHERIT += \"rm_work\"" >> ${IMAGE_ROOTFS}/home/builder/poky/build/conf/auto.conf
        mkdir -p ${IMAGE_ROOTFS}/home/builder/pseudo
        echo "export PSEUDO_PREFIX=/usr" >> ${IMAGE_ROOTFS}/home/builder/.bashrc
        echo "export PSEUDO_LOCALSTATEDIR=/home/builder/pseudo" >> ${IMAGE_ROOTFS}/home/builder/.bashrc
        echo "export PSEUDO_LIBDIR=/usr/lib/pseudo/lib64" >> ${IMAGE_ROOTFS}/home/builder/.bashrc

        chown builder.builder ${IMAGE_ROOTFS}/home/builder/pseudo

	chown -R builder.builder  ${IMAGE_ROOTFS}/home/builder/poky
}

IMAGE_PREPROCESS_COMMAND += "do_populate_poky_src; "

python do_get_poky_src () {
    bb.build.exec_func('base_do_fetch', d)
    bb.build.exec_func('base_do_unpack', d)
}
addtask do_get_poky_src before do_rootfs
