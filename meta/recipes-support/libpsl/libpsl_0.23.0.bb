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
           file://0001-Support-reproducible-builds.patch \
           file://0002-psl-make-dafsa-embed-only-the-basename-of-the-input-.patch \
           "
SRC_URI[sha256sum] = "f39b9631b3d369a21259ea4654f8875c0ec6995ce9551c0eb5d423e4c011f911"

GITHUB_BASE_URI = "https://github.com/rockdaboot/libpsl/releases"

inherit meson gtk-doc pkgconfig lib_package github-releases gtk-doc

# Do not build the bundled tests and fuzzers.
EXTRA_OEMESON = "-Dtests=false"

PACKAGECONFIG ?= "idn2"
PACKAGECONFIG[icu] = "-Druntime=libicu,,icu"
PACKAGECONFIG[idn2] = "-Druntime=libidn2,,libidn2 libunistring"
BBCLASSEXTEND = "native nativesdk"
