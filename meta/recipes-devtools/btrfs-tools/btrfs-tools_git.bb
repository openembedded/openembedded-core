SUMMARY = "Checksumming Copy on Write Filesystem utilities"
DESCRIPTION = "Btrfs is a new copy on write filesystem for Linux aimed at \
implementing advanced features while focusing on fault tolerance, repair and \
easy administration. \
This package contains utilities (mkfs, fsck, btrfsctl) used to work with \
btrfs and an utility (btrfs-convert) to make a btrfs filesystem from an ext3."

HOMEPAGE = "https://btrfs.wiki.kernel.org"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=fcb02dc552a041dee27e4b85c7396067"
SECTION = "base"
DEPENDS = "util-linux"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/mason/btrfs-progs-unstable.git;protocol=git;tag=1b444cd2e6ab8dcafdd47dbaeaae369dd1517c17;branch=master"

S = "${WORKDIR}/git"

PR = "r2"

SRC_URI += "file://upstream-tmp/0001-Btrfs-progs-add-a-btrfs-select-super-command-to-over.patch \
	    file://upstream-tmp/0002-Btrfs-progs-use-safe-string-manipulation-functions.patch \
	    file://upstream-tmp/0003-Btrfs-progs-utils-Informative-errors.patch \
	    file://upstream-tmp/0004-update-man-page-to-new-defragment-command-interface.patch \
	    file://upstream-tmp/0005-Improve-error-handling-in-the-btrfs-command.patch \
	    file://upstream-tmp/0006-Btrfs-progs-update-super-fields-for-space-cache.patch \
	    file://upstream-tmp/0007-Btrfs-progs-add-support-for-mixed-data-metadata-bloc.patch \
	    file://upstream-tmp/0008-Update-for-lzo-support.patch \
	    file://upstream-tmp/0009-Update-clean-up-btrfs-help-and-man-page-V2.patch \
	    file://upstream-tmp/0010-Deprecate-btrfsctl-btrfs-show-btrfs-vol.patch \
	    file://upstream-tmp/0011-Add-the-btrfs-filesystem-label-command.patch \
	    file://upstream-tmp/0012-Btrfs-progs-Update-man-page-for-mixed-data-metadata-.patch \
	    file://upstream-tmp/0013-btrfs-progs-Add-new-feature-to-mkfs.btrfs-to-make-fi.patch \
	    file://upstream-tmp/0014-btrfs-progs-fix-wrong-extent-buffer-size-when-readin.patch \
	    file://upstream-tmp/0015-btrfs-progs-add-discard-support-to-mkfs.patch \
	    file://upstream-for-dragonn/0001-Fill-missing-devices-so-degraded-filesystems-can-be-.patch \
	    file://upstream-for-dragonn/0002-Check-for-RAID10-in-set_avail_alloc_bits.patch \
	    file://upstream-for-dragonn/0003-Print-the-root-generation-in-btrfs-debug-tree.patch \
	    file://upstream-for-dragonn/0004-Allow-partial-FS-opens-for-btrfsck-scanning.patch \
	    file://mkfs-xin-fixes.patch \
	    file://debian/01-labels.patch \
	    file://debian/02-ftbfs.patch \
	    file://fix_use_of_gcc.patch \
	    file://weak-defaults.patch \
		"

SRC_URI[md5sum] = "78b1700d318de8518abfaab71f99a885"
SRC_URI[sha256sum] = "1285774e0cb72984fac158dd046c8d405324754febd30320cd31e459253e4b65"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
}

BBCLASSEXTEND = "native"
