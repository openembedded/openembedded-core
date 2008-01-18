DESCRIPTION = "top-like statistics of X11 server resource usage by clients"
SECTION = "x11/utils"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/xrestop"
LICENSE = "GPL"
PR = "r1"

DEPENDS = "libxres libxext virtual/libx11"

SRC_URI = "http://projects.o-hand.com/sources/xrestop/xrestop-${PV}.tar.gz"

inherit autotools
