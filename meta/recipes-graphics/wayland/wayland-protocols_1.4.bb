SUMMARY = "Collection of additional Wayland protocols"
DESCRIPTION = "Wayland protocols that add functionality not \
available in the Wayland core protocol. Such protocols either add \
completely new functionality, or extend the functionality of some other \
protocol either in Wayland core, or some other protocol in \
wayland-protocols."
HOMEPAGE = "http://wayland.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=c7b12b6702da38ca028ace54aae3d484 \
                    file://stable/presentation-time/presentation-time.xml;endline=26;md5=4646cd7d9edc9fa55db941f2d3a7dc53"

SRC_URI = "https://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz \
           "
SRC_URI[md5sum] = "fd8089abf13a1d04e4baa6509ee72baf"
SRC_URI[sha256sum] = "014a9a23c21ed14f49b1005b3e8efa66d6337d4ceafc97f7b0d6707e7e3df572"

inherit allarch autotools pkgconfig

PACKAGES = "${PN}"
FILES_${PN} += "${datadir}/pkgconfig/wayland-protocols.pc"
