DESCRIPTION = "libnl is a library for applications dealing with netlink sockets"
SECTION = "libs/network"
LICENSE = "LGPL"
HOMEPAGE = "http://people.suug.ch/~tgr/libnl/"
PRIORITY = "optional"

inherit autotools pkgconfig

PR = "r1"

SRC_URI= "http://people.suug.ch/~tgr/libnl/files/${PN}-${PV}.tar.gz \
          file://local-includes.patch;patch=1 \
          file://compilefix.patch;patch=1"
