require intltool.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r1"

SRC_URI += "file://intltool-nowarn-0.50.0.patch \
           ${NATIVEPATCHES} \
           "

#
# All of the intltool scripts have the correct paths to perl already
# embedded into them and can find perl fine, so we add the remove xml-check
# in the intltool.m4 via the remove-xml-check.patch
NATIVEPATCHES = "file://noperlcheck.patch \
                 file://remove-xml-check.patch"
NATIVEPATCHES_virtclass-native = "file://use-nativeperl.patch" 

SRC_URI[md5sum] = "0da9847a60391ca653df35123b1f7cc0"
SRC_URI[sha256sum] = "dccfb0b7dd35a170130e8934bfd30c29da6ae73bcd3ca4ba71317c977b2893d6"
