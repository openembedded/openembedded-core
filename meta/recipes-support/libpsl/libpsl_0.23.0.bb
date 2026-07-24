SUMMARY = "Public Suffix List library"
DESCRIPTION = "The libpsl package provides a library for accessing and \
resolving information from the Public Suffix List (PSL). The PSL is a set of \
domain names beyond the standard suffixes, such as .com."

HOMEPAGE = "https://rockdaboot.github.io/libpsl/"
BUGTRACKER = "https://github.com/rockdaboot/libpsl/issues"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=49296c1806ef92c28297fb264163d81e \
                    file://COPYING;md5=49296c1806ef92c28297fb264163d81e \
                    "

SRC_URI = "${GITHUB_BASE_URI}/download/${PV}/${BP}.tar.gz \
           "
SRC_URI[sha256sum] = "f39b9631b3d369a21259ea4654f8875c0ec6995ce9551c0eb5d423e4c011f911"

GITHUB_BASE_URI = "https://github.com/rockdaboot/libpsl/releases"

inherit autotools gettext gtk-doc manpages pkgconfig lib_package github-releases

PACKAGECONFIG ?= "idn2"
PACKAGECONFIG[manpages] = "--enable-man,--disable-man,libxslt-native"
PACKAGECONFIG[icu] = "--enable-runtime=libicu --enable-builtin=libicu,,icu"
PACKAGECONFIG[idn2] = "--enable-runtime=libidn2 --enable-builtin=libidn2,,libidn2 libunistring"
BBCLASSEXTEND = "native nativesdk"
