DESCRIPTION = "Tools for managing memory technology devices."
SECTION = "base"
DEPENDS = "zlib lzo e2fsprogs util-linux"
HOMEPAGE = "http://www.linux-mtd.infradead.org/"
LICENSE = "GPLv2"

SRC_URI = "git://git.infradead.org/mtd-utils.git;protocol=git;tag=v${PV} \
		file://add-exclusion-to-mkfs-jffs2-git-2.patch;patch=1 \
		file://fix-ignoreerrors-git.patch;patch=1 \
		file://add-oobsize-64-and-writesize-4096-as-normal-nand.patch;patch=1"

S = "${WORKDIR}/git/"

EXTRA_OEMAKE = "'CC=${CC}' 'CFLAGS=${CFLAGS} -I${S}/include -DWITHOUT_XATTR' 'BUILDDIR=${S}'"

do_install () {
	oe_runmake install DESTDIR=${D} SBINDIR=${sbindir} MANDIR=${mandir} INCLUDEDIR=${includedir}
	install -d ${D}${includedir}/mtd/
	for f in ${S}/include/mtd/*.h; do
		install -m 0644 $f ${D}${includedir}/mtd/
	done
}

PARALLEL_MAKE = ""

BBCLASSEXTEND = "native"
