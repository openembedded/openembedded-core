SUMMARY = "Host side USB console utilities"
DESCRIPTION = "Contains the lsusb utility for inspecting the devices connected to the USB bus."
HOMEPAGE = "http://www.linux-usb.org"
SECTION = "base"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "libusb zlib virtual/libiconv udev"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/usb/usbutils/usbutils-${PV}.tar.gz \
           file://usb-devices-avoid-dependency-on-bash.patch \
           file://Fix-NULL-pointer-crash.patch \
          "

SRC_URI[md5sum] = "b5dbc498b2eb5058f7a57fc6532d0aad"
SRC_URI[sha256sum] = "e73543293a17c7803994eac97a49e58b377e08e6299ba11aad09794b91340e8b"

inherit autotools pkgconfig distro_features_check

FILES_${PN}-dev += "${datadir}/pkgconfig"

RRECOMMENDS_${PN} = "udev-hwdb"
RDEPENDS_${PN}-ptest = "libboost-system libboost-thread"
