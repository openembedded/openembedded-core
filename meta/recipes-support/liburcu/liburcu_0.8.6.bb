SUMMARY = "Userspace RCU (read-copy-update) library"
HOMEPAGE = "http://lttng.org/urcu"
BUGTRACKER = "http://lttng.org/project/issues"

LICENSE = "LGPLv2.1+ & MIT-style"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0f060c30a27922ce9c0d557a639b4fa3 \
                    file://urcu.h;beginline=4;endline=32;md5=4de0d68d3a997643715036d2209ae1d9 \
                    file://urcu/uatomic/x86.h;beginline=4;endline=21;md5=220552f72c55b102f2ee35929734ef42"

SRC_URI = "http://lttng.org/files/urcu/userspace-rcu-${PV}.tar.bz2 \
           file://Revert-Blacklist-ARM-gcc-4.8.0-4.8.1-4.8.2.patch \
           file://aarch64.patch \
          "

SRC_URI[md5sum] = "834d91d939dd55108a05763be9879856"
SRC_URI[sha256sum] = "b1a5d3bce014ba7a702759bc60b692c1cd46ff0e8a5b53f0d0a95e22db74ab21"

S = "${WORKDIR}/userspace-rcu-${PV}"
CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
inherit autotools
