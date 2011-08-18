require binutils.inc

DEFAULT_PREFERENCE = "-1"

SRC_URI = "\
     http://www.codesourcery.com/gnu_toolchains/arm/portal/package2553/public/arm-none-eabi/arm-2008q1-126-arm-none-eabi.src.tar.bz2 \
     file://binutils-2.16.91.0.6-objcopy-rename-errorcode.patch \
     file://binutils-uclibc-100-uclibc-conf.patch \
     file://110-arm-eabi-conf.patch \
     file://binutils-uclibc-300-001_ld_makefile_patch.patch \
     file://binutils-uclibc-300-006_better_file_error.patch \
     file://binutils-uclibc-300-012_check_ldrunpath_length.patch \
     file://docs_hack2.patch \
     "

PV = "2.18+csl-arm-2008q1-126"

S = "${WORKDIR}/binutils-stable"

do_unpack2() {
	cd ${WORKDIR}
	tar -xvjf ./arm-2008q1-126-arm-none-eabi/binutils-2008q1-126.tar.bz2
}

addtask unpack2 after do_unpack before do_patch
