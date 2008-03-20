LICENSE = "GPLv2"
DEPENDS = "libxml-simple-perl-native"
PR = "r1"

SRC_URI = "http://tango.freedesktop.org/releases/icon-naming-utils-0.8.2.tar.gz"

S = "${WORKDIR}/icon-naming-utils-${PV}"

inherit autotools native
