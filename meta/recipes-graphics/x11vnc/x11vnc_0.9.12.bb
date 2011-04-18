DESCRIPTION = "Export your X session on-the-fly via VNC"
HOMEPAGE = "http://www.karlrunge.com/x11vnc/"

SECTION = "x11/utils"
AUTHOR = "Karl Runge"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=361b6b837cad26c6900a926b62aada5f \
                    file://x11vnc/x11vnc.h;endline=33;md5=ee4946e57bb73ecf93d7d98a3d48c6be"

DEPENDS = "openssl virtual/libx11 libxext avahi jpeg zlib"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libvncserver/x11vnc/${PV}/x11vnc-${PV}.tar.gz\
           file://starting-fix.patch \
           file://endian-fix.patch "

SRC_URI[md5sum] = "1498a68d02aa7b6c97bf746c073c8d00"
SRC_URI[sha256sum] = "60a7cceee2c9a5f1c854340b2bae13f975ac55906237042f81f795b28a154a79"

inherit autotools

