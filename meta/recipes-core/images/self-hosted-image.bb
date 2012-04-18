DESCRIPTION = "Version 1.0-r6 of the self-hosted image."
IMAGE_INSTALL = "task-core-boot task-core-apps-console task-core-ssh-openssh task-self-hosted"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r15"

IMAGE_FEATURES += "x11-mini package-management"

# Ensure there's enough space to do a core-image-sato build, with rm_work enabled
IMAGE_ROOTFS_EXTRA_SPACE = "41943040"

# Do a quiet boot with limited console messages
APPEND += "quiet"

IMAGE_FSTYPES = "vmdk"

inherit core-image

SRCREV = "3ab5d73f0c49df9fefa2a46031d33436bbd7d7d8"
SRC_URI = "git://git.yoctoproject.org/poky;protocol=git"

IMAGE_CMD_ext3_append () {
	# We don't need to reserve much space for root, 0.5% is more than enough
	tune2fs -m 0.5 ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3
}

fakeroot do_populate_poky_src () {
	# Because fetch2's git's unpack uses -s cloneflag, the unpacked git repo
	# will become invalid in the target.
	rm -rf ${WORKDIR}/git/.git
	rm -f ${WORKDIR}/git/.gitignore

	cp -Rp ${WORKDIR}/git ${IMAGE_ROOTFS}/home/builder/poky

	mkdir -p ${IMAGE_ROOTFS}/home/builder/poky/build/conf
	mkdir -p ${IMAGE_ROOTFS}/home/builder/poky/build/downloads
	cp -RpL ${DL_DIR}/* ${IMAGE_ROOTFS}/home/builder/poky/build/downloads/

	# Remove the git2_* tarballs -- this is ok since we still have the git2/.
	rm -rf ${IMAGE_ROOTFS}/home/builder/poky/build/downloads/git2_*

	echo "/usr/bin" > ${IMAGE_ROOTFS}/home/builder/poky/build/pseudodone
	echo "INHERIT += \"rm_work\"" >> ${IMAGE_ROOTFS}/home/builder/poky/build/conf/auto.conf
	mkdir -p ${IMAGE_ROOTFS}/home/builder/pseudo
	echo "export PSEUDO_PREFIX=/usr" >> ${IMAGE_ROOTFS}/home/builder/.bashrc
	echo "export PSEUDO_LOCALSTATEDIR=/home/builder/pseudo" >> ${IMAGE_ROOTFS}/home/builder/.bashrc
	echo "export PSEUDO_LIBDIR=/usr/lib/pseudo/lib64" >> ${IMAGE_ROOTFS}/home/builder/.bashrc

	chown builder.builder ${IMAGE_ROOTFS}/home/builder/pseudo

	chown -R builder.builder  ${IMAGE_ROOTFS}/home/builder/poky

	# Allow builder to use sudo to setup tap/tun
	echo "builder ALL=(ALL) NOPASSWD: ALL" >> ${IMAGE_ROOTFS}/etc/sudoers

	# Use Clearlooks GTK+ theme
	mkdir -p ${IMAGE_ROOTFS}/etc/gtk-2.0
	echo 'gtk-theme-name = "Clearlooks"' > ${IMAGE_ROOTFS}/etc/gtk-2.0/gtkrc
}

IMAGE_PREPROCESS_COMMAND += "do_populate_poky_src; "

python do_get_poky_src () {
    bb.build.exec_func('base_do_fetch', d)
    bb.build.exec_func('base_do_unpack', d)
}
addtask do_get_poky_src before do_rootfs
