SUMMARY = "SBC Audio Codec"
DESCRIPTION = "Bluetooth low-complexity, subband codec (SBC) library."
HOMEPAGE = "https://www.bluez.org"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://sbc/sbc.h;beginline=1;endline=25;md5=0a7e4f502980cc3ee0541341fa2db47e"

DEPENDS = "libsndfile1"

SRC_URI = "${KERNELORG_MIRROR}/linux/bluetooth/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "ecadadbfd4b1dfe7b98f446c69126b23"
SRC_URI[sha256sum] = "9a30ad2dc20979a0847a7b1a06ea073498810f358a02fcad68ab414f239cfbc6"

inherit autotools
