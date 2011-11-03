DESCRIPTION = "libnl is a library for applications dealing with netlink sockets."
HOMEPAGE = "http://www.infradead.org/~tgr/libnl/"
SECTION = "libs/network"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2b41e13261a330ee784153ecbb6a82bc"

DEPENDS = "flex-native bison-native"
PR = "r3"

SRC_URI = "\
  http://www.infradead.org/~tgr/${BPN}/files/${BP}.tar.gz \
  file://fix-pktloc_syntax_h-race.patch \
"

SRC_URI[md5sum] = "6aaf1e9802a17a7d702bb0638044ffa7"
SRC_URI[sha256sum] = "5a40dc903d3ca1074da7424b908bec8ff16936484798c7e46e53e9db8bc87a9c"

inherit autotools pkgconfig

LEAD_SONAME = "libnl.so"
