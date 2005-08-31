SECTION = "base"
DESCRIPTION = "Gives a fake root environment"
HOMEPAGE = "http://joostje.op.het.net/fakeroot/index.html"
LICENSE = "GPL"

SRC_URI = "${DEBIAN_MIRROR}/main/f/fakeroot/fakeroot_${PV}.tar.gz"

inherit autotools
