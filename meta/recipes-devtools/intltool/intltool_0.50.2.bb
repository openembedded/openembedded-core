require intltool.inc
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI += "file://intltool-nowarn.patch \
           file://uclibc.patch \
           ${NATIVEPATCHES} \
           "

#
# All of the intltool scripts have the correct paths to perl already
# embedded into them and can find perl fine, so we add the remove xml-check
# in the intltool.m4 via the remove-xml-check.patch
NATIVEPATCHES = "file://noperlcheck.patch \
                 file://remove-xml-check.patch"
NATIVEPATCHES_class-native = "file://use-nativeperl.patch" 

SRC_URI[md5sum] = "23fbd879118253cb99aeac067da5f591"
SRC_URI[sha256sum] = "67f25c5c6fb71d095793a7f895b245e65e829e8bde68c6c8b4c912144ff34406"
