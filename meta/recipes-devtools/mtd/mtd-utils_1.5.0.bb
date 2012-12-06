DESCRIPTION = "Tools for managing memory technology devices."
SECTION = "base"
DEPENDS = "zlib lzo e2fsprogs util-linux"
HOMEPAGE = "http://www.linux-mtd.infradead.org/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://include/common.h;beginline=1;endline=17;md5=ba05b07912a44ea2bf81ce409380049c"

SRCREV = "ca39eb1d98e736109c64ff9c1aa2a6ecca222d8f"
SRC_URI = "git://git.infradead.org/mtd-utils.git;protocol=git \
		file://add-exclusion-to-mkfs-jffs2-git-2.patch \
        file://mtd-utils-fix-corrupt-cleanmarker-with-flash_erase--j-command.patch "

S = "${WORKDIR}/git/"

PR = "r2"

EXTRA_OEMAKE = "'CC=${CC}' 'RANLIB=${RANLIB}' 'AR=${AR}' 'CFLAGS=${CFLAGS} -I${S}/include -DWITHOUT_XATTR' 'BUILDDIR=${S}'"

do_install () {
	oe_runmake install DESTDIR=${D} SBINDIR=${sbindir} MANDIR=${mandir} INCLUDEDIR=${includedir}
}

PARALLEL_MAKE = ""

BBCLASSEXTEND = "native"
