SUMMARY = "a fast C/C++ compiler cache"
DESCRIPTION = "ccache is a compiler cache. It speeds up recompilation \
by caching the result of previous compilations and detecting when the \
same compilation is being done again. Supported languages are C, C\+\+, \
Objective-C and Objective-C++."
HOMEPAGE = "http://ccache.samba.org"
SECTION = "devel"
LICENSE = "GPLv3+"

DEPENDS = "zlib"

SRC_URI = "http://samba.org/ftp/ccache/ccache-${PV}.tar.xz"

inherit autotools

BBCLASSEXTEND = "native"

TARGET_CC_ARCH += "${LDFLAGS}"
