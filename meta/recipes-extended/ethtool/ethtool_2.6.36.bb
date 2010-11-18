SUMMARY = "Display or change ethernet card settings"
DESCRIPTION = "A small utility for examining and tuning the settings of your ethernet-based network interfaces."
HOMEPAGE = "http://sourceforge.net/projects/gkernel/"
SECTION = "console/network"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://ethtool.c;firstline=4;endline=16;md5=6db6b005b8390ea76cb85a2c7bda8e6b"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/gkernel/ethtool-${PV}.tar.gz"

SRC_URI[md5sum] = "3b2322695e9ee7bf447ebcdb85f93e83"
SRC_URI[sha256sum] = "639622180fe48dc7bb117ffbf263395d7ae47aac9819b8d9f83ff053ecf17bdd"

inherit autotools
