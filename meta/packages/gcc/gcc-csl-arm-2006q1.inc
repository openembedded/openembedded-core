require gcc-common.inc

BINV = "4.1.0"
PV = "4.1.0+csl-arm-2006q1-6"

FILESDIR = "${FILE_DIRNAME}/gcc-csl-arm"

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-eabi/arm-2006q1-6-arm-none-eabi.src.tar.bz2 \
           file://gcc-configure-no-fortran.patch;patch=1;pnum=1 \
           file://gcc-new-makeinfo.patch;patch=1"

S = "${WORKDIR}/gcc-2006q1"

do_unpack2() {
	cd ${WORKDIR}
	tar -xvjf ./arm-2006q1-6-arm-none-eabi/gcc-2006q1-6.tar.bz2
}

addtask unpack2 after do_unpack before do_patch
