SUMMARY = "SBC Audio Codec"
DESCRIPTION = "Bluetooth low-complexity, subband codec (SBC) library."
HOMEPAGE = "https://www.bluez.org"
SECTION = "libs"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://sbc/sbc.h;beginline=1;endline=25;md5=0a7e4f502980cc3ee0541341fa2db47e"

PR = "r0"

DEPENDS = "libsndfile1"

SRC_URI = "${KERNELORG_MIRROR}/linux/bluetooth/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "aa2bc39c4a09aade064efea4bbbc4b2d"
SRC_URI[sha256sum] = "bf970aa21226c594bb04ba3d949770c8fd91dc8f953556305f20c1016b16b882"

inherit autotools
