SUMMARY = "the Git linkable library"
HOMEPAGE = "https://libgit2.org/"
LICENSE = "GPL-2.0-only WITH GCC-exception-2.0 AND MIT AND OpenSSL AND BSD-3-Clause AND Zlib AND ISC AND LGPL-2.1-or-later AND CC0-1.0 AND BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=8289345c8713c385f45ec0c6c8a0d080"

DEPENDS = "curl openssl zlib libssh2 libgcrypt libpcre2"

SRC_URI = "git://github.com/libgit2/libgit2.git;branch=maint/v1.9;protocol=https;tag=v${PV} \
           file://0001-cmake-mark-system-libraries-are-private-link-librari.patch"

SRCREV = "26055f5af74ab1cf636d272e8a34315496d3f06f"

inherit cmake

EXTRA_OECMAKE = "\
    -DBUILD_TESTS=OFF \
    -DCMAKE_BUILD_TYPE=RelWithDebInfo \
    -DREGEX_BACKEND='pcre2' \
"

BBCLASSEXTEND = "native nativesdk"
