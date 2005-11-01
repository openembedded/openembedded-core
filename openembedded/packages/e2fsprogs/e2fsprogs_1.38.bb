DESCRIPTION = "EXT2 Filesystem Utilities"
HOMEPAGE = "http://e2fsprogs.sourceforge.net"
LICENSE = "GPL"
SECTION = "base"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/e2fsprogs/e2fsprogs-${PV}.tar.gz"
S = "${WORKDIR}/e2fsprogs-${PV}"

inherit autotools

EXTRA_OECONF = "--enable-dynamic-e2fsck --sbindir=${base_sbindir}"

do_compile_prepend () {
	find ./ -print|xargs chmod u=rwX
	( cd util; ${BUILD_CC} subst.c -o subst )
}

ext2fsheaders = "ext2_ext_attr.h bitops.h ext2_err.h \
		 ext2_types.h ext2_fs.h ext2_io.h \
		 ext2fs.h"
e2pheaders = "e2p.h"

do_stage () {
	oe_libinstall -a -C lib libe2p ${STAGING_LIBDIR}/
	oe_libinstall -a -C lib libext2fs ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/e2p
	for h in ${e2pheaders}; do
		install -m 0644 lib/e2p/$h ${STAGING_INCDIR}/e2p/ || die "failed to install $h"
	done
	install -d ${STAGING_INCDIR}/ext2fs
	for h in ${ext2fsheaders}; do
		install -m 0644 lib/ext2fs/$h ${STAGING_INCDIR}/ext2fs/ || die "failed to install $h"
	done
}

pkg_postinst_e2fsprogs-e2fsck() {
	ln -s ${base_sbindir}/e2fsck ${base_sbindir}/fsck.ext2
	ln -s ${base_sbindir}/e2fsck ${base_sbindir}/fsck.ext3
}

pkg_postinst_e2fsprogs-mke2fs() {
	ln -s ${base_sbindir}/mke2fs ${base_sbindir}/mkfs.ext2
	ln -s ${base_sbindir}/mke2fs ${base_sbindir}/mkfs.ext3
}

PACKAGES_prepend = "e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-fsck "
FILES_e2fsprogs-fsck = "${base_sbindir}/fsck"
FILES_e2fsprogs-e2fsck = "${base_sbindir}/e2fsck"
FILES_e2fsprogs-mke2fs = "${base_sbindir}/mke2fs"
