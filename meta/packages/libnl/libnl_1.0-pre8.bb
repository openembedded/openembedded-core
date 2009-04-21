DESCRIPTION = "libnl is a library for applications dealing with netlink sockets"
SECTION = "libs/network"
LICENSE = "LGPL"
HOMEPAGE = "http://people.suug.ch/~tgr/libnl/"
PRIORITY = "optional"
PV = "0.99+1.0-pre8"

inherit autotools_stage pkgconfig

SRC_URI= "http://people.suug.ch/~tgr/libnl/files/${PN}-1.0-pre8.tar.gz \
           file://local-includes.patch;patch=1"

S = "${WORKDIR}/${PN}-1.0-pre8"
