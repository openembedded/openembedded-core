SUMMARY = "SBC Audio Codec"
DESCRIPTION = "Bluetooth low-complexity, subband codec (SBC) library."
HOMEPAGE = "https://www.bluez.org"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://sbc/sbc.h;beginline=1;endline=26;md5=0f57d0df22b0d40746bdd29805a4361b"

DEPENDS = "libsndfile1"

SRC_URI = "${KERNELORG_MIRROR}/linux/bluetooth/${BP}.tar.xz"

SRC_URI[md5sum] = "ec65c444ad4c32aa85702641045b19e9"
SRC_URI[sha256sum] = "c2f01ea54f7473704825113a9cdd46a23e67c650eff575f0670c3d9d66c4a5dc"

inherit autotools pkgconfig
