DESCRIPTION = "Export your X session on-the-fly via VNC"
HOMEPAGE = "http://www.karlrunge.com/x11vnc/"

SECTION = "x11/utils"
AUTHOR = "Karl Runge"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=361b6b837cad26c6900a926b62aada5f \
                    file://x11vnc/x11vnc.h;endline=33;md5=ee4946e57bb73ecf93d7d98a3d48c6be"

DEPENDS = "openssl virtual/libx11 libxext avahi jpeg zlib"

SRC_URI = "${SOURCEFORGE_MIRROR}/libvncserver/x11vnc-${PV}.tar.gz"

inherit autotools

