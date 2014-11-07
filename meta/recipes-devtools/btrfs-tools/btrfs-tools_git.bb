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
DEPENDS = "util-linux attr e2fsprogs lzo acl"

SRCREV = "44cdb62d3478c834f41c87ea79d261b9c8982dce"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/kdave/btrfs-progs.git"
S = "${WORKDIR}/git"

PV = "3.14.2+git${SRCPV}"

EXTRA_OEMAKE += "DISABLE_DOCUMENTATION=1"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
}

BBCLASSEXTEND = "native"
