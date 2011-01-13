require xorg-app-common.inc

DESCRIPTION = "a program to create an index of X font files in a directory"

PE = "1"
PR = "r0"

RDEPENDS_${PN} += "mkfontscale"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=b4fcf2b90cadbfc15009b9e124dc3a3f"

SRC_URI[md5sum] = "dc342dd8858416254bb5f71a9ddce589"
SRC_URI[sha256sum] = "55d56c6310f8d2268cb8978e838d01d27c7d70e30282c373c5a935ab3fb8c859"
