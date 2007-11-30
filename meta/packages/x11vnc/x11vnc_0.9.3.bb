DESCRIPTION = "Export your X session on-the-fly via VNC"
SECTION = "x11/utils"
HOMEPAGE = "http://www.karlrunge.com/x11vnc/"
AUTHOR = "Karl Runge"
LICENSE = "GPL"
DEPENDS = "openssl virtual/libx11 libxext avahi jpeg zlib"

SRC_URI = "http://www.karlrunge.com/x11vnc/x11vnc-0.9.3.tar.gz"

inherit autotools

