DESCRIPTION = "libnl is a library for applications dealing with netlink sockets."
HOMEPAGE = "http://www.infradead.org/~tgr/libnl/"
SECTION = "libs/network"
PRIORITY = "optional"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2b41e13261a330ee784153ecbb6a82bc \
                    file://src/utils.c;beginline=4;endline=9;md5=6e99dfba5cfd64e92b4eb2c7b31e5e4e"

PR = "r1"

SRC_URI= "http://www.infradead.org/~tgr/libnl/files/${PN}-${PV}.tar.gz \
          file://local-includes.patch;apply=yes \
          file://compilefix.patch;apply=yes"

inherit autotools pkgconfig
