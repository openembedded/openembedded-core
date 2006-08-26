PR = "r6"
DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"

inherit autotools gettext

require gcc-package.inc

SRC_URI = "http://ftp.gnu.org/pub/gnu/gcc/gcc-4.1.1/gcc-4.1.1.tar.bz2 \
	file://100-uclibc-conf.patch;patch=1 \
	file://110-arm-eabi.patch;patch=1 \
	file://200-uclibc-locale.patch;patch=1 \
	file://300-libstdc++-pic.patch;patch=1 \
	file://301-missing-execinfo_h.patch;patch=1 \
	file://302-c99-snprintf.patch;patch=1 \
	file://303-c99-complex-ugly-hack.patch;patch=1 \
	file://304-index_macro.patch;patch=1 \
	file://602-sdk-libstdc++-includes.patch;patch=1 \
	file://740-sh-pr24836.patch;patch=1 \
	file://800-arm-bigendian.patch;patch=1 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
	file://gcc41-configure.in.patch;patch=1 \
	file://arm-thumb.patch;patch=1 \
	file://arm-thumb-cache.patch;patch=1 \
	file://ldflags.patch;patch=1 \
	file://cse.patch;patch=1 \
	file://zecke-xgcc-cpp.patch;patch=1 "

SRC_URI_append_fail-fast = " file://zecke-no-host-includes.patch;patch=1 "

#Set the fortran bits
FORTRAN = "" 
HAS_GFORTRAN = "no"
HAS_G2C = "no"

#Set the java bits
JAVA_arm = ""  

LANGUAGES = "c,c++"
require gcc3-build.inc


EXTRA_OECONF += " --disable-libssp "
  
