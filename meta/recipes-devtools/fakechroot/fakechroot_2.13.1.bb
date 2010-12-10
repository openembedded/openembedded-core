DESCRIPTION = "Gives a fake root environment which can support chroot"
SECTION = "base"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakechroot/fakechroot_${PV}.orig.tar.gz \
          "

SRC_URI[md5sum] = "280a828869a15059f0681a5d11a5e255"
SRC_URI[md5sum] = "15489437d8602cc393d886f74004ee75af049b1fab42b27331f355650e4e57c8"

inherit autotools

PR = "r0"

BBCLASSEXTEND = "native"
