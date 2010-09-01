DESCRIPTION = "Export your X session on-the-fly via VNC"
SECTION = "x11/utils"
HOMEPAGE = "http://www.karlrunge.com/x11vnc/"
AUTHOR = "Karl Runge"
LICENSE = "GPL"
DEPENDS = "openssl virtual/libx11 libxext avahi jpeg zlib"

SRC_URI = "${SOURCEFORGE_MIRROR}/libvncserver/x11vnc-${PV}.tar.gz"

inherit autotools

