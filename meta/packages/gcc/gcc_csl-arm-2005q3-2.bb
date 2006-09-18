DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
BINV = "3.4.4"
PV = "3.4.4+csl-arm-2005q3-2"
PR = "r1"

FILESDIR = "${FILE_DIRNAME}/gcc-csl-arm"

inherit autotools gettext

require gcc-package.inc

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-linux-gnueabi/arm-2005q3-2-arm-none-linux-gnueabi.src.tar.bz2"

do_unpack2() {
	cd ${WORKDIR}
	tar -xvjf ./arm-2005q3-2-arm-none-linux-gnueabi/gcc-2005q3-2.tar.bz2
	rm -rf ./arm-2005q3-2-arm-none-linux-gnueabi
}

addtask unpack2 after do_unpack before do_patch

require gcc3-build.inc

S = "${WORKDIR}/gcc-2005q3"
