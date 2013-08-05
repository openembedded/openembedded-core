DESCRIPTION = "The userspace read-copy update library by Mathieu Desnoyers"
HOMEPAGE = "http://lttng.org/urcu"
BUGTRACKER = "http://lttng.org/project/issues"

LICENSE = "LGPLv2.1+ & MIT-style"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0f060c30a27922ce9c0d557a639b4fa3 \
                    file://urcu.h;beginline=4;endline=32;md5=4de0d68d3a997643715036d2209ae1d9 \
                    file://urcu/uatomic/x86.h;beginline=4;endline=21;md5=220552f72c55b102f2ee35929734ef42"

SRC_URI = "http://lttng.org/files/urcu/userspace-rcu-${PV}.tar.bz2"

SRC_URI[md5sum] = "d14de3ff32eb1ab9424b258599c8a6f3"
SRC_URI[sha256sum] = "8ab8a7d8fea47c09ceac24c9277f00da626bbd9426c973eb325b1baf33b4bdfb"

S = "${WORKDIR}/userspace-rcu-${PV}"
CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
inherit autotools
