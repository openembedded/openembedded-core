SUMMARY = "Free Implementation of the Unicode Bidirectional Algorithm"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a916467b91076e631dd8edb7424769c7"

SRC_URI = "https://github.com/${BPN}/${BPN}/releases/download/v${PV}/${BP}.tar.bz2 \
           "
SRC_URI[md5sum] = "962c7d8ebaa711d4e306161dbe14aa55"
SRC_URI[sha256sum] = "94c7b68d86ad2a9613b4dcffe7bbeb03523d63b5b37918bdf2e4ef34195c1e6c"

UPSTREAM_CHECK_URI = "https://github.com/${BPN}/${BPN}/releases"

inherit meson lib_package pkgconfig

CVE_PRODUCT = "gnu_fribidi"

BBCLASSEXTEND = "native nativesdk"
