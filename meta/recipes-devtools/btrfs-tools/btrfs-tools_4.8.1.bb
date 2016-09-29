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
RDEPENDS_${PN} = "libgcc"

SRCREV = "d8d0a60406b14b140e83a8c0d5eaedb5dca44092"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/kdave/btrfs-progs.git \
"

inherit autotools-brokensep pkgconfig manpages

PACKAGECONFIG[manpages] = "--enable-documentation, --disable-documentation, asciidoc-native xmlto-native"
EXTRA_OECONF_append_libc-musl = " --disable-backtrace "


do_configure_prepend() {
      sh autogen.sh
}

S = "${WORKDIR}/git"

BBCLASSEXTEND = "native"
