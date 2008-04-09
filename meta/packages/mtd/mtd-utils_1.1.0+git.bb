DESCRIPTION = "Tools for managing memory technology devices."
SECTION = "base"
DEPENDS = "zlib lzo"
HOMEPAGE = "http://www.linux-mtd.infradead.org/"
LICENSE = "GPLv2"

SRC_URI = "git://git.infradead.org/mtd-utils.git;protocol=git;tag=e6088d987c545d60a86e1f44836ab8ba072fffd9 \
           file://add-exclusion-to-mkfs-jffs2-git.patch;patch=1 \
	   file://remove-ubi.patch;patch=1 \
	   file://fix-ignoreerrors-git.patch;patch=1"

S = "${WORKDIR}/git/"

EXTRA_OEMAKE = "'CC=${CC}' 'CFLAGS=${CFLAGS} -I${S}/include -DWITHOUT_XATTR'"

do_stage () {
	install -d ${STAGING_INCDIR}/mtd
	for f in ${S}/include/mtd/*.h; do
		install -m 0644 $f ${STAGING_INCDIR}/mtd/
	done
	for binary in ${mtd_utils}; do
		install -m 0755 $binary ${STAGING_BINDIR}
	done
}

mtd_utils = "ftl_format flash_erase flash_eraseall nanddump doc_loadbios \
             ftl_check mkfs.jffs2 flash_lock flash_unlock flash_info mtd_debug \
             flashcp nandwrite jffs2dump sumtool"

do_install () {
	oe_runmake install DESTDIR=${D}
}

PARALLEL_MAKE = ""
