DESCRIPTION = "Tools for managing memory technology devices."
SECTION = "base"
DEPENDS = "zlib lzo"
HOMEPAGE = "http://www.linux-mtd.infradead.org/"
LICENSE = "GPLv2"
PR = "r2"

SRC_URI = "git://git.infradead.org/mtd-utils.git;protocol=git;tag=b995f89a81589be8d8a41c374a6df109d0ee12b3 \
           file://add-exclusion-to-mkfs-jffs2-git.patch \
	   file://remove-ubi.patch \
	   file://fix-ignoreerrors-git.patch"

S = "${WORKDIR}/git/"

EXTRA_OEMAKE = "'CC=${CC}' 'CFLAGS=${CFLAGS} -I${S}/include -DWITHOUT_XATTR'"

do_install () {
	oe_runmake install DESTDIR=${D} SBINDIR=${sbindir} MANDIR=${mandir} INCLUDEDIR=${includedir}
	install -d ${D}${includedir}/mtd/
	for f in ${S}/include/mtd/*.h; do
		install -m 0644 $f ${D}${includedir}/mtd/
	done

}

PARALLEL_MAKE = ""

BBCLASSEXTEND = "native"
