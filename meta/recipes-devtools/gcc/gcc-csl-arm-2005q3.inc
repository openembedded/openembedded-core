require gcc-common.inc

BINV = "3.4.4"
PV = "3.4.4+csl-arm-2005q3-2"
PV_chinook-compat = "3.4.4cs2005q3.2"

FILESDIR = "${FILE_DIRNAME}/gcc-csl-arm"

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-eabi/arm-2005q3-2-arm-none-eabi.src.tar.bz2 \
           file://gcc_optab_arm.patch \
           file://gcc-3.4.4-eabi-bigendian.patch"

S = "${WORKDIR}/gcc-2005q3"

do_unpack2() {
	cd ${WORKDIR}
	tar -xvjf ./arm-2005q3-2-arm-none-eabi/gcc-2005q3-2.tar.bz2
}

addtask unpack2 after do_unpack before do_patch


