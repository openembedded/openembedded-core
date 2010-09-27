SUMMARY = "Embedded GLIBC (GNU C Library)"
DESCRIPTION = "Embedded GLIBC (EGLIBC) is a variant of the GNU C Library (GLIBC) that is designed to work well on embedded systems. EGLIBC strives to be source and binary compatible with GLIBC. EGLIBC's goals include reduced footprint, configurable components, better support for cross-compilation and cross-testing."
HOMEPAGE = "http://www.eglibc.org/home"
SECTION = "libs"
PRIORITY = "required"
LICENSE = "LGPL"
INC_PR = "r11"
# nptl needs unwind support in gcc, which can't be built without glibc.
DEPENDS = "virtual/${TARGET_PREFIX}gcc-intermediate linux-libc-headers"
#this leads to circular deps, so lets not add it yet
#RDEPENDS_ldd += " bash"
# nptl needs libgcc but dlopens it, so our shlibs code doesn't detect this
#RDEPENDS_${PN} += "${@['','libgcc']['nptl' in '${GLIBC_ADDONS}']}"
PROVIDES = "virtual/libc virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES += "virtual/libintl virtual/libiconv"
inherit autotools

LEAD_SONAME = "libc.so"

GLIBC_EXTRA_OECONF ?= ""
INHIBIT_DEFAULT_DEPS = "1"

ARM_INSTRUCTION_SET = "arm"

PACKAGES = "glibc catchsegv sln nscd ldd localedef glibc-utils glibc-dev glibc-doc glibc-locale libsegfault glibc-extra-nss glibc-thread-db glibc-pcprofile"
