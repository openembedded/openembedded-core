SUMMARY = "Disk partition editing/resizing utility"
HOMEPAGE = "http://www.gnu.org/software/parted/parted.html"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2f31b266d3440dd7ee50f92cf67d8e6c"
SECTION = "console/tools"
DEPENDS = "ncurses readline util-linux"
PR = "r1"

SRC_URI = "${GNU_MIRROR}/parted/parted-${PV}.tar.xz \
           file://no_check.patch \
           file://syscalls.patch \
           file://fix-git-version-gen.patch \
           file://fix-doc-mandir.patch \
           file://fix-dvh-overflows.patch \
           file://fix-deprecated-readline.patch \
           file://run-ptest \
           file://Makefile \
"

SRC_URI[md5sum] = "5d89d64d94bcfefa9ce8f59f4b81bdcb"
SRC_URI[sha256sum] = "5e9cc1f91eaf016e5033d85b9b893fd6d3ffaca532a48de1082df9b94225ca15"

EXTRA_OECONF = "--disable-device-mapper"

inherit autotools pkgconfig gettext texinfo ptest

BBCLASSEXTEND = "native"

do_compile_ptest() {
	oe_runmake -C tests print-align print-max dup-clobber duplicate fs-resize
}

do_install_ptest() {
	t=${D}${PTEST_PATH}
	mkdir $t/build-aux
	cp ${S}/build-aux/test-driver $t/build-aux/
	cp -r ${S}/tests $t
	cp ${WORKDIR}/Makefile $t/tests/
	for i in print-align print-max dup-clobber duplicate fs-resize; \
	  do cp ${B}/tests/.libs/$i $t/tests/; \
	done
	sed -e 's| ../parted||' -i $t/tests/*.sh
}

RDEPENDS_${PN}-ptest = "bash coreutils perl util-linux-losetup python"
