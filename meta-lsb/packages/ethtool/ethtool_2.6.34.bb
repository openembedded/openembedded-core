DESCRIPTION = "A small utility for examining and tuning the settings of your ethernet-based network interfaces."
HOMEPAGE = "http://sourceforge.net/projects/gkernel/"
SECTION = "console/network"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://ethtool.c;firstline=4;endline=16;md5=6db6b005b8390ea76cb85a2c7bda8e6b"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/gkernel/ethtool-${PV}.tar.gz"

inherit autotools
