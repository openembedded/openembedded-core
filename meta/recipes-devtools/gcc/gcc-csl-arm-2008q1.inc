require gcc-common.inc

BINV = "4.2.3"
PV = "4.2.3+csl-arm-2008q1-126"
PR = "r6"

FILESDIR = "${FILE_DIRNAME}/gcc-csl-arm"

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-eabi/arm-2008q1-126-arm-none-eabi.src.tar.bz2 \
           file://gcc41-configure.in.patch \
           file://arm-nolibfloat.patch \
           file://arm-softfloat.patch \
           file://zecke-xgcc-cpp.patch \
"
#           file://100-uclibc-conf.patch
#           file://103-uclibc-conf-noupstream.patch
#           file://200-uclibc-locale.patch
#           file://203-uclibc-locale-no__x.patch
#           file://204-uclibc-locale-wchar_fix.patch
#           file://205-uclibc-locale-update.patch
#           file://300-libstdc++-pic.patch
#           file://302-c99-snprintf.patch
#           file://303-c99-complex-ugly-hack.patch
#           file://304-index_macro.patch
#           file://305-libmudflap-susv3-legacy.patch
#           file://306-libstdc++-namespace.patch
#           file://307-locale_facets.patch
#           file://402-libbackend_dep_gcov-iov.h.patch
#           file://602-sdk-libstdc++-includes.patch
#           file://gfortran.patch
#           file://fortran-static-linking.patch
#           file://gcc-configure-no-fortran.patch
#           file://gcc-new-makeinfo.patch

S = "${WORKDIR}/gcc-4.2"

do_unpack2() {
	cd ${WORKDIR}
	tar -xvjf ./arm-2008q1-126-arm-none-eabi/gcc-2008q1-126.tar.bz2
}

# Language Overrides
FORTRAN = ""
#FORTRAN_linux-gnueabi = ",fortran"
#JAVA = ",java"

EXTRA_OECONF_BASE = "--enable-libssp --disable-bootstrap --disable-libgomp --disable-libmudflap"
EXTRA_OECONF_INITIAL = "--disable-libmudflap --disable-libgomp --disable-libssp --disable-bootstrap"
EXTRA_OECONF_INTERMEDIATE = "--disable-libmudflap --disable-libgomp --disable-libssp --disable-bootstrap"

ARM_INSTRUCTION_SET = "arm"

addtask unpack2 after do_unpack before do_patch
