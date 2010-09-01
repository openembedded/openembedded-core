DESCRIPTION = "Libtirpc is a port of Suns Transport-Independent RPC library to Linux"
SECTION = "libs/network"
PRIORITY = "optional"
HOMEPAGE = "http://sourceforge.net/projects/libtirpc/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=183075&atid=903784"
LICENSE = "Sun Industry Standards Source License 1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=1c32c8e351f97e43e1ad6cf7f62de3bf \
                    file://src/netname.c;beginline=1;endline=27;md5=f8a8cd2cb25ac5aa16767364fb0e3c24"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/libtirpc/libtirpc-${PV}.tar.bz2"

inherit autotools
