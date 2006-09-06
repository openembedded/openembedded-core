DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
BINV = "4.1.0"
PV = "4.1.0+csl-arm-2006q1-6"
PR = "r1"

FILESDIR = "${FILE_DIRNAME}/gcc-csl-arm"

inherit autotools gettext

require gcc-package-no-fortran.inc

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-eabi/arm-2006q1-6-arm-none-eabi.src.tar.bz2 \
           file://gcc-configure-no-fortran.patch;patch=1;pnum=1"

#the optabi patch is already applied

do_unpack2() {
	cd ${WORKDIR}
	pwd
	tar -xvjf ./arm-2006q1-6-arm-none-eabi/gcc-2006q1-6.tar.bz2
}

addtask unpack2 after do_unpack before do_patch

require gcc4-build.inc

S = "${WORKDIR}/gcc-2006q1"
